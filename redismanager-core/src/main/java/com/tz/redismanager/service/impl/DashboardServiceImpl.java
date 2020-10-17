package com.tz.redismanager.service.impl;

import com.tz.redismanager.dao.domain.dto.RedisConfigAnalysisDTO;
import com.tz.redismanager.dao.domain.dto.UserAnalysisDTO;
import com.tz.redismanager.dao.mapper.RedisConfigPOMapper;
import com.tz.redismanager.dao.mapper.UserPOMapper;
import com.tz.redismanager.domain.ApiResult;
import com.tz.redismanager.domain.dto.VisitDataDTO;
import com.tz.redismanager.domain.vo.AnalysisRespVO;
import com.tz.redismanager.enm.ResultCode;
import com.tz.redismanager.service.IDashboardService;
import com.tz.redismanager.service.IStatisticService;
import com.tz.redismanager.util.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * <p>Dashboard服务实现</p>
 *
 * @version 1.5.0
 * @time 2020-10-17 15:37
 **/
@Service
public class DashboardServiceImpl implements IDashboardService {

    @Autowired
    private UserPOMapper userPOMapper;
    @Autowired
    private RedisConfigPOMapper redisConfigPOMapper;
    @Autowired
    private IStatisticService statisticService;

    @Override
    public ApiResult<?> analysis() {
        AnalysisRespVO resp = new AnalysisRespVO();
        List<UserAnalysisDTO> userList = userPOMapper.selectToAnalysis();
        this.buildUserData(resp, userList);

        VisitDataDTO visitDataDTO = statisticService.countVisit();
        this.buildVisitData(resp, visitDataDTO);

        List<RedisConfigAnalysisDTO> configList = redisConfigPOMapper.selectToAnalysis();
        this.buildRedisConfigData(resp, configList);
        return new ApiResult<>(ResultCode.SUCCESS, resp);
    }

    private void buildUserData(AnalysisRespVO resp, List<UserAnalysisDTO> userList) {
        String todayDate = DateUtils.dateToStr(new Date(), DateUtils.YYYY_MM_DD);
        Integer userTotal = 0;
        Integer userTodayTotal = 0;
        for (UserAnalysisDTO temp : userList) {
            String regDate = DateUtils.dateToStr(temp.getRegisterDate(), DateUtils.YYYY_MM_DD);
            userTotal += temp.getUserCount();
            userTodayTotal = todayDate.equals(regDate) ? temp.getUserCount() : 0;
            AnalysisRespVO.UserDayData dayData = new AnalysisRespVO.UserDayData();
            dayData.setX(regDate);
            dayData.setY(temp.getUserCount());
            resp.getUserData().getDayDatas().add(dayData);
        }
        resp.getUserData().setTotal(userTotal);
        resp.getUserData().setDayTotal(userTodayTotal);
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
        dto.getYearDeails().forEach(temp -> {
            AnalysisRespVO.VisitDetailData detail = new AnalysisRespVO.VisitDetailData();
            detail.setX(temp.getDate());
            detail.setY(temp.getCount());
            resp.getVisitData().addYearDatas(detail);
        });
        dto.getMonthDeails().forEach(temp -> {
            AnalysisRespVO.VisitDetailData detail = new AnalysisRespVO.VisitDetailData();
            detail.setX(temp.getDate());
            detail.setY(temp.getCount());
            resp.getVisitData().addMonthDatas(detail);
        });
        dto.getDayDeails().forEach(temp -> {
            AnalysisRespVO.VisitDetailData detail = new AnalysisRespVO.VisitDetailData();
            detail.setX(temp.getDate());
            detail.setY(temp.getCount());
            resp.getVisitData().addDayDatas(detail);
        });
    }

    private void buildRedisConfigData(AnalysisRespVO resp, List<RedisConfigAnalysisDTO> configList) {
        String todayDate = DateUtils.dateToStr(new Date(), DateUtils.YYYY_MM_DD);
        Integer configTotal = 0;
        Integer configTodayTotal = 0;
        for (RedisConfigAnalysisDTO temp : configList) {
            String createDate = DateUtils.dateToStr(temp.getCreateDate(), DateUtils.YYYY_MM_DD);
            configTotal += temp.getConfigCount();
            configTodayTotal = todayDate.equals(createDate) ? temp.getConfigCount() : 0;
            AnalysisRespVO.RedisConfigDayData dayData = new AnalysisRespVO.RedisConfigDayData();
            dayData.setX(createDate);
            dayData.setY(temp.getConfigCount());
            resp.getRedisConfigData().getDayDatas().add(dayData);
        }
        resp.getRedisConfigData().setTotal(configTotal);
        resp.getRedisConfigData().setDayTotal(configTodayTotal);
    }

}
