package com.seomse.jdbc.objects;

import com.seomse.commons.packages.classes.field.FieldUtil;
import com.seomse.jdbc.Database;
import com.seomse.jdbc.JdbcClose;
import com.seomse.jdbc.PrepareStatementData;
import com.seomse.jdbc.annotation.Column;
import com.seomse.jdbc.annotation.Sequence;
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
 *  설    명 : Jdbc 자바형객체를 활용한 이벤트 처리
 *
 *  작 성 자 : macle
 *  작 성 일 : 2019.10.18
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
        Map<String, Field> columnFieldMap = makeColumnFieldMap(objClass);

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


    private static <T>  Map<String, Field> makeColumnFieldMap(Class<T> objClass){
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

        return columnFieldMap;
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
     * 객체결과 얻기
     * @param conn 연결 컨넥션
     * @param objClass 객체 클래스
     * @return jdbc객체
     */
    public static <T> T getObj(Connection conn, Class<T> objClass ) throws IllegalAccessException, SQLException, InstantiationException {
        return getObj(conn,  objClass, null, null, null, null);
    }

    /**
     * 객체결과 얻기
     * @param conn 연결 컨넥션
     * @param objClass 객체 클래스
     * @param whereValue 조건문
     * @return jdbc객체
     */
    public static <T> T getObj(Connection conn, Class<T> objClass , String whereValue) throws IllegalAccessException, SQLException, InstantiationException {
        return getObj(conn,  objClass, null, whereValue, null, null);
    }


    /**
     * 객체결과 얻기
     * @param objClass 객체 클래스
     * @return jdbc객체
     */
    public static <T> T getObj(Class<T> objClass ){
        try {
            return getObj(ApplicationConnectionPool.getInstance().getCommitConnection(), objClass, null, null, null, null);
        }catch(Exception e){
            throw new RuntimeException(e);
        }
    }

    /**
     * 객체결과 얻기
     * @param objClass 객체 클래스
     * @param whereValue 조건문
     * @return jdbc객체
     */
    public static <T> T getObj(Class<T> objClass , String whereValue){
        try {
            return getObj(ApplicationConnectionPool.getInstance().getCommitConnection(), objClass, null, whereValue, null, null);
        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }

    /**
     * 객체결과 얻기
     * @param objClass 객체 클래스
     * @param whereValue 조건문
     * @param orderByValue 정렬문
     * @return jdbc객체
     */
    public static <T> T getObj(Class<T> objClass , String whereValue, String orderByValue){
        try {
            return getObj(ApplicationConnectionPool.getInstance().getCommitConnection(), objClass, null, whereValue, orderByValue, null);
        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }


    /**
     * 객체결과 얻기
     * @param objClass 객체 클래스
     * @param whereValue 조건문
     * @param prepareStatementDataMap 조건데이터
     * @return jdbc객체
     */
    public static <T> T getObj(Class<T> objClass , String whereValue, Map<Integer, PrepareStatementData> prepareStatementDataMap){
        try {
            return getObj(ApplicationConnectionPool.getInstance().getCommitConnection(), objClass, null, whereValue, null, prepareStatementDataMap);
        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }

    /**
     * 객체결과 얻기
     * @param objClass 객체 클래스
     * @param sql 쿼리
     * @param whereValue 조건문
     * @param prepareStatementDataMap 조건데이터
     * @return jdbc객체
     */
    public static <T> T getObj(Class<T> objClass , String sql, String whereValue, Map<Integer, PrepareStatementData> prepareStatementDataMap){
        try {
            return getObj(ApplicationConnectionPool.getInstance().getCommitConnection(), objClass, sql, whereValue, null, prepareStatementDataMap);
        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }

    /**
     * 쿼리를이용한 객체결과 얻기
     * @param objClass 객체 클래스
     * @param sql 쿼리
     * @return jdbc객체
     */
    public static <T> T getSqlObj(Class<T> objClass , String sql){
        try {
            return getObj(ApplicationConnectionPool.getInstance().getCommitConnection(),  objClass, sql, null, null, null);
        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }


    /**
     * 객체결과 얻기
     * @param conn 연결 컨넥션
     * @param objClass 객체 클래스
     * @param sql 쿼리
     * @param whereValue 조건문
     * @param prepareStatementDataMap 조건데이터
     * @return 객체
     */
    public static <T> T getObj(Connection conn, Class<T> objClass, String sql, String whereValue, Map<Integer, PrepareStatementData> prepareStatementDataMap) throws IllegalAccessException, SQLException, InstantiationException {
        return getObj(conn, objClass, sql, whereValue, null, prepareStatementDataMap);
    }
    /**
     * 객체결과 얻기
     * @param conn 연결 컨넥션
     * @param objClass 객체 클래스
     * @param sql 쿼리
     * @param whereValue 조건문
     * @param orderByValue 정렬문
     * @param prepareStatementDataMap 조건데이터
     * @return jdbc객체
     */
    public static <T> T getObj(Connection conn, Class<T> objClass, String sql, String whereValue, String orderByValue, Map<Integer, PrepareStatementData> prepareStatementDataMap) throws IllegalAccessException, SQLException, InstantiationException {


        Table table = objClass.getAnnotation(Table.class);

        Map<String, Field> columnFieldMap = makeColumnFieldMap(objClass);

        String selectSql;
        if(sql == null) {
            selectSql = getSql(objClass, table, columnFieldMap.keySet(), whereValue, orderByValue);
        }else{
            selectSql = sql;
        }

        Statement stmt = null;
        ResultSet result = null;
        T resultObj = null;

        //noinspection CaughtExceptionImmediatelyRethrown
        try{

            StmtResultSet stmtResultSet = StmtResultSetUtil.makeStmtResultSet(conn, selectSql, prepareStatementDataMap);
            stmt = stmtResultSet.getStmt();
            result = stmtResultSet.getResultSet();
            result.setFetchSize(2);
            if(result.next()){
                //noinspection deprecation
                resultObj = objClass.newInstance();
                setFieldsValue(result, columnFieldMap, resultObj);

            }
        }catch(Exception e){
            throw e;
        }finally{
            JdbcClose.statementResultSet(stmt, result);
        }

        return resultObj;
    }


    public static <T> int insert(Connection conn, List<T> objClassList, String insertQueryValue,  boolean isClearParameters){
        if(objClassList == null || objClassList.size() ==0){
            return 0;
        }
        Class<?> objClass = objClassList.get(0).getClass();

        Map<String, Field> columnFieldMap = makeColumnFieldMap(objClass);
        String [] columnNames = columnFieldMap.keySet().toArray(new String[0]);
        String insertSql = getInsertSql(objClass, columnNames, insertQueryValue);

        PreparedStatement pstmt = null;
        //순서정보를 위한 세팅
        Field [] fields = new Field[columnNames.length];
        for (int i = 0; i <columnNames.length ; i++) {
            fields[i] = columnFieldMap.get(i);
        }

        int successCount ;
        try{
            pstmt = conn.prepareStatement(insertSql);

            for(T obj : objClassList){
                StmtResultSetUtil.addBatch(obj, fields, pstmt);
                if(isClearParameters){
                    pstmt.clearParameters();
                }else{
                    pstmt.executeBatch();
                }

            }
            if(isClearParameters){
                pstmt.executeBatch();
            }
            successCount = objClassList.size();
        }catch(Exception e){
            throw new RuntimeException(e);
        }finally{
            //noinspection CatchMayIgnoreException
            try{if(pstmt != null)pstmt.close(); }catch(Exception e){}
        }

        return successCount;

    }



    /**
     * insert 계열의 sql 얻기
     * @return InsertSql
     */
    public static String getInsertSql(Class<?> objClass, String [] columnNames, String insertQueryValue){
        Table table = objClass.getAnnotation(Table.class);

        String tableName = TableSql.getTableName(table, objClass.getName());

        StringBuilder sqlBuilder = new StringBuilder();

        sqlBuilder.append(insertQueryValue).append(" INTO ").append(tableName).append(" (");

        StringBuilder fieldBuilder = new StringBuilder();

        //noinspection ForLoopReplaceableByForEach
        for(int i=0 ; i<columnNames.length ; i++){
            fieldBuilder.append(", ").append(columnNames[i]);
        }
        sqlBuilder.append(fieldBuilder.toString().substring(1));
        sqlBuilder.append(") VALUES (");


        fieldBuilder.setLength(0);
        for(int i=0 ; i<columnNames.length ; i++){
            fieldBuilder.append(", ?");
        }
        sqlBuilder.append(fieldBuilder.toString().substring(1));
        sqlBuilder.append(" )");

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
        StringBuilder sb = new StringBuilder();
        sb.append("@Table(name=\"").append(tableName).append("\")");

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

                sb.append("\n\n");
                String columnName = metaData.getColumnLabel(i);

                Integer pkSeq = pkMap.get(columnName);
                if(pkSeq != null){
                    sb.append("@PrimaryKey(seq = ").append(pkSeq).append(")\n");
                }


                JdbcDataType dataType = jdbcNamingDataType.getType(columnName);
                if(dataType == JdbcDataType.DATE_TIME ){
                    sb.append("@DateTime\n");
                }

                sb.append("@Column(name = \"").append(columnName).append("\")");
            }

        }catch(Exception e){
            throw new RuntimeException(e);
        }finally{
            JdbcClose.statementResultSet(stmt, result);
        }
        return sb.toString();
    }



}
