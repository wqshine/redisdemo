package com.example.redisdemo.security;

import com.example.redisdemo.entity.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

public class MyUserDetails implements UserDetails {
    private static final long serialVersionUID = -821404331789343913L;
    private final User user;
    private final Collection<GrantedAuthority> authorityCollection;
    public MyUserDetails(User user,Collection<GrantedAuthority> authorityCollection){
        this.user=user;
        this.authorityCollection=authorityCollection;
    }

    public  User getUser(){
        return this.user;
    }
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.authorityCollection;
    }

    @Override
    public String getPassword() {
        return this.user.getPassword();
    }

    @Override
    public String getUsername() {
        return this.user.getUserName();
    }

    /*账号没有过期*/
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }
    /*账号没有锁定*/
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }
    /*证书没有过期*/
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }
    /*是否启用*/
    @Override
    public boolean isEnabled() {
        return true;
    }
}
