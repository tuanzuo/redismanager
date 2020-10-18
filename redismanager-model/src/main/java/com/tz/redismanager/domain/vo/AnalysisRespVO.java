package com.tz.redismanager.domain.vo;


import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>分析数据返回vo</p>
 *
 * @version 1.5.0
 * @time 2020-10-17 15:42
 **/
@Getter
@Setter
public class AnalysisRespVO {

    private VisitData visitData = new VisitData();
    private UserVisitData userVisitData = new UserVisitData();
    private UserData userData = new UserData();
    private RoleData roleData = new RoleData();
    private RedisConfigData redisConfigData = new RedisConfigData();

    /**
     * 访问数据
     */
    @Getter
    @Setter
    public static class VisitData {
        private Long total = 0L;
        private Long currentYearTotal = 0L;
        private Long currentMonthTotal = 0L;
        private Long dayTotal = 0L;
        private List<VisitDetailData> totalDatas = new ArrayList<>();
        private List<VisitDetailData> currentDatas = new ArrayList<>();
        public void addTotalDatas(VisitDetailData data){
            totalDatas.add(data);
        }
        public void addCurrentDatas(VisitDetailData data){
            currentDatas.add(data);
        }
    }
    /**
     * 用户访问数据
     */
    @Getter
    @Setter
    public static class UserVisitData {
        private Long currentYearTotal = 0L;
        private Long currentMonthTotal = 0L;
        private Long dayTotal = 0L;
        private List<VisitDetailData> totalDatas = new ArrayList<>();
        private List<VisitDetailData> currentDatas = new ArrayList<>();
        public void addTotalDatas(VisitDetailData data){
            totalDatas.add(data);
        }
        public void addCurrentDatas(VisitDetailData data){
            currentDatas.add(data);
        }
    }
    @Getter
    @Setter
    public static class VisitDetailData {
        private String x;
        private Long y;
    }

    /**
     * 用户数据
     */
    @Getter
    @Setter
    public static class UserData {
        private Integer total = 0;
        private Integer dayTotal = 0;
        private List<UserDayData> dayDatas = new ArrayList<>();
        private void addDayDatas(UserDayData dayData){
            dayDatas.add(dayData);
        }
    }
    @Getter
    @Setter
    public static class UserDayData {
        private String x;
        private Integer y;
    }

    /**
     * 角色数据
     */
    @Getter
    @Setter
    public static class RoleData {
        private Integer total = 0;
        private Integer dayTotal = 0;
        private List<RoleDayData> dayDatas = new ArrayList<>();
        private void addDayDatas(RoleDayData dayData){
            dayDatas.add(dayData);
        }
    }
    @Getter
    @Setter
    public static class RoleDayData {
        private String x;
        private Integer y;
    }

    /**
     * redis连接配置数据
     */
    @Getter
    @Setter
    public static class RedisConfigData {
        private Integer total = 0;
        private Integer dayTotal = 0;
        private List<RedisConfigDayData> dayDatas = new ArrayList<>();
        private void addDayDatas(RedisConfigDayData dayData){
            dayDatas.add(dayData);
        }
    }
    @Getter
    @Setter
    public static class RedisConfigDayData {
        private String x;
        private Integer y;
    }
}
