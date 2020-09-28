package com.jytb.logistics.bean.common;

import co.chexiao.base.contract.enums.UserState;
import com.chexiao.base.dao.annotation.Column;
import com.chexiao.base.dao.annotation.Id;
import com.chexiao.base.dao.annotation.NotDBColumn;
import com.chexiao.base.dao.annotation.Table;
import com.jytb.logistics.bean.role.Role;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 可登录用户信息
 * Created by hyz on 2019-06-07.
 */
@Table(name = "user")
public class User implements Serializable {
    @NotDBColumn
    private static final long serialVersionUID = 9120711668504777828L;

    @Id(insertable = true)
    @Column(name = "id")
    private long id;

    @Column(name = "username")
    private String username;

    @Column(name = "password")
    private String password;

    /**
     * 线路名称
     */
    @Column(name = "route_name")
    private String routeName;

    @Column(name = "remark")
    private String remark;

    /**
     * 状态
     */
    @Column(name = "state")
    private String state = UserState.ACTIVE.getState();

    @Column(name = "valid_time")
    private Date validTime;

    @Column(name = "create_time")
    private Date createTime;

    @Column(name = "update_time")
    private Date updateTime;

    @Column(name = "creator")
    private String creator;

    @Column(name = "operator")
    private String operator;

    @NotDBColumn
    private List<Role> roleList = new ArrayList<Role>();

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRouteName() {
        return routeName;
    }

    public void setRouteName(String routeName) {
        this.routeName = routeName;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
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

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public List<Role> getRoleList() {
        return roleList;
    }

    public void setRoleList(List<Role> roleList) {
        this.roleList = roleList;
    }
}
