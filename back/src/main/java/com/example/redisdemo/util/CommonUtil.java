package com.example.redisdemo.util;

import java.util.Random;

public class CommonUtil {
    public static String genRandomNum(int char_len) {
        char[] str = new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9'};
        return getRandomChar(char_len, str);
    }

    private static String getRandomChar(int pwd_len, char[] str) {
        int maxNum = str.length;
        int count = 0;
        StringBuffer pwd = new StringBuffer("");
        Random r = new Random();

        while(count < pwd_len) {
            int i = Math.abs(r.nextInt(maxNum));
            if(i >= 0 && i < str.length) {
                pwd.append(str[i]);
                ++count;
            }
        }

        return pwd.toString();
    }

    public static boolean IsNotNullOrEmpty(String s){
        if(s!=null && !"".equals(s)){
            return true;
        }else{
            return false;
        }
    }

    public static boolean isNullOrEmpty(String s) {
        return s == null || s.isEmpty();
    }

    public static boolean isBlank(CharSequence cs) {
        int strLen = length(cs);
        if (strLen == 0) {
            return true;
        } else {
            for(int i = 0; i < strLen; ++i) {
                if (!Character.isWhitespace(cs.charAt(i))) {
                    return false;
                }
            }
            return true;
        }
    }

    public static int length(CharSequence cs) {
        return cs == null ? 0 : cs.length();
    }

}
