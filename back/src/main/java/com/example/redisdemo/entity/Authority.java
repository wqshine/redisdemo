package com.example.redisdemo.entity;

import com.example.redisdemo.converter.AuthorityConverter;
import com.example.redisdemo.enumConst.AuthorityEnum;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Data
@Entity
@Schema(description = "权限类")
@JsonIdentityInfo(generator = ObjectIdGenerators.UUIDGenerator.class)
@Table(
        name = "sys_authority",
        indexes = {@Index(columnList = "authorityName"),@Index(columnList = "roleName")}
)
public class Authority implements Serializable {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Integer id;
    /**
     * 权限名称
     */
    @Column(unique = true)
    @Schema(description = "权限名称")
    private String authorityName;
    /**
     * 权限类别(URL,MENU)
     */
    @Schema(description = "权限类别")
    private String authorityType;
    /**
     * 角色名
     * 这里因权限资源少，并没有建立角色-权限的关联关系
     */
    @Schema(description = "角色名")
    private String roleName;
    /**
     * 权限掩码
     * 当type=menu时，可具体为增删改查权限，用掩码记录
     */
    @Schema(description = "权限掩码")
    @Convert(converter = AuthorityConverter.class )
    private List<AuthorityEnum> authorityEnums;
    /**
     * 备注
     */
    @Schema(description = "备注")
    private String memo;
}
