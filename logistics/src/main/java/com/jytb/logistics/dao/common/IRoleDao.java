package com.jytb.logistics.dao.common;

import com.jytb.logistics.bean.role.Role;

import java.util.List;

/**
 * Created by fulei on 2017/1/16.
 */
public interface IRoleDao {

    public Role findById(long id) throws Exception;

    public List<Role> find(String condition, String orderby) throws Exception;

    public List<Role> getListByPage(String condition, int currentpage, int pagesize, String orderby) throws Exception;

    public List<Role> getListByPage(String condition, int currentpage, int pageSize) throws Exception;

    public Object insert(Role model) throws Exception;

    public void update(Role model) throws Exception;

    public void updateByStatement(String updateStatement, String condition) throws Exception;

    public void delete(String condition) throws Exception;

    public void deleteByID(long id) throws Exception;

    public List<Role> loadByIds(List<Long> ids) throws Exception;

}
