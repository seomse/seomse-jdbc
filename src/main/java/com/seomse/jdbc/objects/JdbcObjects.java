package com.seomse.jdbc.objects;

import com.seomse.commons.packages.classes.field.FieldUtil;
import com.seomse.jdbc.Database;
import com.seomse.jdbc.JdbcClose;
import com.seomse.jdbc.PrepareStatementData;
import com.seomse.jdbc.annotation.Column;
import com.seomse.jdbc.annotation.Table;
import com.seomse.jdbc.common.JdbcField;
import com.seomse.jdbc.common.StmtResultSet;
import com.seomse.jdbc.common.StmtResultSetUtil;
import com.seomse.jdbc.common.TableSql;
import com.seomse.jdbc.connection.ApplicationConnectionPool;
import com.seomse.jdbc.exception.FieldNullException;
import com.seomse.jdbc.exception.TableNameEmptyException;
import com.seomse.jdbc.naming.JdbcDataType;
import com.seomse.jdbc.naming.JdbcNamingDataType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.sql.*;
import java.util.*;

/**
 * <pre>
 *  파 일 명 : JdbcObjects.java
 *  설    명 : Jdbc 자바형객체
 *
 *  작 성 자 : macle
 *  작 성 일 : 2019.10.17
 *  버    전 : 1.0
 *  수정이력 :
 *  기타사항 :
 * </pre>
 * @author Copyrights 2019 by ㈜섬세한사람들. All right reserved.
 */
public class JdbcObjects {

    private static final Logger logger = LoggerFactory.getLogger(JdbcObjects.class);


    /**
     * 객체결과 리스트 얻기
     * @param conn 연결 컨넥션
     * @param objClass 객체 클래스
     * @return jdbc객체리스트
     */
    public static <T> List<T> getObjList(Connection conn, Class<T> objClass ) throws IllegalAccessException, SQLException, InstantiationException {
        return getObjList(conn, objClass, null, null, null, -1, null);

    }

    /**
     * 객체결과 리스트 얻기
     * @param conn 연결 컨넥션
     * @param objClass 객체 클래스
     * @param whereValue 조건문
     * @return jdbc객체리스트
     */
    public static <T> List<T> getObjList(Connection conn, Class<T> objClass , String whereValue) throws IllegalAccessException, SQLException, InstantiationException {
        return getObjList(conn,  objClass, null, whereValue, null, -1, null);
    }


    /**
     * 객체결과 리스트 얻기
     * @param conn 연결 컨넥션
     * @param objClass 객체 클래스
     * @param whereValue 조건문
     * @param orderByValue 정렬문
     * @return jdbc 객체리스트
     */
    public static <T> List<T> getObjList(Connection conn, Class<T> objClass , String whereValue, String orderByValue) throws IllegalAccessException, SQLException, InstantiationException {
        return getObjList(conn,  objClass, null, whereValue, orderByValue, -1, null);
    }

    /**
     * 객체결과 리스트 얻기
     * @param conn 연결 컨넥션
     * @param objClass 객체 클래스
     * @param whereValue 조건문
     * @param orderByValue 정렬문
     * @param size 건수제한
     * @return jdbc 객체리스트
     */
    public static <T> List<T> getObjList(Connection conn, Class<T> objClass , String whereValue, String orderByValue, int size) throws IllegalAccessException, SQLException, InstantiationException {
        return getObjList(conn,  objClass, null, whereValue, orderByValue, size, null);
    }

    /**
     * 객체결과 리스트 얻기
     * @param objClass 객체 클래스
     * @return jdbc객체리스트
     */
    public static <T> List<T> getObjList(Class<T> objClass ){
        try {
            return getObjList(ApplicationConnectionPool.getInstance().getCommitConnection(), objClass, null, null, null, -1, null);
        }catch(Exception e){
            throw new RuntimeException(e);
        }
    }

    /**
     * 객체결과 리스트 얻기
     * @param objClass 객체 클래스
     * @param whereValue 조건문
     * @return jdbc 객체리스트
     */
    public static <T> List<T> getObjList(Class<T> objClass , String whereValue){
        try {
            return getObjList(ApplicationConnectionPool.getInstance().getCommitConnection(), objClass, null, whereValue, null, -1, null);
        }catch(Exception e){
            throw new RuntimeException(e);
        }
    }

    /**
     * 객체결과 리스트 얻기
     * @param objClass 객체 클래스
     * @param whereValue 조건문
     * @param size 건수제한
     * @return jdbc 객체리스트
     */
    public static <T> List<T> getObjList(Class<T> objClass , String whereValue, int size){
        try {
            return getObjList(ApplicationConnectionPool.getInstance().getCommitConnection(),  objClass, null, whereValue, null, size, null);
        }catch(Exception e){
            throw new RuntimeException(e);
        }
    }

    /**
     * 객체결과 리스트 얻기
     * @param objClass 객체 클래스
     * @param whereValue 조건문
     * @param orderByValue 정렬문
     * @return jdbc 객체리스트
     */
    public static <T> List<T> getObjList(Class<T> objClass , String whereValue, String orderByValue){
        try {
            return getObjList(ApplicationConnectionPool.getInstance().getCommitConnection(),  objClass, null, whereValue, orderByValue, -1, null);
        }catch(Exception e){
            throw new RuntimeException(e);
        }
    }

    /**
     * 객체결과 리스트 얻기
     * @param objClass 객체 클래스
     * @param whereValue 조건문
     * @param orderByValue 정렬문
     * @param size 건수제한
     * @return jdbc객체리스트
     */
    public static <T> List<T> getObjList(Class<T> objClass , String whereValue, String orderByValue, int size){
        try {
            return getObjList(ApplicationConnectionPool.getInstance().getCommitConnection(),  objClass, null, whereValue, orderByValue, size, null);
        }catch(Exception e){
            throw new RuntimeException(e);
        }
    }


    /**
     * 객체결과 리스트 얻기
     * @param objClass 객체 클래스
     * @param whereValue 조건문
     * @param prepareStatementDataMap 조건데이터
     * @return jdbc객체리스트
     */
    public static <T> List<T> getObjList(Class<T> objClass , String whereValue, Map<Integer, PrepareStatementData> prepareStatementDataMap){
        try {
            return getObjList(ApplicationConnectionPool.getInstance().getCommitConnection(),  objClass, null, whereValue, null, -1, prepareStatementDataMap);
        }catch(Exception e){
            throw new RuntimeException(e);
        }
    }

    /**
     * 객체결과 리스트 얻기
     * @param objClass 객체 클래스
     * @param sql 쿼리
     * @param whereValue 조건문
     * @param prepareStatementDataMap 조건데이터
     * @return jdbc객체리스트
     */
    public static <T> List<T> getObjList(Class<T> objClass , String sql, String whereValue, Map<Integer, PrepareStatementData> prepareStatementDataMap){
        try {
            return getObjList(ApplicationConnectionPool.getInstance().getCommitConnection(),  objClass, sql, whereValue, null, -1, prepareStatementDataMap);
        }catch(Exception e){
            throw new RuntimeException(e);
        }
    }

    /**
     * 쿼리를이용한 객체결과 리스트 얻기
     * @param objClass 객체 클래스
     * @param sql 쿼리
     * @return jdbc객체리스트
     */
    public static <T> List<T> getSqlObjList(Class<T> objClass , String sql){
        try {
            return getObjList(ApplicationConnectionPool.getInstance().getCommitConnection(),  objClass, sql, null, null, -1, null);
        }catch(Exception e){
            throw new RuntimeException(e);
        }
    }


    /**
     * 객체결과 리스트 얻기
     * @param conn 연결 컨넥션
     * @param objClass 객체 클래스
     * @param sql 쿼리
     * @param whereValue 조건문
     * @param prepareStatementDataMap 조건데이터
     * @return jdbc객체리스트
     */
    public static <T> List<T> getObjList(Connection conn, Class<T> objClass , String sql, String whereValue, Map<Integer, PrepareStatementData> prepareStatementDataMap) throws IllegalAccessException, SQLException, InstantiationException {
        return getObjList(conn, objClass, sql, whereValue , null, -1 , prepareStatementDataMap);
    }

    /**
     * 객체결과 리스트 얻기
     * @param conn 연결 컨넥션
     * @param objClass 객체 클래스
     * @param sql 쿼리
     * @param whereValue 조건문
     * @param orderByValue 정렬문
     * @param prepareStatementDataMap 조건데이터
     * @return jdbc객체리스트
     */
    public static <T> List<T> getObjList(Connection conn, Class<T> objClass , String sql, String whereValue, String orderByValue, int size, Map<Integer, PrepareStatementData> prepareStatementDataMap) throws IllegalAccessException, SQLException, InstantiationException {

        List<T> resultList = new ArrayList<>();


        Table table = objClass.getAnnotation(Table.class);

        Field[] fields = FieldUtil.getFieldArrayToParents(objClass);


        Map<String, Field> columnFieldMap = new HashMap<>();

        for(Field field: fields){
            Column column = field.getAnnotation(Column.class);

            if(column != null){
                String name = column.name();
                if("".equals(name)){
                    columnFieldMap.put(field.getName(), field);
                }else{
                    columnFieldMap.put(name, field);
                }
            }
        }

        String selectSql;
        if(sql == null) {
            selectSql = getSql(objClass, table, columnFieldMap.keySet(), whereValue, orderByValue);
        }else{
            selectSql = sql;
        }

        Statement stmt = null;
        ResultSet result = null;

        if(size == -1) {
            size = table.size();
        }

        //noinspection CaughtExceptionImmediatelyRethrown
        try{
            StmtResultSet stmtResultSet = StmtResultSetUtil.makeStmtResultSet(conn, selectSql, prepareStatementDataMap);
            stmt = stmtResultSet.getStmt();
            result = stmtResultSet.getResultSet();

            int fetchSize = table.fetchSize();
            if(fetchSize > 0){
                stmt.setFetchSize(table.fetchSize());
                result.setFetchSize(table.fetchSize());
            }
            if(size == -1){
                while(result.next()){
                    //noinspection deprecation
                    T resultObj = objClass.newInstance();

                    setFieldsValue(result, columnFieldMap, resultObj);

                    resultList.add(resultObj);
                }
            }else{
                int checkCount = 0;
                while(result.next()){

                    //noinspection deprecation
                    T resultObj = objClass.newInstance();

                    setFieldsValue(result, columnFieldMap, resultObj);

                    resultList.add(resultObj);
                    checkCount ++ ;
                    if(checkCount >= size)
                        break;
                }
            }

        }catch(Exception e){
            throw e;
        }finally{
            JdbcClose.statementResultSet(stmt, result);

        }
        return resultList;
    }

    /**
     * 필드에 값 설정
     * @param result ResultSet
     * @param columnFieldMap Map<String, Field>
     * @param resultObj Object
     */
    private static void setFieldsValue(ResultSet result, Map<String, Field> columnFieldMap, Object resultObj ) throws IllegalArgumentException, IllegalAccessException, SQLException{

        Set<String> columnNames = columnFieldMap.keySet();

        for(String columnName : columnNames){
            Field field = columnFieldMap.get(columnName);
            JdbcField.setFieldObject(result, field, columnName, resultObj);
        }
    }

    /**
     * sql 얻기
     * @return sql
     */
    public static <T> String getSql(Class<T> objClass, Table table , Set<String> columnNameSet , String whereValue, String orderByValue){
        StringBuilder sqlBuilder = new StringBuilder();

        String tableSql = table.sql();
        if(tableSql.equals(Table.EMPTY)){

            if(table.name().equals(Table.EMPTY)){
                throw new TableNameEmptyException(objClass.getName());
            }

            if(columnNameSet == null || columnNameSet.size() ==0){
                throw new FieldNullException(objClass.getName());
            }
            StringBuilder fieldBuilder = new StringBuilder();

            for(String columnName : columnNameSet){
                fieldBuilder.append(", ").append(columnName);
            }
            sqlBuilder.append("SELECT ");
            sqlBuilder.append(fieldBuilder.toString().substring(1));
            sqlBuilder.append(" FROM ").append(table.name());
        }else{
            sqlBuilder.append(tableSql);
        }

        sqlBuilder.append(TableSql.getWhereOrderBySql(table, whereValue, orderByValue));
        return sqlBuilder.toString();
    }






    /**
     * 클래스 어노테이션 생성
     * 소스를 편하게 짜기위한 복사 붙여넣기 값 생성
     * @param tableName tableName
     * @return 어노테이션 생성값
     */
    public static String makeObjectValue( String tableName) {

        try {
            return makeObjectValue(ApplicationConnectionPool.getInstance().getCommitConnection(), tableName);
        }catch(Exception e){
            throw new RuntimeException(e);
        }
    }


    /**
     * 클래스 어노테이션 생성
     * 소스를 편하게 짜기위한 복사 붙여넣기 값 생성
     * @param conn Connection
     * @param tableName tableName
     * @return 어노테이션 생성값
     */
    public static String makeObjectValue(Connection conn, String tableName){
        Statement stmt = null;
        ResultSet result = null;
        StringBuilder fieldBuilder = new StringBuilder();

        JdbcNamingDataType jdbcNamingDataType = JdbcNamingDataType.getInstance();
        try{
            Map<String, Integer> pkMap = Database.getPrimaryKeyColumnsForTable(conn, tableName);

            Map<String,String> defaultValueMap = Database.getDefaultValue(tableName);
            stmt = conn.createStatement();
            //noinspection SqlDialectInspection,SqlNoDataSourceInspection
            result = stmt.executeQuery("SELECT * FROM " + tableName);
            ResultSetMetaData metaData = result.getMetaData();
            int count = metaData.getColumnCount(); //number of column

            for (int i = 1; i <= count; i++){

                String columnName = metaData.getColumnLabel(i);

                Integer pkSeq = pkMap.get(columnName);
                if(pkSeq != null){
                    fieldBuilder.append("@PrimaryKey(seq = ").append(pkSeq).append(")\n");
                }


                JdbcDataType dataType = jdbcNamingDataType.getType(columnName);
                if(dataType == JdbcDataType.DATE_TIME ){
                    fieldBuilder.append("@DateTime\n");
                }

                fieldBuilder.append("@Column(name = \"").append(columnName).append("\")\n\n");
            }

        }catch(Exception e){
            throw new RuntimeException(e);
        }finally{
            JdbcClose.statementResultSet(stmt, result);
        }
        return fieldBuilder.toString();
    }



}
