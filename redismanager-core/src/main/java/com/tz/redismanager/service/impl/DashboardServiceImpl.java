package com.tz.redismanager.service.impl;

import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.LoadingCache;
import com.tz.redismanager.constant.ConstInterface;
import com.tz.redismanager.dao.domain.dto.RedisConfigAnalysisDTO;
import com.tz.redismanager.dao.domain.dto.RoleAnalysisDTO;
import com.tz.redismanager.dao.domain.dto.UserAnalysisDTO;
import com.tz.redismanager.dao.mapper.RedisConfigPOMapper;
import com.tz.redismanager.dao.mapper.RolePOMapper;
import com.tz.redismanager.dao.mapper.UserPOMapper;
import com.tz.redismanager.domain.ApiResult;
import com.tz.redismanager.domain.dto.VisitDataDTO;
import com.tz.redismanager.domain.param.AnalysisParam;
import com.tz.redismanager.domain.vo.AnalysisRespVO;
import com.tz.redismanager.enm.ResultCode;
import com.tz.redismanager.service.IDashboardService;
import com.tz.redismanager.service.IStatisticService;
import com.tz.redismanager.trace.TraceLoggerFactory;
import com.tz.redismanager.util.DateUtils;
import com.tz.redismanager.util.JsonUtils;
import org.slf4j.Logger;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

/**
 * <p>Dashboard服务实现</p>
 *
 * @version 1.5.0
 * @time 2020-10-17 15:37
 **/
@Service
public class DashboardServiceImpl implements IDashboardService, InitializingBean {

    private static final Logger logger = TraceLoggerFactory.getLogger(DashboardServiceImpl.class);

    private static Map<String, LoadingCache> cacheMap = new ConcurrentHashMap<>();

    @Autowired
    private UserPOMapper userPOMapper;
    @Autowired
    private RolePOMapper rolePOMapper;
    @Autowired
    private RedisConfigPOMapper redisConfigPOMapper;
    @Autowired
    private IStatisticService statisticService;

    @Override
    public ApiResult<?> analysis(AnalysisParam param) {
        LoadingCache<AnalysisParam, AnalysisRespVO> analysisCacher = this.getAnalysisCache();
        return new ApiResult<>(ResultCode.SUCCESS, analysisCacher.get(param));
    }

    private LoadingCache<AnalysisParam, AnalysisRespVO> getAnalysisCache() {
        return cacheMap.get(ConstInterface.Cacher.ANALYSIS_CACHER);
    }

    private AnalysisRespVO queryAnalysisData(AnalysisParam param) {
        AnalysisRespVO resp = new AnalysisRespVO();
        VisitDataDTO visitDataDTO = statisticService.countVisit(param);
        this.buildVisitData(resp, visitDataDTO);

        List<UserAnalysisDTO> userList = userPOMapper.selectToAnalysis();
        this.buildUserData(resp, userList);

        List<RoleAnalysisDTO> roleList = rolePOMapper.selectToAnalysis();
        this.buildRoleData(resp, roleList);

        List<RedisConfigAnalysisDTO> configList = redisConfigPOMapper.selectToAnalysis();
        this.buildRedisConfigData(resp, configList);
        return resp;
    }

    private void buildVisitData(AnalysisRespVO resp, VisitDataDTO dto) {
        resp.getVisitData().setTotal(dto.getTotal());
        resp.getVisitData().setCurrentYearTotal(dto.getCurrentYearTotal());
        resp.getVisitData().setCurrentMonthTotal(dto.getCurrentMonthTotal());
        resp.getVisitData().setDayTotal(dto.getCurrentDayTotal());
        dto.getTotalDeails().forEach(temp -> {
            AnalysisRespVO.VisitDetailData detail = new AnalysisRespVO.VisitDetailData();
            detail.setX(temp.getDate());
            detail.setY(temp.getCount());
            resp.getVisitData().addTotalDatas(detail);
        });
        dto.getCurrentQueryDetails().forEach(temp -> {
            AnalysisRespVO.VisitDetailData detail = new AnalysisRespVO.VisitDetailData();
            detail.setX(temp.getDate());
            detail.setY(temp.getCount());
            resp.getVisitData().addCurrentDatas(detail);
        });
    }


    private void buildUserData(AnalysisRespVO resp, List<UserAnalysisDTO> userList) {
        String todayDate = DateUtils.dateToStr(new Date(), DateUtils.YYYY_MM_DD);
        Integer userTotal = 0;
        Integer userTodayTotal = 0;
        for (UserAnalysisDTO temp : userList) {
            String date = DateUtils.dateToStr(temp.getRegisterDate(), DateUtils.YYYY_MM_DD);
            userTotal += temp.getUserCount();
            userTodayTotal = todayDate.equals(date) ? temp.getUserCount() : 0;
            AnalysisRespVO.UserDayData dayData = new AnalysisRespVO.UserDayData();
            dayData.setX(date);
            dayData.setY(temp.getUserCount());
            resp.getUserData().getDayDatas().add(dayData);
        }
        resp.getUserData().setTotal(userTotal);
        resp.getUserData().setDayTotal(userTodayTotal);
    }

    private void buildRoleData(AnalysisRespVO resp, List<RoleAnalysisDTO> roleList) {
        String todayDate = DateUtils.dateToStr(new Date(), DateUtils.YYYY_MM_DD);
        Integer userTotal = 0;
        Integer userTodayTotal = 0;
        for (RoleAnalysisDTO temp : roleList) {
            String date = DateUtils.dateToStr(temp.getCreateDate(), DateUtils.YYYY_MM_DD);
            userTotal += temp.getRoleCount();
            userTodayTotal = todayDate.equals(date) ? temp.getRoleCount() : 0;
            AnalysisRespVO.RoleDayData dayData = new AnalysisRespVO.RoleDayData();
            dayData.setX(date);
            dayData.setY(temp.getRoleCount());
            resp.getRoleData().getDayDatas().add(dayData);
        }
        resp.getRoleData().setTotal(userTotal);
        resp.getRoleData().setDayTotal(userTodayTotal);
    }

    private void buildRedisConfigData(AnalysisRespVO resp, List<RedisConfigAnalysisDTO> configList) {
        String todayDate = DateUtils.dateToStr(new Date(), DateUtils.YYYY_MM_DD);
        Integer configTotal = 0;
        Integer configTodayTotal = 0;
        for (RedisConfigAnalysisDTO temp : configList) {
            String date = DateUtils.dateToStr(temp.getCreateDate(), DateUtils.YYYY_MM_DD);
            configTotal += temp.getConfigCount();
            configTodayTotal = todayDate.equals(date) ? temp.getConfigCount() : 0;
            AnalysisRespVO.RedisConfigDayData dayData = new AnalysisRespVO.RedisConfigDayData();
            dayData.setX(date);
            dayData.setY(temp.getConfigCount());
            resp.getRedisConfigData().getDayDatas().add(dayData);
        }
        resp.getRedisConfigData().setTotal(configTotal);
        resp.getRedisConfigData().setDayTotal(configTodayTotal);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        LoadingCache<AnalysisParam, AnalysisRespVO> analysisCacher = Caffeine.newBuilder()
                //写了之后多久过期
                .expireAfterWrite(30L, TimeUnit.SECONDS)
                .initialCapacity(10)
                .maximumSize(1000)
                .build((param) -> {
                    logger.info("[本地缓存] [{}] [回源查询] {param:{}}", ConstInterface.Cacher.ANALYSIS_CACHER, JsonUtils.toJsonStr(param));
                    return this.queryAnalysisData(param);
                });
        cacheMap.put(ConstInterface.Cacher.ANALYSIS_CACHER, analysisCacher);
        logger.info("[AnalysisCache] [本地缓存] [{}] [初始化完成]", ConstInterface.Cacher.ANALYSIS_CACHER);
    }

}
