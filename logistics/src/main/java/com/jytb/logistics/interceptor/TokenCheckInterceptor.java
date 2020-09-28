package com.jytb.logistics.interceptor;

import co.chexiao.base.contract.enums.common.ResultEnum;
import co.chexiao.common.util.StringUtil;
import com.jytb.logistics.bean.common.Token;
import com.jytb.logistics.bean.common.User;
import com.jytb.logistics.service.token.IMKTokenService;
import com.jytb.logistics.service.user.IUserService;
import net.sf.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
import java.util.HashMap;

/**
 * Created by fulei on 2017/2/16.
 */
public class TokenCheckInterceptor extends HandlerInterceptorAdapter {

    private static final Logger logger =
            LoggerFactory.getLogger(TokenCheckInterceptor.class);

    @Autowired
    private IMKTokenService tokenService;

    @Autowired
    private IUserService userService;

    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response, Object handler) throws Exception {

        if (handler.getClass().isAssignableFrom(HandlerMethod.class)) {
//            logger.info("app请求验证token开始，时间：" + new Date());
            TokenCheck tokenCheck = ((HandlerMethod) handler).getMethodAnnotation(TokenCheck.class);

            //没有声明需要权限,或者声明不验证权限
            if (tokenCheck == null || tokenCheck.validate() == false) {
                return true;
            } else {
                //正式环境应该是从header获取，开发方便没有的话从reqeust获取
                String tokenStr = "";
                tokenStr = request.getHeader("token");
                if (StringUtil.isEmpty(tokenStr)) {
                    tokenStr = request.getParameter("token");
                }
                Token token = tokenService.findByToken(tokenStr);
                // token不存在
                if (StringUtil.isEmpty(tokenStr) || token == null) {
                    logger.info("token不存在，token：" + tokenStr);
                    JSONObject resultObj = new JSONObject();
                    resultObj.put("result", ResultEnum.SUCCESS.getCode());
                    HashMap data = new HashMap();
                    data.put("code", ResultEnum.TOKEN_NOTEXIST.getCode());
                    data.put("errormsg", ResultEnum.TOKEN_NOTEXIST.getDesc());
                    resultObj.put("data", data);
                    returnJson(response, resultObj.toString());
                    return false;
                }

                //token失效
                if (token.getValidTime().before(new Date())) {
                    logger.info("token失效，token：" + tokenStr);
                    JSONObject resultObj = new JSONObject();
                    resultObj.put("result", ResultEnum.FAILED.getCode());
                    HashMap data = new HashMap();
                    data.put("code", ResultEnum.TOKEN_INVALID.getCode());
                    data.put("errormsg", ResultEnum.TOKEN_INVALID.getDesc());
                    resultObj.put("data", data);
                    returnJson(response, resultObj.toString());
                    return false;
                }
                //token中无username
                String userName = token.getUserName();
                User user = userService.findByUsername(userName);
                if (StringUtil.isEmpty(userName) || user == null) {
                    JSONObject resultObj = new JSONObject();
                    resultObj.put("result", ResultEnum.FAILED.getCode());
                    HashMap data = new HashMap();
                    data.put("code", ResultEnum.TOKEN_UNAMENOTEXIST.getCode());
                    data.put("errormsg", ResultEnum.TOKEN_UNAMENOTEXIST.getDesc());
                    resultObj.put("data", data);
                    returnJson(response, resultObj.toString());
                    return false;
                }

                request.setAttribute("userToken", token);
                return true;
            }
        }
        return true;
    }

    private void returnJson(HttpServletResponse response, String json) throws Exception {
        PrintWriter writer = null;
        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/html; charset=utf-8");
        try {
            writer = response.getWriter();
            writer.print(json);

        } catch (IOException e) {
            logger.error("response error", e);
        } finally {
            if (writer != null)
                writer.close();
        }
    }

}
