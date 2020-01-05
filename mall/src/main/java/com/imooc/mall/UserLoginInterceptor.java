package com.imooc.mall;

import com.imooc.mall.exception.UserLoginException;
import com.imooc.mall.pojo.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static com.imooc.mall.consts.MallConst.CURRENT_USER;

@Slf4j
public class UserLoginInterceptor implements HandlerInterceptor {
    /**
     * true 表示繼續流程，false 表示中斷
     *
     * @param request
     * @param response
     * @param handler
     * @return
     * @throws Exception
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        log.info("preHandle...");
        User user = (User) request.getSession().getAttribute(CURRENT_USER);
        if (user == null) {
            log.info("user=null");

            throw new UserLoginException();
            // return ResponseVo.error(ResponseEnum.NEED_LOGIN);
        }
        return true;
    }
}
