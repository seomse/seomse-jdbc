package com.seomse.jdbc.common;

import com.seomse.jdbc.annotation.DateTime;

import java.lang.reflect.Field;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

/**
 * <pre>
 *  파 일 명 : JdbcField.java
 *  설    명 : jdbc를 활용한 field 처리
 *
 *  작 성 자 : macle
 *  작 성 일 : 2019.10.17
 *  버    전 : 1.0
 *  수정이력 :
 *  기타사항 :
 * </pre>
 * @author Copyrights 2019 by ㈜섬세한사람들. All right reserved.
 */
public class JdbcField {

    /**
     * 필드를 활용한 Object 세팅
     * @param result
     * @param field
     * @param columnName
     * @param resultObj
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws SQLException
     */
    public static void setFieldObject(ResultSet result, Field field, String columnName, Object resultObj ) throws IllegalArgumentException, IllegalAccessException, SQLException{
        field.setAccessible(true);
        DateTime dateTime =  field.getAnnotation(DateTime.class);
        if(dateTime == null){
            Class<?> classType  = field.getType();
            if(classType == String.class){
                field.set(resultObj, result.getString(field.getName()));
            }else if(classType == Long.class || classType == Long.TYPE){
                try{
                    long value =  result.getLong(field.getName());
                    if(result.wasNull()){
                        field.set(resultObj, null);
                    }else{
                        field.set(resultObj, value);
                    }

                }catch(Exception e){
                    String value =  result.getString(field.getName());
                    if(result.wasNull()){
                        field.set(resultObj, null);
                    }else{
                        field.set(resultObj, Long.parseLong(value));
                    }
                }

            }else if(classType == Integer.class || classType == Integer.TYPE){

                try{
                    int value =  result.getInt(field.getName());
                    if(result.wasNull()){
                        field.set(resultObj, null);
                    }else{
                        field.set(resultObj, value);
                    }

                }catch(Exception e){
                    String value =  result.getString(field.getName());
                    if(result.wasNull()){
                        field.set(resultObj, null);
                    }else{
                        field.set(resultObj, Integer.parseInt(value));
                    }
                }


            }else if(classType == Float.class || classType == Float.TYPE){

                try{
                    float value =  result.getFloat(field.getName());
                    if(result.wasNull()){
                        field.set(resultObj, null);
                    }else{
                        field.set(resultObj, value);
                    }

                }catch(Exception e){
                    String value =  result.getString(field.getName());
                    if(result.wasNull()){
                        field.set(resultObj, null);
                    }else{
                        field.set(resultObj, Float.parseFloat(value));
                    }
                }

            }else if(classType == Double.class || classType == Double.TYPE){

                try{
                    double value =  result.getDouble(field.getName());
                    if(result.wasNull()){
                        field.set(resultObj, null);
                    }else{
                        field.set(resultObj, value);
                    }

                }catch(Exception e){
                    String value =  result.getString(field.getName());
                    if(result.wasNull()){
                        field.set(resultObj, null);
                    }else{
                        field.set(resultObj, Double.parseDouble(value));
                    }
                }
            }
        }else{

            try{
                Timestamp timeStamp = result.getTimestamp(field.getName());
                if(timeStamp == null){
                    field.set(resultObj, null);
                }else{
                    field.set(resultObj, timeStamp.getTime());
                }
            }catch(Exception e){
                Object value = result.getObject(field.getName());
                if( value == null){
                    field.set(resultObj, null);
                }else{
                    if(value.getClass() == Long.class || value.getClass() == Long.TYPE){
                        field.set(resultObj, value);
                    }else{
                        field.set(resultObj, Long.parseLong(value.toString()));
                    }

                }


            }
        }
    }

}
