package com.jytb.logistics.control.common;

import co.chexiao.base.contract.enums.common.ResultEnum;
import co.chexiao.common.util.StringUtil;
import com.alibaba.fastjson.JSONObject;
import com.jytb.logistics.bean.common.Token;
import com.jytb.logistics.service.common.MkSessionHolder;
import com.jytb.logistics.service.token.IMKTokenService;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by fulei on 2017/1/17.
 */
public class BaseController {

    @Autowired
    private IMKTokenService tokenService;
    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(BaseController.class);

    public void setUser(ModelMap model, HttpServletRequest request) {

        MkSessionHolder.setCurrentHttpSession(request.getSession());//当前线程放入session
        if (MkSessionHolder.get() == null) {
            return;
        }
        model.addAttribute("mksessionId", request.getSession().getId());
    }

    public static Map<String, String> queryMap(String queryStr) {
        Map<String, String> paramMap = new HashMap<>();

        String[] queryArr = queryStr.split("\\&");
        for (String string : queryArr) {
            String[] strValue = string.split("=");
            String value = null;
            if (strValue.length > 1) {
                try {
                    value = URLDecoder.decode(strValue[1], "utf-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
            paramMap.put(strValue[0], value);
        }
        return paramMap;
    }

    protected String createPageJson(String sEcho, int totalCount, int displayCount, Object datas) {
        JSONObject getObj = new JSONObject();
        getObj.put("sEcho", sEcho);
        getObj.put("iTotalRecords", totalCount);
        getObj.put("iTotalDisplayRecords", displayCount);
        getObj.put("aaData", datas);
        return getObj.toString();
    }

    protected String createResultJson(String successStr, String errorStr) {
        JSONObject getObj = new JSONObject();
        getObj.put("success", successStr);
        getObj.put("error", errorStr);
        return getObj.toString();
    }


    protected String returnResultJson(String msg, ResultEnum resultEnum, JSONObject data, org.slf4j.Logger logger, String info, Exception e) {
        JSONObject getObj = new JSONObject();
        data.put("msg", msg);
        getObj.put("data", data);
        getObj.put("result", resultEnum.getCode());
        if (null != logger && null != e) {
            logger.info(info, e);
        }

        return getObj.toString();
    }

    protected JSONObject returnCodeJson(ResultEnum resultEnum) {
        JSONObject data = new JSONObject();
        data.put("code", resultEnum.getCode());
        return data;
    }

    protected Token getMkToken(HttpServletRequest request) {
        String tokenStr = "";
        tokenStr = request.getHeader("token");
        if (StringUtil.isEmpty(tokenStr)) {
            tokenStr = request.getParameter("token");
        }
        if (StringUtil.isEmpty(tokenStr)) return null;
        Token mkToken = null;
        try {
            mkToken = tokenService.findByToken(tokenStr);
        } catch (Exception e) {
            logger.error("获得MkToken失败，tokenStr：" + tokenStr, e);
        }
        return mkToken;
    }

    /**
     * sql语句拼接
     *
     * @param map
     * @return
     */
    protected String makeCondition(Map map) {
        StringBuilder stringBuilder = new StringBuilder();
        map.forEach((k, v) -> {
            stringBuilder.append(" ").append(k).append(" ").append(v).append(" ");
        });
        return stringBuilder.toString();
    }

}
