package com.tz.redismanager.service.impl;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.tz.redismanager.constant.ConstInterface;
import com.tz.redismanager.dao.domain.po.RedisConfigPO;
import com.tz.redismanager.domain.dto.RedisConfigVisitDataDTO;
import com.tz.redismanager.domain.dto.UserVisitDataDTO;
import com.tz.redismanager.domain.dto.VisitDataDTO;
import com.tz.redismanager.domain.param.AnalysisParam;
import com.tz.redismanager.enm.DateTypeEnum;
import com.tz.redismanager.security.domain.AuthContext;
import com.tz.redismanager.service.IRedisConfigService;
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

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * <p>Redis统计实现</p>
 *
 * @author tuanzuo
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
    private static final long START = 0;
    private static final long END = 50;

    private String userOnlineKey = StringUtils.join(ConstInterface.CacheKey.USER_ONLINE, DateUtils.nowDateToStr(DateUtils.YYYYMMDD));

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;
    @Autowired
    private IRedisTemplateExtService redisTemplateExtService;
    @Autowired
    private IRedisConfigService redisConfigService;

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
        redisTemplate.opsForValue().setBit(userOnlineKey, userId, true);
        redisTemplate.expire(userOnlineKey, HOURS_24, TimeUnit.HOURS);
    }

    @Override
    public void removeOnlineUser(Integer userId) {
        if (null == userId) {
            return;
        }
        redisTemplate.opsForValue().setBit(userOnlineKey, userId, false);
    }

    @Override
    public Long countOnlineUser() {
        Long count = redisTemplateExtService.bitCount(userOnlineKey);
        return null == count ? 0 : count;
    }

    @Override
    public void addVisit() {
        DateUtils.CurrentDate currentDate = DateUtils.getCurrentDate();
        String uuid = UUIDUtils.generateId();
        String totalKey = ConstInterface.CacheKey.VISIT_TOTAL_ALL;
        this.incrementVisit(totalKey, uuid, YEAR_10, TimeUnit.DAYS);

        String currentYearKey = StringUtils.join(ConstInterface.CacheKey.VISIT_TOTAL, currentDate.getYYYY());
        this.incrementVisit(currentYearKey, uuid, DAYS_365, TimeUnit.DAYS);

        String totalDetailKey = StringUtils.join(ConstInterface.CacheKey.VISIT_DETAIL, "all");
        this.incrementDetailVisit(totalDetailKey, currentDate.getYYYY(), currentYearKey, YEAR_10);

        String currentMonthKey = StringUtils.join(ConstInterface.CacheKey.VISIT_TOTAL, currentDate.getYYYYMM());
        this.incrementVisit(currentMonthKey, uuid, DAYS_31, TimeUnit.DAYS);

        String yearDetailKey = StringUtils.join(ConstInterface.CacheKey.VISIT_DETAIL, currentDate.getYYYY());
        this.incrementDetailVisit(yearDetailKey, currentDate.getMM(), currentMonthKey, YEAR_1);

        String currentDayKey = StringUtils.join(ConstInterface.CacheKey.VISIT_TOTAL, currentDate.getYYYYMMDD());
        this.incrementVisit(currentDayKey, uuid, DAYS_1, TimeUnit.DAYS);

        String mmDetailKey = StringUtils.join(ConstInterface.CacheKey.VISIT_DETAIL, currentDate.getYYYYMM());
        this.incrementDetailVisit(mmDetailKey, currentDate.getDD(), currentDayKey, DAYS_31);

        int weekOfYear = new DateTime().weekOfWeekyear().get();
        int dayOfWeek = new DateTime().dayOfWeek().get();
        String weekDetailKey = StringUtils.join(ConstInterface.CacheKey.VISIT_DETAIL, currentDate.getYYYY(), ":week:", weekOfYear);
        this.incrementDetailVisit(weekDetailKey, String.valueOf(dayOfWeek), currentDayKey, DAYS_7);

        String currentHourKey = StringUtils.join(ConstInterface.CacheKey.VISIT_TOTAL, currentDate.getYYYYMMDDHH());
        this.incrementVisit(currentHourKey, uuid, HOURS_1, TimeUnit.HOURS);

        String ddDetailKey = StringUtils.join(ConstInterface.CacheKey.VISIT_DETAIL, currentDate.getYYYYMMDD());
        this.incrementDetailVisit(ddDetailKey, currentDate.getHH(), currentHourKey, DAYS_1);
    }

    @Override
    public VisitDataDTO countVisit(AnalysisParam param) {
        VisitDataDTO dto = new VisitDataDTO();
        DateUtils.CurrentDate currentDate = DateUtils.getCurrentDate();
        String totalKey = ConstInterface.CacheKey.VISIT_TOTAL_ALL;
        String currentYearKey = StringUtils.join(ConstInterface.CacheKey.VISIT_TOTAL, currentDate.getYYYY());
        String currentMonthKey = StringUtils.join(ConstInterface.CacheKey.VISIT_TOTAL, currentDate.getYYYYMM());
        String currentDayKey = StringUtils.join(ConstInterface.CacheKey.VISIT_TOTAL, currentDate.getYYYYMMDD());
        dto.setTotal(redisTemplate.opsForHyperLogLog().size(totalKey));
        dto.setCurrentYearTotal(redisTemplate.opsForHyperLogLog().size(currentYearKey));
        dto.setCurrentMonthTotal(redisTemplate.opsForHyperLogLog().size(currentMonthKey));
        dto.setCurrentDayTotal(redisTemplate.opsForHyperLogLog().size(currentDayKey));

        String totalDetailKey = StringUtils.join(ConstInterface.CacheKey.VISIT_DETAIL, "all");
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
            String yearDetailKey = StringUtils.join(ConstInterface.CacheKey.VISIT_DETAIL, currentDate.getYYYY());
            Map<Object, Object> yearDetails = redisTemplate.opsForHash().entries(yearDetailKey);
            yearDetails.forEach((key, value) -> {
                VisitDataDTO.totalDeail deail = new VisitDataDTO.totalDeail();
                deail.setDate(StringUtils.join(currentDate.getYYYY(), ConstInterface.Symbol.MIDDLE_LINE, key));
                deail.setCount(Long.valueOf(String.valueOf(value)));
                dto.addYearDeails(deail);
            });
            dto.setCurrentQueryDetails(dto.getYearDeails().stream().sorted(Comparator.comparing(temp -> {
                return DateUtils.strToDate(temp.getDate(), DateUtils.YYYY_MM).getTime();
            })).collect(Collectors.toList()));
        }

        if (DateTypeEnum.MONTH.getType().equals(param.getDateType())) {
            String mmDetailKey = StringUtils.join(ConstInterface.CacheKey.VISIT_DETAIL, currentDate.getYYYYMM());
            Map<Object, Object> monthDetails = redisTemplate.opsForHash().entries(mmDetailKey);
            monthDetails.forEach((key, value) -> {
                VisitDataDTO.totalDeail deail = new VisitDataDTO.totalDeail();
                deail.setDate(StringUtils.join(currentDate.getYYYY_MM(), ConstInterface.Symbol.MIDDLE_LINE, key));
                deail.setCount(Long.valueOf(String.valueOf(value)));
                dto.addMonthDeails(deail);
            });
            dto.setCurrentQueryDetails(dto.getMonthDeails().stream().sorted(Comparator.comparing(temp -> {
                return DateUtils.strToDate(temp.getDate(), DateUtils.YYYY_MM_DD).getTime();
            })).collect(Collectors.toList()));
        }

        if (DateTypeEnum.WEEK.getType().equals(param.getDateType())) {
            int weekOfYear = new DateTime().weekOfWeekyear().get();
            String weekDetailKey = StringUtils.join(ConstInterface.CacheKey.VISIT_DETAIL, currentDate.getYYYY(), ":week:", weekOfYear);
            Map<Object, Object> weekDetails = redisTemplate.opsForHash().entries(weekDetailKey);
            weekDetails.forEach((key, value) -> {
                VisitDataDTO.totalDeail deail = new VisitDataDTO.totalDeail();
                deail.setDate(StringUtils.join(currentDate.getYYYY(), "年", weekOfYear, "周", key, "天"));
                deail.setCount(Long.valueOf(String.valueOf(value)));
                dto.addWeekDeails(deail);
            });
            dto.setCurrentQueryDetails(dto.getWeekDeails());
        }

        if (DateTypeEnum.TODAY.getType().equals(param.getDateType())) {
            String ddDetailKey = StringUtils.join(ConstInterface.CacheKey.VISIT_DETAIL, currentDate.getYYYYMMDD());
            Map<Object, Object> dayDetails = redisTemplate.opsForHash().entries(ddDetailKey);
            dayDetails.forEach((key, value) -> {
                VisitDataDTO.totalDeail deail = new VisitDataDTO.totalDeail();
                deail.setDate(StringUtils.join(currentDate.getYYYY_MM_DD(), ConstInterface.Symbol.SPACE, key, ConstInterface.Symbol.COLON, "00"));
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
            String yearDetailKey = StringUtils.join(ConstInterface.CacheKey.USER_VISIT_DETAIL, currentDate.getYYYY());
            Map<Object, Object> yearDetails = redisTemplate.opsForHash().entries(yearDetailKey);
            yearDetails.forEach((key, value) -> {
                UserVisitDataDTO.Detail deail = new UserVisitDataDTO.Detail();
                deail.setDate(StringUtils.join(currentDate.getYYYY(), ConstInterface.Symbol.MIDDLE_LINE, key));
                deail.setCount(Long.valueOf(String.valueOf(value)));
                dto.addYearDeails(deail);
            });
            dto.setCurrentQueryDetails(dto.getYearDeails().stream().sorted(Comparator.comparing(temp -> {
                return DateUtils.strToDate(temp.getDate(), DateUtils.YYYY_MM).getTime();
            })).collect(Collectors.toList()));
        }

        if (DateTypeEnum.MONTH.getType().equals(param.getDateType())) {
            String mmDetailKey = StringUtils.join(ConstInterface.CacheKey.USER_VISIT_DETAIL, currentDate.getYYYYMM());
            Map<Object, Object> monthDetails = redisTemplate.opsForHash().entries(mmDetailKey);
            monthDetails.forEach((key, value) -> {
                UserVisitDataDTO.Detail deail = new UserVisitDataDTO.Detail();
                deail.setDate(StringUtils.join(currentDate.getYYYY_MM(), ConstInterface.Symbol.MIDDLE_LINE, key));
                deail.setCount(Long.valueOf(String.valueOf(value)));
                dto.addMonthDeails(deail);
            });
            dto.setCurrentQueryDetails(dto.getMonthDeails().stream().sorted(Comparator.comparing(temp -> {
                return DateUtils.strToDate(temp.getDate(), DateUtils.YYYY_MM_DD).getTime();
            })).collect(Collectors.toList()));
        }

        if (DateTypeEnum.WEEK.getType().equals(param.getDateType())) {
            int weekOfYear = new DateTime().weekOfWeekyear().get();
            String weekDetailKey = StringUtils.join(ConstInterface.CacheKey.USER_VISIT_DETAIL, currentDate.getYYYY(), ":week:", weekOfYear);
            Map<Object, Object> weekDetails = redisTemplate.opsForHash().entries(weekDetailKey);
            weekDetails.forEach((key, value) -> {
                UserVisitDataDTO.Detail deail = new UserVisitDataDTO.Detail();
                deail.setDate(StringUtils.join(currentDate.getYYYY(), "年", weekOfYear, "周", key, "天"));
                deail.setCount(Long.valueOf(String.valueOf(value)));
                dto.addWeekDeails(deail);
            });
            dto.setCurrentQueryDetails(dto.getWeekDeails());
        }

        if (DateTypeEnum.TODAY.getType().equals(param.getDateType())) {
            String ddDetailKey = StringUtils.join(ConstInterface.CacheKey.USER_VISIT_DETAIL, currentDate.getYYYYMMDD());
            Map<Object, Object> dayDetails = redisTemplate.opsForHash().entries(ddDetailKey);
            dayDetails.forEach((key, value) -> {
                UserVisitDataDTO.Detail deail = new UserVisitDataDTO.Detail();
                deail.setDate(StringUtils.join(currentDate.getYYYY_MM_DD(), ConstInterface.Symbol.SPACE, key, ConstInterface.Symbol.COLON, "00"));
                deail.setCount(Long.valueOf(String.valueOf(value)));
                dto.addDayDeails(deail);
            });
            dto.setCurrentQueryDetails(dto.getDayDeails().stream().sorted(Comparator.comparing(temp -> {
                return DateUtils.strToDate(temp.getDate(), DateUtils.YYYY_MM_DD_HH_MM).getTime();
            })).collect(Collectors.toList()));
        }

        //user visit排行榜
        if (DateTypeEnum.YEAR.getType().equals(param.getDateType())) {
            String yearDetailKey = StringUtils.join(ConstInterface.CacheKey.USER_VISIT_RANK_DETAIL, currentDate.getYYYY());
            this.buildUserRankVisitDetail(dto, yearDetailKey);
        }

        if (DateTypeEnum.MONTH.getType().equals(param.getDateType())) {
            String mmDetailKey = StringUtils.join(ConstInterface.CacheKey.USER_VISIT_RANK_DETAIL, currentDate.getYYYYMM());
            this.buildUserRankVisitDetail(dto, mmDetailKey);
        }

        if (DateTypeEnum.WEEK.getType().equals(param.getDateType())) {
            int weekOfYear = new DateTime().weekOfWeekyear().get();
            String weekDetailKey = StringUtils.join(ConstInterface.CacheKey.USER_VISIT_RANK_DETAIL, currentDate.getYYYY(), ":week:", weekOfYear);
            this.buildUserRankVisitDetail(dto, weekDetailKey);
        }

        if (DateTypeEnum.TODAY.getType().equals(param.getDateType())) {
            String ddDetailKey = StringUtils.join(ConstInterface.CacheKey.USER_VISIT_RANK_DETAIL, currentDate.getYYYYMMDD());
            this.buildUserRankVisitDetail(dto, ddDetailKey);
        }
        return dto;
    }

    @Override
    @Async
    public void addRedisConfigVisit(String redisConfigId) {
        DateUtils.CurrentDate currentDate = DateUtils.getCurrentDate();

        String currentYearDetailKey = StringUtils.join(ConstInterface.CacheKey.REDIS_CONFIG_VISIT_RANK_DETAIL, currentDate.getYYYY());
        this.incrementRankVisit(currentYearDetailKey, redisConfigId, DAYS_365);

        String currentMonthDetailKey = StringUtils.join(ConstInterface.CacheKey.REDIS_CONFIG_VISIT_RANK_DETAIL, currentDate.getYYYYMM());
        this.incrementRankVisit(currentMonthDetailKey, redisConfigId, DAYS_31);

        int weekOfYear = new DateTime().weekOfWeekyear().get();
        String currentWeekDetailKey = StringUtils.join(ConstInterface.CacheKey.REDIS_CONFIG_VISIT_RANK_DETAIL, currentDate.getYYYY(), ":week:", weekOfYear);
        this.incrementRankVisit(currentWeekDetailKey, redisConfigId, DAYS_7);

        String currentDayDetailKey = StringUtils.join(ConstInterface.CacheKey.REDIS_CONFIG_VISIT_RANK_DETAIL, currentDate.getYYYYMMDD());
        this.incrementRankVisit(currentDayDetailKey, redisConfigId, DAYS_1);
    }

    @Override
    public RedisConfigVisitDataDTO countRedisConfigVisit(AnalysisParam param) {
        RedisConfigVisitDataDTO dto = new RedisConfigVisitDataDTO();
        DateUtils.CurrentDate currentDate = DateUtils.getCurrentDate();

        if (DateTypeEnum.YEAR.getType().equals(param.getDateType())) {
            String yearDetailKey = StringUtils.join(ConstInterface.CacheKey.REDIS_CONFIG_VISIT_RANK_DETAIL, currentDate.getYYYY());
            this.buildRedisConfigVisitDetail(dto, yearDetailKey);
        }

        if (DateTypeEnum.MONTH.getType().equals(param.getDateType())) {
            String mmDetailKey = StringUtils.join(ConstInterface.CacheKey.REDIS_CONFIG_VISIT_RANK_DETAIL, currentDate.getYYYYMM());
            this.buildRedisConfigVisitDetail(dto, mmDetailKey);
        }

        if (DateTypeEnum.WEEK.getType().equals(param.getDateType())) {
            int weekOfYear = new DateTime().weekOfWeekyear().get();
            String weekDetailKey = StringUtils.join(ConstInterface.CacheKey.REDIS_CONFIG_VISIT_RANK_DETAIL, currentDate.getYYYY(), ":week:", weekOfYear);
            this.buildRedisConfigVisitDetail(dto, weekDetailKey);
        }

        if (DateTypeEnum.TODAY.getType().equals(param.getDateType())) {
            String ddDetailKey = StringUtils.join(ConstInterface.CacheKey.REDIS_CONFIG_VISIT_RANK_DETAIL, currentDate.getYYYYMMDD());
            this.buildRedisConfigVisitDetail(dto, ddDetailKey);
        }
        return dto;
    }

    private void addUserVisit(Integer userId, String userName) {
        if (null == userId || StringUtils.isBlank(userName)) {
            return;
        }
        DateUtils.CurrentDate currentDate = DateUtils.getCurrentDate();

        String currentYearKey = StringUtils.join(ConstInterface.CacheKey.USER_VISIT_TOTAL, currentDate.getYYYY());
        this.incrementVisit(currentYearKey, userId, DAYS_365, TimeUnit.DAYS);

        String currentMonthKey = StringUtils.join(ConstInterface.CacheKey.USER_VISIT_TOTAL, currentDate.getYYYYMM());
        this.incrementVisit(currentMonthKey, userId, DAYS_31, TimeUnit.DAYS);

        String yearDetailKey = StringUtils.join(ConstInterface.CacheKey.USER_VISIT_DETAIL, currentDate.getYYYY());
        this.incrementDetailVisit(yearDetailKey, currentDate.getMM(), currentMonthKey, YEAR_1);

        String currentDayKey = StringUtils.join(ConstInterface.CacheKey.USER_VISIT_TOTAL, currentDate.getYYYYMMDD());
        this.incrementVisit(currentDayKey, userId, DAYS_1, TimeUnit.DAYS);

        String mmDetailKey = StringUtils.join(ConstInterface.CacheKey.USER_VISIT_DETAIL, currentDate.getYYYYMM());
        this.incrementDetailVisit(mmDetailKey, currentDate.getDD(), currentDayKey, DAYS_31);

        int weekOfYear = new DateTime().weekOfWeekyear().get();
        int dayOfWeek = new DateTime().dayOfWeek().get();
        String weekDetailKey = StringUtils.join(ConstInterface.CacheKey.USER_VISIT_DETAIL, currentDate.getYYYY(), ":week:", weekOfYear);
        this.incrementDetailVisit(weekDetailKey, String.valueOf(dayOfWeek), currentDayKey, DAYS_7);

        String currentHourKey = StringUtils.join(ConstInterface.CacheKey.USER_VISIT_TOTAL, currentDate.getYYYYMMDDHH());
        this.incrementVisit(currentHourKey, userId, HOURS_1, TimeUnit.HOURS);

        String ddDetailKey = StringUtils.join(ConstInterface.CacheKey.USER_VISIT_DETAIL, currentDate.getYYYYMMDD());
        this.incrementDetailVisit(ddDetailKey, currentDate.getHH(), currentHourKey, DAYS_1);

        //user visit排行榜
        String currentYearDetailKey = StringUtils.join(ConstInterface.CacheKey.USER_VISIT_RANK_DETAIL, currentDate.getYYYY());
        this.incrementRankVisit(currentYearDetailKey, userName, DAYS_365);

        String currentMonthDetailKey = StringUtils.join(ConstInterface.CacheKey.USER_VISIT_RANK_DETAIL, currentDate.getYYYYMM());
        this.incrementRankVisit(currentMonthDetailKey, userName, DAYS_31);

        String currentWeekDetailKey = StringUtils.join(ConstInterface.CacheKey.USER_VISIT_RANK_DETAIL, currentDate.getYYYY(), ":week:", weekOfYear);
        this.incrementRankVisit(currentWeekDetailKey, userName, DAYS_7);

        String currentDayDetailKey = StringUtils.join(ConstInterface.CacheKey.USER_VISIT_RANK_DETAIL, currentDate.getYYYYMMDD());
        this.incrementRankVisit(currentDayDetailKey, userName, DAYS_1);
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
        Set<ZSetOperations.TypedTuple<Object>> details = Optional.ofNullable(redisTemplate.opsForZSet().reverseRangeWithScores(detailKey, START, END)).orElse(new HashSet<>());
        details.forEach(temp -> {
            UserVisitDataDTO.Detail deail = new UserVisitDataDTO.Detail();
            deail.setDate(String.valueOf(temp.getValue()));
            deail.setCount(temp.getScore());
            dto.addRangeDeails(deail);
        });
    }

    private void buildRedisConfigVisitDetail(RedisConfigVisitDataDTO dto, String detailKey) {
        Set<ZSetOperations.TypedTuple<Object>> details = Optional.ofNullable(redisTemplate.opsForZSet().reverseRangeWithScores(detailKey, START, END)).orElse(new HashSet<>());
        Set<String> redisConfigIds = new HashSet<>();
        details.forEach(temp -> {
            redisConfigIds.add(String.valueOf(temp.getValue()));
        });
        List<RedisConfigPO> configList = redisConfigService.queryList(redisConfigIds);
        //List转成Map，key: RedisConfigPO.id
        ImmutableMap<String, RedisConfigPO> map = Maps.uniqueIndex(configList, (redisConfigPO) -> redisConfigPO.getId());
        details.forEach(temp -> {
            String redisConfigId = String.valueOf(temp.getValue());
            RedisConfigPO configPO = map.get(redisConfigId);
            if (null == configPO) {
                return;
            }
            RedisConfigVisitDataDTO.Detail deail = new RedisConfigVisitDataDTO.Detail();
            deail.setName(configPO.getName());
            deail.setCount(temp.getScore());
            dto.addDetails(deail);
        });
    }
}
