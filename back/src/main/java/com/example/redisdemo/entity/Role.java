package com.example.redisdemo.entity;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Data
@Entity
@Schema(description = "角色类")
@JsonIdentityInfo(generator = ObjectIdGenerators.UUIDGenerator.class)
@Table(
        name = "sys_role",
        indexes = {@Index(columnList = "roleName")},
        uniqueConstraints={@UniqueConstraint(columnNames={"roleName"})}
)
public class Role implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    /**
     * 角色名
     */
    @Schema(description = "角色名")
    private String roleName;
    /**
     * 备注
     */
    @Schema(description = "备注")
    private String memo;
    /**
     * 删除标志
     */
    @Column(nullable = false,columnDefinition = "boolean default 0")
    @Schema(description ="删除标志")
    private boolean deleted;
    /**
     * 用户
     */
    @Schema(description = "用户")
    @ManyToMany(mappedBy = "roles")
    private List<User> users;
}
