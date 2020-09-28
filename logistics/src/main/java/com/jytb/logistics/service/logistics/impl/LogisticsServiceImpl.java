package com.jytb.logistics.service.logistics.impl;

import co.chexiao.base.contract.dao.helper.MKDBHelper;
import co.chexiao.common.util.DateUtil;
import co.chexiao.common.util.StringUtil;
import com.jytb.logistics.bean.common.User;
import com.jytb.logistics.bean.logistics.Logistics;
import com.jytb.logistics.enums.logistics.LogisticsStateEnum;
import com.jytb.logistics.service.logistics.ILogisticsService;
import com.jytb.logistics.service.user.IUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * 〈物流单服务类实现类〉
 *
 * @author hyz
 * @create 2019/6/8
 */
@Service
public class LogisticsServiceImpl implements ILogisticsService {

    private static final Logger logger = LoggerFactory.getLogger(LogisticsServiceImpl.class);

    @Autowired
    private IUserService userService;

    @Override
    public List<Logistics> getLogisticsListPageByCondition(String condition, int currentPage, int pageSize, String orderBy) throws Exception {
        logger.info("查询物流单列表，condition：" + condition);
        return (List<Logistics>) MKDBHelper.getDAOHelper().getListByPage(Logistics.class, condition, "*", currentPage, pageSize, orderBy);
    }

    @Override
    public int getLogisticsCountByCondition(String condition) throws Exception {
        logger.info("查询物流单数量，condition：" + condition);
        return MKDBHelper.getDAOHelper().getCount(Logistics.class, condition);
    }

    @Override
    public void insertLogistics(Logistics logistics) throws Exception {
        MKDBHelper.getDAOHelper().insert(logistics);
    }

    @Override
    public void sendLogistics(String logisticsIds) throws Exception {
        String updateStateMent = "state = " + LogisticsStateEnum.SENDED.getCode();
        String condition = "id in (" + logisticsIds + ")";
        logger.info("分配物流单，updateStateMent:" + updateStateMent + ",condition:" + condition);
        MKDBHelper.getDAOHelper().updateByCustom(Logistics.class, updateStateMent, condition);
    }

    @Override
    public void sendAllLogistics() throws Exception {
        String sql = "update logistics set state = " + LogisticsStateEnum.SENDED.getCode() + " where state = " + LogisticsStateEnum.NOTSEND.getCode();
        MKDBHelper.getDAOHelper().executeSQL(sql);
    }

    @Override
    public Logistics findLogisticsById(long logisticsId) throws Exception {
        return (Logistics) MKDBHelper.getDAOHelper().get(Logistics.class, logisticsId);
    }

    @Override
    public void deleteLogisticsById(long logisticsId) throws Exception {
        MKDBHelper.getDAOHelper().deleteByID(Logistics.class, logisticsId);
    }

    @Override
    public void cancelLogisticsById(long logisticsId) throws Exception {
        String updateStateMent = "state = " + LogisticsStateEnum.CANCEL.getCode();
        String condition = "id = " + logisticsId;
        logger.info("作废物流单，updateStateMent:" + updateStateMent + ",condition:" + condition);
        MKDBHelper.getDAOHelper().updateByCustom(Logistics.class, updateStateMent, condition);
    }

    @Override
    public List<Object[]> getTotalCharge(String date) throws Exception {
        String sql = "select sum(freight_charge),sum(instead_charge) from logistics where " +
                "finish_time >= '" + date + "' and state = " + LogisticsStateEnum.RECEIVED.getCode();
        return MKDBHelper.getDAOHelper().customSql(sql, 2);
    }

    @Override
    public void updateLogistics(Logistics logistics) throws Exception {
        Logistics logisticsDB = this.findLogisticsById(logistics.getId());
        if (logisticsDB != null) {
            Date now = new Date();
            User user = userService.findById(logistics.getUserId());
            if (user != null) {
                logisticsDB.setRouteName(user.getRouteName());
            }
            logisticsDB.setUserId(logistics.getUserId());
            logisticsDB.setReceiver(logistics.getReceiver());
            logisticsDB.setReceiverTel(logistics.getReceiverTel());
            logisticsDB.setFreightCharge(logistics.getFreightCharge());
            logisticsDB.setNowPay(logistics.getNowPay());
            logisticsDB.setReachPay(logistics.getReachPay());
            logisticsDB.setReceiverProvince(logistics.getReceiverProvince());
            logisticsDB.setReceiverProvinceName(logistics.getReceiverProvinceName());
            logisticsDB.setReceiverCity(logistics.getReceiverCity());
            logisticsDB.setReceiverCityName(logistics.getReceiverCityName());
            logisticsDB.setReceiverArea(logistics.getReceiverArea());
            logisticsDB.setReceiverAreaName(logistics.getReceiverAreaName());
            logisticsDB.setReceiverStreet(logistics.getReceiverStreet());
            logisticsDB.setReceiverStreetName(logistics.getReceiverStreetName());
            String reg = "[0-9]+";
            String address = logistics.getReceiverAddress();
            String[] addressSplit = address.split("-");
            if (addressSplit.length >= 1 && addressSplit[0].matches(reg)) {
                logisticsDB.setSortNum(Integer.parseInt(addressSplit[0]));
            }
            logisticsDB.setReceiverAddress(logistics.getReceiverAddress());

            String fullAddress = "";
            if (!StringUtil.isEmpty(logistics.getReceiverProvinceName())) {
                fullAddress += logistics.getReceiverProvinceName();
            }
            if (!StringUtil.isEmpty(logistics.getReceiverCityName())) {
                fullAddress += logistics.getReceiverCityName();
            }
            if (!StringUtil.isEmpty(logistics.getReceiverAreaName())) {
                fullAddress += logistics.getReceiverAreaName();
            }
            if (!StringUtil.isEmpty(logistics.getReceiverStreetName())) {
                fullAddress += logistics.getReceiverStreetName();
            }
            if (!StringUtil.isEmpty(logistics.getReceiverAddress())) {
                fullAddress += logistics.getReceiverAddress();
            }
            logisticsDB.setFullAddress(fullAddress);

            logisticsDB.setInstead(logistics.getInstead());
            logisticsDB.setInsteadCharge(logistics.getInsteadCharge());
            logisticsDB.setSender(logistics.getSender());
            logisticsDB.setSenderTel(logistics.getSenderTel());
            logisticsDB.setSenderAddress(logistics.getSenderAddress());
            logisticsDB.setStoreName(logistics.getStoreName());
            logisticsDB.setStoreNum(logistics.getStoreNum());
            logisticsDB.setGoodsName(logistics.getGoodsName());
            logisticsDB.setRemark(logistics.getRemark());
            logisticsDB.setUpdateTime(now);
            logisticsDB.setCount(logistics.getCount());
            MKDBHelper.getDAOHelper().upateEntity(logisticsDB);
        }
    }

    @Override
    public void deleteHsitoryData() throws Exception {
        String condition = "create_time <= '" + DateUtil.date(DateUtil.addDay(new Date(), -30)) + "'";
        logger.info("删除30天前数据，condition：" + condition);
        MKDBHelper.getDAOHelper().deleteByCustom(Logistics.class, condition);
    }

    /**
     * 生成物流单发货编号
     *
     * @return
     */
    @Override
    public synchronized String getSystemNum() throws Exception {
        String sendNum = "";
        String monthString;
        String dayString;
        Calendar nowC = Calendar.getInstance();
        int year = nowC.get(Calendar.YEAR);
        int month = nowC.get(Calendar.MONTH) + 1;
        int day = nowC.get(Calendar.DAY_OF_MONTH);
        if (month < 10) {
            monthString = "0" + month;
        } else {
            monthString = String.valueOf(month);
        }
        if (day < 10) {
            dayString = "0" + day;
        } else {
            dayString = String.valueOf(day);
        }
        //最后一次发货的发货单
        List<Logistics> logisticsList = (List<Logistics>) MKDBHelper.getDAOHelper().getListByPage(Logistics.class, "system_num != ''", "*", 1, 1, "system_num desc");
        //第一次发货
        if (logisticsList == null || logisticsList.isEmpty()) {
            sendNum = sendNum + year + monthString + dayString + "0001";
        } else {
            Logistics logistics = logisticsList.get(0);
            String latestSendNum = logistics.getSystemNum();

            String yearS = latestSendNum.substring(0, 4);
            String monthS = latestSendNum.substring(4, 6);
            String dayS = latestSendNum.substring(6, 8);
            // 下单数量
            String countS = latestSendNum.substring(8);
            // 同一天的物流单，在物流单号加1
            if (yearS.equals(String.valueOf(year)) && monthS.equals(monthString) && dayS.equals(dayString)) {
                //下一单数量
                int nextCount = Integer.parseInt(countS) + 1;
                String nextCountS;
                nextCountS = getOrderCount(nextCount);
                sendNum = sendNum + year + monthString + dayString + nextCountS;
            } else {
                sendNum = sendNum + year + monthString + dayString + "0001";
            }
        }
        return sendNum;
    }

    /**
     * 系统编号拼接
     */
    private String getOrderCount(int nextCount) {
        String nextCountS;
        if (nextCount < 10) {
            nextCountS = "000" + nextCount;
        } else if (nextCount < 100) {
            nextCountS = "00" + nextCount;
        } else if (nextCount < 1000) {
            nextCountS = "0" + nextCount;
        } else {
            nextCountS = String.valueOf(nextCount);
        }
        return nextCountS;
    }

}