/*
 * Copyright (C) 2020 Seomse Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.seomse.jdbc.common;

import com.seomse.jdbc.annotation.DateTime;
import com.seomse.jdbc.annotation.FlagBoolean;

import java.lang.reflect.Field;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

/**
 * jdbc 를 활용한 필드 세팅
 * @author macle
 */
public class JdbcField {



    /**
     *  필드를 활용한 Object 세팅
     * @param result ResultSet
     * @param field Field
     * @param columnName String
     * @param resultObj Object
     * @throws IllegalArgumentException IllegalArgumentException
     * @throws IllegalAccessException IllegalAccessException
     * @throws SQLException SQLException
     */
    public static void setFieldObject(ResultSet result, Field field, String columnName, Object resultObj ) throws IllegalArgumentException, IllegalAccessException, SQLException{
        field.setAccessible(true);


        if(field.getType().isEnum()){
            //noinspection unchecked,rawtypes
            field.set(resultObj, Enum.valueOf((Class<Enum>)field.getType(), result.getString(columnName)));
            return;
        }


        FlagBoolean flagBoolean = field.getAnnotation(FlagBoolean.class);

        if(flagBoolean != null && (field.getType() == Boolean.TYPE || field.getType() == Boolean.class)){
            String value = result.getString(columnName).trim();

            if(value.length() == 0){
                field.set(resultObj, false);
            }else{
                char ch = value.charAt(0);
                ch = Character.toUpperCase(ch);
                field.set(resultObj, ch == 'Y');
            }

            return;
        }


        DateTime dateTime =  field.getAnnotation(DateTime.class);

        if(dateTime == null){
            Class<?> classType  = field.getType();
            if(classType == String.class){
                field.set(resultObj, result.getString(columnName));
            }else if(classType == Long.class || classType == Long.TYPE){
                try{
                    long value =  result.getLong(field.getName());
                    if(result.wasNull()){
                        field.set(resultObj, null);
                    }else{
                        field.set(resultObj, value);
                    }

                }catch(Exception e){
                    String value =  result.getString(columnName);
                    if(result.wasNull()){
                        field.set(resultObj, null);
                    }else{
                        field.set(resultObj, Long.parseLong(value));
                    }
                }

            }else if(classType == Integer.class || classType == Integer.TYPE){

                try{
                    int value =  result.getInt(columnName);
                    if(result.wasNull()){
                        field.set(resultObj, null);
                    }else{
                        field.set(resultObj, value);
                    }

                }catch(Exception e){
                    String value =  result.getString(columnName);
                    if(result.wasNull()){
                        field.set(resultObj, null);
                    }else{
                        field.set(resultObj, Integer.parseInt(value));
                    }
                }


            }else if(classType == Float.class || classType == Float.TYPE){

                try{
                    float value =  result.getFloat(columnName);
                    if(result.wasNull()){
                        field.set(resultObj, null);
                    }else{
                        field.set(resultObj, value);
                    }

                }catch(Exception e){
                    String value =  result.getString(columnName);
                    if(result.wasNull()){
                        field.set(resultObj, null);
                    }else{
                        field.set(resultObj, Float.parseFloat(value));
                    }
                }

            }else if(classType == Double.class || classType == Double.TYPE){

                try{
                    double value =  result.getDouble(columnName);
                    if(result.wasNull()){
                        field.set(resultObj, null);
                    }else{
                        field.set(resultObj, value);
                    }

                }catch(Exception e){
                    String value =  result.getString(columnName);
                    if(result.wasNull()){
                        field.set(resultObj, null);
                    }else{
                        field.set(resultObj, Double.parseDouble(value));
                    }
                }
            }
        }else{

            try{
                Timestamp timeStamp = result.getTimestamp(columnName);
                if(timeStamp == null){
                    field.set(resultObj, null);
                }else{
                    field.set(resultObj, timeStamp.getTime());
                }
            }catch(Exception e){
                Object value = result.getObject(columnName);
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
