package com.example.redisdemo.entity;

import com.example.redisdemo.converter.SexConverter;
import com.example.redisdemo.enumConst.SexEnum;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Data
@Entity
@Schema(description = "用户类")
@JsonIdentityInfo(generator = ObjectIdGenerators.UUIDGenerator.class)
@Table(
        name = "sys_user",
        indexes = {@Index(columnList = "userName")}
)
public class User implements Serializable {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Integer id;
    /**
     * 用户名
     */
    @Column(unique = true)
    @Schema(description = "用户名")
    private String userName;
    /**
     * 密码
     */
    @JsonIgnore
    @Schema(description = "密码")
    private String password;
    /**
     * 管理员标志
     */
    @Column(nullable = false,columnDefinition = "number(1) default 0")
    @Schema(description = "管理员标志")
    private Boolean admin=false;

    /*以下为用户信息*/
    /**
     * 姓名
     */
    @Schema(description = "姓名")
    private String fullName;
    /**
     * 身份证
     */
    @Schema(description = "身份证")
    private String identity;
    /**
     * 年龄
     */
    @Schema(description = "年龄")
    private Integer age;
    /**
     * 性别
     */
    @Schema(description = "性别")
    @Convert(converter = SexConverter.class)
    private SexEnum sexEnum;
    /**
     * 电话号码
     */
    @Schema(description = "电话号码")
    private String phoneNumber;
    /**
     * qq号
     */
    @Schema(description = "qq号")
    private String qq;
    /**
     * 删除标志
     */
    @Column(nullable = false,columnDefinition ="boolean default 0" )
    @Schema(description ="删除标志")
    private boolean deleted;
    /**
     * 角色
     */
    @Schema(description = "角色")
    @ManyToMany
    private List<Role> roles;
}


