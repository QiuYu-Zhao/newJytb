package com.jytb.logistics.dao.common;

import co.chexiao.base.contract.dao.helper.MKDBHelper;
import com.jytb.logistics.bean.role.Role;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by fulei on 2017/1/16.
 */
@Repository
public class RoleDaoImpl implements IRoleDao {

    @Override
    public Role findById(long id) throws Exception {
        return (Role) MKDBHelper.getDAOHelper().get(Role.class, id);
    }

    @Override
    public List<Role> find(String condition, String orderby) throws Exception {
        return (List<Role>) MKDBHelper.getDAOHelper().getListByCustom(Role.class, "*",
                condition, orderby);
    }

    @Override
    public List<Role> getListByPage(String condition, int currentpage, int pagesize, String orderby) throws Exception {
        return (List<Role>) MKDBHelper.getDAOHelper().getListByPage(Role.class, condition, "*",
                currentpage, pagesize, orderby);
    }

    @Override
    public List<Role> getListByPage(String condition, int currentpage, int pageSize) throws Exception {
        return getListByPage(condition, currentpage, pageSize, "");
    }

    @Override
    public Object insert(Role model) throws Exception {
        return MKDBHelper.getDAOHelper().insert(model);
    }

    @Override
    public void update(Role model) throws Exception {
        MKDBHelper.getDAOHelper().upateEntity(model);
    }

    @Override
    public void updateByStatement(String updateStatement, String condition) throws Exception {
        MKDBHelper.getDAOHelper().updateByCustom(Role.class, updateStatement, condition);
    }

    @Override
    public void delete(String condition) throws Exception {
        MKDBHelper.getDAOHelper().deleteByCustom(Role.class, condition);
    }

    @Override
    public void deleteByID(long id) throws Exception {
        MKDBHelper.getDAOHelper().deleteByID(Role.class, id);
    }

    @Override
    public List<Role> loadByIds(List<Long> ids) throws Exception {
        List<Role> list = null;
        StringBuffer sb = new StringBuffer();
        sb.append("(");
        for (Long id : ids) {
            sb.append(id + ",");
        }
        String sbstr = sb.toString();
        String idsStr = sbstr.substring(0, sbstr.length() - 1);
        idsStr = idsStr + ")";
        String sql = "id in " + idsStr;

        list = find(sql, "orderkey");
        return list;
    }

}
