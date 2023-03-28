package com.example.redisdemo.security;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
/*注销登录处理方案，
注销登录之后，系统自动跳转到登录页面，这也是不合适的，
如果是前后端分离项目，注销登录成功后返回 JSON 即可
*/
@Component
public class LogoutHandler implements LogoutSuccessHandler {
    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        response.setContentType("application/json;charset=utf-8");
        PrintWriter out=response.getWriter();
        out.write("注销成功");
        out.flush();
        out.close();
    }
}
