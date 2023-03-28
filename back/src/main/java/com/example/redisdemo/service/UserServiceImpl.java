package com.example.redisdemo.service;

import com.example.redisdemo.common.Result;
import com.example.redisdemo.entity.User;
import com.example.redisdemo.repo.UserRepository;
import com.example.redisdemo.util.CommonUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

@Slf4j
@Service
public class UserServiceImpl implements UserService{
    private final String checkLoginNameRegex;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final String adminName;
    private final String adminInitPassword;
    private final String userInitPassword;
    {
        checkLoginNameRegex = "[^'\"\\s<>#&]*";
        adminName = "admin";
        adminInitPassword = "tz";
        userInitPassword="123456";
    }
    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * 系统初始化，生成admin用户
     * @return
     */
    @PostConstruct
    public Result initAdmin() {
        long admins = userRepository.countByAdminTrueAndDeletedFalse();
        if (admins == 0) {
            User admin = new User();
            admin.setUserName(adminName);
            admin.setPassword(passwordEncoder.encode(adminInitPassword));
            admin.setAdmin(true);
            admin.setDeleted(false);
            admin.setFullName("超级管理员");
            userRepository.save(admin);
        }
        return Result.successResult();
    }

    @Override
    public User findById(Integer id){
        return userRepository.findById(id).orElse(null);
    }

    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    }
    @Override
    public User getCurrentUser() {
        return UserService.getStaticCurrentUser();
    }

    @Override
    public Result<Object> changePassword(String oldPassword, String newPassword,String repeatPassword) {
        try{
            User anUser = this.getCurrentUser();
            if (anUser == null) {
                return Result.errorResult("当前用户不存在，可能登录超时，请尝试重新登录");
            }
            else {
                if(newPassword!=null && !"".equals(newPassword)) {
                    if(repeatPassword!=null && repeatPassword.equals(newPassword)) {
                        /*BCryptPasswordEncoder相同密码生成的密文不一致，因此不能调用encode比较密文，应该直接调用match方法*/
                        if (passwordEncoder.matches(oldPassword,anUser.getPassword())) {
                            User changeUser = userRepository.findByUserNameAndDeletedFalse(anUser.getUserName());
                            changeUser.setPassword(passwordEncoder.encode(newPassword));
                            userRepository.save(changeUser);
                            //新密码写入当前用户
                            anUser.setPassword(changeUser.getPassword());
                            return Result.successResult();
                        } else {
                            return Result.errorResult("旧密码不正确");
                        }
                    }
                    else{
                        return Result.errorResult("两次输入密码不一致");
                    }
                }
                else
                    return Result.errorResult("修改后密码不能为空");
            }
        }
        catch (Exception e){
            log.error("UserServiceImpl changePassword error:",e);
            return Result.errorResult(e.getMessage());
        }
    }

    @Override
    public Result<Object> saveUser(User user) {
        try{
            if(CommonUtil.isNullOrEmpty(user.getUserName())){
                return Result.errorResult("用户名不能为空");
            }
            boolean check=checkUserName(user.getUserName());
            User theSameNameUser=userRepository.findByUserNameAndDeletedFalse(user.getUserName());
            if(user.getId()==null){
                if(theSameNameUser!=null){
                    return Result.errorResult("用户名已存在");
                }
                else {
                    String password=passwordEncoder.encode(userInitPassword);
                    user.setPassword(password);
                    userRepository.save(user);
                    return Result.successResult("用户创建成功");
                }
            }else {
                if (theSameNameUser!=null){
                    if(user.getId().equals(theSameNameUser.getId())){
                        user.setPassword(theSameNameUser.getPassword());
                        userRepository.save(user);
                        return Result.successResult("编辑用户成功");
                    } else
                        return Result.errorResult("登录名已经存在");
                }else {
                    User user1 = userRepository.getReferenceById(user.getId());
                    user.setPassword(user1.getPassword());
                    userRepository.save(user);
                    return Result.successResult("编辑用户成功");
                }
            }
        }
        catch (Exception e){
            log.error("UserServiceImpl saveUser error:",e);
            return Result.errorResult(e.getMessage());
        }

    }

    /*软删除用户，不删除用户角色关联关系*/
    @Override
    public Result deleteUsers(List<User> users){
        try{
            List<User> disabledUsers=new ArrayList<>();
            for (User user : users) {
                User user1 = userRepository.getReferenceById(user.getId());
                if (user1 != null) {
                    if(user1.getAdmin())
                        return Result.errorResult("超级管理员不能禁用");
                    else {
                        user1.setDeleted(true);
                        disabledUsers.add(user1);
                    }
                }
            }
            if(disabledUsers.size()>0)
                userRepository.saveAll(disabledUsers);
            return Result.successResult();
        }
        catch (Exception e){
            log.error("UserServiceImpl disableUsers error:",e);
            return Result.errorResult(e.getMessage());
        }
    }
    private boolean checkUserName(String userName) {
        return Pattern.matches(checkLoginNameRegex, userName);
    }

}
