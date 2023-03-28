package com.example.redisdemo.security;
import com.example.redisdemo.common.Result;
import org.springframework.security.authentication.*;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
/*登陆失败处理方案，返回登陆失败详细信息*/
@Component
public class LoginFailureHandler implements AuthenticationFailureHandler {
    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        response.setContentType("application/json;charset=utf-8");
        PrintWriter out=response.getWriter();
        Result<Object> result=Result.errorResult(exception.getMessage());
        if(exception instanceof LockedException){
            result.setMessage("账户被锁定，请联系管理员");
        }else if(exception instanceof CredentialsExpiredException){
            result.setMessage("密码过期，请联系管理员");
        }else if(exception instanceof AccountExpiredException){
            result.setMessage("账户过期，请联系管理员");
        }else if(exception instanceof DisabledException){
            result.setMessage("账户被禁用,请联系管理员");
        }else if(exception instanceof BadCredentialsException){
            result.setMessage("用户名或密码错误，请重新输入！");
        }
        out.write(result.getMessage());
        out.flush();
        out.close();
    }
}
