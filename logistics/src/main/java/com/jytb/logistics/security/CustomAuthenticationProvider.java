package com.jytb.logistics.security;

import com.jytb.logistics.bean.role.Role;
import com.jytb.logistics.service.user.IUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.AbstractUserDetailsAuthenticationProvider;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 58 on 2016-12-12.
 */
public class CustomAuthenticationProvider extends
        AbstractUserDetailsAuthenticationProvider {

    private static final Logger logger = LoggerFactory.getLogger(CustomAuthenticationProvider.class);

    @Autowired
    private IUserService userService;

    private MessageSourceAccessor messages = CustomMessageSource.getAccessor();

    @Override
    protected void additionalAuthenticationChecks(UserDetails userDetails,
                                                  UsernamePasswordAuthenticationToken authentication) throws AuthenticationException {
        String encPass = userDetails.getPassword();//存储的加密密码
        String rowPass = (String) authentication.getCredentials();
        logger.info("用户登录 username==" + userDetails.getUsername());
        if (!SecurityUtil.matches(encPass, rowPass)) {
            throw new AuthenticationServiceException(messages.getMessage("WrongPassword"));
        }
    }

    @Override
    protected UserDetails retrieveUser(String username,
                                       UsernamePasswordAuthenticationToken authentication)
            throws AuthenticationException {

        com.jytb.logistics.bean.common.User user = userService.findByUsername(username);
        System.out.println("User : " + user);
        if (user == null) {
            logger.info("登录失败 用户不存在 userusername==" + username);
            throw new AuthenticationServiceException(messages.getMessage("WrongPassword"));
        }
        User userNextStep = new User(user.getUsername(), user.getPassword(),
                user.getState().equals("Active"), true, true, true, getGrantedAuthorities(user));
        return userNextStep;
    }

    private List<GrantedAuthority> getGrantedAuthorities(com.jytb.logistics.bean.common.User user) {
        List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();

        for (Role role : user.getRoleList()) {
            authorities.add(new SimpleGrantedAuthority("ROLE_" + role.getRole()));
        }

        logger.info("登录 user==" + user.getUsername() + "角色==" + authorities);
        return authorities;
    }
}
