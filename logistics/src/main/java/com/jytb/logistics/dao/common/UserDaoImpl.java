package com.jytb.logistics.dao.common;

import co.chexiao.base.contract.dao.helper.MKDBHelper;
import com.jytb.logistics.bean.common.User;
import com.jytb.logistics.bean.role.Role;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by fulei on 2017/1/16.
 */
@Repository
public class UserDaoImpl implements IUserDao {

    @Override
    public User findById(long id) throws Exception {
        return (User) MKDBHelper.getDAOHelper().get(User.class, id);
    }

    @Override
    public User findByCondition(String condition) throws Exception {
        System.out.println(condition);
        List<User> userList = (List<User>) MKDBHelper.getDAOHelper().getListByCustom(User.class, "*", condition, "");
        User user = null;
        if (userList != null && !userList.isEmpty()) {
            user = userList.get(0);
        } else {
            return null;
        }
        String roleCondition = "user_id = " + user.getId();
        List<Role> roleList = (List<Role>) MKDBHelper.getDAOHelper().getListByCustom(Role.class, "*", roleCondition, "");
        user.setRoleList(roleList);
        return user;
    }

    @Override
    public List<User> getUserListByPage(String condition, int currentPage, int pageSize) throws Exception {
        return (List<User>) MKDBHelper.getDAOHelper().getListByPage(User.class, condition.toString(), "*", currentPage, pageSize,
                " id desc");
    }

    @Override
    public List<User> getListByPage(String condition, int currentpage, int pagesize, String orderby) throws Exception {
        return (List<User>) MKDBHelper.getDAOHelper().getListByPage(User.class, condition, "*",
                currentpage, pagesize, orderby);
    }

    @Override
    public List<User> getListByPage(String condition, int currentpage, int pageSize) throws Exception {
        return getListByPage(condition, currentpage, pageSize, "");
    }

    @Override
    public Object insert(User model) throws Exception {
        return MKDBHelper.getDAOHelper().insert(model);
    }

    @Override
    public void update(User model) throws Exception {
        MKDBHelper.getDAOHelper().upateEntity(model);
    }

    @Override
    public void updateByStatement(String updateStatement, String condition) throws Exception {
        MKDBHelper.getDAOHelper().updateByCustom(User.class, updateStatement, condition);
    }

    @Override
    public void delete(String condition) throws Exception {
        MKDBHelper.getDAOHelper().deleteByCustom(User.class, condition);
    }

    @Override
    public void deleteByID(long id) throws Exception {
        MKDBHelper.getDAOHelper().deleteByID(User.class, id);
    }

    @Override
    public int getCountByCondition(String condition) throws Exception {
        return MKDBHelper.getDAOHelper().getCount(User.class, condition);
    }
}
