package com.jytb.logistics.bean.common;

import com.chexiao.base.dao.annotation.Column;
import com.chexiao.base.dao.annotation.Id;
import com.chexiao.base.dao.annotation.Table;

import java.io.Serializable;
import java.util.Date;

/**
 * 移动端Token
 * Created by hyz on 2019/6/8.
 */
@Table(name = "token")
public class Token implements Serializable {

    @Id(insertable = true)
    @Column(name = "id")
    private long id;

    @Column(name = "token")
    private String token;

    @Column(name = "user_id")
    private long userId;

    @Column(name = "user_name")
    private String userName;

    @Column(name = "valid_time")
    private Date validTime;

    @Column(name = "create_time")
    private Date createTime;

    @Column(name = "update_time")
    private Date updateTime;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Date getValidTime() {
        return validTime;
    }

    public void setValidTime(Date validTime) {
        this.validTime = validTime;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
}
