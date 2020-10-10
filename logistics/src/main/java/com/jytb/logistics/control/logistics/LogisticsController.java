package com.jytb.logistics.control.logistics;


import co.chexiao.base.contract.enums.common.ResultEnum;
import co.chexiao.common.util.CollectionUtil;
import co.chexiao.common.util.DateUtil;
import co.chexiao.common.util.StringUtil;
import co.chexiao.common.util.UniqueIDUtil;
import co.chexiao.phoenix.contract.bean.excel.ExcelData;
import com.jytb.logistics.bean.common.User;
import com.jytb.logistics.bean.logistics.Logistics;
import com.jytb.logistics.bean.vo.LogisticsVO;
import com.jytb.logistics.control.ControllerTool;
import com.jytb.logistics.enums.logistics.LogisticsStateEnum;
import com.jytb.logistics.service.area.IAreaService;
import com.jytb.logistics.service.common.MkSessionHolder;
import com.jytb.logistics.service.logistics.ILogisticsService;
import com.jytb.logistics.service.user.IUserService;
import com.jytb.logistics.util.excle.ExcelUtil;
import net.sf.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.List;

/**
 * Created by zkl on 2017/01/18.
 */
@Controller
@RequestMapping("mk/logistics")
public class LogisticsController {

    private static final Logger logger = LoggerFactory.getLogger(LogisticsController.class);

    @Autowired
    private ILogisticsService logisticsService;

    @Autowired
    private IAreaService areaService;

    @Autowired
    private IUserService userService;


    @RequestMapping(value = "list", produces = "text/html;charset=UTF-8")
    public String list(ModelMap model) {
        try {
            model.addAttribute("userList", userService.getAppRoleUserList());
        } catch (Exception e) {
            logger.error("进入物流列表错误，", e);
        }
        return "logistics/logistics_list";
    }


    @RequestMapping(value = "list/page", produces = "text/html;charset=UTF-8")
    @ResponseBody
    public String listPage(@RequestParam String aoData, HttpServletRequest request) {
        ControllerTool.Page page = new ControllerTool().createPage(aoData);
        int totalCount = 0;
        List<LogisticsVO> logisticsVOList = new ArrayList<>();
        try {
            String condition = getCondition(request);
            totalCount = logisticsService.getLogisticsCountByCondition(condition);
            if (totalCount > 0) {
                List<Logistics> logisticsList = logisticsService.getLogisticsListPageByCondition(condition, page.getCurrentPage(), page.iDisplayLength, "id desc");
                for (Logistics logistics : logisticsList) {
                    logisticsVOList.add(new LogisticsVO(logistics));
                }
            }
        } catch (Exception e) {
            logger.error("查询物流单列表出错", e);
        }
        JSONObject getObj = new JSONObject();
        getObj.put("sEcho", page.sEcho);
        getObj.put("iTotalRecords", totalCount);
        getObj.put("iTotalDisplayRecords", totalCount);
        getObj.put("aaData", logisticsVOList);
        return getObj.toString();
    }


    @RequestMapping(value = "toCreate", produces = "text/html;charset=UTF-8")
    public String toCreate(ModelMap model) {
        try {
            model.addAttribute("provinceList", areaService.getAllProvinceList());
            model.addAttribute("userList", userService.getAppRoleUserList());
        } catch (Exception e) {
            logger.error("进入创建物流单页面错误，", e);
        }
        return "logistics/logistics_create";
    }


    /**
     * 创建增加系统编号
     *
     * @param request
     * @param logistics
     * @return
     */
    @RequestMapping(value = "create", produces = "text/html;charset=UTF-8")
    public String create(HttpServletRequest request, Logistics logistics) {
        try {
            Date now = new Date();
            logistics.setId(UniqueIDUtil.getUniqueID());
            logistics.setState(LogisticsStateEnum.NOTSEND.getCode());
            User user = userService.findById(logistics.getUserId());
            if (user != null) {
                logistics.setRouteName(user.getRouteName());
            }

            String reg = "[0-9]+";
            String storeNum = logistics.getReceiverStoreNum();
            String[] storeNumSplit = storeNum.split("-");
            if (storeNumSplit.length == 1 && storeNumSplit[0].matches(reg)) {
                logistics.setSortNum(Integer.parseInt(storeNumSplit[0]));
            } else if (storeNumSplit.length == 2 && storeNumSplit[0].matches(reg) && storeNumSplit[1].matches(reg)) {
                logistics.setSortNum(Integer.parseInt(storeNumSplit[0]));
                logistics.setSortNumNext(Integer.parseInt(storeNumSplit[1]));
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
            logistics.setCreator(MkSessionHolder.get().getUsername());
            logistics.setOperator(MkSessionHolder.get().getUsername());
            String systemNum = logisticsService.getSystemNum();
            logistics.setSystemNum(systemNum);
            logisticsService.insertLogistics(logistics);
        } catch (Exception e) {
            logger.error("进入创建物流单页面错误!", e);
        }

        return "redirect:/mk/logistics/list.html";
    }

    @RequestMapping(value = "send", produces = "text/html;charset=UTF-8")
    @ResponseBody
    public String send(HttpServletRequest request, @RequestParam String logisticsIds) {
        JSONObject result = new JSONObject();
        int code = ResultEnum.SUCCESS.getCode();
        String errormsg = "";
        try {
            logisticsService.sendLogistics(logisticsIds);
        } catch (Exception e) {
            logger.error("分配物流单错误，", e);
            code = ResultEnum.FAILED.getCode();
            errormsg = "分配物流单失败，请联系管理员";
        }
        result.put("code", code);
        result.put("errormsg", errormsg);
        return result.toString();
    }


    /**
     * 物流单全部发送
     *
     * @param request
     * @return
     */
    @RequestMapping(value = "sendAll", produces = "text/html;charset=UTF-8")
    @ResponseBody
    public String sendAll(HttpServletRequest request) {
        JSONObject result = new JSONObject();
        int code = ResultEnum.SUCCESS.getCode();
        String errormsg = "";
        try {
            logisticsService.sendAllLogistics();
        } catch (Exception e) {
            logger.error("物流单全部发送错误，", e);
            code = ResultEnum.FAILED.getCode();
            errormsg = "物流单全部发送失败，请联系管理员";
        }
        result.put("code", code);
        result.put("errormsg", errormsg);
        return result.toString();
    }


    /**
     * 进入查看或编辑页面
     *
     * @param model
     * @param logisticsId
     * @param type        1.查看，2.编辑
     * @return
     */
    @RequestMapping(value = "toView", produces = "text/html;charset=UTF-8")
    public String toView(ModelMap model, @RequestParam long logisticsId, @RequestParam String type) {
        try {
            Logistics logistics = logisticsService.findLogisticsById(logisticsId);
            if (logistics != null) {
                LogisticsVO vo = new LogisticsVO(logistics);
                model.addAttribute("logistics", vo);
                model.addAttribute("provinceList", areaService.getAllProvinceList());
                model.addAttribute("userList", userService.getAppRoleUserList());
            }
        } catch (Exception e) {
            logger.error("查看物流单页面错误，", e);
        }
        //查看
        if (type.equals("1")) {
            return "logistics/logistics_view";
        } else { //编辑
            return "logistics/logistics_update";
        }
    }

    @RequestMapping(value = "update", produces = "text/html;charset=UTF-8")
    public String update(HttpServletRequest request, Logistics logistics) {
        try {
            logisticsService.updateLogistics(logistics);
        } catch (Exception e) {
            logger.error("进入创建物流单页面错误，", e);
        }
        return "redirect:/mk/logistics/list.html";
    }


    @RequestMapping(value = "delete", produces = "text/html;charset=UTF-8")
    @ResponseBody
    public String delete(HttpServletRequest request, @RequestParam long logisticsId) {
        JSONObject result = new JSONObject();
        int code = ResultEnum.SUCCESS.getCode();
        String errormsg = "";
        try {
            logisticsService.deleteLogisticsById(logisticsId);
        } catch (Exception e) {
            logger.error("删除物流单错误，", e);
            code = ResultEnum.FAILED.getCode();
            errormsg = "删除物流单失败，请联系管理员";
        }
        result.put("code", code);
        result.put("errormsg", errormsg);
        return result.toString();
    }


    @RequestMapping(value = "cancel", produces = "text/html;charset=UTF-8")
    @ResponseBody
    public String cancel(HttpServletRequest request, @RequestParam long logisticsId) {
        JSONObject result = new JSONObject();
        int code = ResultEnum.SUCCESS.getCode();
        String errormsg = "";
        try {
            logisticsService.cancelLogisticsById(logisticsId);
        } catch (Exception e) {
            logger.error("作废物流单错误，", e);
            code = ResultEnum.FAILED.getCode();
            errormsg = "作废物流单失败，请联系管理员";
        }
        result.put("code", code);
        result.put("errormsg", errormsg);
        return result.toString();
    }


    /**
     * 查询总计
     *
     * @param request
     * @return
     */
    @RequestMapping(value = "getTotalCharge", produces = "text/html;charset=UTF-8")
    @ResponseBody
    public String getTotalCharge(HttpServletRequest request) {
        JSONObject result = new JSONObject();
        String date = DateUtil.date(new Date());
        //运费总计
        String freightChargeTotal = "";
        //代收总计
        String insteadChargeTotal = "";
        try {
            List<Object[]> objects = logisticsService.getTotalCharge(date);
            if (objects != null && !objects.isEmpty()) {
                freightChargeTotal = StringUtil.g(objects.get(0)[0]);
                insteadChargeTotal = StringUtil.g(objects.get(0)[1]);
            }
        } catch (Exception e) {
            logger.error("作废物流单错误，", e);
        }
        result.put("date", date);
        result.put("freightChargeTotal", freightChargeTotal);
        result.put("insteadChargeTotal", insteadChargeTotal);
        logger.info("查询总计返回信息：" + result.toString());
        return result.toString();
    }


    /**
     * 删除30天之前的数据
     *
     * @param request
     * @return
     */
    @RequestMapping(value = "deleteHsitoryData", produces = "text/html;charset=UTF-8")
    @ResponseBody
    public String deleteHsitoryData(HttpServletRequest request) {
        JSONObject result = new JSONObject();
        int code = ResultEnum.SUCCESS.getCode();
        String errormsg = "";
        try {
            logisticsService.deleteHsitoryData();
        } catch (Exception e) {
            logger.error("删除30天之前的数据错误，", e);
            code = ResultEnum.FAILED.getCode();
            errormsg = "删除30天之前的数据失败，请联系管理员";
        }
        result.put("code", code);
        result.put("errormsg", errormsg);
        return result.toString();
    }


    /**
     * 导出
     *
     * @param request
     * @return
     */
    @RequestMapping(value = "exportData", produces = "text/html;charset=UTF-8")
    @ResponseBody
    public void exportData(HttpServletRequest request, HttpServletResponse response) {
        try {
            String condition = getCondition(request);
            logger.info("导出时condition : {}", condition);
            List<Logistics> logisticsList;
            List<Logistics> materialLists = new ArrayList<>();
            String fileName = "金益通宝" + DateUtil.dateTime(new Date());
            try {
                int currentPage = 1;
                while (true) {
                    logisticsList = logisticsService.getLogisticsListPageByCondition(condition, currentPage, 1000, "id desc");
                    if (null != logisticsList && !logisticsList.isEmpty()) {
                        currentPage++;
                        materialLists.addAll(logisticsList);
                        Thread.sleep(20);
                    } else {
                        break;
                    }
                }
            } catch (Exception e) {
                logger.error("查询原材料列表出错", e);
            }
            List<List<ExcelData>> data = new ArrayList<>();
            Collection<String> headList;
            if (materialLists.size() > 0) {
                materialLists.forEach(material -> {
                    List<ExcelData> list = new ArrayList<>();
                    list.add(new ExcelData(StringUtil.g(material.getSystemNum())));
                    list.add(new ExcelData(StringUtil.g(material.getSender())));
                    list.add(new ExcelData(StringUtil.g(material.getSenderTel())));
                    list.add(new ExcelData(StringUtil.g(material.getSenderAddress())));
                    list.add(new ExcelData(StringUtil.g(material.getStoreName())));
                    list.add(new ExcelData(StringUtil.g(material.getSortNum())));
                    list.add(new ExcelData(StringUtil.g(material.getReceiver())));
                    list.add(new ExcelData(StringUtil.g(material.getReceiverTel())));
                    list.add(new ExcelData(StringUtil.g(material.getReceiverAddress())));
                    list.add(new ExcelData(StringUtil.g(material.getRouteName())));
                    list.add(new ExcelData(StringUtil.g(material.getCount())));
                    list.add(new ExcelData(StringUtil.g(material.getNowPay())));
                    list.add(new ExcelData(StringUtil.g(material.getReachPay())));
                    list.add(new ExcelData(StringUtil.g(material.getFreightCharge())));
                    list.add(new ExcelData(StringUtil.g(material.getInstead() == 1 ? "是" : "否")));
                    list.add(new ExcelData(StringUtil.g(material.getInsteadCharge())));
                    list.add(new ExcelData(StringUtil.g(material.getGoodsName())));
                    list.add(new ExcelData(StringUtil.g(material.getCreateTime())));
                    list.add(new ExcelData(StringUtil.g(material.getRemark())));
                    if (!CollectionUtil.isEmpty(list)) {
                        data.add(list);
                    }
                });
                headList = Arrays.asList("编号", "发件人", "发件人电话", "发货地址", "门店名", "门店编号", "收件人", "收件人电话", "收货地址", "线路", "数量", "现付", "到付", "运费（元）", "是否代收", "代收费（元）", "货物名称", "创建时间", "备注");
                ExcelUtil.exportExcel(headList, data, response, fileName);
            } else {
                headList = Arrays.asList("编号", "发件人", "发件人电话", "发货地址", "门店名", "门店编号", "收件人", "收件人电话", "收货地址", "线路", "数量", "现付", "到付", "运费（元）", "是否代收", "代收费（元）", "货物名称", "创建时间", "备注");
                ExcelUtil.exportExcel(headList, data, response, fileName);
            }
        } catch (Exception e) {
            logger.error("查询物流单列表出错", e);
        }
    }


    private String getCondition(HttpServletRequest request) {
        StringBuilder condition = new StringBuilder("1=1");
        String systemNum = StringUtil.g(request.getParameter("systemNum"));
        String sender = StringUtil.g(request.getParameter("sender"));
        String senderTel = StringUtil.g(request.getParameter("senderTel"));
        String senderAddress = StringUtil.g(request.getParameter("senderAddress"));
        String storeName = StringUtil.g(request.getParameter("storeName"));
        String storeNum = StringUtil.g(request.getParameter("storeNum"));
        String receiver = StringUtil.g(request.getParameter("receiver"));
        String receiverTel = StringUtil.g(request.getParameter("receiverTel"));
        String receiverAddress = StringUtil.g(request.getParameter("receiverAddress"));
        String insteadCharge = StringUtil.g(request.getParameter("insteadCharge"));
        String userId = StringUtil.g(request.getParameter("userId"));
        String count = StringUtil.g(request.getParameter("count"));
        String nowPay = StringUtil.g(request.getParameter("nowPay"));
        String reachPay = StringUtil.g(request.getParameter("reachPay"));
        String goodsName = StringUtil.g(request.getParameter("goodsName"));
        String remark = StringUtil.g(request.getParameter("remark"));
        String createTimeStart = StringUtil.g(request.getParameter("createTimeStart"));
        String createTimeEnd = StringUtil.g(request.getParameter("createTimeEnd"));
        String state = StringUtil.g(request.getParameter("state"));

        if (!StringUtil.isEmpty(systemNum)) {
            condition.append(" AND system_num like '%").append(systemNum).append("%' ");
        }

        if (!StringUtil.isEmpty(receiver)) {
            condition.append(" AND receiver like '%").append(receiver).append("%' ");
        }

        if (!StringUtil.isEmpty(receiverTel)) {
            condition.append(" AND receiver_tel like '%").append(receiverTel).append("%' ");
        }

        if (!StringUtil.isEmpty(senderAddress)) {
            condition.append(" AND sender_address like '%").append(senderAddress).append("%' ");
        }

        if (!StringUtil.isEmpty(goodsName)) {
            condition.append(" AND goods_name like '%").append(goodsName).append("%' ");
        }

        if (!StringUtil.isEmpty(receiverAddress)) {
            condition.append(" AND full_address like '%").append(receiverAddress).append("%' ");
        }

        if (!StringUtil.isEmpty(insteadCharge)) {
            condition.append(" AND instead_charge = ").append(insteadCharge).append(" ");
        }

        if (!StringUtil.isEmpty(storeName)) {
            condition.append(" AND store_name like '%").append(storeName).append("%' ");
        }

        if (!StringUtil.isEmpty(storeNum)) {
            condition.append(" AND store_num like '%").append(storeNum).append("%' ");
        }

        if (!StringUtil.isEmpty(remark)) {
            condition.append(" AND remark like '%").append(remark).append("%' ");
        }

        if (!StringUtil.isEmpty(sender)) {
            condition.append(" AND sender like '%").append(sender).append("%' ");
        }

        if (!StringUtil.isEmpty(senderTel)) {
            condition.append(" AND sender_tel like '%").append(senderTel).append("%' ");
        }

        if (!StringUtil.isEmpty(userId)) {
            condition.append(" AND user_id = ").append(userId).append(" ");
        }

        if (!StringUtil.isEmpty(count)) {
            condition.append(" AND count = ").append(count).append(" ");
        }

        if (!StringUtil.isEmpty(nowPay)) {
            condition.append(" AND now_pay = ").append(nowPay).append(" ");
        }

        if (!StringUtil.isEmpty(reachPay)) {
            condition.append(" AND reach_pay = ").append(reachPay).append(" ");
        }

        if (!StringUtil.isEmpty(state)) {
            condition.append(" AND state = ").append(state).append(" ");
        }

        if (!StringUtil.isEmpty(createTimeStart)) {
            condition.append(" AND create_time >= '").append(createTimeStart).append("' ");
        }

        if (!StringUtil.isEmpty(createTimeEnd)) {
            condition.append(" AND create_time <= '").append(createTimeEnd).append("' ");
        }
        return condition.toString();
    }

}

