package com.tz.redismanager.service.impl;

import com.tz.redismanager.constant.ConstInterface;
import com.tz.redismanager.domain.dto.VisitDataDTO;
import com.tz.redismanager.security.AuthContext;
import com.tz.redismanager.service.IRedisTemplateExtService;
import com.tz.redismanager.service.IStatisticService;
import com.tz.redismanager.util.DateUtils;
import com.tz.redismanager.util.UUIDUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

/**
 * <p>Redis统计实现</p>
 *
 * @version 1.4.0
 * @time 2020-10-12 22:38
 **/
@Service
public class RedisStatisticServiceImpl implements IStatisticService {

    private static final Integer HOURS_24 = 24;
    private static final Integer HOURS_1 = 1;
    private static final Integer DAYS_365 = 365;
    private static final Integer DAYS_31 = 31;
    private static final Integer DAYS_1 = 1;
    private static final Integer YEAR_10 = DAYS_365 * 10;

    @Autowired
    private RedisTemplate<String,Object> redisTemplate;
    @Autowired
    private IRedisTemplateExtService redisTemplateExtService;

    @Override
    @Async
    public void statisticsToAsync(AuthContext authContext) {
        Integer userId = Optional.ofNullable(authContext).map(AuthContext::getUserId).orElse(null);
        this.addOnlineUser(userId);
        this.addVisit();
    }

    @Override
    public void addOnlineUser(Integer userId) {
        if (null == userId) {
            return;
        }
        String onlineKey = ConstInterface.CacheKey.USER_ONLINE + DateUtils.nowDateToStr(DateUtils.YYYYMMDD);
        redisTemplate.opsForValue().setBit(onlineKey, userId, true);
        redisTemplate.expire(onlineKey, HOURS_24, TimeUnit.HOURS);
    }

    @Override
    public void removeOnlineUser(Integer userId) {
        if (null == userId) {
            return;
        }
        String onlineKey = ConstInterface.CacheKey.USER_ONLINE + DateUtils.nowDateToStr(DateUtils.YYYYMMDD);
        redisTemplate.opsForValue().setBit(onlineKey, userId, false);
    }

    @Override
    public Long countOnlineUser() {
        String onlineKey = ConstInterface.CacheKey.USER_ONLINE + DateUtils.nowDateToStr(DateUtils.YYYYMMDD);
        Long count = redisTemplateExtService.bitCount(onlineKey);
        return null == count ? 0 : count;
    }

    @Override
    public void addVisit() {
        String uuid = UUIDUtils.generateId();
        String totalKey = ConstInterface.CacheKey.VISIT_ALL_TOTAL;
        redisTemplate.opsForHyperLogLog().add(totalKey, uuid);
        redisTemplate.expire(totalKey, YEAR_10, TimeUnit.DAYS);

        String yyyy = DateUtils.nowDateToStr(DateUtils.YYYY);
        String yyyyMM = DateUtils.nowDateToStr(DateUtils.YYYYMM);
        String yyyyMMdd = DateUtils.nowDateToStr(DateUtils.YYYYMMDD);
        String YYYYMMDDHH = DateUtils.nowDateToStr(DateUtils.YYYYMMDDHH);
        String mm = DateUtils.nowDateToStr(DateUtils.MM);
        String dd = DateUtils.nowDateToStr(DateUtils.DD);
        String hh = DateUtils.nowDateToStr(DateUtils.HH);

        String currentYearKey = ConstInterface.CacheKey.VISIT_TOTAL + yyyy;
        redisTemplate.opsForHyperLogLog().add(currentYearKey, uuid);
        redisTemplate.expire(currentYearKey, DAYS_365, TimeUnit.DAYS);

        String totalDetailKey = ConstInterface.CacheKey.VISIT_DETAIL + "all";
        redisTemplate.opsForHash().put(totalDetailKey, yyyy, redisTemplate.opsForHyperLogLog().size(currentYearKey));
        redisTemplate.expire(totalDetailKey, YEAR_10, TimeUnit.DAYS);

        String currentMonthKey = ConstInterface.CacheKey.VISIT_TOTAL + yyyyMM;
        redisTemplate.opsForHyperLogLog().add(currentMonthKey, uuid);
        redisTemplate.expire(currentMonthKey, DAYS_31, TimeUnit.DAYS);

        String yearDetailKey = ConstInterface.CacheKey.VISIT_DETAIL + yyyy;
        redisTemplate.opsForHash().put(yearDetailKey, mm, redisTemplate.opsForHyperLogLog().size(currentMonthKey));
        redisTemplate.expire(yearDetailKey, YEAR_10, TimeUnit.DAYS);

        String currentDayKey = ConstInterface.CacheKey.VISIT_TOTAL + yyyyMMdd;
        redisTemplate.opsForHyperLogLog().add(currentDayKey, uuid);
        redisTemplate.expire(currentDayKey, DAYS_1, TimeUnit.DAYS);

        String mmDetailKey = ConstInterface.CacheKey.VISIT_DETAIL + yyyyMM;
        redisTemplate.opsForHash().put(mmDetailKey, dd, redisTemplate.opsForHyperLogLog().size(currentDayKey));
        redisTemplate.expire(mmDetailKey, YEAR_10, TimeUnit.DAYS);

        String currentHourKey = ConstInterface.CacheKey.VISIT_TOTAL + YYYYMMDDHH;
        redisTemplate.opsForHyperLogLog().add(currentHourKey, uuid);
        redisTemplate.expire(currentHourKey, HOURS_1, TimeUnit.HOURS);

        String ddDetailKey = ConstInterface.CacheKey.VISIT_DETAIL + yyyyMMdd;
        redisTemplate.opsForHash().put(ddDetailKey, hh, redisTemplate.opsForHyperLogLog().size(currentHourKey));
        redisTemplate.expire(ddDetailKey, YEAR_10, TimeUnit.DAYS);
    }

    @Override
    public VisitDataDTO countVisit() {
        VisitDataDTO dto = new VisitDataDTO();
        String totalKey = ConstInterface.CacheKey.VISIT_ALL_TOTAL;
        String currentYearKey = ConstInterface.CacheKey.VISIT_TOTAL + DateUtils.nowDateToStr(DateUtils.YYYY);
        String currentMonthKey = ConstInterface.CacheKey.VISIT_TOTAL + DateUtils.nowDateToStr(DateUtils.YYYYMM);
        String currentDayKey = ConstInterface.CacheKey.VISIT_TOTAL + DateUtils.nowDateToStr(DateUtils.YYYYMMDD);
        dto.setTotal(redisTemplate.opsForHyperLogLog().size(totalKey));
        dto.setCurrentYearTotal(redisTemplate.opsForHyperLogLog().size(currentYearKey));
        dto.setCurrentMonthTotal(redisTemplate.opsForHyperLogLog().size(currentMonthKey));
        dto.setCurrentDayTotal(redisTemplate.opsForHyperLogLog().size(currentDayKey));

        String yyyy = DateUtils.nowDateToStr(DateUtils.YYYY);
        String yyyyMM = DateUtils.nowDateToStr(DateUtils.YYYYMM);
        String yyyyMMdd = DateUtils.nowDateToStr(DateUtils.YYYYMMDD);

        String totalDetailKey = ConstInterface.CacheKey.VISIT_DETAIL + "all";
        Map<Object, Object> totalDetails = redisTemplate.opsForHash().entries(totalDetailKey);
        totalDetails.forEach((key, value) -> {
            VisitDataDTO.totalDeail deail = new VisitDataDTO.totalDeail();
            deail.setDate(String.valueOf(key));
            deail.setCount(Long.valueOf(String.valueOf(value)));
            dto.addTotalDeails(deail);
        });

        String yearDetailKey = ConstInterface.CacheKey.VISIT_DETAIL + yyyy;
        Map<Object, Object> yearDetails = redisTemplate.opsForHash().entries(yearDetailKey);
        yearDetails.forEach((key, value) -> {
            VisitDataDTO.totalDeail deail = new VisitDataDTO.totalDeail();
            deail.setDate(yyyy + key);
            deail.setCount(Long.valueOf(String.valueOf(value)));
            dto.addYearDeails(deail);
        });

        String mmDetailKey = ConstInterface.CacheKey.VISIT_DETAIL + yyyyMM;
        Map<Object, Object> monthDetails = redisTemplate.opsForHash().entries(mmDetailKey);
        monthDetails.forEach((key, value) -> {
            VisitDataDTO.totalDeail deail = new VisitDataDTO.totalDeail();
            deail.setDate(yyyyMM + key);
            deail.setCount(Long.valueOf(String.valueOf(value)));
            dto.addMonthDeails(deail);
        });

        String ddDetailKey = ConstInterface.CacheKey.VISIT_DETAIL + yyyyMMdd;
        Map<Object, Object> dayDetails = redisTemplate.opsForHash().entries(ddDetailKey);
        dayDetails.forEach((key, value) -> {
            VisitDataDTO.totalDeail deail = new VisitDataDTO.totalDeail();
            deail.setDate(yyyyMMdd + key);
            deail.setCount(Long.valueOf(String.valueOf(value)));
            dto.addDayDeails(deail);
        });
        return dto;
    }
}
