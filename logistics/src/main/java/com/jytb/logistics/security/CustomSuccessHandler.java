package com.jytb.logistics.security;

import co.chexiao.common.util.CollectionUtil;
import com.jytb.logistics.bean.common.User;
import com.jytb.logistics.service.user.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by ful on 2016-12-12.
 */
public class CustomSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();
    @Autowired
    private IUserService userService;

    @Override
    protected void handle(HttpServletRequest request, HttpServletResponse response, Authentication authentication)
            throws IOException {
        String targetUrl = determineTargetUrl(authentication);

        if (response.isCommitted()) {
            System.out.println("Can't redirect");
            return;
        }
        UserDetails userDetails =
                (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userService.findByUsername(userDetails.getUsername());
        request.getSession().setAttribute("user",user);
        request.getSession().setAttribute("loginFlag","1");
        redirectStrategy.sendRedirect(request, response, targetUrl);

    }

    /*
     * This method extracts the roles of currently logged-in user and returns
     * appropriate URL according to his/her role.
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

    @Override
    public void setRedirectStrategy(RedirectStrategy redirectStrategy) {
        this.redirectStrategy = redirectStrategy;
    }

    @Override
    protected RedirectStrategy getRedirectStrategy() {
        return redirectStrategy;
    }
}
