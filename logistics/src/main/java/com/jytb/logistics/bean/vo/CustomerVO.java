package com.jytb.logistics.bean.vo;

import co.chexiao.common.util.DateUtil;
import com.jytb.logistics.bean.customer.Customer;

import java.io.Serializable;

/**
 * 物流单信息
 * Created by hyz on 2019-06-07.
 */
public class CustomerVO implements Serializable {

    private static final long serialVersionUID = -2428729812740563966L;

    private Long id;

    /**
     * 编号
     */
    private String systemNum;

    /**
     * 客户姓名
     */
    private String customerName;

    /**
     * 客户门店名称
     */
    private String customerShopName;

    /**
     * 客户门店名称
     */
    private Integer sortNum;

    /**
     * 手机号
     */
    private String phoneNum;

    /**
     * 地址
     */
    private String address;

    /**
     * 线路名称
     */
    private String routeName;

    private Long userId;

    /**
     * 备注
     */
    private String remark;

    /**
     * 创建时间
     */
    private String createTime;

    /**
     * 更新时间
     */
    private String updateTime;

    /**
     * 创建者
     */
    private String creator;

    /**
     * 操作者
     */
    private String operator;

    public CustomerVO() {
    }

    public CustomerVO(Customer customer) {
        if (customer == null) {
            return;
        }
        this.id = customer.getId();
        this.systemNum = customer.getSystemNum();
        this.customerName = customer.getCustomerName();
        this.customerShopName = customer.getCustomerShopName();
        this.sortNum = customer.getSortNum();
        this.phoneNum = customer.getPhoneNum();
        this.address = customer.getAddress();
        this.routeName = customer.getRouteName();
        this.userId = customer.getUserId();
        this.remark = customer.getRemark();
        this.createTime = DateUtil.dateTime(customer.getCreateTime());
        this.updateTime = DateUtil.dateTime(customer.getUpdateTime());
        this.creator = customer.getCreator();
        this.operator = customer.getOperator();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSystemNum() {
        return systemNum;
    }

    public void setSystemNum(String systemNum) {
        this.systemNum = systemNum;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCustomerShopName() {
        return customerShopName;
    }

    public void setCustomerShopName(String customerShopName) {
        this.customerShopName = customerShopName;
    }

    public Integer getSortNum() {
        return sortNum;
    }

    public void setSortNum(Integer sortNum) {
        this.sortNum = sortNum;
    }

    public String getPhoneNum() {
        return phoneNum;
    }

    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getRouteName() {
        return routeName;
    }

    public void setRouteName(String routeName) {
        this.routeName = routeName;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
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
}
