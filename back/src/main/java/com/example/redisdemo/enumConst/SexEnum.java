package com.example.redisdemo.enumConst;

import lombok.Getter;

@Getter
public enum SexEnum {
    /*0、未知，1、男，2、女，3、女改男，4、男改女，5、其他*/
    Unknown("未知",0),
    Male("男",1),
    Female("女",2),
    FemaleToMale("女改男",3),
    MaleToFemale("男改女",4),
    Other("其他",5);
    private final String sex;
    private final int code;
    SexEnum(String sex, int code){
        this.sex=sex;
        this.code=code;
    }

    public static SexEnum fromCode(int code) {
        for (SexEnum sexEnum : SexEnum.values()) {
            if(code== sexEnum.getCode()){
                return sexEnum;
            }
        }
        throw new IllegalArgumentException();
    }

}
