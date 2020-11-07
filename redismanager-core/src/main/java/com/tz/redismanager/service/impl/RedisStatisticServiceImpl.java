package com.tz.redismanager.service.impl;

import com.tz.redismanager.constant.ConstInterface;
import com.tz.redismanager.dao.domain.po.RedisConfigPO;
import com.tz.redismanager.domain.dto.RedisConfigVisitDataDTO;
import com.tz.redismanager.domain.dto.UserVisitDataDTO;
import com.tz.redismanager.domain.dto.VisitDataDTO;
import com.tz.redismanager.domain.param.AnalysisParam;
import com.tz.redismanager.enm.DateTypeEnum;
import com.tz.redismanager.security.domain.AuthContext;
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
    private RedisTemplate<String, Object> redisTemplate;
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
        DateUtils.CurrentDate currentDate = DateUtils.getCurrentDate();
        String uuid = UUIDUtils.generateId();
        String totalKey = ConstInterface.CacheKey.VISIT_TOTAL_ALL;
        incrementVisit(totalKey, uuid, YEAR_10, TimeUnit.DAYS);

        String currentYearKey = ConstInterface.CacheKey.VISIT_TOTAL + currentDate.getYYYY();
        incrementVisit(currentYearKey, uuid, DAYS_365, TimeUnit.DAYS);

        String totalDetailKey = ConstInterface.CacheKey.VISIT_DETAIL + "all";
        incrementDetailVisit(totalDetailKey, currentDate.getYYYY(), currentYearKey, YEAR_10);

        String currentMonthKey = ConstInterface.CacheKey.VISIT_TOTAL + currentDate.getYYYYMM();
        incrementVisit(currentMonthKey, uuid, DAYS_31, TimeUnit.DAYS);

        String yearDetailKey = ConstInterface.CacheKey.VISIT_DETAIL + currentDate.getYYYY();
        incrementDetailVisit(yearDetailKey, currentDate.getMM(), currentMonthKey, YEAR_1);

        String currentDayKey = ConstInterface.CacheKey.VISIT_TOTAL + currentDate.getYYYYMMDD();
        incrementVisit(currentDayKey, uuid, DAYS_1, TimeUnit.DAYS);

        String mmDetailKey = ConstInterface.CacheKey.VISIT_DETAIL + currentDate.getYYYYMM();
        incrementDetailVisit(mmDetailKey, currentDate.getDD(), currentDayKey, DAYS_31);

        int weekOfYear = new DateTime().weekOfWeekyear().get();
        int dayOfWeek = new DateTime().dayOfWeek().get();
        String weekDetailKey = ConstInterface.CacheKey.VISIT_DETAIL + currentDate.getYYYY() + ":week:" + weekOfYear;
        incrementDetailVisit(weekDetailKey, String.valueOf(dayOfWeek), currentDayKey, DAYS_7);

        String currentHourKey = ConstInterface.CacheKey.VISIT_TOTAL + currentDate.getYYYYMMDDHH();
        incrementVisit(currentHourKey, uuid, HOURS_1, TimeUnit.HOURS);

        String ddDetailKey = ConstInterface.CacheKey.VISIT_DETAIL + currentDate.getYYYYMMDD();
        incrementDetailVisit(ddDetailKey, currentDate.getHH(), currentHourKey, DAYS_1);
    }

    @Override
    public VisitDataDTO countVisit(AnalysisParam param) {
        VisitDataDTO dto = new VisitDataDTO();
        DateUtils.CurrentDate currentDate = DateUtils.getCurrentDate();
        String totalKey = ConstInterface.CacheKey.VISIT_TOTAL_ALL;
        String currentYearKey = ConstInterface.CacheKey.VISIT_TOTAL + currentDate.getYYYY();
        String currentMonthKey = ConstInterface.CacheKey.VISIT_TOTAL + currentDate.getYYYYMM();
        String currentDayKey = ConstInterface.CacheKey.VISIT_TOTAL + currentDate.getYYYYMMDD();
        dto.setTotal(redisTemplate.opsForHyperLogLog().size(totalKey));
        dto.setCurrentYearTotal(redisTemplate.opsForHyperLogLog().size(currentYearKey));
        dto.setCurrentMonthTotal(redisTemplate.opsForHyperLogLog().size(currentMonthKey));
        dto.setCurrentDayTotal(redisTemplate.opsForHyperLogLog().size(currentDayKey));

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

        if (DateTypeEnum.ALL.getType().equals(param.getDateType())) {
            dto.setCurrentQueryDetails(dto.getTotalDeails());
        }
        if (DateTypeEnum.YEAR.getType().equals(param.getDateType())) {
            String yearDetailKey = ConstInterface.CacheKey.VISIT_DETAIL + currentDate.getYYYY();
            Map<Object, Object> yearDetails = redisTemplate.opsForHash().entries(yearDetailKey);
            yearDetails.forEach((key, value) -> {
                VisitDataDTO.totalDeail deail = new VisitDataDTO.totalDeail();
                deail.setDate(currentDate.getYYYY() + ConstInterface.Symbol.MIDDLE_LINE + key);
                deail.setCount(Long.valueOf(String.valueOf(value)));
                dto.addYearDeails(deail);
            });
            dto.setCurrentQueryDetails(dto.getYearDeails().stream().sorted(Comparator.comparing(temp -> {
                return DateUtils.strToDate(temp.getDate(), DateUtils.YYYY_MM).getTime();
            })).collect(Collectors.toList()));
        }

        if (DateTypeEnum.MONTH.getType().equals(param.getDateType())) {
            String mmDetailKey = ConstInterface.CacheKey.VISIT_DETAIL + currentDate.getYYYYMM();
            Map<Object, Object> monthDetails = redisTemplate.opsForHash().entries(mmDetailKey);
            monthDetails.forEach((key, value) -> {
                VisitDataDTO.totalDeail deail = new VisitDataDTO.totalDeail();
                deail.setDate(currentDate.getYYYY_MM() + ConstInterface.Symbol.MIDDLE_LINE + key);
                deail.setCount(Long.valueOf(String.valueOf(value)));
                dto.addMonthDeails(deail);
            });
            dto.setCurrentQueryDetails(dto.getMonthDeails().stream().sorted(Comparator.comparing(temp -> {
                return DateUtils.strToDate(temp.getDate(), DateUtils.YYYY_MM_DD).getTime();
            })).collect(Collectors.toList()));
        }

        if (DateTypeEnum.WEEK.getType().equals(param.getDateType())) {
            int weekOfYear = new DateTime().weekOfWeekyear().get();
            String weekDetailKey = ConstInterface.CacheKey.VISIT_DETAIL + currentDate.getYYYY() + ":week:" + weekOfYear;
            Map<Object, Object> weekDetails = redisTemplate.opsForHash().entries(weekDetailKey);
            weekDetails.forEach((key, value) -> {
                VisitDataDTO.totalDeail deail = new VisitDataDTO.totalDeail();
                deail.setDate(currentDate.getYYYY() + "年" + weekOfYear + "周" + key + "天");
                deail.setCount(Long.valueOf(String.valueOf(value)));
                dto.addWeekDeails(deail);
            });
            dto.setCurrentQueryDetails(dto.getWeekDeails());
        }

        if (DateTypeEnum.TODAY.getType().equals(param.getDateType())) {
            String ddDetailKey = ConstInterface.CacheKey.VISIT_DETAIL + currentDate.getYYYYMMDD();
            Map<Object, Object> dayDetails = redisTemplate.opsForHash().entries(ddDetailKey);
            dayDetails.forEach((key, value) -> {
                VisitDataDTO.totalDeail deail = new VisitDataDTO.totalDeail();
                deail.setDate(currentDate.getYYYY_MM_DD() + ConstInterface.Symbol.SPACE + key + ConstInterface.Symbol.COLON + "00");
                deail.setCount(Long.valueOf(String.valueOf(value)));
                dto.addDayDeails(deail);
            });
            dto.setCurrentQueryDetails(dto.getDayDeails().stream().sorted(Comparator.comparing(temp -> {
                return DateUtils.strToDate(temp.getDate(), DateUtils.YYYY_MM_DD_HH_MM).getTime();
            })).collect(Collectors.toList()));
        }
        return dto;
    }

    @Override
    public UserVisitDataDTO countUserVisit(AnalysisParam param) {
        UserVisitDataDTO dto = new UserVisitDataDTO();
        DateUtils.CurrentDate currentDate = DateUtils.getCurrentDate();
        if (DateTypeEnum.YEAR.getType().equals(param.getDateType())) {
            String yearDetailKey = ConstInterface.CacheKey.USER_VISIT_DETAIL + currentDate.getYYYY();
            Map<Object, Object> yearDetails = redisTemplate.opsForHash().entries(yearDetailKey);
            yearDetails.forEach((key, value) -> {
                UserVisitDataDTO.Detail deail = new UserVisitDataDTO.Detail();
                deail.setDate(currentDate.getYYYY() + ConstInterface.Symbol.MIDDLE_LINE + key);
                deail.setCount(Long.valueOf(String.valueOf(value)));
                dto.addYearDeails(deail);
            });
            dto.setCurrentQueryDetails(dto.getYearDeails().stream().sorted(Comparator.comparing(temp -> {
                return DateUtils.strToDate(temp.getDate(), DateUtils.YYYY_MM).getTime();
            })).collect(Collectors.toList()));
        }

        if (DateTypeEnum.MONTH.getType().equals(param.getDateType())) {
            String mmDetailKey = ConstInterface.CacheKey.USER_VISIT_DETAIL + currentDate.getYYYYMM();
            Map<Object, Object> monthDetails = redisTemplate.opsForHash().entries(mmDetailKey);
            monthDetails.forEach((key, value) -> {
                UserVisitDataDTO.Detail deail = new UserVisitDataDTO.Detail();
                deail.setDate(currentDate.getYYYY_MM() + ConstInterface.Symbol.MIDDLE_LINE + key);
                deail.setCount(Long.valueOf(String.valueOf(value)));
                dto.addMonthDeails(deail);
            });
            dto.setCurrentQueryDetails(dto.getMonthDeails().stream().sorted(Comparator.comparing(temp -> {
                return DateUtils.strToDate(temp.getDate(), DateUtils.YYYY_MM_DD).getTime();
            })).collect(Collectors.toList()));
        }

        if (DateTypeEnum.WEEK.getType().equals(param.getDateType())) {
            int weekOfYear = new DateTime().weekOfWeekyear().get();
            String weekDetailKey = ConstInterface.CacheKey.USER_VISIT_DETAIL + currentDate.getYYYY() + ":week:" + weekOfYear;
            Map<Object, Object> weekDetails = redisTemplate.opsForHash().entries(weekDetailKey);
            weekDetails.forEach((key, value) -> {
                UserVisitDataDTO.Detail deail = new UserVisitDataDTO.Detail();
                deail.setDate(currentDate.getYYYY() + "年" + weekOfYear + "周" + key + "天");
                deail.setCount(Long.valueOf(String.valueOf(value)));
                dto.addWeekDeails(deail);
            });
            dto.setCurrentQueryDetails(dto.getWeekDeails());
        }

        if (DateTypeEnum.TODAY.getType().equals(param.getDateType())) {
            String ddDetailKey = ConstInterface.CacheKey.USER_VISIT_DETAIL + currentDate.getYYYYMMDD();
            Map<Object, Object> dayDetails = redisTemplate.opsForHash().entries(ddDetailKey);
            dayDetails.forEach((key, value) -> {
                UserVisitDataDTO.Detail deail = new UserVisitDataDTO.Detail();
                deail.setDate(currentDate.getYYYY_MM_DD() + ConstInterface.Symbol.SPACE + key + ConstInterface.Symbol.COLON + "00");
                deail.setCount(Long.valueOf(String.valueOf(value)));
                dto.addDayDeails(deail);
            });
            dto.setCurrentQueryDetails(dto.getDayDeails().stream().sorted(Comparator.comparing(temp -> {
                return DateUtils.strToDate(temp.getDate(), DateUtils.YYYY_MM_DD_HH_MM).getTime();
            })).collect(Collectors.toList()));
        }

        //user visit排行榜
        if (DateTypeEnum.YEAR.getType().equals(param.getDateType())) {
            String yearDetailKey = ConstInterface.CacheKey.USER_VISIT_RANK_DETAIL + currentDate.getYYYY();
            this.buildUserRankVisitDetail(dto, yearDetailKey);
        }

        if (DateTypeEnum.MONTH.getType().equals(param.getDateType())) {
            String mmDetailKey = ConstInterface.CacheKey.USER_VISIT_RANK_DETAIL + currentDate.getYYYYMM();
            this.buildUserRankVisitDetail(dto, mmDetailKey);
        }

        if (DateTypeEnum.WEEK.getType().equals(param.getDateType())) {
            int weekOfYear = new DateTime().weekOfWeekyear().get();
            String weekDetailKey = ConstInterface.CacheKey.USER_VISIT_RANK_DETAIL + currentDate.getYYYY() + ":week:" + weekOfYear;
            this.buildUserRankVisitDetail(dto, weekDetailKey);
        }

        if (DateTypeEnum.TODAY.getType().equals(param.getDateType())) {
            String ddDetailKey = ConstInterface.CacheKey.USER_VISIT_RANK_DETAIL + currentDate.getYYYYMMDD();
            this.buildUserRankVisitDetail(dto, ddDetailKey);
        }
        return dto;
    }

    @Override
    @Async
    public void addRedisConfigVisit(String redisConfigId) {
        DateUtils.CurrentDate currentDate = DateUtils.getCurrentDate();

        String currentYearDetailKey = ConstInterface.CacheKey.REDIS_CONFIG_VISIT_RANK_DETAIL + currentDate.getYYYY();
        incrementRankVisit(currentYearDetailKey, redisConfigId, DAYS_365);

        String currentMonthDetailKey = ConstInterface.CacheKey.REDIS_CONFIG_VISIT_RANK_DETAIL + currentDate.getYYYYMM();
        incrementRankVisit(currentMonthDetailKey, redisConfigId, DAYS_31);

        int weekOfYear = new DateTime().weekOfWeekyear().get();
        String currentWeekDetailKey = ConstInterface.CacheKey.REDIS_CONFIG_VISIT_RANK_DETAIL + currentDate.getYYYY() + ":week:" + weekOfYear;
        incrementRankVisit(currentWeekDetailKey, redisConfigId, DAYS_7);

        String currentDayDetailKey = ConstInterface.CacheKey.REDIS_CONFIG_VISIT_RANK_DETAIL + currentDate.getYYYYMMDD();
        incrementRankVisit(currentDayDetailKey, redisConfigId, DAYS_1);
    }

    @Override
    public RedisConfigVisitDataDTO countRedisConfigVisit(AnalysisParam param) {
        RedisConfigVisitDataDTO dto = new RedisConfigVisitDataDTO();
        DateUtils.CurrentDate currentDate = DateUtils.getCurrentDate();

        if (DateTypeEnum.YEAR.getType().equals(param.getDateType())) {
            String yearDetailKey = ConstInterface.CacheKey.REDIS_CONFIG_VISIT_RANK_DETAIL + currentDate.getYYYY();
            this.buildRedisConfigVisitDetail(dto, yearDetailKey);
        }

        if (DateTypeEnum.MONTH.getType().equals(param.getDateType())) {
            String mmDetailKey = ConstInterface.CacheKey.REDIS_CONFIG_VISIT_RANK_DETAIL + currentDate.getYYYYMM();
            this.buildRedisConfigVisitDetail(dto, mmDetailKey);
        }

        if (DateTypeEnum.WEEK.getType().equals(param.getDateType())) {
            int weekOfYear = new DateTime().weekOfWeekyear().get();
            String weekDetailKey = ConstInterface.CacheKey.REDIS_CONFIG_VISIT_RANK_DETAIL + currentDate.getYYYY() + ":week:" + weekOfYear;
            this.buildRedisConfigVisitDetail(dto, weekDetailKey);
        }

        if (DateTypeEnum.TODAY.getType().equals(param.getDateType())) {
            String ddDetailKey = ConstInterface.CacheKey.REDIS_CONFIG_VISIT_RANK_DETAIL + currentDate.getYYYYMMDD();
            this.buildRedisConfigVisitDetail(dto, ddDetailKey);
        }
        return dto;
    }

    private void addUserVisit(Integer userId, String userName) {
        if (null == userId || StringUtils.isBlank(userName)) {
            return;
        }
        DateUtils.CurrentDate currentDate = DateUtils.getCurrentDate();

        String currentYearKey = ConstInterface.CacheKey.USER_VISIT_TOTAL + currentDate.getYYYY();
        incrementVisit(currentYearKey, userId, DAYS_365, TimeUnit.DAYS);

        String currentMonthKey = ConstInterface.CacheKey.USER_VISIT_TOTAL + currentDate.getYYYYMM();
        incrementVisit(currentMonthKey, userId, DAYS_31, TimeUnit.DAYS);

        String yearDetailKey = ConstInterface.CacheKey.USER_VISIT_DETAIL + currentDate.getYYYY();
        incrementDetailVisit(yearDetailKey, currentDate.getMM(), currentMonthKey, YEAR_1);

        String currentDayKey = ConstInterface.CacheKey.USER_VISIT_TOTAL + currentDate.getYYYYMMDD();
        incrementVisit(currentDayKey, userId, DAYS_1, TimeUnit.DAYS);

        String mmDetailKey = ConstInterface.CacheKey.USER_VISIT_DETAIL + currentDate.getYYYYMM();
        incrementDetailVisit(mmDetailKey, currentDate.getDD(), currentDayKey, DAYS_31);

        int weekOfYear = new DateTime().weekOfWeekyear().get();
        int dayOfWeek = new DateTime().dayOfWeek().get();
        String weekDetailKey = ConstInterface.CacheKey.USER_VISIT_DETAIL + currentDate.getYYYY() + ":week:" + weekOfYear;
        incrementDetailVisit(weekDetailKey, String.valueOf(dayOfWeek), currentDayKey, DAYS_7);

        String currentHourKey = ConstInterface.CacheKey.USER_VISIT_TOTAL + currentDate.getYYYYMMDDHH();
        incrementVisit(currentHourKey, userId, HOURS_1, TimeUnit.HOURS);

        String ddDetailKey = ConstInterface.CacheKey.USER_VISIT_DETAIL + currentDate.getYYYYMMDD();
        incrementDetailVisit(ddDetailKey, currentDate.getHH(), currentHourKey, DAYS_1);

        //user visit排行榜
        String currentYearDetailKey = ConstInterface.CacheKey.USER_VISIT_RANK_DETAIL + currentDate.getYYYY();
        incrementRankVisit(currentYearDetailKey, userName, DAYS_365);

        String currentMonthDetailKey = ConstInterface.CacheKey.USER_VISIT_RANK_DETAIL + currentDate.getYYYYMM();
        incrementRankVisit(currentMonthDetailKey, userName, DAYS_31);

        String currentWeekDetailKey = ConstInterface.CacheKey.USER_VISIT_RANK_DETAIL + currentDate.getYYYY() + ":week:" + weekOfYear;
        incrementRankVisit(currentWeekDetailKey, userName, DAYS_7);

        String currentDayDetailKey = ConstInterface.CacheKey.USER_VISIT_RANK_DETAIL + currentDate.getYYYYMMDD();
        incrementRankVisit(currentDayDetailKey, userName, DAYS_1);
    }

    /**
     * 增加详情访问数据
     *
     * @param cacheKey      缓存key
     * @param cacheValue    缓存value
     * @param countCacheKey 查询数量缓存key
     * @param expireDay     过期天数
     */
    private void incrementDetailVisit(String cacheKey, String cacheValue, String countCacheKey, Integer expireDay) {
        redisTemplate.opsForHash().put(cacheKey, cacheValue, redisTemplate.opsForHyperLogLog().size(countCacheKey));
        redisTemplate.expire(cacheKey, expireDay, TimeUnit.DAYS);
    }

    /**
     * 增加访问数据
     *
     * @param cacheKey   缓存key
     * @param cacheValue 缓存value
     * @param expireTime 过期时间
     * @param timeUnit   时间单位
     */
    private void incrementVisit(String cacheKey, Object cacheValue, Integer expireTime, TimeUnit timeUnit) {
        redisTemplate.opsForHyperLogLog().add(cacheKey, cacheValue);
        redisTemplate.expire(cacheKey, expireTime, timeUnit);
    }

    /**
     * 增加排行榜数据
     *
     * @param cacheKey   缓存key
     * @param cacheValue 缓存value
     * @param expireDay  过期天数
     */
    private void incrementRankVisit(String cacheKey, String cacheValue, Integer expireDay) {
        redisTemplate.opsForZSet().incrementScore(cacheKey, cacheValue, 1);
        redisTemplate.expire(cacheKey, expireDay, TimeUnit.DAYS);
    }

    private void buildUserRankVisitDetail(UserVisitDataDTO dto, String detailKey) {
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
