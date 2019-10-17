package com.seomse.jdbc.common;

import com.seomse.jdbc.PrepareStatementData;
import com.seomse.jdbc.naming.JdbcDataType;

import java.sql.*;
import java.util.Map;
import java.util.Set;

/**
 * <pre>
 *  파 일 명 : StmtResultSetUtil.java
 *  설    명 : stmt 와 ResultSet 관련 처리
 *
 *  작 성 자 : macle
 *  작 성 일 : 2019.10.17
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
     * prepareStatementDataMap을 이용한 stmt 설정
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
}
