package com.jytb.logistics.bean.vo;

import co.chexiao.common.util.DateUtil;
import com.chexiao.base.dao.annotation.NotDBColumn;
import com.jytb.logistics.bean.logistics.Logistics;
import com.jytb.logistics.enums.logistics.LogisticsStateEnum;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 物流单信息
 * Created by hyz on 2019-06-07.
 */
public class LogisticsVO implements Serializable {
    @NotDBColumn
    private static final long serialVersionUID = 9120711668501120785L;

    private long id;

    /**
     * 物流单状态（1.代发货，2.已发货，3.已到货，-1.已作废）
     */
    private int state;

    /**
     * 物流单状态（1.代发货，2.已发货，3.已到货，-1.已作废）
     */
    private String stateS;

    /**
     * 系统编号
     */
    private String systemNum;

    /**
     * 收货人
     */
    private String receiver;

    /**
     * 收货人电话
     */
    private String receiverTel;

    /**
     * 收货人所在省编码
     */
    private int receiverProvince;

    /**
     * 收货人所在市编码
     */
    private int receiverCity;

    /**
     * 收货人所在区编码
     */
    private int receiverArea;

    /**
     * 收货人所在街道编码
     */
    private int receiverStreet;

    /**
     * 收货人所在省名称
     */
    private String receiverProvinceName;

    /**
     * 收货人所在市名称
     */
    private String receiverCityName;

    /**
     * 收货人所在区名称
     */
    private String receiverAreaName;

    /**
     * 收货人所在街道名称
     */
    private String receiverStreetName;

    /**
     * 收货详细地址名称
     */
    private String receiverAddress;

    /**
     * 运费
     */
    private BigDecimal freightCharge;

    /**
     * 现付
     */
    private BigDecimal nowPay;

    /**
     * 到付
     */
    private BigDecimal reachPay;

    /**
     * 是否代收（0.否，1.是）
     */
    private int instead;

    /**
     * 代收费用
     */
    private BigDecimal insteadCharge;

    /**
     * 备注
     */
    private String remark;

    /**
     * 发货人
     */
    private String sender;

    /**
     * 发货人电话
     */
    private String senderTel;

    /**
     * 发货地址
     */
    private String senderAddress;

    /**
     * 门店名
     */
    private String storeName;

    /**
     * 门店编号
     */
    private String storeNum;

    /**
     * 货物名称
     */
    private String goodsName;

    /**
     * 所属用户
     */
    private long userId;

    /**
     * 线路名称
     */
    private String routeName;

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

    private int count;

    public LogisticsVO(Logistics logistics) {
        if (logistics == null) {
            return;
        }
        this.id = logistics.getId();
        this.state = logistics.getState();
        this.stateS = LogisticsStateEnum.getDescByCode(logistics.getState());
        this.systemNum = logistics.getSystemNum();
        this.receiver = logistics.getReceiver();
        this.receiverTel = logistics.getReceiverTel();
        this.receiverProvince = logistics.getReceiverProvince();
        this.receiverCity = logistics.getReceiverCity();
        this.receiverArea = logistics.getReceiverArea();
        this.receiverStreet = logistics.getReceiverStreet();
        this.receiverAddress = logistics.getReceiverAddress();
        this.receiverProvinceName = logistics.getReceiverProvinceName();
        this.receiverCityName = logistics.getReceiverCityName();
        this.receiverAreaName = logistics.getReceiverAreaName();
        this.receiverStreetName = logistics.getReceiverStreetName();
        this.freightCharge = logistics.getFreightCharge();
        this.nowPay = logistics.getNowPay();
        this.reachPay = logistics.getReachPay();
        this.instead = logistics.getInstead();
        this.insteadCharge = logistics.getInsteadCharge();
        this.remark = logistics.getRemark();
        this.sender = logistics.getSender();
        this.senderTel = logistics.getSenderTel();
        this.senderAddress = logistics.getSenderAddress();
        this.storeName = logistics.getStoreName();
        this.storeNum = logistics.getStoreName();
        this.goodsName = logistics.getGoodsName();
        this.userId = logistics.getUserId();
        this.routeName = logistics.getRouteName();
        this.createTime = DateUtil.dateTime(logistics.getCreateTime());
        this.updateTime = DateUtil.dateTime(logistics.getUpdateTime());
        this.creator = logistics.getCreator();
        this.operator = logistics.getOperator();
        this.count = logistics.getCount();
    }

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

    public String getStateS() {
        return stateS;
    }

    public void setStateS(String stateS) {
        this.stateS = stateS;
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

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
