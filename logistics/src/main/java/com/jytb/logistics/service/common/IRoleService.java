package com.jytb.logistics.service.common;

import com.jytb.logistics.bean.common.User;

import java.util.List;

/**
 * Created by fulei on 2017/4/15.
 */
public interface IRoleService {

    List<User> getUserListByRoleTypes(String[] type) throws Exception;
}
