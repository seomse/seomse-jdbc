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
package com.seomse.jdbc.objects;

import com.seomse.commons.utils.ExceptionUtil;
import com.seomse.commons.utils.packages.classes.field.FieldUtil;
import com.seomse.jdbc.Database;
import com.seomse.jdbc.PrepareStatementData;
import com.seomse.jdbc.annotation.Column;
import com.seomse.jdbc.annotation.FlagBoolean;
import com.seomse.jdbc.annotation.PrimaryKey;
import com.seomse.jdbc.annotation.Table;
import com.seomse.jdbc.common.*;
import com.seomse.jdbc.connection.ApplicationConnectionPool;
import com.seomse.jdbc.exception.FieldNullException;
import com.seomse.jdbc.exception.PrimaryKeyNotSetException;
import com.seomse.jdbc.exception.TableNameEmptyException;
import com.seomse.jdbc.naming.JdbcDataType;
import com.seomse.jdbc.naming.JdbcNamingDataType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.sql.*;
import java.util.*;

/**
 * JdbcObjects
 * class 에 컬럼 속성을 annotation 으로 정의하여 Db테이블과 일치화 하여 사용할떄 이용
 * DB용 객체와 사용 클래스를 일치화 할때 사용
 * @author macle
 */
public class JdbcObjects {

    private static final Logger logger = LoggerFactory.getLogger(JdbcObjects.class);



    /**
     * List 얻기
     * @param conn Connection
     * @param objClass Class
     * @param <T> Table, Column annotation object
     * @return List
     * @throws IllegalAccessException IllegalAccessException
     * @throws SQLException SQLException
     * @throws InstantiationException InstantiationException
     */
    public static <T> List<T> getObjList(Connection conn, Class<T> objClass ) throws IllegalAccessException, SQLException, InstantiationException {
        return getObjList(conn, objClass, null, null, null, -1, null);

    }

    /**
     * List 얻기
     * @param conn Connection
     * @param objClass Class
     * @param whereValue String where query
     * @param <T> Table, Column annotation object
     * @return List
     * @throws IllegalAccessException IllegalAccessException
     * @throws SQLException SQLException
     * @throws InstantiationException InstantiationException
     */
    public static <T> List<T> getObjList(Connection conn, Class<T> objClass , String whereValue) throws IllegalAccessException, SQLException, InstantiationException {
        return getObjList(conn,  objClass, null, whereValue, null, -1, null);
    }


    /**
     * List 얻기
     * @param conn Connection
     * @param objClass Class
     * @param whereValue String where query
     * @param orderByValue String order by query
     * @param <T> Table, Column annotation object
     * @return List
     * @throws IllegalAccessException IllegalAccessException
     * @throws SQLException SQLException
     * @throws InstantiationException InstantiationException
     */
    public static <T> List<T> getObjList(Connection conn, Class<T> objClass , String whereValue, String orderByValue) throws IllegalAccessException, SQLException, InstantiationException {
        return getObjList(conn,  objClass, null, whereValue, orderByValue, -1, null);
    }

    /**
     * List 얻기
     * @param conn Connection
     * @param objClass Class
     * @param whereValue String where query
     * @param orderByValue String order by query
     * @param size int cut size
     * @param <T> Table, Column annotation object
     * @return List
     * @throws IllegalAccessException IllegalAccessException
     * @throws SQLException SQLException
     * @throws InstantiationException InstantiationException
     */
    public static <T> List<T> getObjList(Connection conn, Class<T> objClass , String whereValue, String orderByValue, int size) throws IllegalAccessException, SQLException, InstantiationException {
        return getObjList(conn,  objClass, null, whereValue, orderByValue, size, null);
    }

    /**
     * List 얻기
     * @param objClass Class
     * @param <T> Table, Column annotation object
     * @return List
     */
    public static <T> List<T> getObjList(Class<T> objClass ){
        try(Connection conn = ApplicationConnectionPool.getInstance().getCommitConnection()){
            return  getObjList(conn, objClass, null, null, null, -1, null);
        }catch(Exception e){
            throw new RuntimeException(e);
        }
    }

    /**
     * List 얻기
     * @param objClass Class
     * @param whereValue String where query
     * @param <T> Table, Column annotation object
     * @return List
     */
    public static <T> List<T> getObjList(Class<T> objClass , String whereValue){
        try(Connection conn = ApplicationConnectionPool.getInstance().getCommitConnection()){
            return getObjList(conn, objClass, null, whereValue, null, -1, null);
        }catch(Exception e){
            throw new RuntimeException(e);
        }
    }

    /**
     * List 얻기
     * @param objClass Class
     * @param whereValue String where query
     * @param size int cut size
     * @param <T> Table, Column annotation object
     * @return List
     */
    public static <T> List<T> getObjList(Class<T> objClass , String whereValue, int size){
        try(Connection conn = ApplicationConnectionPool.getInstance().getCommitConnection()){
            return getObjList(conn,  objClass, null, whereValue, null, size, null);
        }catch(Exception e){
            throw new RuntimeException(e);
        }
    }

    /**
     * List 얻기
     * @param objClass Class
     * @param whereValue String where query
     * @param orderByValue String order by query
     * @param <T> Table, Column annotation object
     * @return List
     */
    public static <T> List<T> getObjList(Class<T> objClass , String whereValue, String orderByValue){
        try(Connection conn = ApplicationConnectionPool.getInstance().getCommitConnection()){
            return getObjList(conn,  objClass, null, whereValue, orderByValue, -1, null);
        }catch(Exception e){
            throw new RuntimeException(e);
        }
    }

    /**
     * List 얻기
     * @param objClass Class
     * @param whereValue String where query
     * @param orderByValue String order by query
     * @param size int cut size
     * @param <T> Table, Column annotation object
     * @return List
     */
    public static <T> List<T> getObjList(Class<T> objClass , String whereValue, String orderByValue, int size){
        try(Connection conn = ApplicationConnectionPool.getInstance().getCommitConnection()){
            return getObjList(conn,  objClass, null, whereValue, orderByValue, size, null);
        }catch(Exception e){
            throw new RuntimeException(e);
        }
    }

    /**
     * List 얻기
     * @param objClass Class
     * @param whereValue String where query
     * @param prepareStatementDataMap Map 조건 데이터  date time 같이 database query 가 다른 경우
     * @param <T> Table, Column annotation object
     * @return List
     */
    public static <T> List<T> getObjList(Class<T> objClass , String whereValue, Map<Integer, PrepareStatementData> prepareStatementDataMap){
        try(Connection conn = ApplicationConnectionPool.getInstance().getCommitConnection()){
            return getObjList(conn,  objClass, null, whereValue, null, -1, prepareStatementDataMap);
        }catch(Exception e){
            throw new RuntimeException(e);
        }
    }

    /**
     * List 얻기
     * @param objClass Class
     * @param sql String sql
     * @param whereValue  String where query
     * @param prepareStatementDataMap  Map 조건 데이터  date time 같이 database query 가 다른 경우
     * @param <T> Table, Column annotation object
     * @return List
     */
    public static <T> List<T> getObjList(Class<T> objClass , String sql, String whereValue, Map<Integer, PrepareStatementData> prepareStatementDataMap){
        try(Connection conn = ApplicationConnectionPool.getInstance().getCommitConnection()){
            return getObjList(conn,  objClass, sql, whereValue, null, -1, prepareStatementDataMap);
        }catch(Exception e){
            throw new RuntimeException(e);
        }
    }

    /**
     * List 얻기
     * sql 활용
     * @param objClass Class
     * @param sql String sql
     * @param <T> Table, Column annotation object
     * @return List
     */
    public static <T> List<T> getSqlObjList(Class<T> objClass , String sql){
        try(Connection conn = ApplicationConnectionPool.getInstance().getCommitConnection()){
            return getObjList(conn,  objClass, sql, null, null, -1, null);
        }catch(Exception e){
            throw new RuntimeException(e);
        }
    }

    /**
     * List 얻기
     * @param conn Connection
     * @param objClass objClass Class
     * @param sql String sql
     * @param whereValue  String where query
     * @param prepareStatementDataMap Map 조건 데이터  date time 같이 database query 가 다른 경우
     * @param <T> Table, Column annotation object
     * @return List
     * @throws IllegalAccessException IllegalAccessException
     * @throws SQLException SQLException
     * @throws InstantiationException InstantiationException
     */
    public static <T> List<T> getObjList(Connection conn, Class<T> objClass , String sql, String whereValue, Map<Integer, PrepareStatementData> prepareStatementDataMap) throws IllegalAccessException, SQLException, InstantiationException {
        return getObjList(conn, objClass, sql, whereValue , null, -1 , prepareStatementDataMap);
    }

    /**
     *  List 얻기
     * @param conn Connection
     * @param objClass objClass Class
     * @param sql String sql
     * @param whereValue String where query
     * @param orderByValue String order by query
     * @param size int cut size
     * @param prepareStatementDataMap Map 조건 데이터  date time 같이 database query 가 다른 경우
     * @param <T> Table, Column annotation object
     * @return List
     * @throws IllegalAccessException IllegalAccessException
     * @throws SQLException SQLException
     * @throws InstantiationException InstantiationException
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
            StmtResultSet stmtResultSet = JdbcCommon.makeStmtResultSet(conn, selectSql, prepareStatementDataMap, table.fetchSize());
            stmt = stmtResultSet.getStmt();
            result = stmtResultSet.getResultSet();

            if(size == -1){
                while(result.next()){
                    T resultObj = objClass.newInstance();

                    setFieldsValue(result, columnFieldMap, resultObj);

                    resultList.add(resultObj);
                }
            }else{
                int checkCount = 0;
                while(result.next()){
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
     * Map make
     * @param objClass Class objClass
     * @param <T> Table, Column annotation object
     * @return Map
     */
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
     * @param columnFieldMap Map
     * @param resultObj Object
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws SQLException
     */
    private static void setFieldsValue(ResultSet result, Map<String, Field> columnFieldMap, Object resultObj ) throws IllegalArgumentException, IllegalAccessException, SQLException{

        Set<String> columnNames = columnFieldMap.keySet();

        for(String columnName : columnNames){
            Field field = columnFieldMap.get(columnName);
            JdbcField.setFieldObject(result, field, columnName, resultObj);
        }
    }

    /**
     * Table annotation 을 활용하여 sql 생성
     * @param objClass Class objClass
     * @param table Table annotation
     * @param columnNameSet Set
     * @param whereValue String where query
     * @param orderByValue String order by query
     * @param <T> Table, Column annotation object
     * @return String sql
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
     * @param conn Connection
     * @param objClass Class objClass
     * @param <T> Table, Column annotation object
     * @return T Table, Column annotation object
     * @throws IllegalAccessException IllegalAccessException
     * @throws SQLException SQLException
     * @throws InstantiationException InstantiationException
     */
    public static <T> T getObj(Connection conn, Class<T> objClass ) throws IllegalAccessException, SQLException, InstantiationException {
        return getObj(conn,  objClass, null, null, null, null);
    }

    /**
     * 객체결과 얻기
     * @param conn Connection
     * @param objClass Class objClass
     * @param whereValue String where query
     * @param <T> Table, Column annotation object
     * @return T Table, Column annotation object
     * @throws IllegalAccessException IllegalAccessException
     * @throws SQLException SQLException
     * @throws InstantiationException InstantiationException
     */
    public static <T> T getObj(Connection conn, Class<T> objClass , String whereValue) throws IllegalAccessException, SQLException, InstantiationException {
        return getObj(conn,  objClass, null, whereValue, null, null);
    }


    /**
     * 객체결과 얻기
     * @param objClass Class objClass
     * @param <T> Table, Column annotation object
     * @return T Table, Column annotation object
     */
    public static <T> T getObj(Class<T> objClass ){
        try(Connection conn = ApplicationConnectionPool.getInstance().getCommitConnection()){
            return getObj(conn, objClass, null, null, null, null);
        }catch(Exception e){
            throw new RuntimeException(e);
        }
    }

    /**
     * 객체결과 얻기
     * @param objClass Class objClass
     * @param whereValue String where query
     * @param <T> Table, Column annotation object
     * @return T Table, Column annotation object
     */
    public static <T> T getObj(Class<T> objClass , String whereValue){
        try(Connection conn = ApplicationConnectionPool.getInstance().getCommitConnection()){
            return getObj(conn, objClass, null, whereValue, null, null);
        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }

    /**
     *
     * 객체결과 얻기
     * @param objClass Class objClass
     * @param whereValue String where query
     * @param orderByValue String order by query
     * @param <T> Table, Column annotation object
     * @return T Table, Column annotation object
     */
    public static <T> T getObj(Class<T> objClass , String whereValue, String orderByValue){
        try(Connection conn = ApplicationConnectionPool.getInstance().getCommitConnection()){
            return getObj(conn, objClass, null, whereValue, orderByValue, null);
        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }


    /**
     * 객체결과 얻기
     * @param objClass Class objClass
     * @param whereValue String where query
     * @param prepareStatementDataMap  Map 조건 데이터  date time 같이 database query 가 다른 경우
     * @param <T> Table, Column annotation object
     * @return T Table, Column annotation object
     */
    public static <T> T getObj(Class<T> objClass , String whereValue, Map<Integer, PrepareStatementData> prepareStatementDataMap){
        try(Connection conn = ApplicationConnectionPool.getInstance().getCommitConnection()){
            return getObj(conn, objClass, null, whereValue, null, prepareStatementDataMap);
        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }

    /**
     * 객체결과 얻기
     * @param objClass Class objClass
     * @param sql String sql
     * @param whereValue String where query
     * @param prepareStatementDataMap  Map 조건 데이터  date time 같이 database query 가 다른 경우
     * @param <T> Table, Column annotation object
     * @return T Table, Column annotation object
     */
    public static <T> T getObj(Class<T> objClass , String sql, String whereValue, Map<Integer, PrepareStatementData> prepareStatementDataMap){
        try(Connection conn = ApplicationConnectionPool.getInstance().getCommitConnection()){
            return getObj(conn, objClass, sql, whereValue, null, prepareStatementDataMap);
        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }

    /**
     * 객체결과 얻기
     * sql 활용
     * @param objClass Class objClass
     * @param sql String sql
     * @param <T> Table, Column annotation object
     * @return T Table, Column annotation object
     */
    public static <T> T getSqlObj(Class<T> objClass , String sql){
        try(Connection conn = ApplicationConnectionPool.getInstance().getCommitConnection()){
            return getObj(conn,  objClass, sql, null, null, null);
        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }

    /**
     * 객체결과 얻기
     * @param conn Connection
     * @param objClass Class objClass
     * @param sql String sql
     * @param whereValue String where query
     * @param prepareStatementDataMap  Map 조건 데이터  date time 같이 database query 가 다른 경우
     * @param <T> Table, Column annotation object
     * @return T Table, Column annotation object
     * @throws IllegalAccessException IllegalAccessException
     * @throws SQLException SQLException
     * @throws InstantiationException InstantiationException
     */
    public static <T> T getObj(Connection conn, Class<T> objClass, String sql, String whereValue, Map<Integer, PrepareStatementData> prepareStatementDataMap) throws IllegalAccessException, SQLException, InstantiationException {
        return getObj(conn, objClass, sql, whereValue, null, prepareStatementDataMap);
    }

    /**
     * 객체결과 얻기
     * @param conn Connection
     * @param objClass Class objClass
     * @param sql String sql
     * @param whereValue String where query
     * @param orderByValue String order by query
     * @param prepareStatementDataMap  Map 조건 데이터  date time 같이 database query 가 다른 경우
     * @param <T> Table, Column annotation object
     * @return T Table, Column annotation object
     * @throws IllegalAccessException IllegalAccessException
     * @throws SQLException SQLException
     * @throws InstantiationException InstantiationException
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

            StmtResultSet stmtResultSet = JdbcCommon.makeStmtResultSet(conn, selectSql, prepareStatementDataMap);
            stmt = stmtResultSet.getStmt();
            result = stmtResultSet.getResultSet();
            result.setFetchSize(2);
            if(result.next()){
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

    /**
     * upsert
     * @param objClassList List
     * @param <T> Table Column annotation object
     * @return int fail -1
     */
    public static <T> int upsert( List<T> objClassList){
        return upsert(objClassList, true);
    }


    /**
     * upsert
     * @param objClassList List
     * @param isClearParameters boolean
     * @param <T> Table Column annotation object
     * @return int fail -1
     */
    public static <T> int upsert( List<T> objClassList,   boolean isClearParameters){
        ApplicationConnectionPool connectionPool = ApplicationConnectionPool.getInstance();
        try(Connection conn = connectionPool.getConnection()){
            int result =  insert(conn, objClassList, "UPSERT", isClearParameters);
            if(!connectionPool.isAutoCommit()){
                conn.commit();
            }
            return result;
        }catch(Exception e){
            throw new RuntimeException(e);

        }
    }

    /**
     * upsert
     * @param conn Connection
     * @param objClassList List
     * @param isClearParameters boolean
     * @param <T> Table Column annotation object
     * @return int fail -1
     */
    public static <T> int upsert(Connection conn, List<T> objClassList,   boolean isClearParameters){
        return insert(conn, objClassList , "UPSERT", isClearParameters);
    }

    /**
     * insert
     * @param objClassList List
     * @param <T> Table Column annotation object
     * @return int fail -1
     */
    public static <T> int insert( List<T> objClassList){
        ApplicationConnectionPool connectionPool = ApplicationConnectionPool.getInstance();
        try(Connection conn = connectionPool.getConnection()){
            int result =  insert(conn, objClassList, "INSERT", true);
            if(!connectionPool.isAutoCommit()){
                conn.commit();
            }
            return result;
        }catch(Exception e){
            throw new RuntimeException(e);
        }
    }

    /**
     * insert
     * @param objClassList List
     * @param isClearParameters boolean
     * @param <T> Table Column annotation object
     * @return int fail -1
     */
    public static <T> int insert( List<T> objClassList, boolean isClearParameters){
        ApplicationConnectionPool connectionPool = ApplicationConnectionPool.getInstance();
        try(Connection conn = connectionPool.getConnection()){
            int result =  insert(conn, objClassList , "INSERT", isClearParameters);
            if(!connectionPool.isAutoCommit()){
                conn.commit();
            }
            return result;

        }catch(Exception e){
            throw new RuntimeException(e);
        }
    }


    /**
     * insert
     * @param conn Connection
     * @param objClassList List
     * @param isClearParameters boolean
     * @param <T> Table Column annotation object
     * @return int fail -1
     */
    public static <T> int insert(Connection conn, List<T> objClassList,   boolean isClearParameters){
        return insert(conn, objClassList , "INSERT", isClearParameters);
    }

    /**
     * insert
     * @param conn Connection
     * @param objClassList List
     * @param insertQueryValue String upsert, insert
     * @param isClearParameters boolean
     * @param <T> Table Column annotation object
     * @return int fail -1
     */
    public static <T> int insert(Connection conn, List<T> objClassList, String insertQueryValue,  boolean isClearParameters){
        if(objClassList == null || objClassList.size() ==0){
            return 0;
        }
        Class<?> objClass = objClassList.get(0).getClass();

        Map<String, Field> columnFieldMap = makeColumnFieldMap(objClass);
        String [] columnNames = columnFieldMap.keySet().toArray(new String[0]);

        //순서정보를 위한 세팅
        Field [] fields = new Field[columnNames.length];
        for (int i = 0; i <columnNames.length ; i++) {
            fields[i] = columnFieldMap.get(columnNames[i]);
        }

        String insertSql = getInsertSql(objClass, columnNames, insertQueryValue);

        return JdbcCommon.insert(conn, objClassList, fields, insertSql, isClearParameters);

    }


    /**
     * 있으면 update, 없으면 insert
     * @param obj T
     * @param isNullUpdate boolean null value update flag
     * @param <T> Table Column annotation object
     * @return int fail -1
     */
    public static <T> int insertOrUpdate(T obj, boolean isNullUpdate){

        ApplicationConnectionPool connectionPool = ApplicationConnectionPool.getInstance();
        try(Connection conn = connectionPool.getCommitConnection()){
            int result = insertOrUpdate(conn, obj, isNullUpdate);
            if(!connectionPool.isAutoCommit()){
                conn.commit();
            }
            return result;
        }catch(Exception e){
            throw new RuntimeException(e);
        }

    }

    /**
     * 있으면 update, 없으면 insert
     * @param conn Connection
     * @param obj T
     * @param isNullUpdate boolean null value update flag
     * @param <T> Table Column annotation object
     * @return int fail -1
     */
    public static <T> int insertOrUpdate(Connection conn, T obj , boolean isNullUpdate ){
        int successCount ;
        try{

            Object checkObj = getObj(conn, obj.getClass(), null, getCheckWhere(obj), null, null);
            if(checkObj == null){
                successCount =insert(conn, obj);
            }else{
                successCount = update(conn, obj, isNullUpdate);
            }
        }
        catch(Exception e){
            throw new RuntimeException(e);
        }

        return successCount;

    }

    /**
     * data check sql get
     * @param obj T
     * @param <T> Table Column annotation object
     * @return String check sql
     * @throws IllegalAccessException IllegalAccessException
     */
    public static <T> String getCheckWhere(T obj) throws IllegalAccessException {
        Class<?> objClass = obj.getClass();
        String tableName = TableSql.getTableName(objClass.getAnnotation(Table.class), objClass.getName());

        Map<String, Field> columnFieldMap = makeColumnFieldMap(objClass);
        String [] columnNames = columnFieldMap.keySet().toArray(new String[0]);
        //순서정보를 위한 세팅
        Field [] fields = new Field[columnNames.length];
        for (int i = 0; i <columnNames.length ; i++) {
            fields[i] = columnFieldMap.get(columnNames[i]);
        }

        Map<Field,String> columnNameMap = new HashMap<>();


        List<Field> pkColumnList = new LinkedList<>();

        for(int i=0 ; i<fields.length ; i++){
            fields[i].setAccessible(true);
            PrimaryKey pk  = fields[i].getAnnotation(PrimaryKey.class);
            if(pk != null){
                pkColumnList.add(fields[i]);
                columnNameMap.put(fields[i], columnNames[i]);
            }
        }

        if(pkColumnList.size() ==0){
            throw new PrimaryKeyNotSetException(objClass.getName());
        }

        pkColumnList.sort(JdbcCommon.PK_SORT_ASC);

        StringBuilder whereBuilder = new StringBuilder();
        //noinspection ForLoopReplaceableByForEach
        for(int i= 0 ; i < pkColumnList.size() ; i++){
            Field field = pkColumnList.get(i);
            field.setAccessible(true);
            if(field.isAnnotationPresent(FlagBoolean.class)){

                boolean flag  = (boolean)field.get(obj);

                if(flag){
                    whereBuilder.append(" AND ").append(columnNameMap.get(field)).append("='").append("Y").append("'");
                }else{
                    whereBuilder.append(" AND ").append(columnNameMap.get(field)).append("='").append("N").append("'");
                }

            }else if(field.getType().isEnum()){
                whereBuilder.append(" AND ").append(columnNameMap.get(field)).append("='").append(field.get(obj).toString()).append("'");
            }else{
                whereBuilder.append(" AND ").append(columnNameMap.get(field)).append("='").append(field.get(obj)).append("'");
            }
        }

        return whereBuilder.substring(4);
    }

    /**
     * upsert
     * @param conn Connection
     * @param obj T
     * @param <T> Table, Column annotation object
     * @return int fail -1
     */
    public static <T> int upsert(Connection conn, T obj){
        return insert(conn, obj, "UPSERT");
    }


    /**
     * upsert
     * @param obj T
     * @param <T> Table, Column annotation object
     * @return int fail -1
     */
    public static <T> int upsert(T obj){
        ApplicationConnectionPool connectionPool = ApplicationConnectionPool.getInstance();
        try(Connection conn = connectionPool.getConnection()){

            int result =  insert(conn, obj, "UPSERT");
            if(!connectionPool.isAutoCommit()){
                conn.commit();
            }
            return result;
        }catch(Exception e){
            throw new RuntimeException(e);
        }
    }

    /**
     * insert
     * @param obj T
     * @param <T> Table Column annotation object
     * @return int fail -1
     */
    public static <T> int insert(T obj){
        ApplicationConnectionPool connectionPool = ApplicationConnectionPool.getInstance();
        try(Connection conn = connectionPool.getConnection()){

            int result =   insert(conn, obj, "INSERT");
            if(!connectionPool.isAutoCommit()){
                conn.commit();
            }
            return result;

        }catch(Exception e){

            throw new RuntimeException(e);
        }
    }

    /**
     * insert
     * @param conn Connection
     * @param obj T
     * @param <T> Table Column annotation object
     * @return int fail -1
     */
    public static <T> int insert(Connection conn, T obj){
        return insert(conn, obj, "INSERT");
    }

    /**
     * insert
     * @param conn Connection
     * @param obj T
     * @param insertQueryValue String upsert, insert
     * @param <T> Table Column annotation object
     * @return int fail -1
     */
    public static <T> int insert(Connection conn, T obj, String insertQueryValue){

        Class<?> objClass = obj.getClass();
        Map<String, Field> columnFieldMap = makeColumnFieldMap(objClass);
        String [] columnNames = columnFieldMap.keySet().toArray(new String[0]);
        String insertSql = getInsertSql(objClass, columnNames, insertQueryValue);
        //순서정보를 위한 세팅
        Field [] fields = new Field[columnNames.length];
        for (int i = 0; i <columnNames.length ; i++) {
            fields[i] = columnFieldMap.get(columnNames[i]);
        }

        return JdbcCommon.insert(conn, obj, fields, insertSql);
    }

    /**
     * insert sql get
     * @param objClass Class
     * @param columnNames String []
     * @param insertQueryValue String upsert, insert
     * @return String sql
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
     * update
     * @param obj T
     * @param isNullUpdate boolean null value update flag
     * @param <T> Table Column annotation object
     * @return int fail -1
     */
    public static <T> int update(T obj , boolean isNullUpdate ) {
        ApplicationConnectionPool connectionPool = ApplicationConnectionPool.getInstance();
        try(Connection conn = connectionPool.getConnection()){
            int result =   update(conn, obj, isNullUpdate);
            if(!connectionPool.isAutoCommit()){
                conn.commit();
            }
            return result;

        }catch(Exception e){
            throw new RuntimeException(e);
        }
    }


    /**
     * update
     * @param conn Connection
     * @param obj T
     * @param isNullUpdate boolean null value update flag
     * @param <T> Table Column annotation object
     * @return int fail -1
     */
    public static <T> int update(Connection conn,T obj , boolean isNullUpdate ){

        Class<?> objClass = obj.getClass();
        String tableName = TableSql.getTableName(objClass.getAnnotation(Table.class), objClass.getName());

        Map<String, Field> columnFieldMap = makeColumnFieldMap(objClass);
        String [] columnNames = columnFieldMap.keySet().toArray(new String[0]);

        Field [] fields = new Field[columnNames.length];
        for (int i = 0; i <columnNames.length ; i++) {
            fields[i] = columnFieldMap.get(columnNames[i]);
        }

        Map<Field,String> columnNameMap = new HashMap<>();

        StringBuilder sqlBuilder = new StringBuilder();

        sqlBuilder.append("UPDATE ").append(tableName).append(" SET ");

        StringBuilder fieldBuilder = new StringBuilder();
        List<Field> pkColumnList = new LinkedList<>();

        for(int i=0 ; i<fields.length ; i++){
            fields[i].setAccessible(true);
            PrimaryKey pk = fields[i].getAnnotation(PrimaryKey.class);
            if(pk != null){
                pkColumnList.add(fields[i]);
                columnNameMap.put(fields[i], columnNames[i]);
                continue;
            }

            if(!isNullUpdate){
                try{
                    Object object = fields[i].get(obj);
                    if(object == null){
                        continue;
                    }
                }catch(Exception e){
                    logger.error(ExceptionUtil.getStackTrace(e));
                }
            }

            fieldBuilder.append(", ").append(columnNames[i]).append("=?");
        }

        if(pkColumnList.size() ==0){
            throw new PrimaryKeyNotSetException(objClass.getName());
        }

        pkColumnList.sort(JdbcCommon.PK_SORT_ASC);



        sqlBuilder.append(fieldBuilder.toString().substring(1));
        sqlBuilder.append(" WHERE ");

        fieldBuilder.setLength(0);

        //noinspection ForLoopReplaceableByForEach
        for(int i= 0 ; i < pkColumnList.size() ; i++){
            Field field = pkColumnList.get(i);
            fieldBuilder.append(" AND ").append(columnNameMap.get(field)).append("=?");
        }
        sqlBuilder.append(fieldBuilder.substring(4));
        PreparedStatement pstmt = null;

        int successCount;
        try{
            pstmt = conn.prepareStatement(sqlBuilder.toString());
            int index = JdbcCommon.setPrimaryKeyField(pstmt,fields,obj,isNullUpdate);

            //noinspection ForLoopReplaceableByForEach
            for(int i= 0 ; i < pkColumnList.size() ; i++){
                Field field = pkColumnList.get(i);
                Object object = field.get(obj);

                JdbcCommon.setPstmt(obj, object, field, pstmt, index);
                index++;
            }

            pstmt.addBatch();
            pstmt.clearParameters();

            pstmt.executeBatch();
            successCount = 1;
        }catch(Exception e){
            throw new RuntimeException(e);
        }finally{
            //noinspection CatchMayIgnoreException
            try{if(pstmt != null) pstmt.close();}catch(Exception e){}
        }

        return successCount;
    }


    /**
     * 데이터가 없을떄만 insert
     * @param obj T
     * @param <T> Table Column annotation object
     * @return int fail -1
     */
    public static <T> int insertIfNoData(T obj){
        ApplicationConnectionPool connectionPool = ApplicationConnectionPool.getInstance();
        try(Connection conn = connectionPool.getCommitConnection()){


            int result =  insertIfNoData(conn, obj);
            if(!connectionPool.isAutoCommit() && result != -1){
                conn.commit();
            }
            return result;
        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }

    /**
     * 데이터가 없을떄만 insert
     * @param conn Connection
     * @param obj T
     * @param <T> Table Column annotation object
     * @return int fail -1
     */
    public static <T> int insertIfNoData(Connection conn,T obj){

        int successCount = -1;

        try{

            Object checkObj = getObj(conn, obj.getClass(), getCheckWhere(obj));
            if(checkObj == null){
                successCount =insert(conn, obj);
            }
        }
        catch(Exception e){
            throw new RuntimeException(e);
        }

        return successCount;
    }

    /**
     * class 생성 도움 내용 생성
     * @param tableName String
     * @return String Table Column annotation String value
     */
    public static String makeObjectValue( String tableName) {

        try(Connection conn = ApplicationConnectionPool.getInstance().getCommitConnection()){
            return makeObjectValue(conn, tableName);
        }catch(Exception e){
            throw new RuntimeException(e);
        }
    }


    /**
     * class 생성 도움 내용 생성
     * @param conn Connection
     * @param tableName String
     * @return String Table Column annotation String value
     */
    public static String makeObjectValue(Connection conn, String tableName){
        Statement stmt = null;
        ResultSet result = null;
        StringBuilder sb = new StringBuilder();
        sb.append("@Table(name=\"").append(tableName).append("\")");

        JdbcNamingDataType jdbcNamingDataType = JdbcNamingDataType.getInstance();
        try{
            Map<String, Integer> pkMap = Database.getPrimaryKeyColumnsForTable(conn, tableName);

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
                }else {
                    if(jdbcNamingDataType.isFrontPriority()){
                        //앞먼저 검사
                        if(jdbcNamingDataType.isFront() && columnName.startsWith("FG_")){
                            sb.append("@FlagBoolean\n");
                        }else if(jdbcNamingDataType.isBack() && columnName.endsWith("_FG")){
                            sb.append("@FlagBoolean\n");
                        }

                    }else{
                        if(jdbcNamingDataType.isBack() && columnName.endsWith("_FG")){
                            sb.append("@FlagBoolean\n");
                        }else if(jdbcNamingDataType.isFront() && columnName.startsWith("FG_")){
                            sb.append("@FlagBoolean\n");
                        }
                    }
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




    /**
     * 바뀐정보가 있으면 update
     * @param obj T obj
     * @param updateInfo T check obj
     * @param <T> Table, Column annotation object
     * @return boolean isUpdate
     */
    public static <T> boolean updateObj(T obj, T updateInfo) {

        Class<?> objClass = obj.getClass();

        Field[] fields = FieldUtil.getFieldArrayToParents(objClass);

        boolean isUpdate = false;

        try {
            for (Field field : fields) {
                field.setAccessible(true);
                if (field.isAnnotationPresent(Column.class)) {

                    Object data = field.get(obj);

                    Object updateData = field.get(updateInfo);

                    if(data == null &&  updateData == null){
                        continue;
                    }

                    if(data == updateData){
                        continue;
                    }

                    if(
                            (data == null || updateData == null)
                            || !data.equals(updateData)
                    ){
                        field.set(obj, updateData);
                        isUpdate = true;
                    }
                }

            }
        }catch(Exception e){
            throw new RuntimeException(e);
        }

        return isUpdate;
    }

}
