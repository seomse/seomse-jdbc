package com.seomse.jdbc.common;

import java.sql.ResultSet;
import java.sql.Statement;

/**
 * <pre>
 *  파 일 명 : JdbcClose.java
 *  설    명 : jdbc 에서 close 메소드를 호출해야 하는 부분 공통화
 *
 *  작 성 자 : macle
 *  작 성 일 : 2019.06.04
 *  버    전 : 1.0
 *  수정이력 :
 *  기타사항 :
 * </pre>
 * @author Copyrights 2019 by ㈜섬세한사람들. All right reserved.
 */
public class JdbcClose {

    /**
     * Statement, ResultSet close
     * @param statement Statement
     * @param resultSet ResultSet
     */
    public static void statementResultSet(Statement statement, ResultSet resultSet ){

        if(statement != null){
            //noinspection CatchMayIgnoreException
            try{statement.close();}catch(Exception e){}
        }

        if(resultSet != null){
            //noinspection CatchMayIgnoreException
            try{resultSet.close();}catch(Exception e){}
        }

    }

}
