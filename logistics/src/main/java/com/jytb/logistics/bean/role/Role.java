package com.jytb.logistics.bean.role;

import com.chexiao.base.dao.annotation.Column;
import com.chexiao.base.dao.annotation.Id;
import com.chexiao.base.dao.annotation.NotDBColumn;
import com.chexiao.base.dao.annotation.Table;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by hyz on 2019/6/8.
 */
@Table(name = "user_role")
public class Role implements Serializable {
    @NotDBColumn
    private static final long serialVersionUID = 64370051775590181L;

    @Id(insertable = true)
    @Column(name = "id")
    private long id;

    @Column(name = "user_id")
    private long userId;

    /**
     * 角色
     */
    @Column(name = "role")
    private String role;

    @Column(name = "create_time")
    private Date createTime;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}
