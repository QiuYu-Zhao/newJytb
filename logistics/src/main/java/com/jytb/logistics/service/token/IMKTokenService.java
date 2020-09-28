package com.jytb.logistics.service.token;

import com.jytb.logistics.bean.common.Token;
import com.jytb.logistics.bean.common.User;

/**
 * Created by 卓 on 2017/3/23.
 */
public interface IMKTokenService {

    /**
     * 创建MKToken
     * @param username
     * @param user
     * @return
     * @throws Exception
     */
    public Token createToken(String username, User user) throws Exception;

    /**
     * 根据tokenStr得到MKToken
     * @param tokenStr
     * @return
     * @throws Exception
     */
    public Token findByToken(String tokenStr) throws Exception;

    /**
     * 更新token有效期
     * @param token
     * @return
     * @throws Exception
     */
    public Token addTokenValidTime(Token token) throws Exception;

    /**
     * 删除token
     * @param tokenStr
     * @throws Exception
     */
    public void deleteToken(String tokenStr) throws Exception;
}
