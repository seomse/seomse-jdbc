package com.seomse.jdbc.common;

import java.sql.ResultSet;
import java.sql.Statement;
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
public class StmtResultSet {

    Statement stmt = null;
    ResultSet resultSet = null;

    StmtResultSet(){


    }

    public Statement getStmt() {
        return stmt;
    }

    public ResultSet getResultSet() {
        return resultSet;
    }
}
