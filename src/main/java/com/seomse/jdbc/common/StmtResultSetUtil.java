package com.seomse.jdbc.common;

import com.seomse.jdbc.Database;
import com.seomse.jdbc.PrepareStatementData;
import com.seomse.jdbc.annotation.DateTime;
import com.seomse.jdbc.annotation.Sequence;
import com.seomse.jdbc.naming.JdbcDataType;

import java.lang.reflect.Field;
import java.sql.*;
import java.util.Map;
import java.util.Set;

/**
 * <pre>
 *  파 일 명 : StmtResultSetUtil.java
 *  설    명 : stmt 와 ResultSet 관련 처리
 *
 *  작 성 자 : macle
 *  작 성 일 : 2019.10.18
 *  버    전 : 1.0
 *  수정이력 :
 *  기타사항 :
 * </pre>
 * @author Copyrights 2019 by ㈜섬세한사람들. All right reserved.
 */
public class StmtResultSetUtil {



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
     * @param prepareStatementDataMap prepareStatementDataMap
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

    public static <T> void addBatch(T obj, Field [] fields, PreparedStatement pstmt ) throws IllegalAccessException, SQLException {
        for(int i=0 ; i<fields.length ; i++){
            fields[i].setAccessible(true);
            Object object = fields[i].get(obj);

            if(object == null){
                Sequence sequence = fields[i].getAnnotation(Sequence.class);
                if(sequence == null){
                    pstmt.setNull(i+1,  java.sql.Types.NULL);
                }else{
                    String nextVal = Database.nextVal(sequence.name());
                    nextVal =  sequence.prefix() + nextVal;

                    fields[i].set(obj, nextVal);
                    pstmt.setString(i+1, nextVal);
                }

            }else{
                setPstmt(obj,fields[i], pstmt, i);


            }

        }

        pstmt.addBatch();
    }

    public static <T> void setPstmt(T obj, Field field, PreparedStatement pstmt, int i) throws SQLException, IllegalAccessException {

        DateTime dateColumn =  field.getAnnotation(DateTime.class);

        if(dateColumn == null){
            if(field.getType() == String.class){
                pstmt.setString(i+1, (String)field.get(obj));
            }else if(field.getType() == Long.TYPE || field.getType() == Long.class){
                pstmt.setLong(i+1, field.getLong(obj));
            }else if(field.getType() == Integer.TYPE || field.getType() == Integer.class){
                pstmt.setInt(i+1, field.getInt(obj));
            }else if(field.getType() == Float.TYPE || field.getType() == Float.class){
                pstmt.setFloat(i+1, field.getFloat(obj));
            }else if(field.getType() == Double.TYPE ||field.getType() == Double.class){
                pstmt.setDouble(i+1, field.getDouble(obj));
            }
        }else{
            Timestamp timeStamp = new Timestamp(field.getLong(obj));
            pstmt.setTimestamp(i+1, timeStamp);
        }
    }


    public static void main(String[] args) {

    }
}
