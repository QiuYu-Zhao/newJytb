package com.jytb.logistics.control.common;

import co.chexiao.base.contract.enums.common.ResultEnum;
import co.chexiao.common.util.CollectionUtil;
import co.chexiao.common.util.StringUtil;
import com.jytb.logistics.bean.common.User;
import com.jytb.logistics.enums.role.RoleTypeEnum;
import com.jytb.logistics.service.common.MkSessionHolder;
import com.jytb.logistics.service.user.IUserService;
import net.sf.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * 登录控制器
 *
 * @author fulei
 */
@Controller
public class LoginControl extends BaseController {

    private static final Logger logger =
            LoggerFactory.getLogger(LoginControl.class);

    @Autowired
    private AuthenticationManager cxAuthenticationManager;
    @Autowired
    private IUserService userService;


    @RequestMapping(value="/login",method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
    public String login(@RequestParam(defaultValue="") String username,
                        @RequestParam(defaultValue="") String password,
                        HttpServletRequest request){
        username = username.trim();
        UsernamePasswordAuthenticationToken authRequest = new UsernamePasswordAuthenticationToken(username, password);
        String targetUrl = "";

        try {
            Authentication authentication = cxAuthenticationManager.authenticate(authRequest); //调用loadUserByUsername
            SecurityContextHolder.getContext().setAuthentication(authentication);
            HttpSession session = request.getSession();
            session.setAttribute("SPRING_SECURITY_CONTEXT", SecurityContextHolder.getContext()); // 这个非常重要，否则验证后将无法登陆
            targetUrl = determineTargetUrl(authentication);

            UserDetails userDetails =
                    (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            User user = userService.findByUsername(userDetails.getUsername());
            //角色为空或者为app，拒绝登陆
            if(user.getRoleList().isEmpty() || user.getRoleList().get(0).getRole().equals(RoleTypeEnum.APP.getCode())){
                request.setAttribute("errMsg","角色错误，请联系管理员");
                logger.warn("登陆失败 角色错误，请联系管理员 role ==" + user.getRoleList());
                return "login/login_failed";
            }
            session.setAttribute("user",user);
            session.setAttribute("loginFlag","1");

        } catch (AuthenticationException ex) {
            request.setAttribute("errMsg","用户名或密码错误");
            logger.warn("登陆失败 用户名或密码错误 username==" + username);
            return "login/login_failed";
        }
        return "redirect:" + targetUrl;
    }

    /**
     * 根据角色确定登录跳转页
     * @param authentication
     * @return
     */
    protected String determineTargetUrl(Authentication authentication) {
        String url = "";

        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();

        List<String> roles = new ArrayList<String>();

        for (GrantedAuthority a : authorities) {
            roles.add(a.getAuthority());
        }

        if (isUser(roles)) {
            url = "/main.html";
        } else if (isInstall(roles)) {
            url = "/main.html";
        } else {
            url = "/access_denied.html";
        }

        return url;
    }

    private boolean isUser(List<String> roles) {
//        if (roles.contains("ROLE_USER")
//                ||roles.contains("ROLE_ADMIN") || roles.contains("ROLE_CHEXIAO")
//                ||roles.contains("ROLE_SYSADMIN") || roles.contains("ROLE_INSTALL")
//                ||roles.contains("ROLE_CUSTOMER")
//        ) {
        if (!CollectionUtil.isEmpty(roles)) {
            return true;
        }
        return false;
    }

    private boolean isInstall(List<String> roles) {
        if (roles.contains("ROLE_Install")) {
            return true;
        }
        return false;
    }


    @RequestMapping(value = "access_denied", method = RequestMethod.POST)
    public String accessDeniedPage(ModelMap model) {
        model.addAttribute("user", getPrincipal());
        return "/login/access_denied";
    }

    @RequestMapping(value = "loginfailed", method = RequestMethod.POST)
    public String loginfailed(ModelMap model) {
        return "login/login_failed";
    }

    /**
     * 进入首页
     * @param model
     * @param request
     * @return
     */
    @RequestMapping("main")
    public String main(ModelMap model, HttpServletRequest request) {
        super.setUser(model, request);
        //登录时redirect转过来的，session里有登录标识，显示欢迎信息
        Object loginFlag = request.getSession().getAttribute("loginFlag");
        if(loginFlag == null || !"1".equals(loginFlag)) {
            return "redirect:/logout.html";
        }
        return "main";
    }

    /**
     * 退出时清除redis中mksession
     * 由于退出时先执行spring security里的逻辑，这里取的sessionid不是之前放入的，
     * 所以登录时将要删除的sessionid写入navbar.html中
     * 点击退出后异步请求带回删除
     *
     * @param request
     * @return
     */
    @RequestMapping(value = "delMkSession", produces = "text/html;charset=UTF-8")
    @ResponseBody
    public String delMkSession(HttpServletRequest request) {
        String sessionId = request.getParameter("mksessionId");
        int code = ResultEnum.SUCCESS.getCode();
        if (StringUtil.isEmpty(sessionId)) {
            code = ResultEnum.FAILED.getCode();
        } else {
            MkSessionHolder.removeMkSession(sessionId);
        }

        JSONObject resultObj = new JSONObject();
        resultObj.put("code", code);
        return resultObj.toString();
    }

    private String getPrincipal() {
        String userName = null;
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (principal instanceof UserDetails) {
            userName = ((UserDetails) principal).getUsername();
        } else {
            userName = principal.toString();
        }
        return userName;
    }

}
