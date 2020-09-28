package com.jytb.logistics.service.token.Impl;

import co.chexiao.base.contract.dao.helper.MKDBHelper;
import co.chexiao.common.util.DateUtil;
import co.chexiao.common.util.PropertiesUtil;
import co.chexiao.common.util.UniqueIDUtil;
import com.jytb.logistics.bean.common.Token;
import com.jytb.logistics.bean.common.User;
import com.jytb.logistics.service.common.MkSessionHolder;
import com.jytb.logistics.service.token.IMKTokenService;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * Created by 卓 on 2017/3/23.
 */
@Service
public class MKTokenServiceImpl implements IMKTokenService {


    @Override
    public Token createToken(String username, User user) throws Exception {

        Token token = new Token();
        Date now = new Date();
        token.setId(UniqueIDUtil.getUniqueID());
        token.setToken(UUID.randomUUID().toString().replace("-", ""));
        String tokenTime = PropertiesUtil.getProperty("tokenTime", "48");
        token.setUserName(username);
        token.setCreateTime(now);
        token.setUpdateTime(now);
        token.setValidTime(DateUtil.hour(now, Float.parseFloat(tokenTime)));
        token.setUserId(user.getId());
        MKDBHelper.getDAOHelper().insert(token);
        return token;
    }

    @Override
    public Token findByToken(String tokenStr) throws Exception {
        String condition = "token = '" + tokenStr + "'";
        List<Token> tokenList = (List<Token>)MKDBHelper.getDAOHelper().getListByCustom(Token.class,"*",condition,"");
        if(tokenList != null && !tokenList.isEmpty()){
            return tokenList.get(0);
        }
        return null;
    }

    @Override
    public Token addTokenValidTime(Token token) throws Exception {
        Date now = new Date();
        String tokenTime = PropertiesUtil.getProperty("tokentime", "48");//token失效时间，默认24小时 ini.properties没配置的话
        token.setUpdateTime(now);
        token.setValidTime(DateUtil.hour(now, Float.parseFloat(tokenTime)));
        MkSessionHolder.update(token);
        return token;
    }

    @Override
    public void deleteToken(String tokenStr) throws Exception {
        String condition = "token = '" + tokenStr + "'";
        MKDBHelper.getDAOHelper().deleteByCustom(Token.class,condition);
    }
}
