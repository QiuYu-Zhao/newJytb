package com.jytb.logistics.interceptor;

import co.chexiao.base.contract.bean.common.APIToken;
import co.chexiao.base.contract.enums.common.ResultEnum;
import co.chexiao.base.contract.enums.token.APITokenStatusEnum;
import co.chexiao.common.util.StringUtil;
import net.sf.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;

/**
 * Created by fulei on 2017/8/28.
 * API 接口拦截
 */
public class APITokenCheckInterceptor extends HandlerInterceptorAdapter {

    private static final Logger logger =
            LoggerFactory.getLogger(APITokenCheckInterceptor.class);

    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response, Object handler) throws Exception {

        if (handler.getClass().isAssignableFrom(HandlerMethod.class)) {
            APITokenCheck tokenCheck = ((HandlerMethod) handler).getMethodAnnotation(APITokenCheck.class);
            //没有声明需要权限,或者声明不验证权限
            if (tokenCheck == null || tokenCheck.validate() == false) {
                return true;
            } else {
                String tokenStr = request.getParameter("token");
                APIToken token = null;
                // token不存在
                if (StringUtil.isEmpty(tokenStr) || token == null) {
                    JSONObject resultObj = new JSONObject();
                    resultObj.put("result", ResultEnum.SUCCESS.getCode());
                    HashMap data = new HashMap();
                    data.put("code", ResultEnum.TOKEN_NOTEXIST.getCode());
                    data.put("errormsg", ResultEnum.TOKEN_NOTEXIST.getDesc());
                    resultObj.put("data", data);
                    returnJson(response, resultObj.toString());
                    return false;
                }else if(token.getStatus() == APITokenStatusEnum.INVAILD.getCode()){
                    JSONObject resultObj = new JSONObject();
                    resultObj.put("result", ResultEnum.SUCCESS.getCode());
                    HashMap data = new HashMap();
                    data.put("code", ResultEnum.TOKEN_INVALID.getCode());
                    data.put("errormsg", ResultEnum.TOKEN_INVALID.getDesc());
                    resultObj.put("data", data);
                    returnJson(response, resultObj.toString());
                    return false;
                }
                request.setAttribute("apitoken", token);
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
