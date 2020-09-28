package com.jytb.logistics.control.customer;


import co.chexiao.base.contract.enums.common.ResultEnum;
import co.chexiao.common.util.StringUtil;
import co.chexiao.common.util.UniqueIDUtil;
import com.jytb.logistics.bean.common.User;
import com.jytb.logistics.bean.customer.Customer;
import com.jytb.logistics.bean.vo.CustomerVO;
import com.jytb.logistics.control.ControllerTool;
import com.jytb.logistics.service.area.IAreaService;
import com.jytb.logistics.service.common.MkSessionHolder;
import com.jytb.logistics.service.customer.ICustomerService;
import com.jytb.logistics.service.user.IUserService;
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
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 客户管理控制类
 *
 * @author zqy
 * @date 2020/09/25
 */
@Controller
@RequestMapping("mk/customer")
public class CustomerController {

    private static final Logger logger = LoggerFactory.getLogger(CustomerController.class);

    @Autowired
    private ICustomerService customerService;

    @Autowired
    private IAreaService areaService;

    @Autowired
    private IUserService userService;


    @RequestMapping(value = "list", produces = "text/html;charset=UTF-8")
    public String list(ModelMap model) {
        try {
            model.addAttribute("userList", userService.getAppRoleUserList());
        } catch (Exception e) {
            logger.error("进入客户管理列表错误，", e);
        }
        return "customer/customer_list";
    }


    @RequestMapping(value = "list/page", produces = "text/html;charset=UTF-8")
    @ResponseBody
    public String listPage(@RequestParam String aoData, HttpServletRequest request) {
        ControllerTool.Page page = new ControllerTool().createPage(aoData);
        int totalCount = 0;
        List<CustomerVO> customersVOList = new ArrayList<>();
        try {
            String condition = getCondition(request);
            totalCount = customerService.getCustomerCountByCondition(condition);
            if (totalCount > 0) {
                List<Customer> customersList = customerService.getCustomerListPageByCondition(condition, page.getCurrentPage(), page.iDisplayLength, "id desc");
                for (Customer customers : customersList) {
                    customersVOList.add(new CustomerVO(customers));
                }
            }
        } catch (Exception e) {
            logger.error("查询客户列表出错", e);
        }
        JSONObject getObj = new JSONObject();
        getObj.put("sEcho", page.sEcho);
        getObj.put("iTotalRecords", totalCount);
        getObj.put("iTotalDisplayRecords", totalCount);
        getObj.put("aaData", customersVOList);
        return getObj.toString();
    }


    /**
     * 创建增加系统编号
     *
     * @param request
     * @param customer
     * @return
     */
    @RequestMapping(value = "create", produces = "text/html;charset=UTF-8")
    public String create(HttpServletRequest request, Customer customer) {
        try {
            Date now = new Date();
            customer.setId(UniqueIDUtil.getUniqueID());
            User user = userService.findById(customer.getUserId());
            if (user != null) {
                customer.setRouteName(user.getRouteName());
            }
            String reg = "[0-9]+";
            String customerName = customer.getCustomerName();
            String[] customerSplit = customerName.split("-");
            if (customerSplit.length >= 1 && customerSplit[0].matches(reg)) {
                customer.setSortNum(Integer.parseInt(customerSplit[0]));
            }
            customer.setCreateTime(now);
            customer.setUpdateTime(now);
            customer.setCreator(MkSessionHolder.get().getUsername());
            customer.setOperator(MkSessionHolder.get().getUsername());
            customerService.insertCustomer(customer);
        } catch (Exception e) {
            logger.error("进入客户管理页面错误!", e);
        }
        return "redirect:/mk/customer/list.html";
    }


    /**
     * 进入查看或编辑页面
     *
     * @param model
     * @param customerId
     * @param type        1.查看，2.编辑
     * @return
     */
    @RequestMapping(value = "toView", produces = "text/html;charset=UTF-8")
    public String toView(ModelMap model, @RequestParam long customerId, @RequestParam String type) {
        try {
            Customer customer = customerService.findCustomerById(customerId);
            if (customer != null) {
                CustomerVO vo = new CustomerVO(customer);
                model.addAttribute("customer", vo);
                model.addAttribute("userList", userService.getAppRoleUserList());
            }
        } catch (Exception e) {
            logger.error("查看客户管理页面错误，", e);
        }

        if (type.equals("1")) {
            return "customer/customer_view";
        } else {
            return "customer/customer_update";
        }
    }

    @RequestMapping(value = "update", produces = "text/html;charset=UTF-8")
    public String update(HttpServletRequest request, Customer customer) {
        try {
            customerService.updateCustomer(customer);
        } catch (Exception e) {
            logger.error("修改客户页面错误，", e);
        }
        return "redirect:/mk/customer/list.html";
    }


    @RequestMapping(value = "delete", produces = "text/html;charset=UTF-8")
    @ResponseBody
    public String delete(HttpServletRequest request, @RequestParam long customerId) {
        JSONObject result = new JSONObject();
        int code = ResultEnum.SUCCESS.getCode();
        String errormsg = "";
        try {
            customerService.deleteCustomerById(customerId);
        } catch (Exception e) {
            logger.error("删除物客户错误，", e);
            code = ResultEnum.FAILED.getCode();
            errormsg = "删除物客户失败，请联系管理员";
        }
        result.put("code", code);
        result.put("errormsg", errormsg);
        return result.toString();
    }


    private String getCondition(HttpServletRequest request) {
        StringBuilder condition = new StringBuilder("1=1");
        String systemNum = StringUtil.g(request.getParameter("systemNum"));
        String customerName = StringUtil.g(request.getParameter("customerName"));
        String customerShopName = StringUtil.g(request.getParameter("customerShopName"));
        String phoneNum = StringUtil.g(request.getParameter("phoneNum"));
        String address = StringUtil.g(request.getParameter("address"));
        String remark = StringUtil.g(request.getParameter("remark"));
        String userId = StringUtil.g(request.getParameter("userId"));

        if (!StringUtil.isEmpty(systemNum)) {
            condition.append(" AND system_num like '%").append(systemNum).append("%' ");
        }

        if (!StringUtil.isEmpty(customerName)) {
            condition.append(" AND customer_name like '%").append(customerName).append("%' ");
        }

        if (!StringUtil.isEmpty(customerShopName)) {
            condition.append(" AND customer_shop_name = '").append(customerShopName).append("' ");
        }

        if (!StringUtil.isEmpty(address)) {
            condition.append(" AND address like '%").append(address).append("%' ");
        }

        if (!StringUtil.isEmpty(remark)) {
            condition.append(" AND remark like '%").append(remark).append("%' ");
        }

        if (!StringUtil.isEmpty(phoneNum)) {
            condition.append(" AND phone_num like '%").append(phoneNum).append("%' ");
        }

        if (!StringUtil.isEmpty(userId)) {
            condition.append(" AND user_id = ").append(userId).append(" ");
        }

        return condition.toString();
    }

}

