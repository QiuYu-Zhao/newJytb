package com.jytb.logistics.interceptor;

import com.jytb.logistics.service.common.MkSessionHolder;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Created by fulei on 2017/3/20.
 * 所有的 springmvc请求都拦截，将cookie sessionid 放到当前上下文中
 */
public class SessionInterceptor implements HandlerInterceptor {
//    private static final Logger logger = LoggerFactory.getLogger(SessionInterceptor.class);

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object object, Exception modelAndView)
            throws Exception {
//        logger.info("处理完毕 移除===" + MkSessionHolder.getCurrentHttpSession());
        MkSessionHolder.removeCurrentHttpSession();//处理完成后移除
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object object, ModelAndView modelAndView)
            throws Exception {

    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object object) throws Exception {
        HttpSession session = request.getSession(true);
        //session中获取用户名信息
        /*Object obj = session.getAttribute("user");
        if (obj == null || "".equals(obj.toString())) {
            request.getRequestDispatcher("/logout.html").forward(request,response);
            //request.getRequestDispatcher("/logout.jsp").forward(request,response);

            return true;
        }*/
        MkSessionHolder.setCurrentHttpSession(request.getSession());//当前线程放入session
        return true;
    }

}
