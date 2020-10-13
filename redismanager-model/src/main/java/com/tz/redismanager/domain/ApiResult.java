package com.tz.redismanager.domain;

import com.tz.redismanager.enm.ResultCode;

/**
 * 接口返回对象
 *
 * @version 1.0
 * @time 2019-07-29 19:51
 **/
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

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getDatas() {
        return datas;
    }

    public void setDatas(T datas) {
        this.datas = datas;
    }

    @Override
    public String toString() {
        return "ApiResult{" +
                "code='" + code + '\'' +
                ", msg='" + msg + '\'' +
                ", datas=" + datas +
                '}';
    }
}
