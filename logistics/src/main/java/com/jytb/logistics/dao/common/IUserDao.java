package com.jytb.logistics.dao.common;

import com.jytb.logistics.bean.common.User;

import java.util.List;

/**
 * Created by fulei on 2017/1/16.
 */
public interface IUserDao {

    public User findById(long id) throws Exception;

    public User findByCondition(String condittion) throws Exception;

    public List<User> getUserListByPage(String condition, int currentPage, int pageSize) throws Exception;

    public List<User> getListByPage(String condition, int currentpage, int pagesize, String orderby) throws Exception;

    public List<User> getListByPage(String condition, int currentpage, int pageSize) throws Exception;

    public Object insert(User model) throws Exception;

    public void update(User model) throws Exception;

    public void updateByStatement(String updateStatement, String condition) throws Exception;

    public void delete(String condition) throws Exception;

    public void deleteByID(long id) throws Exception;

    public int getCountByCondition(String condition) throws Exception;

}
