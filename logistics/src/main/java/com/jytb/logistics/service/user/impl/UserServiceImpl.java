package com.jytb.logistics.service.user.impl;

import co.chexiao.base.contract.dao.helper.MKDBHelper;
import co.chexiao.common.util.CollectionUtil;
import co.chexiao.common.util.DateUtil;
import co.chexiao.common.util.UniqueIDUtil;
import com.jytb.logistics.bean.common.User;
import com.jytb.logistics.bean.role.Role;
import com.jytb.logistics.dao.common.IRoleDao;
import com.jytb.logistics.dao.common.IUserDao;
import com.jytb.logistics.security.SecurityUtil;
import com.jytb.logistics.service.common.MkSessionHolder;
import com.jytb.logistics.service.user.IUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/**
 * Created by 58 on 2016-12-10.
 */
@Service("userService")
public class UserServiceImpl implements IUserService {
    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    @Autowired
    private IUserDao userDao;

    @Autowired
    private IRoleDao roleDao;

    @Override
    public User findById(long id) {
        User user = null;
        try {
            user = userDao.findById(id);
            if (user != null) {
                List<Role> roleList = roleDao.getListByPage("user_id=" + user.getId(), 1, 100);
                if (!CollectionUtil.isEmpty(roleList)) {
                    user.setRoleList(roleList);
                }
            }
        } catch (Exception e) {
            logger.error("查用户失败", e);
        }
        return user;
    }

    @Override
    public User findByUsername(String username) {
        User user = null;
        try {
            user = userDao.findByCondition("username = '" + username + "'");
            if (user != null) {
                List<Role> roleList = roleDao.getListByPage("user_id=" + user.getId(), 1, 100);
                if (!CollectionUtil.isEmpty(roleList)) {
                    user.setRoleList(roleList);
                }
            }
        } catch (Exception e) {
            logger.error("根据用户名查询出错", e);
        }
        return user;
    }

    @Override
    public List<User> getAllUserBean() {
        List<User> list = new ArrayList<User>();
        try {
            list = userDao.getUserListByPage("", 1, 1000);
        } catch (Exception e) {
            logger.error("查询所有用户", e);
        }
        return list;
    }

    @Override
    public List<User> getListByPage(String condition, int currentPage, int pageSize, String orderBy) throws Exception {
        return userDao.getListByPage(condition, currentPage, pageSize, orderBy);
    }

    @Override
    public Object insert(User model) throws Exception {
        long uid = 0;
        if (model.getId() < 1) {
            uid = UniqueIDUtil.getUniqueID();
            model.setId(uid);
        }

        Date now = new Date();
        model.setPassword(SecurityUtil.encode(model.getPassword()));
        model.setCreateTime(now);
        model.setUpdateTime(now);
        model.setValidTime(DateUtil.year(now,99));
        model.setCreator(MkSessionHolder.get().getUsername());
        model.setOperator(MkSessionHolder.get().getUsername());
        Object o = userDao.insert(model);

        for (Role role : model.getRoleList()) {
            role.setUserId(uid);
            role.setId(UniqueIDUtil.getUniqueID());
            role.setCreateTime(now);
            roleDao.insert(role);
        }

        return o;
    }

    @Override
    public void update(User model) throws Exception {
        Date now = new Date();
        long uid = model.getId();
        User dbUser = userDao.findById(model.getId());
        dbUser.setRouteName(model.getRouteName());
        dbUser.setRemark(model.getRemark());
        dbUser.setRoleList(model.getRoleList());
        dbUser.setPassword(SecurityUtil.encode(model.getPassword()));
        dbUser.setUpdateTime(now);
        roleDao.delete("user_id=" + model.getId());
        for (Role role : model.getRoleList()) {
            role.setId(UniqueIDUtil.getUniqueID());
            role.setUserId(uid);
            role.setCreateTime(now);
            roleDao.insert(role);
        }
        userDao.update(dbUser);
    }

    @Override
    public void updatePassword(User user, String newPassword) throws Exception {
        Date now = new Date();

        user.setPassword(SecurityUtil.encode(newPassword));
        user.setUpdateTime(now);

        this.userDao.update(user);
    }

    @Override
    public void delete(String condition) throws Exception {
        userDao.delete(condition);
    }

    @Override
    public void deleteByID(long id) throws Exception {
        roleDao.delete("user_id = " + id);
        userDao.deleteByID(id);
    }

    @Override
    public int getCountByCondition(String condition) throws Exception {
        return userDao.getCountByCondition(condition);
    }

    @Override
    public List<User> getAppRoleUserList() throws Exception {
        String sql = "select u.* from user u left join user_role ur on u.id = ur.user_id where ur.role = 'APP'";
        List<User> userList = MKDBHelper.getDAOHelper().getListBySQL(User.class,sql,new String[]{});
        return userList;
    }
}
