package com.tz.redismanager.domain;

import com.tz.redismanager.enm.ResultCode;
import lombok.Getter;
import lombok.Setter;

/**
 * 接口返回对象
 *
 * @version 1.0
 * @time 2019-07-29 19:51
 **/
@Getter
@Setter
public class ApiResult<T> {
    /**
     * @see ResultCode
     */
    private String code;
    private String msg;
    private T datas;

    public ApiResult() {
        super();
    }

    public ApiResult(ResultCode resultCode) {
        this.code = resultCode.getCode();
        this.msg = resultCode.getMsg();
    }

    public ApiResult(ResultCode resultCode, T datas) {
        this.code = resultCode.getCode();
        this.msg = resultCode.getMsg();
        this.datas = datas;
    }

    public ApiResult(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public ApiResult(String code, String msg, T datas) {
        this.code = code;
        this.msg = msg;
        this.datas = datas;
    }

    public static ApiResult buildSuccess() {
        return new ApiResult<>(ResultCode.SUCCESS);
    }

    public static <T> ApiResult buildSuccess(T datas) {
        return new ApiResult<>(ResultCode.SUCCESS, datas);
    }
}
