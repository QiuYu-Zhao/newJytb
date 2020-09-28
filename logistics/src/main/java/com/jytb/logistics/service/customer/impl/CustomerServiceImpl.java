package com.jytb.logistics.service.customer.impl;

import co.chexiao.base.contract.dao.helper.MKDBHelper;
import com.jytb.logistics.bean.common.User;
import com.jytb.logistics.bean.customer.Customer;
import com.jytb.logistics.service.customer.ICustomerService;
import com.jytb.logistics.service.user.IUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * 〈客户管理服务类〉
 *
 * @author zqy
 * @date 2020/09/25
 */
@Service
public class CustomerServiceImpl implements ICustomerService {

    private static final Logger logger = LoggerFactory.getLogger(CustomerServiceImpl.class);

    @Autowired
    private IUserService userService;

    @Override
    public List<Customer> getCustomerListPageByCondition(String condition, int currentPage, int pageSize, String orderBy) throws Exception {
        logger.info("查询客户列表，condition : {}", condition);
        return (List<Customer>) MKDBHelper.getDAOHelper().getListByPage(Customer.class, condition, "*", currentPage, pageSize, orderBy);
    }

    @Override
    public int getCustomerCountByCondition(String condition) throws Exception {
        logger.info("查询客户数量，condition : {}", condition);
        return MKDBHelper.getDAOHelper().getCount(Customer.class, condition);
    }

    @Override
    public void insertCustomer(Customer customer) throws Exception {
        MKDBHelper.getDAOHelper().insert(customer);
    }

    @Override
    public Customer findCustomerById(long customerId) throws Exception {
        return (Customer) MKDBHelper.getDAOHelper().get(Customer.class, customerId);
    }

    @Override
    public void deleteCustomerById(long customerId) throws Exception {
        MKDBHelper.getDAOHelper().deleteByID(Customer.class, customerId);
    }

    @Override
    public void updateCustomer(Customer customer) throws Exception {
        Customer logisticsDB = this.findCustomerById(customer.getId());
        if (logisticsDB != null) {
            logisticsDB.setSystemNum(customer.getSystemNum());
            logisticsDB.setCustomerName(customer.getCustomerName());
            logisticsDB.setCustomerShopName(customer.getCustomerShopName());
            logisticsDB.setPhoneNum(customer.getPhoneNum());
            logisticsDB.setAddress(customer.getAddress());
            Date now = new Date();
            User user = userService.findById(customer.getUserId());
            if (user != null) {
                logisticsDB.setRouteName(user.getRouteName());
            }
            logisticsDB.setUserId(customer.getUserId());
            String reg = "[0-9]+";
            String address = customer.getCustomerName();
            String[] addressSplit = address.split("-");
            if (addressSplit.length >= 1 && addressSplit[0].matches(reg)) {
                logisticsDB.setSortNum(Integer.parseInt(addressSplit[0]));
            }

            logisticsDB.setRemark(customer.getRemark());
            logisticsDB.setUpdateTime(now);
            MKDBHelper.getDAOHelper().upateEntity(logisticsDB);
        }
    }

}