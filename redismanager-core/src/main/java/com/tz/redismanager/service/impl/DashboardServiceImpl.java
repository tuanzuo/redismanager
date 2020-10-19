package com.tz.redismanager.service.impl;

import com.tz.redismanager.constant.ConstInterface;
import com.tz.redismanager.dao.domain.dto.RedisConfigAnalysisDTO;
import com.tz.redismanager.dao.domain.dto.RoleAnalysisDTO;
import com.tz.redismanager.dao.domain.dto.UserAnalysisDTO;
import com.tz.redismanager.dao.mapper.RedisConfigPOMapper;
import com.tz.redismanager.dao.mapper.RolePOMapper;
import com.tz.redismanager.dao.mapper.UserPOMapper;
import com.tz.redismanager.domain.ApiResult;
import com.tz.redismanager.domain.dto.RedisConfigVisitDataDTO;
import com.tz.redismanager.domain.dto.UserVisitDataDTO;
import com.tz.redismanager.domain.dto.VisitDataDTO;
import com.tz.redismanager.domain.param.AnalysisParam;
import com.tz.redismanager.domain.vo.AnalysisRespVO;
import com.tz.redismanager.enm.ResultCode;
import com.tz.redismanager.service.ICacheService;
import com.tz.redismanager.service.IDashboardService;
import com.tz.redismanager.service.IStatisticService;
import com.tz.redismanager.util.DateUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * <p>Dashboard服务实现</p>
 *
 * @version 1.5.0
 * @time 2020-10-17 15:37
 **/
@Service
public class DashboardServiceImpl implements IDashboardService, InitializingBean {

    @Autowired
    private UserPOMapper userPOMapper;
    @Autowired
    private RolePOMapper rolePOMapper;
    @Autowired
    private RedisConfigPOMapper redisConfigPOMapper;
    @Autowired
    private IStatisticService statisticService;
    @Autowired
    private ICacheService cacheService;

    @Override
    public ApiResult<?> analysis(AnalysisParam param) {
        return new ApiResult<>(ResultCode.SUCCESS, cacheService.getCacher(ConstInterface.Cacher.ANALYSIS_CACHER).get(param));
    }

    private AnalysisRespVO queryAnalysisData(AnalysisParam param) {
        AnalysisRespVO resp = new AnalysisRespVO();
        VisitDataDTO visitDataDTO = statisticService.countVisit(param);
        this.buildVisitData(resp, visitDataDTO);

        UserVisitDataDTO userVisitDataDTO = statisticService.countUserVisit(param);
        this.buildUserVisitData(resp, userVisitDataDTO);

        RedisConfigVisitDataDTO redisConfigVisitDataDTO = statisticService.countRedisConfigVisit(param);
        this.buildRedisConfigVisitData(resp, redisConfigVisitDataDTO);

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

    private void buildUserVisitData(AnalysisRespVO resp, UserVisitDataDTO dto) {
        dto.getCurrentQueryDetails().forEach(temp -> {
            AnalysisRespVO.VisitDetailData detail = new AnalysisRespVO.VisitDetailData();
            detail.setX(temp.getDate());
            detail.setY(temp.getCount());
            resp.getUserVisitData().addCurrentDatas(detail);
        });
        dto.getRangeDetails().forEach(temp -> {
            AnalysisRespVO.VisitDetailData detail = new AnalysisRespVO.VisitDetailData();
            detail.setX(temp.getDate());
            detail.setY(temp.getCount());
            resp.getUserVisitData().addRangeDatas(detail);
        });
    }

    private void buildRedisConfigVisitData(AnalysisRespVO resp, RedisConfigVisitDataDTO dto) {
        dto.getDetails().forEach(temp -> {
            AnalysisRespVO.VisitDetailData detail = new AnalysisRespVO.VisitDetailData();
            detail.setX(temp.getName());
            detail.setY(temp.getCount());
            resp.getRedisConfigVisitData().addCurrentDatas(detail);
        });
    }

    private void buildUserData(AnalysisRespVO resp, List<UserAnalysisDTO> userList) {
        String todayDate = DateUtils.dateToStr(new Date(), DateUtils.YYYY_MM_DD);
        Integer userTotal = 0;
        Integer userTodayTotal = 0;
        for (UserAnalysisDTO temp : userList) {
            Date createTime = Optional.ofNullable(temp).map(UserAnalysisDTO::getCreateTime).orElse(new Date());
            String date = DateUtils.dateToStr(createTime, DateUtils.YYYY_MM_DD);
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
            Date createTime = Optional.ofNullable(temp).map(RoleAnalysisDTO::getCreateTime).orElse(new Date());
            String date = DateUtils.dateToStr(createTime, DateUtils.YYYY_MM_DD);
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
            Date createTime = Optional.ofNullable(temp).map(RedisConfigAnalysisDTO::getCreateTime).orElse(new Date());
            String date = DateUtils.dateToStr(createTime, DateUtils.YYYY_MM_DD);
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
        cacheService.initCacher(ConstInterface.Cacher.ANALYSIS_CACHER, (param) -> queryAnalysisData(AnalysisParam.class.cast(param)));
    }
}
