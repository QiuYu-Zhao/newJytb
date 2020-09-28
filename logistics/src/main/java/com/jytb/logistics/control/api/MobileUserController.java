package com.jytb.logistics.control.api;

import co.chexiao.base.contract.enums.common.ResultEnum;
import co.chexiao.base.contract.enums.common.UserEnum;
import co.chexiao.common.util.StringUtil;
import com.jytb.logistics.bean.common.Token;
import com.jytb.logistics.bean.common.User;
import com.jytb.logistics.enums.role.RoleTypeEnum;
import com.jytb.logistics.interceptor.TokenCheck;
import com.jytb.logistics.security.SecurityUtil;
import com.jytb.logistics.service.token.IMKTokenService;
import com.jytb.logistics.service.user.IUserService;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

/**
 * Created by fulei on 2017/2/16.
 */
@Controller
@RequestMapping("api/user")
public class MobileUserController {

    @Autowired
    private IUserService userService;

    @Autowired
    private IMKTokenService tokenService;

    private static final Logger logger =
            LoggerFactory.getLogger(MobileUserController.class);

    /**
     * 用户登录
     *
     * @param request
     * @return
     */
    @RequestMapping(value = "login", produces = "text/html;charset=UTF-8")
    @ResponseBody
    public String login(HttpServletRequest request) {
        //登陆新逻辑
        String uname = request.getParameter("uname");
        String upwd = request.getParameter("upwd");
        logger.info("app登陆，uname：" + uname + ",upwd:" + upwd);
        JSONObject resultObj = new JSONObject();
        JSONObject data = new JSONObject();
        User user = userService.findByUsername(uname);

        logger.info("用户登录 username==" + uname);
        if (user == null || (!user.getPassword().equals(upwd))) {
            return ResultEnum.getFailedReturnInfo("用户名或密码错误");
        }
        /**
         * 不是APP角色不能登陆
         */
        if (user.getRoleList().isEmpty() || !user.getRoleList().get(0).getRole().equals(RoleTypeEnum.APP.getCode())) {
            return ResultEnum.getFailedReturnInfo("用户权限错误，登陆失败");
        }
        Token token = null;
        try {
            //每次登陆均创建新token
            token = tokenService.createToken(uname, user);
            resultObj.put("result", ResultEnum.SUCCESS.getCode());
            data.put("token", token.getToken());
            data.put("uname", uname);
            data.put("name", user.getRouteName());
            data.put("uid", token.getUserId());
            data.put("code", ResultEnum.SUCCESS.getCode());
            data.put("validtime", token.getValidTime().getTime());
            resultObj.put("data", data);
        } catch (Exception e) {
            logger.error("生成token错误", e);
            resultObj.put("result", ResultEnum.ERROR.getCode());
            resultObj.put("errormsg", ResultEnum.ERROR.getDesc());
        }
        logger.info("app登陆，返回结果：" + resultObj.toString());
        return resultObj.toString();
    }

    /**
     * 用户退出登录
     *
     * @param request
     * @return
     */
    @RequestMapping(value = "logout", produces = "text/html;charset=UTF-8")
    @ResponseBody
    public String logout(HttpServletRequest request) {
        try {
            String tokenStr = request.getParameter("token");
            Token token = tokenService.findByToken(tokenStr);
            if (token == null) {
                return ResultEnum.getFailedReturnInfo("token不存在，退出登陆失败");
            }
            User user = userService.findByUsername(token.getUserName());
            if (user == null) {
                return ResultEnum.getFailedReturnInfo("用户不存在，退出登陆失败");
            }
            //app退出后，重新设置密码，原密码失效
            String password = UUID.randomUUID().toString().substring(0, 6);
            user.setPassword(password);
            userService.update(user);
            tokenService.deleteToken(tokenStr);
            return ResultEnum.getSuccessReturnInfo();
        } catch (Exception e) {
            logger.error("生成token错误", e);
            return ResultEnum.getFailedReturnInfo("退出错误");
        }
    }

    /**
     * 用户重新打开app
     *
     * @param request
     * @return
     */
    @RequestMapping(value = "reopen", produces = "text/html;charset=UTF-8")
    @ResponseBody
    public String open(HttpServletRequest request) {
        Token mkToken = getMkToken(request);
        JSONObject resultObj = new JSONObject();
        if (mkToken == null) {
            resultObj.put("result", ResultEnum.SUCCESS.getCode());
            HashMap<String, Object> data = new HashMap<String, Object>();
            data.put("code", ResultEnum.TOKEN_NOTEXIST.getCode());
            data.put("errormsg", ResultEnum.TOKEN_NOTEXIST.getDesc());
            resultObj.put("data", data);
            return resultObj.toString();
        }

        //token失效了
        if (mkToken.getValidTime().before(new Date())) {
            resultObj.put("result", ResultEnum.SUCCESS.getCode());
            HashMap<String, Object> data = new HashMap<String, Object>();
            data.put("code", ResultEnum.TOKEN_INVALID.getCode());
            data.put("errormsg", ResultEnum.TOKEN_INVALID.getDesc());
            resultObj.put("data", data);
            return resultObj.toString();
        }

        try {
            Token token = tokenService.addTokenValidTime(mkToken);

            resultObj.put("result", ResultEnum.SUCCESS.getCode());
            HashMap<String, Object> data = new HashMap<String, Object>();
            data.put("code", ResultEnum.SUCCESS.getCode());
            data.put("token", token.getToken());

            User user = userService.findByUsername(mkToken.getUserName());
            if (token.getUserId() == 0) {
                token.setUserId(user.getId());
                tokenService.addTokenValidTime(token);
            }

            data.put("uid", token.getUserId());
            data.put("validtime", token.getValidTime().getTime());
            resultObj.put("data", data);
            return resultObj.toString();
        } catch (Exception e) {
            logger.error("更新token错误", e);
            resultObj.put("result", ResultEnum.ERROR.getCode());
            resultObj.put("errormsg", ResultEnum.ERROR.getDesc());
            return resultObj.toString();
        }

    }

    private Token getMkToken(HttpServletRequest request) {
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

    @RequestMapping(value = "updatePasswd", produces = "text/html;charset=UTF-8")
    @ResponseBody
    public String updatePasswd(HttpServletRequest request) {

        Token mkToken = getMkToken(request);
        JSONObject resultObj = new JSONObject();
        if (mkToken == null) {
            resultObj.put("result", ResultEnum.SUCCESS.getCode());
            HashMap<String, Object> data = new HashMap<String, Object>();
            data.put("code", ResultEnum.TOKEN_NOTEXIST.getCode());
            data.put("errormsg", ResultEnum.TOKEN_NOTEXIST.getDesc());
            resultObj.put("data", data);
            return resultObj.toString();
        }

        //token失效了
        if (mkToken.getValidTime().before(new Date())) {
            resultObj.put("result", ResultEnum.SUCCESS.getCode());
            HashMap<String, Object> data = new HashMap<String, Object>();
            data.put("code", ResultEnum.TOKEN_INVALID.getCode());
            data.put("errormsg", ResultEnum.TOKEN_INVALID.getDesc());
            resultObj.put("data", data);
            return resultObj.toString();
        }

        String orgPasswd = request.getParameter("orgPasswd");
        String newPasswd = request.getParameter("newPasswd");
        String newPasswdConfirm = request.getParameter("confirmPasswd");

        User user = userService.findByUsername(mkToken.getUserName());
        int code = ResultEnum.SUCCESS.getCode();
        String msg = ResultEnum.SUCCESS.getDesc();
        if (null == user) {
            code = ResultEnum.TOKEN_UNAMENOTEXIST.getCode();
            msg = ResultEnum.TOKEN_UNAMENOTEXIST.getDesc();
        } else {
            if (!user.getPassword().equalsIgnoreCase(SecurityUtil.encode(orgPasswd))) {
                code = UserEnum.ILLEGAL_ORG_PASSWD.getCode();
                msg = UserEnum.ILLEGAL_ORG_PASSWD.getDesc();
            }
            if (StringUtil.isNullOrEmpty(newPasswd) || StringUtil.isNullOrEmpty(newPasswdConfirm)) {
                code = UserEnum.EMPTY_PASSWD.getCode();
                msg = UserEnum.EMPTY_PASSWD.getDesc();
            }
            if (!newPasswd.equalsIgnoreCase(newPasswdConfirm)) {
                code = UserEnum.NOT_MATCH_PASSWD.getCode();
                msg = UserEnum.NOT_MATCH_PASSWD.getDesc();
            }
            user.setPassword(SecurityUtil.encode(newPasswd));
            try {
                userService.updatePassword(user, newPasswd);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        resultObj.put("result", ResultEnum.SUCCESS.getCode());
        HashMap<String, Object> data = new HashMap<String, Object>();
        data.put("code", code);
        data.put("msg", msg);
        resultObj.put("data", data);
        return resultObj.toString();
    }

    /**
     * 获取所有线路
     *
     * @param request
     * @return
     */
    @RequestMapping(value = "getAllAppUser", produces = "text/html;charset=UTF-8")
    @ResponseBody
    @TokenCheck
    public String getAllAppUser(HttpServletRequest request) {
        JSONObject resultObj = new JSONObject();
        JSONObject data = new JSONObject();
        try {
            JSONArray appUserArr = new JSONArray();
            List<User> userList = userService.getAppRoleUserList();
            for (User user : userList) {
                JSONObject appUserObj = new JSONObject();
                appUserObj.put("userId", user.getId());
                appUserObj.put("userName", user.getRouteName());
                appUserArr.add(appUserObj);
            }

            resultObj.put("result", ResultEnum.SUCCESS.getCode());
            data.put("code", ResultEnum.SUCCESS.getCode());
            data.put("appUser", appUserArr);
            resultObj.put("data", data);
        } catch (Exception e) {
            logger.error("app获取所有线路", e);
            resultObj.put("result", ResultEnum.ERROR.getCode());
            resultObj.put("errormsg", ResultEnum.ERROR.getDesc());
        }
        logger.info("app获取所有线路，返回结果：" + resultObj.toString());
        return resultObj.toString();
    }

}
