package com.tz.redismanager.dao.domain.po;

import java.util.Date;

/**
 * <p>配置PO</p>
 *
 * @author tuanzuo
 * @version 1.7.0
 * @time 2021-04-02 21:47
 **/
public class ConfigPO {

    private Long id;

    private Byte type;

    private String note;

    private String creater;

    private Date createTime;

    private String updater;

    private Date updateTime;

    private Byte ifDel;

    private String config;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Byte getType() {
        return type;
    }

    public void setType(Byte type) {
        this.type = type;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note == null ? null : note.trim();
    }

    public String getCreater() {
        return creater;
    }

    public void setCreater(String creater) {
        this.creater = creater == null ? null : creater.trim();
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getUpdater() {
        return updater;
    }

    public void setUpdater(String updater) {
        this.updater = updater == null ? null : updater.trim();
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public Byte getIfDel() {
        return ifDel;
    }

    public void setIfDel(Byte ifDel) {
        this.ifDel = ifDel;
    }

    public String getConfig() {
        return config;
    }

    public void setConfig(String config) {
        this.config = config == null ? null : config.trim();
    }
}