package com.example.redisdemo.controller;

import com.example.redisdemo.common.Result;
import com.example.redisdemo.entity.User;
import com.example.redisdemo.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name="用户控制器")
@RestController
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @Operation(summary = "用户保存")
    @PostMapping("/saveUser")
    public Result<Object> saveUser(@Parameter(description = "用户") @RequestBody User user){
        return userService.saveUser(user);
    }

    /**
     * 删除用户
     * @param users 用户集合
     * @return 是否成功
     */
    @PostMapping("/deleteUsers")
    @Operation(summary ="批量删除用户")
    public Result deleteUsers(@Parameter(description = "要删除的用户") @RequestBody List<User> users) {
        return userService.deleteUsers(users);
    }

    /**
     * 获取系统当前登录用户
     * 返回的是User对象
     * @return 当前登录用户
     */
    @PostMapping("/getCurrentUser")
    @Operation(summary = "获取系统当前登录用户，返回的是User对象")
    public User getCurrentUser(){
        return UserService.getStaticCurrentUser();
    }
    /**
     * 查询用户
     * @param id 用户ID
     * @return 用户，此用户可以是deleted=true的用户，同时返回关联的Roles，关联Roles中包含已删除的角色
     */
    @PostMapping("/findUser")
    @Operation(summary ="根据用户ID查询用户，同时返回关联的角色，返回的用户可以是已删除用户，返回的关联角色包含已删除的角色")
    public Result<User> findUser(@Parameter(description = "用户id") @RequestParam Integer id) {
        Result<User> result = new Result<User>();
        try {
            User user=userService.findById(id);
            user.getRoles().size();
            result.setData(user);
            result.setSuccess(true);
        } catch (Exception e) {
            result.setMessage(e.getMessage());
        }
        return result;
    }
    @PostMapping("/listUsers")
    @Operation(summary ="获取用户集合")
    public Result<List<User>> listUsers(){
        Result<List<User>> result=new Result<>();
        try {
            List<User> users=userService.findAll();
            result.setData(users);
            result.setSuccess(true);
        }catch (Exception e){
            result.setMessage(e.getMessage());
        }
        return result;
    }

    /**
     * 当前用户修改密码
     * @param oldPassword 旧密码
     * @param newPassword 新密码
     * @param repeatPassword 再次输入新密码
     * @return 是否成功
     */

    @PostMapping("/changePassword")
    @Operation(summary ="当前用户修改密码")
    public Result changePassword(@Parameter(description = "旧密码") @RequestParam String oldPassword,
                                 @Parameter(description = "新密码") @RequestParam String newPassword,
                                 @Parameter(description = "再次输入密码") @RequestParam String repeatPassword) {
        return userService.changePassword(oldPassword,newPassword,repeatPassword);
    }
}
