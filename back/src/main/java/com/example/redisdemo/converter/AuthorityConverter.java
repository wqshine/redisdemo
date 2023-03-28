package com.example.redisdemo.converter;

import com.example.redisdemo.enumConst.AuthorityEnum;

import javax.persistence.AttributeConverter;
import java.util.List;

public class AuthorityConverter implements AttributeConverter<List<AuthorityEnum>,Integer> {
    @Override
    public Integer convertToDatabaseColumn(List<AuthorityEnum> attribute) {
        if(attribute==null||attribute.size()==0) return null;
        int code=0;
        for (AuthorityEnum authorityEnum:attribute) {
            code=code+authorityEnum.getCode();
        }
        return code;
    }

    @Override
    public List<AuthorityEnum> convertToEntityAttribute(Integer dbData) {
        return dbData==null?null:AuthorityEnum.getAuthoritiesFromCode(dbData);
    }
}
