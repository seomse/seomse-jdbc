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
 * <pre>
 *  파 일 명 : JdbcCommon.java
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
public class JdbcCommon {



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
                setPstmt(obj,object, fields[i], pstmt, i);


            }

        }

        pstmt.addBatch();
    }

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


    public final static Comparator<Field> PK_SORT_ASC =  new Comparator<Field>() {
        @Override
        public int compare(Field f1, Field f2 ) {
            return Integer.compare(f1.getAnnotation(PrimaryKey.class).seq(), f2.getAnnotation(PrimaryKey.class).seq());
        }
    };


    public static void main(String[] args) {

    }
}
