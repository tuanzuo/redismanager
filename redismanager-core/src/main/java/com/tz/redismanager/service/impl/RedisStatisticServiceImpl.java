package com.tz.redismanager.service.impl;

import com.tz.redismanager.constant.ConstInterface;
import com.tz.redismanager.dao.domain.po.RedisConfigPO;
import com.tz.redismanager.domain.dto.RedisConfigVisitDataDTO;
import com.tz.redismanager.domain.dto.UserVisitDataDTO;
import com.tz.redismanager.domain.dto.VisitDataDTO;
import com.tz.redismanager.domain.param.AnalysisParam;
import com.tz.redismanager.enm.DateTypeEnum;
import com.tz.redismanager.security.AuthContext;
import com.tz.redismanager.service.ICacheService;
import com.tz.redismanager.service.IRedisTemplateExtService;
import com.tz.redismanager.service.IStatisticService;
import com.tz.redismanager.util.DateUtils;
import com.tz.redismanager.util.UUIDUtils;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

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
    private static final Integer DAYS_7 = 7;
    private static final Integer DAYS_1 = 1;
    private static final Integer YEAR_1 = DAYS_365;
    private static final Integer YEAR_10 = DAYS_365 * 10;

    @Autowired
    private RedisTemplate<String,Object> redisTemplate;
    @Autowired
    private IRedisTemplateExtService redisTemplateExtService;
    @Autowired
    private ICacheService cacheService;

    @Override
    @Async
    public void statisticsToAsync(AuthContext authContext) {
        Integer userId = Optional.ofNullable(authContext).map(AuthContext::getUserId).orElse(null);
        this.addOnlineUser(userId);
        this.addVisit();
        this.addUserVisit(userId, authContext.getUserName());
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
        String totalKey = ConstInterface.CacheKey.VISIT_TOTAL_ALL;
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
        redisTemplate.expire(yearDetailKey, YEAR_1, TimeUnit.DAYS);

        String currentDayKey = ConstInterface.CacheKey.VISIT_TOTAL + yyyyMMdd;
        redisTemplate.opsForHyperLogLog().add(currentDayKey, uuid);
        redisTemplate.expire(currentDayKey, DAYS_1, TimeUnit.DAYS);

        String mmDetailKey = ConstInterface.CacheKey.VISIT_DETAIL + yyyyMM;
        redisTemplate.opsForHash().put(mmDetailKey, dd, redisTemplate.opsForHyperLogLog().size(currentDayKey));
        redisTemplate.expire(mmDetailKey, DAYS_31, TimeUnit.DAYS);

        int weekOfYear = new DateTime().weekOfWeekyear().get();
        int dayOfWeek = new DateTime().dayOfWeek().get();
        String weekDetailKey = ConstInterface.CacheKey.VISIT_DETAIL + yyyy + ":week:" + weekOfYear;
        redisTemplate.opsForHash().put(weekDetailKey, String.valueOf(dayOfWeek), redisTemplate.opsForHyperLogLog().size(currentDayKey));
        redisTemplate.expire(weekDetailKey, DAYS_7, TimeUnit.DAYS);

        String currentHourKey = ConstInterface.CacheKey.VISIT_TOTAL + YYYYMMDDHH;
        redisTemplate.opsForHyperLogLog().add(currentHourKey, uuid);
        redisTemplate.expire(currentHourKey, HOURS_1, TimeUnit.HOURS);

        String ddDetailKey = ConstInterface.CacheKey.VISIT_DETAIL + yyyyMMdd;
        redisTemplate.opsForHash().put(ddDetailKey, hh, redisTemplate.opsForHyperLogLog().size(currentHourKey));
        redisTemplate.expire(ddDetailKey, DAYS_1, TimeUnit.DAYS);
    }

    @Override
    public VisitDataDTO countVisit(AnalysisParam param) {
        VisitDataDTO dto = new VisitDataDTO();
        String totalKey = ConstInterface.CacheKey.VISIT_TOTAL_ALL;
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
        String yyyy_MM = DateUtils.nowDateToStr(DateUtils.YYYY_MM);
        String yyyy_MM_dd = DateUtils.nowDateToStr(DateUtils.YYYY_MM_DD);

        String totalDetailKey = ConstInterface.CacheKey.VISIT_DETAIL + "all";
        Map<Object, Object> totalDetails = redisTemplate.opsForHash().entries(totalDetailKey);
        totalDetails.forEach((key, value) -> {
            VisitDataDTO.totalDeail deail = new VisitDataDTO.totalDeail();
            deail.setDate(String.valueOf(key));
            deail.setCount(Long.valueOf(String.valueOf(value)));
            dto.addTotalDeails(deail);
        });
        dto.setTotalDeails(dto.getTotalDeails().stream().sorted(Comparator.comparing(temp -> {
            return DateUtils.strToDate(temp.getDate(), DateUtils.YYYY).getTime();
        })).collect(Collectors.toList()));

        if(DateTypeEnum.ALL.getType().equals(param.getDateType())){
            dto.setCurrentQueryDetails(dto.getTotalDeails());
        }
        if(DateTypeEnum.YEAR.getType().equals(param.getDateType())){
            String yearDetailKey = ConstInterface.CacheKey.VISIT_DETAIL + yyyy;
            Map<Object, Object> yearDetails = redisTemplate.opsForHash().entries(yearDetailKey);
            yearDetails.forEach((key, value) -> {
                VisitDataDTO.totalDeail deail = new VisitDataDTO.totalDeail();
                deail.setDate(yyyy + ConstInterface.Symbol.MIDDLE_LINE + key);
                deail.setCount(Long.valueOf(String.valueOf(value)));
                dto.addYearDeails(deail);
            });
            dto.setCurrentQueryDetails(dto.getYearDeails().stream().sorted(Comparator.comparing(temp -> {
                return DateUtils.strToDate(temp.getDate(), DateUtils.YYYY_MM).getTime();
            })).collect(Collectors.toList()));
        }

        if(DateTypeEnum.MONTH.getType().equals(param.getDateType())){
            String mmDetailKey = ConstInterface.CacheKey.VISIT_DETAIL + yyyyMM;
            Map<Object, Object> monthDetails = redisTemplate.opsForHash().entries(mmDetailKey);
            monthDetails.forEach((key, value) -> {
                VisitDataDTO.totalDeail deail = new VisitDataDTO.totalDeail();
                deail.setDate(yyyy_MM + ConstInterface.Symbol.MIDDLE_LINE + key);
                deail.setCount(Long.valueOf(String.valueOf(value)));
                dto.addMonthDeails(deail);
            });
            dto.setCurrentQueryDetails(dto.getMonthDeails().stream().sorted(Comparator.comparing(temp -> {
                return DateUtils.strToDate(temp.getDate(), DateUtils.YYYY_MM_DD).getTime();
            })).collect(Collectors.toList()));
        }

        if(DateTypeEnum.WEEK.getType().equals(param.getDateType())){
            int weekOfYear = new DateTime().weekOfWeekyear().get();
            String weekDetailKey = ConstInterface.CacheKey.VISIT_DETAIL + yyyy + ":week:" + weekOfYear;
            Map<Object, Object> weekDetails = redisTemplate.opsForHash().entries(weekDetailKey);
            weekDetails.forEach((key, value) -> {
                VisitDataDTO.totalDeail deail = new VisitDataDTO.totalDeail();
                deail.setDate(yyyy + "年" + weekOfYear + "周" + key + "天");
                deail.setCount(Long.valueOf(String.valueOf(value)));
                dto.addWeekDeails(deail);
            });
            dto.setCurrentQueryDetails(dto.getWeekDeails());
        }

        if(DateTypeEnum.TODAY.getType().equals(param.getDateType())){
            String ddDetailKey = ConstInterface.CacheKey.VISIT_DETAIL + yyyyMMdd;
            Map<Object, Object> dayDetails = redisTemplate.opsForHash().entries(ddDetailKey);
            dayDetails.forEach((key, value) -> {
                VisitDataDTO.totalDeail deail = new VisitDataDTO.totalDeail();
                deail.setDate(yyyy_MM_dd + ConstInterface.Symbol.SPACE + key + ConstInterface.Symbol.COLON +"00");
                deail.setCount(Long.valueOf(String.valueOf(value)));
                dto.addDayDeails(deail);
            });
            dto.setCurrentQueryDetails(dto.getDayDeails().stream().sorted(Comparator.comparing(temp -> {
                return DateUtils.strToDate(temp.getDate(), DateUtils.YYYY_MM_DD_HH_MM).getTime();
            })).collect(Collectors.toList()));
        }
        return dto;
    }

    private void addUserVisit(Integer userId,String userName) {
        if (null == userId || StringUtils.isBlank(userName)) {
            return;
        }
        String yyyy = DateUtils.nowDateToStr(DateUtils.YYYY);
        String yyyyMM = DateUtils.nowDateToStr(DateUtils.YYYYMM);
        String yyyyMMdd = DateUtils.nowDateToStr(DateUtils.YYYYMMDD);
        String YYYYMMDDHH = DateUtils.nowDateToStr(DateUtils.YYYYMMDDHH);
        String mm = DateUtils.nowDateToStr(DateUtils.MM);
        String dd = DateUtils.nowDateToStr(DateUtils.DD);
        String hh = DateUtils.nowDateToStr(DateUtils.HH);

        String currentYearKey = ConstInterface.CacheKey.USER_VISIT_TOTAL + yyyy;
        redisTemplate.opsForHyperLogLog().add(currentYearKey, userId);
        redisTemplate.expire(currentYearKey, DAYS_365, TimeUnit.DAYS);

        String currentMonthKey = ConstInterface.CacheKey.USER_VISIT_TOTAL + yyyyMM;
        redisTemplate.opsForHyperLogLog().add(currentMonthKey, userId);
        redisTemplate.expire(currentMonthKey, DAYS_31, TimeUnit.DAYS);

        String yearDetailKey = ConstInterface.CacheKey.USER_VISIT_DETAIL + yyyy;
        redisTemplate.opsForHash().put(yearDetailKey, mm, redisTemplate.opsForHyperLogLog().size(currentMonthKey));
        redisTemplate.expire(yearDetailKey, YEAR_1, TimeUnit.DAYS);

        String currentDayKey = ConstInterface.CacheKey.USER_VISIT_TOTAL + yyyyMMdd;
        redisTemplate.opsForHyperLogLog().add(currentDayKey, userId);
        redisTemplate.expire(currentDayKey, DAYS_1, TimeUnit.DAYS);

        String mmDetailKey = ConstInterface.CacheKey.USER_VISIT_DETAIL + yyyyMM;
        redisTemplate.opsForHash().put(mmDetailKey, dd, redisTemplate.opsForHyperLogLog().size(currentDayKey));
        redisTemplate.expire(mmDetailKey, DAYS_31, TimeUnit.DAYS);

        int weekOfYear = new DateTime().weekOfWeekyear().get();
        int dayOfWeek = new DateTime().dayOfWeek().get();
        String weekDetailKey = ConstInterface.CacheKey.USER_VISIT_DETAIL + yyyy + ":week:" + weekOfYear;
        redisTemplate.opsForHash().put(weekDetailKey, String.valueOf(dayOfWeek), redisTemplate.opsForHyperLogLog().size(currentDayKey));
        redisTemplate.expire(weekDetailKey, DAYS_7, TimeUnit.DAYS);

        String currentHourKey = ConstInterface.CacheKey.USER_VISIT_TOTAL + YYYYMMDDHH;
        redisTemplate.opsForHyperLogLog().add(currentHourKey, userId);
        redisTemplate.expire(currentHourKey, HOURS_1, TimeUnit.HOURS);

        String ddDetailKey = ConstInterface.CacheKey.USER_VISIT_DETAIL + yyyyMMdd;
        redisTemplate.opsForHash().put(ddDetailKey, hh, redisTemplate.opsForHyperLogLog().size(currentHourKey));
        redisTemplate.expire(ddDetailKey, DAYS_1, TimeUnit.DAYS);


        //user visit排行榜
        String currentYearDetailKey = ConstInterface.CacheKey.USER_VISIT_RANGE_DETAIL + yyyy;
        redisTemplate.opsForZSet().incrementScore(currentYearDetailKey, userName, 1);
        redisTemplate.expire(currentYearDetailKey, DAYS_365, TimeUnit.DAYS);

        String currentMonthDetailKey = ConstInterface.CacheKey.USER_VISIT_RANGE_DETAIL + yyyyMM;
        redisTemplate.opsForZSet().incrementScore(currentMonthDetailKey, userName, 1);
        redisTemplate.expire(currentMonthDetailKey, DAYS_31, TimeUnit.DAYS);

        String currentWeekDetailKey = ConstInterface.CacheKey.USER_VISIT_RANGE_DETAIL + yyyy + ":week:" + weekOfYear;
        redisTemplate.opsForZSet().incrementScore(currentWeekDetailKey, userName, 1);
        redisTemplate.expire(currentWeekDetailKey, DAYS_7, TimeUnit.DAYS);

        String currentDayDetailKey = ConstInterface.CacheKey.USER_VISIT_RANGE_DETAIL + yyyyMMdd;
        redisTemplate.opsForZSet().incrementScore(currentDayDetailKey, userName, 1);
        redisTemplate.expire(currentDayDetailKey, DAYS_1, TimeUnit.DAYS);
    }

    @Override
    public UserVisitDataDTO countUserVisit(AnalysisParam param) {
        UserVisitDataDTO dto = new UserVisitDataDTO();
        String currentYearKey = ConstInterface.CacheKey.USER_VISIT_TOTAL + DateUtils.nowDateToStr(DateUtils.YYYY);
        String currentMonthKey = ConstInterface.CacheKey.USER_VISIT_TOTAL + DateUtils.nowDateToStr(DateUtils.YYYYMM);
        String currentDayKey = ConstInterface.CacheKey.USER_VISIT_TOTAL + DateUtils.nowDateToStr(DateUtils.YYYYMMDD);
        dto.setCurrentYearTotal(redisTemplate.opsForHyperLogLog().size(currentYearKey));
        dto.setCurrentMonthTotal(redisTemplate.opsForHyperLogLog().size(currentMonthKey));
        dto.setCurrentDayTotal(redisTemplate.opsForHyperLogLog().size(currentDayKey));

        String yyyy = DateUtils.nowDateToStr(DateUtils.YYYY);
        String yyyyMM = DateUtils.nowDateToStr(DateUtils.YYYYMM);
        String yyyyMMdd = DateUtils.nowDateToStr(DateUtils.YYYYMMDD);
        String yyyy_MM = DateUtils.nowDateToStr(DateUtils.YYYY_MM);
        String yyyy_MM_dd = DateUtils.nowDateToStr(DateUtils.YYYY_MM_DD);

        if(DateTypeEnum.YEAR.getType().equals(param.getDateType())){
            String yearDetailKey = ConstInterface.CacheKey.USER_VISIT_DETAIL + yyyy;
            Map<Object, Object> yearDetails = redisTemplate.opsForHash().entries(yearDetailKey);
            yearDetails.forEach((key, value) -> {
                UserVisitDataDTO.Detail deail = new UserVisitDataDTO.Detail();
                deail.setDate(yyyy + ConstInterface.Symbol.MIDDLE_LINE + key);
                deail.setCount(Long.valueOf(String.valueOf(value)));
                dto.addYearDeails(deail);
            });
            dto.setCurrentQueryDetails(dto.getYearDeails().stream().sorted(Comparator.comparing(temp -> {
                return DateUtils.strToDate(temp.getDate(), DateUtils.YYYY_MM).getTime();
            })).collect(Collectors.toList()));
        }

        if(DateTypeEnum.MONTH.getType().equals(param.getDateType())){
            String mmDetailKey = ConstInterface.CacheKey.USER_VISIT_DETAIL + yyyyMM;
            Map<Object, Object> monthDetails = redisTemplate.opsForHash().entries(mmDetailKey);
            monthDetails.forEach((key, value) -> {
                UserVisitDataDTO.Detail deail = new UserVisitDataDTO.Detail();
                deail.setDate(yyyy_MM + ConstInterface.Symbol.MIDDLE_LINE + key);
                deail.setCount(Long.valueOf(String.valueOf(value)));
                dto.addMonthDeails(deail);
            });
            dto.setCurrentQueryDetails(dto.getMonthDeails().stream().sorted(Comparator.comparing(temp -> {
                return DateUtils.strToDate(temp.getDate(), DateUtils.YYYY_MM_DD).getTime();
            })).collect(Collectors.toList()));
        }

        if(DateTypeEnum.WEEK.getType().equals(param.getDateType())){
            int weekOfYear = new DateTime().weekOfWeekyear().get();
            String weekDetailKey = ConstInterface.CacheKey.USER_VISIT_DETAIL + yyyy + ":week:" + weekOfYear;
            Map<Object, Object> weekDetails = redisTemplate.opsForHash().entries(weekDetailKey);
            weekDetails.forEach((key, value) -> {
                UserVisitDataDTO.Detail deail = new UserVisitDataDTO.Detail();
                deail.setDate(yyyy + "年" + weekOfYear + "周" + key + "天");
                deail.setCount(Long.valueOf(String.valueOf(value)));
                dto.addWeekDeails(deail);
            });
            dto.setCurrentQueryDetails(dto.getWeekDeails());
        }

        if(DateTypeEnum.TODAY.getType().equals(param.getDateType())){
            String ddDetailKey = ConstInterface.CacheKey.USER_VISIT_DETAIL + yyyyMMdd;
            Map<Object, Object> dayDetails = redisTemplate.opsForHash().entries(ddDetailKey);
            dayDetails.forEach((key, value) -> {
                UserVisitDataDTO.Detail deail = new UserVisitDataDTO.Detail();
                deail.setDate(yyyy_MM_dd + ConstInterface.Symbol.SPACE + key + ConstInterface.Symbol.COLON +"00");
                deail.setCount(Long.valueOf(String.valueOf(value)));
                dto.addDayDeails(deail);
            });
            dto.setCurrentQueryDetails(dto.getDayDeails().stream().sorted(Comparator.comparing(temp -> {
                return DateUtils.strToDate(temp.getDate(), DateUtils.YYYY_MM_DD_HH_MM).getTime();
            })).collect(Collectors.toList()));
        }


        //user visit排行榜
        if(DateTypeEnum.YEAR.getType().equals(param.getDateType())){
            String yearDetailKey = ConstInterface.CacheKey.USER_VISIT_RANGE_DETAIL + yyyy;
            this.buildUserRangeVisitDetail(dto, yearDetailKey);
        }

        if(DateTypeEnum.MONTH.getType().equals(param.getDateType())){
            String mmDetailKey = ConstInterface.CacheKey.USER_VISIT_RANGE_DETAIL + yyyyMM;
            this.buildUserRangeVisitDetail(dto, mmDetailKey);
        }

        if(DateTypeEnum.WEEK.getType().equals(param.getDateType())){
            int weekOfYear = new DateTime().weekOfWeekyear().get();
            String weekDetailKey = ConstInterface.CacheKey.USER_VISIT_RANGE_DETAIL + yyyy + ":week:" + weekOfYear;
            this.buildUserRangeVisitDetail(dto, weekDetailKey);
        }

        if(DateTypeEnum.TODAY.getType().equals(param.getDateType())){
            String ddDetailKey = ConstInterface.CacheKey.USER_VISIT_RANGE_DETAIL + yyyyMMdd;
            this.buildUserRangeVisitDetail(dto, ddDetailKey);
        }

        return dto;
    }

    @Override
    @Async
    public void addRedisConfigVisit(String redisConfigId) {
        String yyyy = DateUtils.nowDateToStr(DateUtils.YYYY);
        String yyyyMM = DateUtils.nowDateToStr(DateUtils.YYYYMM);
        String yyyyMMdd = DateUtils.nowDateToStr(DateUtils.YYYYMMDD);

        String currentYearDetailKey = ConstInterface.CacheKey.REDIS_CONFIG_VISIT_DETAIL + yyyy;
        redisTemplate.opsForZSet().incrementScore(currentYearDetailKey, redisConfigId, 1);
        redisTemplate.expire(currentYearDetailKey, DAYS_365, TimeUnit.DAYS);

        String currentMonthDetailKey = ConstInterface.CacheKey.REDIS_CONFIG_VISIT_DETAIL + yyyyMM;
        redisTemplate.opsForZSet().incrementScore(currentMonthDetailKey, redisConfigId, 1);
        redisTemplate.expire(currentMonthDetailKey, DAYS_31, TimeUnit.DAYS);

        int weekOfYear = new DateTime().weekOfWeekyear().get();
        String currentWeekDetailKey = ConstInterface.CacheKey.REDIS_CONFIG_VISIT_DETAIL + yyyy + ":week:" + weekOfYear;
        redisTemplate.opsForZSet().incrementScore(currentWeekDetailKey, redisConfigId, 1);
        redisTemplate.expire(currentWeekDetailKey, DAYS_7, TimeUnit.DAYS);

        String currentDayDetailKey = ConstInterface.CacheKey.REDIS_CONFIG_VISIT_DETAIL + yyyyMMdd;
        redisTemplate.opsForZSet().incrementScore(currentDayDetailKey, redisConfigId, 1);
        redisTemplate.expire(currentDayDetailKey, DAYS_1, TimeUnit.DAYS);
    }

    @Override
    public RedisConfigVisitDataDTO countRedisConfigVisit(AnalysisParam param) {
        RedisConfigVisitDataDTO dto = new RedisConfigVisitDataDTO();
        String yyyy = DateUtils.nowDateToStr(DateUtils.YYYY);
        String yyyyMM = DateUtils.nowDateToStr(DateUtils.YYYYMM);
        String yyyyMMdd = DateUtils.nowDateToStr(DateUtils.YYYYMMDD);

        if(DateTypeEnum.YEAR.getType().equals(param.getDateType())){
            String yearDetailKey = ConstInterface.CacheKey.REDIS_CONFIG_VISIT_DETAIL + yyyy;
            this.buildRedisConfigVisitDetail(dto, yearDetailKey);
        }

        if(DateTypeEnum.MONTH.getType().equals(param.getDateType())){
            String mmDetailKey = ConstInterface.CacheKey.REDIS_CONFIG_VISIT_DETAIL + yyyyMM;
            this.buildRedisConfigVisitDetail(dto, mmDetailKey);
        }

        if(DateTypeEnum.WEEK.getType().equals(param.getDateType())){
            int weekOfYear = new DateTime().weekOfWeekyear().get();
            String weekDetailKey = ConstInterface.CacheKey.REDIS_CONFIG_VISIT_DETAIL + yyyy + ":week:" + weekOfYear;
            this.buildRedisConfigVisitDetail(dto, weekDetailKey);
        }

        if(DateTypeEnum.TODAY.getType().equals(param.getDateType())){
            String ddDetailKey = ConstInterface.CacheKey.REDIS_CONFIG_VISIT_DETAIL + yyyyMMdd;
            this.buildRedisConfigVisitDetail(dto, ddDetailKey);
        }
        return dto;
    }

    private void buildUserRangeVisitDetail(UserVisitDataDTO dto, String detailKey) {
        Set<ZSetOperations.TypedTuple<Object>> details = redisTemplate.opsForZSet().reverseRangeWithScores(detailKey, 0, 50);
        details.forEach(temp -> {
            UserVisitDataDTO.Detail deail = new UserVisitDataDTO.Detail();
            deail.setDate(String.valueOf(temp.getValue()));
            deail.setCount(temp.getScore());
            dto.addRangeDeails(deail);
        });
    }

    private void buildRedisConfigVisitDetail(RedisConfigVisitDataDTO dto, String detailKey) {
        Set<ZSetOperations.TypedTuple<Object>> details = redisTemplate.opsForZSet().reverseRangeWithScores(detailKey, 0, 50);
        details.forEach(temp -> {
            String id = String.valueOf(temp.getValue());
            RedisConfigVisitDataDTO.Detail deail = new RedisConfigVisitDataDTO.Detail();
            RedisConfigPO configPO = (RedisConfigPO) cacheService.getCacher(ConstInterface.Cacher.REDIS_CONFIG_CACHER).get(id);
            deail.setName(configPO.getName());
            deail.setCount(temp.getScore());
            dto.addDetails(deail);
        });
    }
}
