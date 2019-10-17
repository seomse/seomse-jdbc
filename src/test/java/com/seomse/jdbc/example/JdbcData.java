package com.seomse.jdbc.example;

import com.seomse.commons.packages.classes.field.FieldUtil;

import java.lang.reflect.Field;

public class JdbcData {
    String test;
    long logs;
    Long longs;

    public static void main(String[] args) {
        Field[] fields = FieldUtil.getFieldArrayToParents(JdbcData.class);

        for(Field field : fields){
            if(Long.TYPE ==field.getType()){
                System.out.println("롱타입");
            }

            if(String.class == field.getType()){
                System.out.println("문자열타입");
            }

            System.out.println(field.getName() + ", " + field.getType());
        }
    }



}
