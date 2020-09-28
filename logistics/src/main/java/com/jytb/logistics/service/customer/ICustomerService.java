package com.jytb.logistics.service.customer;

import com.jytb.logistics.bean.customer.Customer;

import java.util.List;

/**
 * 〈客户管理服务类〉
 *
 * @author zqy
 * @date 2020/09/25
 */
public interface ICustomerService {

    /**
     * 通过条件查询物流单列表，分页查询
     *
     * @param condition
     * @param currentPage
     * @param pageSize
     * @param orderBy
     * @return
     * @throws Exception
     */
    List<Customer> getCustomerListPageByCondition(String condition, int currentPage, int pageSize, String orderBy) throws Exception;

    /**
     * 根据条件查询物流单数量
     *
     * @param condition
     * @return
     * @throws Exception
     */
    int getCustomerCountByCondition(String condition) throws Exception;

    /**
     * 插入客户
     *
     * @param customer
     * @throws Exception
     */
    void insertCustomer(Customer customer) throws Exception;

    /**
     * 通过id查询物流单
     *
     * @param customerId
     * @return
     * @throws Exception
     */
    Customer findCustomerById(long customerId) throws Exception;

    /**
     * 通过id删除物流单
     *
     * @param customerId
     * @throws Exception
     */
    void deleteCustomerById(long customerId) throws Exception;

    /**
     * 更新物流单
     *
     * @param customer
     * @throws Exception
     */
    void updateCustomer(Customer customer) throws Exception;

}
