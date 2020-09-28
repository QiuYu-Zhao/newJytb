package com.jytb.logistics.service.common;

import co.chexiao.base.contract.bean.common.APIToken;
import com.jytb.logistics.bean.common.Token;
import com.jytb.logistics.bean.common.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpSession;

/**
 * Created by lzj on 2017/3/9.
 */
public class MkSessionHolder {

    private static final Logger logger = LoggerFactory.getLogger(MkSessionHolder.class);


    private static ThreadLocal<HttpSession> currentSession = new ThreadLocal<>();//当前上下文的httpsessionId


    private MkSessionHolder() {
    }


    public static User get() {
        HttpSession session = currentSession.get();
        return (User)session.getAttribute("user");
    }

    public static void removeMkSession(String sessionId) {
        //RedisHelper.getRedisHelper().delOne(SESSION_KEY + "_" + sessionId);
    }

    //通过token 获取移动端的MkToken 对象
    public static Token get(String token) {
        return null;
        //return RedisHelper.getTokenRedisHelper().getEntity(MOBILE_SESSION_KEY + "_" + token);
    }

    //更新或者set 移动端token
    public static void update(Token mkToken) {
        //RedisHelper.getTokenRedisHelper().setExEntity(MOBILE_SESSION_KEY + "_" + mkToken.getToken(), MOBILE_EXPIRE_SECONDS, mkToken);
    }

    //通过token 获取apitoken 对象
    public static APIToken getAPIToken(String token) {
        return null;
        //return RedisHelper.getTokenRedisHelper().getEntity(API_SESSION_KEY + "_" + token);
    }

    //更新或者set API token 不会失效
    public static void update(APIToken apiToken) {
        //RedisHelper.getTokenRedisHelper().setEntity(API_SESSION_KEY + "_" + apiToken.getToken(), apiToken);
    }

    /**
     * 将sessionid 放到当前上下文中
     *
     * @param session
     */
    public static void setCurrentHttpSession(HttpSession session) {
        currentSession.set(session);
    }

    public static HttpSession getCurrentHttpSession() {
        return currentSession.get();
    }


    /**
     * 将sessionid 从当前上下文中移除
     */
    public static void removeCurrentHttpSession() {
        currentSession.remove();
    }


}
