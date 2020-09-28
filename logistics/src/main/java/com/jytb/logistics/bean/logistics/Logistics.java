package com.jytb.logistics.bean.logistics;

import com.chexiao.base.dao.annotation.Column;
import com.chexiao.base.dao.annotation.Id;
import com.chexiao.base.dao.annotation.NotDBColumn;
import com.chexiao.base.dao.annotation.Table;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 物流单信息
 * Created by hyz on 2019-06-07.
 */
@Table(name = "logistics")
public class Logistics implements Serializable {
    @NotDBColumn
    private static final long serialVersionUID = 9120711668504771785L;

    @Id(insertable = true)
    @Column(name = "id")
    private long id;

    /**
     * 物流单状态（1.代发货，2.已发货，3.已到货，-1.已作废）
     */
    @Column(name = "state")
    private int state;

    /**
     * 系统编号
     */
    @Column(name = "system_num")
    private String systemNum;

    /**
     * 收货人
     */
    @Column(name = "receiver")
    private String receiver;

    /**
     * 收货人电话
     */
    @Column(name = "receiver_tel")
    private String receiverTel;

    /**
     * 收货人所在省
     */
    @Column(name = "receiver_province")
    private int receiverProvince;

    /**
     * 收货人所在市
     */
    @Column(name = "receiver_city")
    private int receiverCity;

    /**
     * 收货人所在区
     */
    @Column(name = "receiver_area")
    private int receiverArea;

    /**
     * 收货人所在街道
     */
    @Column(name = "receiver_street")
    private int receiverStreet;

    /**
     * 收货人所在省
     */
    @Column(name = "receiver_province_name")
    private String receiverProvinceName;

    /**
     * 收货人所在市
     */
    @Column(name = "receiver_city_name")
    private String receiverCityName;

    /**
     * 收货人所在区
     */
    @Column(name = "receiver_area_name")
    private String receiverAreaName;

    /**
     * 收货人所在街道
     */
    @Column(name = "receiver_street_name")
    private String receiverStreetName;

    /**
     * 收货详细地址
     */
    @Column(name = "receiver_address")
    private String receiverAddress;

    /**
     * 收货地址全称（xx省xx市xx区xx街道xxxxx）
     */
    @Column(name = "full_address")
    private String fullAddress;

    /**
     * 排序字段
     */
    @Column(name = "sort_num")
    private int sortNum;

    /**
     * 数量
     */
    @Column(name = "count")
    private int count;

    /**
     * 运费
     */
    @Column(name = "freight_charge")
    private BigDecimal freightCharge;

    /**
     * 现付
     */
    @Column(name = "now_pay")
    private BigDecimal nowPay;

    /**
     * 到付
     */
    @Column(name = "reach_pay")
    private BigDecimal reachPay;

    /**
     * 是否代收（0.否，1.是）
     */
    @Column(name = "instead")
    private int instead;

    /**
     * 代收费用
     */
    @Column(name = "instead_charge")
    private BigDecimal insteadCharge;

    /**
     * 备注
     */
    @Column(name = "remark")
    private String remark;

    /**
     * 发货人
     */
    @Column(name = "sender")
    private String sender;

    /**
     * 发货人电话
     */
    @Column(name = "sender_tel")
    private String senderTel;

    /**
     * 发货地址
     */
    @Column(name = "sender_address")
    private String senderAddress;

    /**
     * 门店名
     */
    @Column(name = "store_name")
    private String storeName;

    /**
     * 门店编号
     */
    @Column(name = "store_num")
    private String storeNum;

    /**
     * 货物名称
     */
    @Column(name = "goods_name")
    private String goodsName;

    /**
     * 所属用户
     */
    @Column(name = "user_id")
    private long userId;

    /**
     * 线路名称
     */
    @Column(name = "route_name")
    private String routeName;

    /**
     * 拨打电话状态（0.未拨打，1.已拨打）
     */
    @Column(name = "tel_state")
    private int telState;

    /**
     * 创建时间
     */
    @Column(name = "create_time")
    private Date createTime;

    /**
     * 更新时间
     */
    @Column(name = "update_time")
    private Date updateTime;

    /**
     * 完成时间
     */
    @Column(name = "finish_time")
    private Date finishTime;

    /**
     * 创建者
     */
    @Column(name = "creator")
    private String creator;

    /**
     * 操作者
     */
    @Column(name = "operator")
    private String operator;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public String getSystemNum() {
        return systemNum;
    }

    public void setSystemNum(String systemNum) {
        this.systemNum = systemNum;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public String getReceiverTel() {
        return receiverTel;
    }

    public void setReceiverTel(String receiverTel) {
        this.receiverTel = receiverTel;
    }

    public int getReceiverProvince() {
        return receiverProvince;
    }

    public void setReceiverProvince(int receiverProvince) {
        this.receiverProvince = receiverProvince;
    }

    public int getReceiverCity() {
        return receiverCity;
    }

    public void setReceiverCity(int receiverCity) {
        this.receiverCity = receiverCity;
    }

    public int getReceiverArea() {
        return receiverArea;
    }

    public void setReceiverArea(int receiverArea) {
        this.receiverArea = receiverArea;
    }

    public int getReceiverStreet() {
        return receiverStreet;
    }

    public void setReceiverStreet(int receiverStreet) {
        this.receiverStreet = receiverStreet;
    }

    public String getReceiverProvinceName() {
        return receiverProvinceName;
    }

    public void setReceiverProvinceName(String receiverProvinceName) {
        this.receiverProvinceName = receiverProvinceName;
    }

    public String getReceiverCityName() {
        return receiverCityName;
    }

    public void setReceiverCityName(String receiverCityName) {
        this.receiverCityName = receiverCityName;
    }

    public String getReceiverAreaName() {
        return receiverAreaName;
    }

    public void setReceiverAreaName(String receiverAreaName) {
        this.receiverAreaName = receiverAreaName;
    }

    public String getReceiverStreetName() {
        return receiverStreetName;
    }

    public void setReceiverStreetName(String receiverStreetName) {
        this.receiverStreetName = receiverStreetName;
    }

    public String getReceiverAddress() {
        return receiverAddress;
    }

    public void setReceiverAddress(String receiverAddress) {
        this.receiverAddress = receiverAddress;
    }

    public String getFullAddress() {
        return fullAddress;
    }

    public void setFullAddress(String fullAddress) {
        this.fullAddress = fullAddress;
    }

    public int getSortNum() {
        return sortNum;
    }

    public void setSortNum(int sortNum) {
        this.sortNum = sortNum;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public BigDecimal getFreightCharge() {
        return freightCharge;
    }

    public void setFreightCharge(BigDecimal freightCharge) {
        this.freightCharge = freightCharge;
    }

    public BigDecimal getNowPay() {
        return nowPay;
    }

    public void setNowPay(BigDecimal nowPay) {
        this.nowPay = nowPay;
    }

    public BigDecimal getReachPay() {
        return reachPay;
    }

    public void setReachPay(BigDecimal reachPay) {
        this.reachPay = reachPay;
    }

    public int getInstead() {
        return instead;
    }

    public void setInstead(int instead) {
        this.instead = instead;
    }

    public BigDecimal getInsteadCharge() {
        return insteadCharge;
    }

    public void setInsteadCharge(BigDecimal insteadCharge) {
        this.insteadCharge = insteadCharge;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getSenderTel() {
        return senderTel;
    }

    public void setSenderTel(String senderTel) {
        this.senderTel = senderTel;
    }

    public String getSenderAddress() {
        return senderAddress;
    }

    public void setSenderAddress(String senderAddress) {
        this.senderAddress = senderAddress;
    }

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public String getStoreNum() {
        return storeNum;
    }

    public void setStoreNum(String storeNum) {
        this.storeNum = storeNum;
    }

    public String getGoodsName() {
        return goodsName;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public String getRouteName() {
        return routeName;
    }

    public void setRouteName(String routeName) {
        this.routeName = routeName;
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

    public Date getFinishTime() {
        return finishTime;
    }

    public void setFinishTime(Date finishTime) {
        this.finishTime = finishTime;
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

    public int getTelState() {
        return telState;
    }

    public void setTelState(int telState) {
        this.telState = telState;
    }
}
