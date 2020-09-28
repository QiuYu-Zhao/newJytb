package com.jytb.logistics.service.common;

import com.jytb.logistics.bean.common.User;
import com.jytb.logistics.bean.role.Role;
import com.jytb.logistics.dao.common.IRoleDao;
import com.jytb.logistics.dao.common.RoleDaoImpl;
import com.jytb.logistics.service.user.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by fulei on 2017/4/15.
 */
@Service("roleService")
public class RoleServiceImpl implements IRoleService{

    private IRoleDao roleDao = new RoleDaoImpl();
    @Autowired
    private IUserService userService;
    @Override
    public List<User> getUserListByRoleTypes(String[] type) throws Exception{
        StringBuilder condition = new StringBuilder();
        for (int i = 0 ; i < type.length ;i ++) {
            if(i == 0 ) {
                condition.append("type = '" + type[i]).append("'");
            } else  {
                condition.append(" OR type = '" + type[i]).append("'");
            }

        }
        List<Role> roleList = roleDao.find(condition.toString(), " id desc");
        List<User> userList = new ArrayList<>();
        if(!roleList.isEmpty()) {
            for(Role role : roleList) {
                User user = userService.findById(role.getUserId());
                if (user != null) {
                    userList.add(user);
                }
            }
        }
        return userList;
    }
}
