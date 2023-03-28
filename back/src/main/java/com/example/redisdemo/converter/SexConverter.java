package com.example.redisdemo.converter;

import com.example.redisdemo.enumConst.SexEnum;

import javax.persistence.AttributeConverter;

public class SexConverter implements AttributeConverter<SexEnum, Integer> {
    @Override
    public Integer convertToDatabaseColumn(SexEnum attribute) {
        if(attribute==null) return null;
        for (SexEnum sexEnum : SexEnum.values()) {
            if(attribute.equals(sexEnum)){
                return sexEnum.getCode();
            }
        }
        return null;
    }

    @Override
    public SexEnum convertToEntityAttribute(Integer dbData) {
        return dbData==null?null: SexEnum.fromCode(dbData);
    }

}
