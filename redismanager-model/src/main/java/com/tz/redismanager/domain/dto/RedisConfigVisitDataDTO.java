package com.tz.redismanager.domain.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>Redis连接配置访问数据DTO</p>
 *
 * @version 1.5.0
 * @time 2020-10-17 18:02
 **/
@Getter
@Setter
public class RedisConfigVisitDataDTO {

    private List<Detail> details = new ArrayList<>();

    public void addDetails(Detail deail) {
        details.add(deail);
    }

    @Getter
    @Setter
    public static class Detail {
        private String name;
        private Double count;
    }
}
