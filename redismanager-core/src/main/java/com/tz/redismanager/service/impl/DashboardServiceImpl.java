package com.tz.redismanager.service.impl;

import com.tz.redismanager.cacher.annotation.Cacheable;
import com.tz.redismanager.cacher.annotation.L1Cache;
import com.tz.redismanager.cacher.annotation.L2Cache;
import com.tz.redismanager.constant.ConstInterface;
import com.tz.redismanager.dao.domain.dto.RedisConfigAnalysisDTO;
import com.tz.redismanager.dao.domain.dto.RoleAnalysisDTO;
import com.tz.redismanager.dao.domain.dto.UserAnalysisDTO;
import com.tz.redismanager.domain.dto.RedisConfigVisitDataDTO;
import com.tz.redismanager.domain.dto.UserVisitDataDTO;
import com.tz.redismanager.domain.dto.VisitDataDTO;
import com.tz.redismanager.domain.param.AnalysisParam;
import com.tz.redismanager.domain.vo.AnalysisRespVO;
import com.tz.redismanager.service.*;
import com.tz.redismanager.util.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

/**
 * <p>Dashboard服务实现</p>
 *
 * @author tuanzuo
 * @version 1.5.0
 * @time 2020-10-17 15:37
 **/
@Service
public class DashboardServiceImpl implements IDashboardService {

    @Autowired
    private IUserService userService;
    @Autowired
    private IRoleService roleService;
    @Autowired
    private IRedisConfigService redisConfigService;
    @Autowired
    private IStatisticService statisticService;

    @Cacheable(name = "分析页缓存", key = ConstInterface.CacheKey.ANALYSIS, var = "#param.dateType", l1Cache = @L1Cache(expireDuration = 60, expireUnit = TimeUnit.SECONDS), l2Cache = @L2Cache(expireDuration = 120, expireUnit = TimeUnit.SECONDS))
    @Override
    public AnalysisRespVO analysis(AnalysisParam param) {
        return this.queryAnalysisData(param);
    }

    private AnalysisRespVO queryAnalysisData(AnalysisParam param) {
        AnalysisRespVO resp = new AnalysisRespVO();
        VisitDataDTO visitDataDTO = statisticService.countVisit(param);
        //请求量
        this.buildVisitData(resp, visitDataDTO);

        UserVisitDataDTO userVisitDataDTO = statisticService.countUserVisit(param);
        //用户访问量
        this.buildUserVisitData(resp, userVisitDataDTO);

        RedisConfigVisitDataDTO redisConfigVisitDataDTO = statisticService.countRedisConfigVisit(param);
        //redis连接排行榜
        this.buildRedisConfigVisitData(resp, redisConfigVisitDataDTO);

        List<UserAnalysisDTO> userList = userService.queryUserAnalysis();
        //注册人数
        this.buildUserData(resp, userList);

        List<RoleAnalysisDTO> roleList = roleService.queryRoleAnalysis();
        //角色数
        this.buildRoleData(resp, roleList);

        List<RedisConfigAnalysisDTO> configList = redisConfigService.queryRedisConfigAnalysis();
        //redis连接配置数
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

}
