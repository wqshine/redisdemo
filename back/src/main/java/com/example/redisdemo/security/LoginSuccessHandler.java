package com.example.redisdemo.security;

import com.example.redisdemo.entity.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
/*登陆成功处理方案*/
@Component
public class LoginSuccessHandler implements AuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        /*获取用户信息*/
        User user =((MyUserDetails)authentication.getPrincipal()).getUser();
        response.setContentType("application/json;charset=utf-8");
        PrintWriter out=response.getWriter();
        out.write(new ObjectMapper().writeValueAsString(user));
        out.flush();
        out.close();
    }


}
