package com.tz.redismanager.domain.vo;


import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>分析数据返回vo</p>
 *
 * @author tuanzuo
 * @version 1.5.0
 * @time 2020-10-17 15:42
 **/
@Getter
@Setter
public class AnalysisRespVO {

    /**
     * 请求量数据
     */
    private VisitData visitData = new VisitData();
    /**
     * 用户访问量数据
     */
    private UserVisitData userVisitData = new UserVisitData();
    /**
     * Redis连接配置排行榜数据
     */
    private RedisConfigVisitData redisConfigVisitData = new RedisConfigVisitData();
    /**
     * 用户数据
     */
    private UserData userData = new UserData();
    /**
     * 角色数据
     */
    private RoleData roleData = new RoleData();
    /**
     * Redis连接配置数据
     */
    private RedisConfigData redisConfigData = new RedisConfigData();

    /**
     * 访问数据
     */
    @Getter
    @Setter
    public static class VisitData {
        /**
         * 总量
         */
        private Long total = 0L;
        /**
         * 当年总量
         */
        private Long currentYearTotal = 0L;
        /**
         * 当月总量
         */
        private Long currentMonthTotal = 0L;
        /**
         * 当日总量
         */
        private Long dayTotal = 0L;
        /**
         * 每年访问量集合
         */
        private List<VisitDetailData> totalDatas = new ArrayList<>();
        /**
         * 当年每月访问量集合
         */
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
        /**
         * 当年总量
         */
        private Long currentYearTotal = 0L;
        /**
         * 当月总量
         */
        private Long currentMonthTotal = 0L;
        /**
         * 当日总量
         */
        private Long dayTotal = 0L;
        /**
         * 每年访问量集合
         */
        private List<VisitDetailData> totalDatas = new ArrayList<>();
        /**
         * 当年每月访问量集合
         */
        private List<VisitDetailData> currentDatas = new ArrayList<>();
        /**
         * 用户请求量排名集合
         */
        private List<VisitDetailData> rangeDatas = new ArrayList<>();

        public void addTotalDatas(VisitDetailData data){
            totalDatas.add(data);
        }

        public void addCurrentDatas(VisitDetailData data){
            currentDatas.add(data);
        }

        public void addRangeDatas(VisitDetailData data){
            rangeDatas.add(data);
        }
    }

    @Getter
    @Setter
    public static class RedisConfigVisitData {
        /**
         * Redis连接配置使用次数集合
         */
        private List<VisitDetailData> currentDatas = new ArrayList<>();

        public void addCurrentDatas(VisitDetailData data){
            currentDatas.add(data);
        }
    }

    @Getter
    @Setter
    public static class VisitDetailData {
        /**
         * 横坐标
         */
        private String x;
        /**
         * 纵坐标
         * Long/Double
         */
        private Object y;
    }

    /**
     * 用户数据
     */
    @Getter
    @Setter
    public static class UserData {
        /**
         * 用户总数
         */
        private Integer total = 0;
        /**
         * 当日新增用户总数
         */
        private Integer dayTotal = 0;
        /**
         * 用户数每日增加总数集合
         */
        private List<UserDayData> dayDatas = new ArrayList<>();

        private void addDayDatas(UserDayData dayData){
            dayDatas.add(dayData);
        }
    }

    @Getter
    @Setter
    public static class UserDayData {
        /**
         * 日期
         * 格式：yyyy-MM-dd
         */
        private String x;
        /**
         * 数量
         */
        private Integer y;
    }

    /**
     * 角色数据
     */
    @Getter
    @Setter
    public static class RoleData {
        /**
         * 角色总数
         */
        private Integer total = 0;
        /**
         * 当日新增角色总数
         */
        private Integer dayTotal = 0;
        /**
         * 角色数每日增加总数集合
         */
        private List<RoleDayData> dayDatas = new ArrayList<>();

        private void addDayDatas(RoleDayData dayData){
            dayDatas.add(dayData);
        }
    }

    @Getter
    @Setter
    public static class RoleDayData {
        /**
         * 日期
         * 格式：yyyy-MM-dd
         */
        private String x;
        /**
         * 数量
         */
        private Integer y;
    }

    /**
     * redis连接配置数据
     */
    @Getter
    @Setter
    public static class RedisConfigData {
        /**
         * Redis连接配置总数
         */
        private Integer total = 0;
        /**
         * Redis连接配置当天增加总数
         */
        private Integer dayTotal = 0;
        /**
         * Redis连接配置每日增加总数集合
         */
        private List<RedisConfigDayData> dayDatas = new ArrayList<>();

        private void addDayDatas(RedisConfigDayData dayData){
            dayDatas.add(dayData);
        }
    }

    @Getter
    @Setter
    public static class RedisConfigDayData {
        /**
         * 日期
         * 格式：yyyy-MM-dd
         */
        private String x;
        /**
         * 数量
         */
        private Integer y;
    }
}
