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

import com.seomse.jdbc.Database;
import com.seomse.jdbc.PrepareStatementData;
import com.seomse.jdbc.annotation.DateTime;
import com.seomse.jdbc.annotation.FlagBoolean;
import com.seomse.jdbc.annotation.PrimaryKey;
import com.seomse.jdbc.annotation.Sequence;
import com.seomse.jdbc.naming.JdbcDataType;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Comparator;
import java.util.Map;
import java.util.Set;

/**
 * jdbc common method util
 * @author macle
 */
public class JdbcCommon {


    /**
     * PreparedStatement ResultSet setting
     * @param conn Connection
     * @param sql String
     * @param prepareStatementDataMap Map<Integer, PrepareStatementData>
     * @return StmtResultSet
     * @throws SQLException
     */
    public static StmtResultSet makeStmtResultSet(Connection conn, String sql,  Map<Integer, PrepareStatementData> prepareStatementDataMap) throws SQLException {

        StmtResultSet stmtResultSet = new StmtResultSet();
        if(prepareStatementDataMap != null){
            PreparedStatement pstmt = conn.prepareStatement(sql);
            stmtResultSet.stmt = pstmt;

            setStmt(pstmt,prepareStatementDataMap);
            stmtResultSet.resultSet = pstmt.executeQuery();
        }else{
            stmtResultSet.stmt = conn.createStatement();
            stmtResultSet.resultSet = stmtResultSet.stmt.executeQuery(sql);
        }


        return stmtResultSet;
    }


    /**
     * prepareStatementDataMap 을 이용한 stmt 설정
     * @param pstmt PreparedStatement
     * @param prepareStatementDataMap  Map<Integer, PrepareStatementData>
     * @throws SQLException
     */
    public static void setStmt(PreparedStatement pstmt, Map<Integer, PrepareStatementData> prepareStatementDataMap) throws SQLException {
        Set<Integer> indexSet = prepareStatementDataMap.keySet();
        for(Integer index : indexSet){
            PrepareStatementData prepareStatementData = prepareStatementDataMap.get(index);
            if(prepareStatementData.getType() == JdbcDataType.DATE_TIME){
                Timestamp timestamp = new Timestamp((Long)prepareStatementData.getData());
                pstmt.setTimestamp(index, timestamp);
            }else if(prepareStatementData.getType() == JdbcDataType.STRING){
                pstmt.setString(index, (String) prepareStatementData.getData());
            }else if(prepareStatementData.getType() == JdbcDataType.INTEGER){
                pstmt.setInt(index, (Integer)prepareStatementData.getData());
            }else if(prepareStatementData.getType() == JdbcDataType.DOUBLE){
                pstmt.setDouble(index, (Double)prepareStatementData.getData());
            }else if(prepareStatementData.getType() == JdbcDataType.LONG){
                pstmt.setLong(index, (Long)prepareStatementData.getData());
            }
        }
    }

    /**
     *
     * @param obj  T jdbc object
     * @param fields Field []
     * @param pstmt PreparedStatement
     * @param <T> T jdbc object
     * @throws IllegalAccessException
     * @throws SQLException
     */
    public static <T> void addBatch(T obj, Field [] fields, PreparedStatement pstmt ) throws IllegalAccessException, SQLException {
        for(int i=0 ; i<fields.length ; i++){

            fields[i].setAccessible(true);
            Object object = fields[i].get(obj);

            if(object == null){
                setNullPstmt(obj, fields[i], pstmt, i);

            }else{
                setPstmt(obj,object, fields[i], pstmt, i);
            }

        }
        pstmt.addBatch();
    }

    /**
     *
     * @param obj T jdbc object
     * @param field Field
     * @param pstmt PreparedStatement
     * @param i int index
     * @param <T> T jdbc object
     * @throws SQLException
     * @throws IllegalAccessException
     */
    public static <T> void setNullPstmt(T obj, Field field, PreparedStatement pstmt, int i) throws SQLException, IllegalAccessException {
        Sequence sequence = field.getAnnotation(Sequence.class);
        if(sequence != null){
            String nextVal = Database.nextVal(sequence.name());
            nextVal =  sequence.prefix() + nextVal;

            field.set(obj, nextVal);
            pstmt.setString(i+1, nextVal);
            return;
        }

        DateTime dateTime = field.getAnnotation(DateTime.class);
        if(dateTime != null && !dateTime.isNullable()) {
            Timestamp timeStamp = new Timestamp(System.currentTimeMillis());
            pstmt.setTimestamp(i+1, timeStamp);
            return;
        }

        pstmt.setNull(i+1,  java.sql.Types.NULL);

    }

    /**
     * PreparedStatement data set
     * @param obj T jdbc object
     * @param object Object field set data
     * @param field Field
     * @param pstmt PreparedStatement
     * @param i int index
     * @param <T> T jdbc object
     * @throws SQLException
     * @throws IllegalAccessException
     */
    public static <T> void setPstmt(T obj, Object object,Field field, PreparedStatement pstmt, int i) throws SQLException, IllegalAccessException {

        if(field.getType().isEnum()){
            pstmt.setString(i+1, field.get(obj).toString());
            return;
        }


        FlagBoolean flagBoolean = field.getAnnotation(FlagBoolean.class);

        if(flagBoolean != null && (field.getType() == Boolean.TYPE || field.getType() == Boolean.class)){
            boolean isObj = field.getBoolean(obj);
            if(isObj){
                pstmt.setString(i+1, "Y");
            }else{
                pstmt.setString(i+1, "N");
            }

            return;
        }


        DateTime dateColumn =  field.getAnnotation(DateTime.class);
        if(dateColumn == null){
            if(field.getType() == String.class){
                pstmt.setString(i+1, (String)object);
            }else if(field.getType() == Long.TYPE || field.getType() == Long.class){
                pstmt.setLong(i+1, (long)object);
            }else if(field.getType() == Integer.TYPE || field.getType() == Integer.class){
                pstmt.setInt(i+1, (int)object);
            }else if(field.getType() == Float.TYPE || field.getType() == Float.class){
                pstmt.setFloat(i+1, (float)object);
            }else if(field.getType() == Double.TYPE ||field.getType() == Double.class){
                pstmt.setDouble(i+1, (double)object);
            }
        }else{

            Timestamp timeStamp = new Timestamp((long)object);
            pstmt.setTimestamp(i+1, timeStamp);
        }
    }

    /**
     * pk sort
     */
    public final static Comparator<Field> PK_SORT_ASC = Comparator.comparingInt(f -> f.getAnnotation(PrimaryKey.class).seq());

}
