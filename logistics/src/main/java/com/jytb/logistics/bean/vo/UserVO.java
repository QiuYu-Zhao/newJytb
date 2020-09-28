package com.jytb.logistics.bean.vo;

import co.chexiao.common.util.DateUtil;
import com.chexiao.base.dao.annotation.NotDBColumn;
import com.jytb.logistics.bean.common.User;

import java.io.Serializable;

/**
 * 用户信息
 * Created by hyz on 2019-06-07.
 */
public class UserVO implements Serializable {
    @NotDBColumn
    private static final long serialVersionUID = 9120711660058120785L;

    private long id;

    /**
     * 线路名称
     */
    private String routeName;

    /**
     * 用户名
     */
    private String userName;

    /**
     * 创建时间
     */
    private String createTime;

    /**
     * 角色
     */
    private String role;

    public UserVO(User user) {
        this.id = user.getId();
        this.routeName = user.getRouteName();
        this.userName = user.getUsername();
        this.createTime = DateUtil.dateTime(user.getCreateTime());
        this.role = !user.getRoleList().isEmpty() ? user.getRoleList().get(0).getRole() : "";
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getRouteName() {
        return routeName;
    }

    public void setRouteName(String routeName) {
        this.routeName = routeName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
