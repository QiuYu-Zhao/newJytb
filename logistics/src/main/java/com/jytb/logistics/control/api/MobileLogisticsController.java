package com.jytb.logistics.control.api;

import co.chexiao.base.contract.enums.common.ResultEnum;
import co.chexiao.common.util.DateUtil;
import co.chexiao.common.util.StringUtil;
import co.chexiao.common.util.UniqueIDUtil;
import com.jytb.logistics.bean.common.Token;
import com.jytb.logistics.bean.common.User;
import com.jytb.logistics.bean.logistics.Logistics;
import com.jytb.logistics.enums.logistics.LogisticsStateEnum;
import com.jytb.logistics.enums.logistics.LogisticsTelStateEnum;
import com.jytb.logistics.interceptor.TokenCheck;
import com.jytb.logistics.service.api.IMobileLogisticsService;
import com.jytb.logistics.service.logistics.ILogisticsService;
import com.jytb.logistics.service.user.IUserService;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;

/**
 * Created by fulei on 2017/2/16.
 */
@Controller
@RequestMapping("api/logistics")
public class MobileLogisticsController {

    private static final Logger logger = LoggerFactory.getLogger(MobileLogisticsController.class);

    @Autowired
    private IMobileLogisticsService mobileLogisticsService;

    @Autowired
    private ILogisticsService logisticsService;

    @Autowired
    private IUserService userService;


    /**
     * 查询物流单列表
     *
     * @param request
     * @return
     */
    @RequestMapping(value = "list", produces = "text/html;charset=UTF-8")
    @ResponseBody
    @TokenCheck
    public String list(HttpServletRequest request) {

        String state = request.getParameter("state");
        Token token = (Token) request.getAttribute("userToken");
        JSONObject resultObj = new JSONObject();
        JSONObject data = new JSONObject();
        JSONArray logisticsArray = new JSONArray();

        if (StringUtil.isEmpty(state)) {
            return ResultEnum.getFailedReturnInfo("物流状态为空");
        }
        String page = request.getParameter("page");
        String pageSize = request.getParameter("pageSize");
        if (StringUtil.isEmpty(pageSize)) {
            pageSize = "20";
        }
        int pageSizeInt = Integer.parseInt(pageSize);
        if (StringUtil.isEmpty(page)) {
            page = "1";
        }
        int pageInt = Integer.parseInt(page);
        try {
            String condition = "user_id = " + token.getUserId() + " and state = " + state;
            //已到货只查询当天的记录
            if (Integer.parseInt(state) == LogisticsStateEnum.RECEIVED.getCode()) {
                condition += " and finish_time >= '" + DateUtil.dateTime(DateUtil.getStart(new Date())) + "'";
            }
            List<Logistics> logisticsList = mobileLogisticsService.getLogisticsListPageByCondition(condition, pageInt, pageSizeInt, "sort_num,receiver");
            for (Logistics logistics : logisticsList) {
                JSONObject obj = new JSONObject();
                String address = "";
                obj.put("id", StringUtil.g(logistics.getId()));
                obj.put("receiver", StringUtil.g(logistics.getReceiver()));
                obj.put("receiverTel", StringUtil.g(logistics.getReceiverTel()));
                obj.put("telState", StringUtil.g(logistics.getTelState()));
                obj.put("freightCharge", StringUtil.g(logistics.getFreightCharge()));
                obj.put("insteadCharge", StringUtil.g(logistics.getInsteadCharge()));
                obj.put("sender", StringUtil.g(logistics.getSender()));
                obj.put("senderTel", StringUtil.g(logistics.getSenderTel()));
                obj.put("remark", StringUtil.g(logistics.getRemark()));
                obj.put("count", logistics.getCount());
                if (!StringUtil.isEmpty(logistics.getReceiverProvinceName())) {
                    address += logistics.getReceiverProvinceName();
                }
                if (!StringUtil.isEmpty(logistics.getReceiverCityName())) {
                    address += logistics.getReceiverCityName();
                }
                if (!StringUtil.isEmpty(logistics.getReceiverAreaName())) {
                    address += logistics.getReceiverAreaName();
                }
                if (!StringUtil.isEmpty(logistics.getReceiverStreetName())) {
                    address += logistics.getReceiverStreetName();
                }
                if (!StringUtil.isEmpty(logistics.getReceiverAddress())) {
                    address += logistics.getReceiverAddress();
                }
                obj.put("address", address);
                logisticsArray.add(obj);
            }
            resultObj.put("result", ResultEnum.SUCCESS.getCode());
            data.put("logisticsList", logisticsArray);
            data.put("code", ResultEnum.SUCCESS.getCode());
            data.put("hasNext", logisticsArray.size() > 0 ? true : false);
            data.put("page", pageInt);
            data.put("errormsg", "");
            resultObj.put("data", data);
        } catch (Exception e) {
            logger.error("app查询物流单列表错误", e);
            resultObj.put("result", ResultEnum.ERROR.getCode());
            resultObj.put("errormsg", ResultEnum.ERROR.getDesc());
        }
        logger.info("app查询物流列表返回参数：" + resultObj.toString());
        return resultObj.toString();
    }

    /**
     * app确认收货
     *
     * @param request
     * @return
     */
    @RequestMapping(value = "receiveConfirm", produces = "text/html;charset=UTF-8")
    @ResponseBody
    @TokenCheck
    public String receiveConfirm(HttpServletRequest request) {

        String logisticsId = request.getParameter("logisticsId");
        JSONObject resultObj = new JSONObject();
        JSONObject data = new JSONObject();

        if (StringUtil.isEmpty(logisticsId)) {
            return ResultEnum.getFailedReturnInfo("物流单id为空");
        }
        try {
            Logistics logistics = logisticsService.findLogisticsById(Long.parseLong(logisticsId));
            if (logistics == null) {
                return ResultEnum.getFailedReturnInfo("未查到物流单，id：" + logisticsId);
            }
            logistics.setState(LogisticsStateEnum.RECEIVED.getCode());
            logistics.setFinishTime(new Date());
            mobileLogisticsService.updateLogistics(logistics);
            resultObj.put("result", ResultEnum.SUCCESS.getCode());
            data.put("code", ResultEnum.SUCCESS.getCode());
            data.put("errormsg", "");
            resultObj.put("data", data);
        } catch (Exception e) {
            logger.error("app确认收货错误", e);
            resultObj.put("result", ResultEnum.ERROR.getCode());
            resultObj.put("errormsg", ResultEnum.ERROR.getDesc());
        }
        return resultObj.toString();
    }

    /**
     * 设置成已打电话状态
     *
     * @param request
     * @return
     */
    @RequestMapping(value = "setTelState", produces = "text/html;charset=UTF-8")
    @ResponseBody
    @TokenCheck
    public String setTelState(HttpServletRequest request) {

        String logisticsId = request.getParameter("logisticsId");
        JSONObject resultObj = new JSONObject();
        JSONObject data = new JSONObject();

        if (StringUtil.isEmpty(logisticsId)) {
            return ResultEnum.getFailedReturnInfo("物流单id为空");
        }
        try {
            Logistics logistics = logisticsService.findLogisticsById(Long.parseLong(logisticsId));
            if (logistics == null) {
                return ResultEnum.getFailedReturnInfo("未查到物流单，id：" + logisticsId);
            }
            logistics.setTelState(LogisticsTelStateEnum.TEL.getCode());
            logistics.setUpdateTime(new Date());
            mobileLogisticsService.updateLogistics(logistics);
            return ResultEnum.getSuccessReturnInfo();
        } catch (Exception e) {
            logger.error("app设置成已打电话状态错误", e);
            resultObj.put("result", ResultEnum.ERROR.getCode());
            resultObj.put("errormsg", ResultEnum.ERROR.getDesc());
        }
        return resultObj.toString();
    }

    /**
     * 原创建单方法
     *
     * @param request
     * @param logistics
     * @return
     */
    @RequestMapping(value = "create", produces = "text/html;charset=UTF-8")
    @ResponseBody
    @TokenCheck
    public String create(HttpServletRequest request, Logistics logistics) {
        JSONObject resultObj = new JSONObject();
        Token token = (Token) request.getAttribute("userToken");
        try {
            Date now = new Date();
            logistics.setId(UniqueIDUtil.getUniqueID());
            logistics.setState(LogisticsStateEnum.NOTSEND.getCode());
            User user = userService.findById(logistics.getUserId());
            if (user != null) {
                logistics.setRouteName(user.getRouteName());
            }
            String reg = "[0-9]+";
            String address = logistics.getReceiverAddress();
            String[] addressSplit = address.split("-");
            if (addressSplit.length >= 1 && addressSplit[0].matches(reg)) {
                logistics.setSortNum(Integer.parseInt(addressSplit[0]));
            }

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

            logistics.setFullAddress(fullAddress);
            logistics.setCreateTime(now);
            logistics.setUpdateTime(now);
            logistics.setCreator(token.getUserName());
            logistics.setOperator(token.getUserName());
            logisticsService.insertLogistics(logistics);
            return ResultEnum.getSuccessReturnInfo();
        } catch (Exception e) {
            logger.error("app设置成已打电话状态错误", e);
            resultObj.put("result", ResultEnum.ERROR.getCode());
            resultObj.put("errormsg", ResultEnum.ERROR.getDesc());
        }

        return resultObj.toString();
    }


    /***
     * 升级后创建单方法
     * @param request
     * @param logistics
     * @return
     */
    @RequestMapping(value = "create0", produces = "text/html;charset=UTF-8")
    @ResponseBody
    @TokenCheck
    public String create0(HttpServletRequest request, Logistics logistics) {
        JSONObject resultObj = new JSONObject();
        JSONObject data = new JSONObject();

        Token token = (Token) request.getAttribute("userToken");
        try {
            Date now = new Date();
            logistics.setId(UniqueIDUtil.getUniqueID());
            logistics.setState(LogisticsStateEnum.NOTSEND.getCode());
            User user = userService.findById(logistics.getUserId());
            if (user != null) {
                logistics.setRouteName(user.getRouteName());
            }
            String reg = "[0-9]+";
            String address = logistics.getReceiverAddress();
            String[] addressSplit = address.split("-");
            if (addressSplit.length >= 1 && addressSplit[0].matches(reg)) {
                logistics.setSortNum(Integer.parseInt(addressSplit[0]));
            }

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

            logistics.setFullAddress(fullAddress);
            logistics.setCreateTime(now);
            logistics.setUpdateTime(now);
            logistics.setCreator(token.getUserName());
            logistics.setOperator(token.getUserName());
            String systemNum = logisticsService.getSystemNum();
            logistics.setSystemNum(systemNum);
            logisticsService.insertLogistics(logistics);
            data.put("code", ResultEnum.SUCCESS.getCode());
            data.put("systemNum", systemNum);
            resultObj.put("result", ResultEnum.SUCCESS.getCode());
            resultObj.put("data", data);
            return resultObj.toString();
        } catch (Exception e) {
            logger.error("app创建单错误", e);
            resultObj.put("result", ResultEnum.ERROR.getCode());
            resultObj.put("errormsg", ResultEnum.ERROR.getDesc());
            return resultObj.toString();
        }
    }

}
