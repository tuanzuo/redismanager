package com.tz.redismanager.bean.vo;

import com.tz.redismanager.constant.ConstInterface;

public class RedisConfigVO {

    private String id;

    /**
     * 名称
     */
    private String name;

    /**
     * 类型[1=单机,2=集群]
     *
     * @see ConstInterface.TYPE
     */
    private Integer type;

    /**
     * 地址
     * 单机-->127.0.0.1:6379
     * 集群-->192.168.1.32:7000,192.168.1.32:7001,192.168.1.32:7002,192.168.1.32:7003,192.168.1.32:7004,192.168.1.32:7005
     */
    private String address;

    /**
     * 密码
     */
    private String password;

    /**
     * 序列化代码
     */
    private String serCode;

    /**
     * 备注
     */
    private String note;

    /**
     * 创建人
     */
    private String creater;


    /**
     * 修改人
     */
    private String updater;

    /**
     * 来源：1添加，2修改
     * @see ConstInterface.SOURCE
     */
    private Integer source;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSerCode() {
        return serCode;
    }

    public void setSerCode(String serCode) {
        this.serCode = serCode;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getCreater() {
        return creater;
    }

    public void setCreater(String creater) {
        this.creater = creater;
    }

    public String getUpdater() {
        return updater;
    }

    public void setUpdater(String updater) {
        this.updater = updater;
    }

    public Integer getSource() {
        return source;
    }

    public void setSource(Integer source) {
        this.source = source;
    }

    @Override
    public String toString() {
        return "RedisConfigVO{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", type=" + type +
                ", address='" + address + '\'' +
                ", password='" + password + '\'' +
                ", serCode='" + serCode + '\'' +
                ", note='" + note + '\'' +
                ", creater='" + creater + '\'' +
                ", updater='" + updater + '\'' +
                ", source='" + source + '\'' +
                '}';
    }
}