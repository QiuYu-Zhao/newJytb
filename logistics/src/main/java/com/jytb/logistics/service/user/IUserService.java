package com.jytb.logistics.service.user;

import com.jytb.logistics.bean.common.User;

import java.util.List;

/**
 * Created by 58 on 2016-12-10.
 */
public interface IUserService {
    public User findById(long id);

    public User findByUsername(String username);

    public List<User> getAllUserBean();


    public List<User> getListByPage(String condition, int currentPage, int pageSize, String orderBy) throws Exception;


    public Object insert(User model) throws Exception;

    public void update(User model) throws Exception;

    public void delete(String condition) throws Exception;

    public void deleteByID(long id) throws Exception;

    public int getCountByCondition(String condition) throws Exception;

    void updatePassword(User user, String newPassword) throws Exception;

    /**
     * 查询app角色的用户
     * @return
     * @throws Exception
     */
    public List<User> getAppRoleUserList() throws Exception;

}
