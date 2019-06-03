package com.tz.redismanager.bean.vo;


import org.apache.commons.collections.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * key对应的Tree节点
 */
public class RedisTreeNode {
    /**
     * tree节点显示的名称
     */
    private String title;
    /**
     * tree节点的key不能重复
     */
    private String key;
    /**
     * 是否是叶子节点：true是,fals否
     */
    private Boolean isLeaf = false;
    /**
     * 禁掉checkbox
     */
    private Boolean disableCheckbox = false;
    /**
     * 禁掉响应
     */
    private Boolean disabled = false;
    /**
     * 设置节点是否可被选中
     */
    private Boolean selectable = true;
    /**
     * 子节点
     */
    private List<RedisTreeNode> children;

    public RedisTreeNode(String title, String key, Boolean isLeaf) {
        this.title = title;
        this.key = key;
        this.isLeaf = isLeaf;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public Boolean getIsLeaf() {
        return isLeaf;
    }

    public void setLeaf(Boolean leaf) {
        isLeaf = leaf;
    }

    public List<RedisTreeNode> getChildren() {
        return children;
    }

    public void setChildren(List<RedisTreeNode> children) {
        this.children = children;
    }

    public void addChildren(RedisTreeNode node) {
        if (CollectionUtils.isEmpty(children)) {
            children = new ArrayList<>();
        }
        children.add(node);
    }

    public Boolean getDisableCheckbox() {
        return disableCheckbox;
    }

    public void setDisableCheckbox(Boolean disableCheckbox) {
        this.disableCheckbox = disableCheckbox;
    }

    public Boolean getDisabled() {
        return disabled;
    }

    public void setDisabled(Boolean disabled) {
        this.disabled = disabled;
    }

    public Boolean getSelectable() {
        return selectable;
    }

    public void setSelectable(Boolean selectable) {
        this.selectable = selectable;
    }

    /**
     * 重新equals和hashCode方法:只要key相同就认为是同一个对象
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RedisTreeNode node = (RedisTreeNode) o;
        return Objects.equals(key, node.key);
    }

    @Override
    public int hashCode() {
        return Objects.hash(key);
    }
}
