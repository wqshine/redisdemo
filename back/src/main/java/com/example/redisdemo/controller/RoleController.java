package com.example.redisdemo.controller;


import com.example.redisdemo.common.Result;
import com.example.redisdemo.entity.Role;
import com.example.redisdemo.service.RoleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name="角色控制器")
@RestController
@RequestMapping("/role")
public class RoleController {

    private final RoleService roleService;
    public RoleController(RoleService roleService) {
        this.roleService = roleService;
    }

    @PostMapping("/listRoles")
    @Operation(summary ="获取角色集合")
    public Result<List<Role>> listRoles(){
        Result<List<Role>> result=new Result<>();
        try {
            List<Role> roles=roleService.findAll();
            result.setData(roles);
            result.setSuccess(true);
        }catch (Exception e){
            result.setMessage(e.getMessage());
        }
        return result;
    }
}
