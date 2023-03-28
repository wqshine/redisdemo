package com.example.redisdemo.enumConst;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public enum AuthorityEnum {
    Readonly("只读",1),
    Add("新增",2),
    Edit("编辑",4),
    Delete("删除",8);
    private final String name;
    private final int code;
    AuthorityEnum(String name,int code){
        this.name=name;
        this.code=code;
    }

    public static List<AuthorityEnum> getAuthoritiesFromCode(int code){
        List<AuthorityEnum> authorityEnums=new ArrayList<>();
        for (AuthorityEnum authorityEnum:AuthorityEnum.values()) {
            if(authorityEnum.code == (authorityEnum.code & code)){
                authorityEnums.add(authorityEnum);
            }
        }
        return authorityEnums;
    }
}
