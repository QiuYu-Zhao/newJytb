package com.jytb.logistics.security;

import co.chexiao.common.util.PropertiesUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.encoding.ShaPasswordEncoder;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * Created by fulei on 2016-12-13.
 */
public class SecurityUtil {

    private static final Logger logger = LoggerFactory.getLogger(SecurityUtil.class);

    static BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(11);
    static ShaPasswordEncoder shaEncoder = new ShaPasswordEncoder(256);
    static String defaultPassword = "111111";
    static String salt = "safe";

    static final String LOGINCODE = "LOGINCODE";
    static int loginWaitTime = Integer.valueOf(PropertiesUtil.getProperty("loginWaitTime", "120")); //生成的登录图片有效时间，单位秒

    static int width = 90;//定义图片的width
    static int height = 20;//定义图片的height
    static int codeCount = 4;//定义图片上显示验证码的个数
    static int xx = 15;
    static int fontHeight = 18;
    static int codeY = 16;
    static char[] codeSequence = { 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H',
//            'I',
            'J','K',
//            'L',
            'M', 'N',
//            'O',
            'P',
//            'Q',
            'R', 'S', 'T', 'U', 'V', 'W',
            'X', 'Y', 'Z',
//            '0', '1',
            '2', '3', '4', '5', '6', '7', '8', '9' };


    public static String encode(String pwd) {
        return shaEncoder.encodePassword(pwd, salt);
    }

    /**
     * @param encPass 加密的密码
     * @param rawPass 验证的密码
     * @return 是否匹配
     */
    public static boolean matches(String encPass, String rawPass) {
        return shaEncoder.isPasswordValid(encPass, rawPass, salt);
    }

    public static String getDefaultPassword() {
        return encode(defaultPassword);
    }

    private static void logout() {
        logger.error("您已超时，请重新登录");
        SecurityContextHolder.getContext().setAuthentication(null);
        SecurityContextHolder.clearContext();
    }



}
