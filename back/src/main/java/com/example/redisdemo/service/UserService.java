package com.example.redisdemo.service;

import com.example.redisdemo.common.Result;
import com.example.redisdemo.entity.User;
import com.example.redisdemo.security.MyUserDetails;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;

public interface UserService {
    /**
     * @param user
     * @return
     */
    Result<Object> saveUser(User user);
    Result deleteUsers(List<User> users);
    User getCurrentUser();
    User findById(Integer id);
    List<User> findAll();
    static User getStaticCurrentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        MyUserDetails myUserDetails;
     /* getAuthorities: 获取用户权限，一般情况下获取到的是用户的角色信息。
        getCredentials: 获取证明用户认证的信息，通常情况下获取到的是密码等信息。
        getDetails: 获取用户的额外信息，（这部分信息可以是我们的用户表中的信息）。
        getPrincipal: 获取用户身份信息，在未认证的情况下获取到的是用户名，在已认证的情况下获取到的是 UserDetails。*/
        if (auth != null) {
            if (auth.getPrincipal() instanceof UserDetails) {
                myUserDetails = (MyUserDetails) auth.getPrincipal();
            } else if (auth.getDetails() instanceof UserDetails) {
                myUserDetails= (MyUserDetails) auth.getDetails();
            } else {
                return null;
            }
            return myUserDetails.getUser();
        } else
            return null;
    }
    Result<Object> changePassword(String oldPassword, String newPassword,String repeatPassword);
}
