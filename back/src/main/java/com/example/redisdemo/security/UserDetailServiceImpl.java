package com.example.redisdemo.security;

import com.example.redisdemo.entity.Role;
import com.example.redisdemo.entity.User;
import com.example.redisdemo.repo.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


/*用户登录验证，对比从数据库中取得的密码*/
public class UserDetailServiceImpl implements UserDetailsService {
    private final UserRepository userRepository;
    @Autowired
    private EntityManager entityManager;
    public UserDetailServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        /*关键在于此查询上的@EntityGraph(attributePaths="roles")注解，可以实现懒加载获取roles*/
        User user= userRepository.findByUserNameAndDeletedFalse(username);
        if (user!=null){
            UserDetails myUserDetails;
           /* String sql="select * from sys_role sr where sr .id in (select sur.roles_id from sys_user_roles sur where sur.users_id=:userId)";
            Query query= entityManager.createNativeQuery(sql,Role.class);
            query.setParameter("userId",user.getId());*/
            List<Role> roles=user.getRoles();
            Collection<GrantedAuthority> grantedAuthorities=new ArrayList<>();
            SimpleGrantedAuthority simpleGrantedAuthority;
            for (Role role:roles) {
                /*因权限与角色名（roleName）关联，传入roleName*/
                simpleGrantedAuthority=new SimpleGrantedAuthority(role.getRoleName());
                grantedAuthorities.add(simpleGrantedAuthority);
            }
            myUserDetails=new MyUserDetails(user,grantedAuthorities);
            return myUserDetails;
        }else
            throw new UsernameNotFoundException("用户名错误");
    }
}
