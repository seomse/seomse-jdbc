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
package com.seomse.jdbc.naming;

import com.seomse.commons.utils.ExceptionUtil;
import com.seomse.commons.utils.packages.classes.field.FieldUtil;
import com.seomse.jdbc.Database;
import com.seomse.jdbc.PrepareStatementData;
import com.seomse.jdbc.annotation.PrimaryKey;
import com.seomse.jdbc.annotation.Table;
import com.seomse.jdbc.common.*;
import com.seomse.jdbc.connection.ApplicationConnectionPool;
import com.seomse.jdbc.exception.FieldNullException;
import com.seomse.jdbc.exception.PrimaryKeyNotSetException;
import com.seomse.jdbc.exception.TableNameEmptyException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.sql.*;
import java.util.*;

/**
 * <pre>
 *  파 일 명 : JdbcNaming.java
 *  설    명 : 명명규칙을 활용한 jdbc활용
 *         
 *  작 성 자 : macle
 *  작 성 일 : 2017.09
 *  버    전 : 1.5
 *  수정이력 :  2017.11, 2018.04, 2018.07, 2019.10, 2019.12.19
 *  기타사항 :
 * </pre>
 * @author Copyrights 2017 ~ 2019 by ㈜섬세한사람들. All right reserved.
 */
public class JdbcNaming {

	private static final Logger logger = LoggerFactory.getLogger(JdbcNaming.class);

	
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
		try(Connection conn = ApplicationConnectionPool.getInstance().getCommitConnection()){
			return getObjList(conn, objClass, null, null, null, -1, null);
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
		try(Connection conn = ApplicationConnectionPool.getInstance().getCommitConnection()){
			return getObjList(conn, objClass, null, whereValue, null, -1, null);
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
		try(Connection conn = ApplicationConnectionPool.getInstance().getCommitConnection()){
			return getObjList(conn,  objClass, null, whereValue, null, size, null);
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
		try(Connection conn = ApplicationConnectionPool.getInstance().getCommitConnection()){
			return getObjList(conn,  objClass, null, whereValue, orderByValue, -1, null);
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
		try(Connection conn = ApplicationConnectionPool.getInstance().getCommitConnection()){
			return getObjList(conn,  objClass, null, whereValue, orderByValue, size, null);
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
		try(Connection conn = ApplicationConnectionPool.getInstance().getCommitConnection()){
			return getObjList(conn,  objClass, null, whereValue, null, -1, prepareStatementDataMap);
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
		try(Connection conn = ApplicationConnectionPool.getInstance().getCommitConnection()){
			return getObjList(conn,  objClass, sql, whereValue, null, -1, prepareStatementDataMap);
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
		try(Connection conn = ApplicationConnectionPool.getInstance().getCommitConnection()){
			return getObjList(conn,  objClass, sql, null, null, -1, null);
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

		Field [] fields = getFields(objClass);
		String selectSql;
		if(sql == null) {
			selectSql = getSql(objClass, table, fields, whereValue, orderByValue);
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

			StmtResultSet stmtResultSet = JdbcCommon.makeStmtResultSet(conn, selectSql, prepareStatementDataMap);
			stmt = stmtResultSet.getStmt();
			result = stmtResultSet.getResultSet();
			
			int fetchSize = table.fetchSize(); 
			if(fetchSize > 0){
				stmt.setFetchSize(table.fetchSize());
				result.setFetchSize(table.fetchSize());
			}
			if(size == -1){
				while(result.next()){
					T resultObj = objClass.newInstance();

					for(Field field : fields){
						JdbcField.setFieldObject(result, field, field.getName(), resultObj);
					}
					
					resultList.add(resultObj);
				}	
			}else{
				int checkCount = 0;
				while(result.next()){

					T resultObj = objClass.newInstance();


					for(Field field : fields){
						JdbcField.setFieldObject(result, field, field.getName(), resultObj);
					}
						
						
					
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
	 * sql 얻기
	 * @return sql
	 */
	public static <T> String getSql(Class<T> objClass, Table table , Field [] fields, String whereValue, String orderByValue){
		StringBuilder sqlBuilder = new StringBuilder();

		String tableSql = table.sql();
		if(tableSql.equals(Table.EMPTY)){

			if(table.name().equals(Table.EMPTY)){
				throw new TableNameEmptyException(objClass.getName());
			}

			if(fields == null || fields.length ==0){
				throw new FieldNullException(objClass.getName());
			}
			StringBuilder fieldBuilder = new StringBuilder();

			for(Field field : fields){

				fieldBuilder.append(", ").append(field.getName());


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
		try(Connection conn = ApplicationConnectionPool.getInstance().getCommitConnection()){
			return getObj(conn, objClass, null, null, null, null);
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
		try(Connection conn = ApplicationConnectionPool.getInstance().getCommitConnection()){
			return getObj(conn, objClass, null, whereValue, null, null);
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
		try(Connection conn = ApplicationConnectionPool.getInstance().getCommitConnection()){
			return getObj(conn, objClass, null, whereValue, orderByValue, null);
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
		try(Connection conn = ApplicationConnectionPool.getInstance().getCommitConnection()){
			return getObj(conn, objClass, null, whereValue, null, prepareStatementDataMap);
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
		try(Connection conn = ApplicationConnectionPool.getInstance().getCommitConnection()){
			return getObj(conn, objClass, sql, whereValue, null, prepareStatementDataMap);
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
		try(Connection conn = ApplicationConnectionPool.getInstance().getCommitConnection()){
			return getObj(conn,  objClass, sql, null, null, null);
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

		Field [] fields = FieldUtil.getFieldArrayToParents(objClass);

		String makeSql;
		if(sql == null) {
			makeSql = getSql(objClass,  objClass.getAnnotation(Table.class), fields, whereValue, orderByValue);
		}else{
			makeSql = sql;
		}
		

		Statement stmt = null;
		ResultSet result = null;
		
		T resultObj = null;

		//noinspection CaughtExceptionImmediatelyRethrown
		try{

			StmtResultSet stmtResultSet = JdbcCommon.makeStmtResultSet(conn, makeSql, prepareStatementDataMap);
			stmt = stmtResultSet.getStmt();
			result = stmtResultSet.getResultSet();
			result.setFetchSize(2);
			if(result.next()){
				resultObj = objClass.newInstance();

				for(Field field : fields){
					JdbcField.setFieldObject(result, field, field.getName(), resultObj);
				}
			}	
		}catch(Exception e){
			throw e;
		}finally{
			JdbcClose.statementResultSet(stmt, result);
		}
		
		return resultObj;	
	}
	

	/**
	 * obj를 이용한 데이터 upsert
	 * @param objClassList 객체리시트
	 * @return fail -1
	 */
	public static <T> int upsert( List<T> objClassList){
		ApplicationConnectionPool connectionPool = ApplicationConnectionPool.getInstance();
		try(Connection conn = connectionPool.getConnection()){
			int result = insert(conn, objClassList, "UPSERT", true);
			if(!connectionPool.isAutoCommit()){
				conn.commit();
			}

			return result;
		}catch(Exception e){
			throw new RuntimeException(e);
		}
	}
	

	/**
	 * obj를 이용한 데이터 upsert
	 * @param objClassList 객체리시트
	 * @param isClearParameters ClearParameters 여부
	 * @return fail -1
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
	 * obj를 이용한 데이터 upsert
	 * @param conn 연결 컨넥션
	 * @param objClassList 객체리시트
	 * @param isClearParameters ClearParameters 여부
	 * @return fail -1
	 */
	public static <T> int upsert(Connection conn, List<T> objClassList,   boolean isClearParameters){
		return insert(conn, objClassList , "UPSERT", isClearParameters);
	}
	
	/**
	 * obj를 이용한 데이터 insert
	 * @param objClassList 객체리시트
	 * @return fail -1
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
	 * obj를 이용한 데이터 insert
	 * @param objClassList 객체리시트
	 * @param isClearParameters ClearParameters 여부
	 * @return fail -1
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
	 * obj를 이용한 데이터 insert
	 * @param conn 연결 컨넥션
	 * @param objClassList 객체리시트
	 * @param isClearParameters ClearParameters 여부
	 * @return fail -1
	 */
	public static <T> int insert(Connection conn, List<T> objClassList,   boolean isClearParameters){
		return insert(conn, objClassList , "INSERT", isClearParameters);
	}


	/**
	 * obj를 이용한 데이터 insert
	 * @param conn 연결 컨넥션
	 * @param objClassList 객체리시트
	 * @param insertQueryValue insert ot upsert
	 * @param isClearParameters ClearParameters 여부
	 * @return fail -1
	 */
	public static <T> int insert(Connection conn, List<T> objClassList, String insertQueryValue,  boolean isClearParameters){
		if(objClassList == null || objClassList.size() ==0){
			return 0;
		}


		Class<?> objClass = objClassList.get(0).getClass();

		Field [] fields = getFields(objClass);

		String insertSql = getInsertSql(objClass, fields, insertQueryValue);


		PreparedStatement pstmt = null;

		int successCount ;

		try{
			pstmt = conn.prepareStatement(insertSql);

			for(T obj : objClassList){
				JdbcCommon.addBatch(obj, fields, pstmt);
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
	public static String getInsertSql(Class<?> objClass, Field [] fields , String insertQueryValue){

		Table table = objClass.getAnnotation(Table.class);

		String tableName = TableSql.getTableName(table, objClass.getName());

		StringBuilder sqlBuilder = new StringBuilder();

		sqlBuilder.append(insertQueryValue).append(" INTO ").append(tableName).append(" (");

		StringBuilder fieldBuilder = new StringBuilder();

		//noinspection ForLoopReplaceableByForEach
		for(int i=0 ; i<fields.length ; i++){
			fieldBuilder.append(", ").append(fields[i].getName());
		}
		sqlBuilder.append(fieldBuilder.toString().substring(1));
		sqlBuilder.append(") VALUES (");


		fieldBuilder.setLength(0);
		for(int i=0 ; i<fields.length ; i++){
			fieldBuilder.append(", ?");
		}
		sqlBuilder.append(fieldBuilder.toString().substring(1));
		sqlBuilder.append(" )");

		return sqlBuilder.toString();
	}

	/**
	 * 있으면 업데이트 없으면 추가
	 * @param obj jdbcObject
	 * @param isNullUpdate null column update flag
	 * @return fail -1
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
	 * 있으면 업데이트 없으면 추가
	 * @param conn Connection
	 * @param obj jdbcObject
	 * @param isNullUpdate null column update flag
	 * @return 실패 -1
	 */
	public static <T> int insertOrUpdate(Connection conn, Object obj , boolean isNullUpdate ){


		int successCount ;
		try{

			Object checkVo = getObj(conn, obj.getClass(), null, getCheckWhere(obj), null, null);
			if(checkVo == null){
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
	 * 객체를이용한 자동 upsert 
	 * @param conn Connection
	 * @param obj jdbcObject
	 * @return fail -1
	 */
	public static <T> int upsert(Connection conn, T obj){
		return insert(conn, obj, "UPSERT");
	}

	/**
	 * 객체를이용한 자동 upsert 
	 * @param obj jdbcObject
	 * @return success 1, fail -1
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
	 * 객체를이용한 자동 insert 
	 * @param obj jdbcObject
	 * @return success 1, fail -1
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
	 * 객체를이용한 자동 insert 
	 * @param conn Connection
	 * @param obj jdbcObject
	 * @return success 1, fail -1
	 */
	public static <T> int insert(Connection conn, T obj){
		return insert(conn, obj, "INSERT");
	}
	
	/**
	 * 객체를이용한 자동 insert 
	 * @param conn Connection
	 * @param obj jdbcObject
	 * @return success 1, fail -1
	 */
	public static <T> int insert(Connection conn, T obj, String insertQueryValue){
		
		Class<?> objClass = obj.getClass();
		Field [] fields =getFields(objClass);

		String insertSql = getInsertSql(objClass, fields, insertQueryValue);

		int successCount ;


		PreparedStatement pstmt = null;

		//noinspection TryFinallyCanBeTryWithResources
		try{
			pstmt = conn.prepareStatement(insertSql);

			JdbcCommon.addBatch(obj, fields, pstmt);
			pstmt.clearParameters();	
			
			pstmt.executeBatch();
			successCount = 1;
		}catch(Exception e){
			throw new RuntimeException(e);
		}finally{
			//noinspection CatchMayIgnoreException
			try{if(pstmt!=null)pstmt.close();  }catch(Exception e){}
		} 
				
		return successCount;
	}



	/**
	 * 객체를 이용한 update
	 * @param obj jdbcObject
	 * @param isNullUpdate null 업데이트 여부
	 * @return  success 1, fail -1
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
    * 객체를 이용한 update
    * @param conn Connection
    * @param obj jdbcObject
    * @param isNullUpdate null 업데이트 여부
    * @return  success 1, fail -1
    */
	public static <T> int update(Connection conn,T obj , boolean isNullUpdate ){
			
		Class<?> objClass = obj.getClass();
		String tableName = TableSql.getTableName(objClass.getAnnotation(Table.class), objClass.getName());
		
		Field [] fields = getFields(objClass);

		StringBuilder sqlBuilder = new StringBuilder();
	
		sqlBuilder.append("UPDATE ").append(tableName).append(" SET ");
		
		StringBuilder fieldBuilder = new StringBuilder();
		List<Field> pkColumnList = new LinkedList<>();

		//noinspection ForLoopReplaceableByForEach
		for(int i=0 ; i<fields.length ; i++){
			fields[i].setAccessible(true);
			PrimaryKey  pk = fields[i].getAnnotation(PrimaryKey.class);
			if(pk != null){
				pkColumnList.add(fields[i]);
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
	
			fieldBuilder.append(", ").append(fields[i].getName()).append("=?");
		}
		
		if(pkColumnList.size() ==0){
			throw new PrimaryKeyNotSetException(objClass.getName());
		}




		sqlBuilder.append(fieldBuilder.toString().substring(1));		
		sqlBuilder.append(" WHERE ");
		
		fieldBuilder.setLength(0);

		pkColumnList.sort(JdbcCommon.PK_SORT_ASC);
		//noinspection ForLoopReplaceableByForEach
		for(int i= 0 ; i < pkColumnList.size() ; i++){
			Field field = pkColumnList.get(i);
			fieldBuilder.append(" AND ").append(field.getName()).append("=?");
		}
		sqlBuilder.append(fieldBuilder.substring(4));
		PreparedStatement pstmt = null;

		int successCount;
		try{
			pstmt = conn.prepareStatement(sqlBuilder.toString());
				
			int index = 0;
			//noinspection ForLoopReplaceableByForEach
			for(int i=0 ; i<fields.length ; i++){
				
				PrimaryKey  pk = fields[i].getAnnotation(PrimaryKey.class);
				if(pk != null){
					continue;
				}
				fields[i].setAccessible(true);
				Object object = fields[i].get(obj);
				if(!isNullUpdate){
					
					if(object == null){
						continue;
					}
				}

				if(object == null){
					JdbcCommon.setNullPstmt(obj,fields[i],pstmt,index);
				}else{
					JdbcCommon.setPstmt(obj, object, fields[i], pstmt, index);
				}
				index++;
						
			}
				
			for(int i= 0 ; i < pkColumnList.size() ; i++){
				Field field = pkColumnList.get(i);
				Object object = field.get(obj);
				JdbcCommon.setPstmt(obj, object, fields[i], pstmt, index);
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
	 * 클래스 맴버변수 생성
	 * @param conn Connection
	 * @param tableName tableName
	 * @return 변수생성값
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

				if(dataType == JdbcDataType.STRING){


					fieldBuilder.append("private String ").append(columnName);

					String defaultValue = defaultValueMap.get(columnName);
					if(defaultValue != null){
						fieldBuilder.append(" = ").append(defaultValue.replace("'", "\""));
					}
					fieldBuilder.append(";\n");


				}else if(dataType == JdbcDataType.DATE_TIME ){
					fieldBuilder.append("@DateTime\nprivate Long ").append(columnName);


					String defaultValue = defaultValueMap.get(columnName);
					if(defaultValue != null){
						if(defaultValue.replace("'", "").toUpperCase().trim().equals("SYSDATE")){
							fieldBuilder.append( " = System.currentTimeMillis()" );
						}
					}
					fieldBuilder.append(";\n");

				}else if(dataType == JdbcDataType.LONG ){
					fieldBuilder.append("private Long ").append(columnName);

					String defaultValue = defaultValueMap.get(columnName);
					if(defaultValue != null){
						defaultValue = defaultValue.replace("'", "").trim();
						fieldBuilder.append(" = ").append(defaultValue).append("L");

					}
					fieldBuilder.append(";\n");

				}else if(dataType == JdbcDataType.DOUBLE ){
					fieldBuilder.append("private Double ").append(columnName);

					String defaultValue = defaultValueMap.get(columnName);
					if(defaultValue != null){
						defaultValue = defaultValue.replace("'", "").trim();
						fieldBuilder.append(" = ").append(defaultValue);
						if(!defaultValue.contains(".")){
							fieldBuilder.append(".0");
						}


					}
					fieldBuilder.append(";\n");

				}else if(dataType == JdbcDataType.INTEGER
						){
					fieldBuilder.append("private Integer ").append(columnName);

					String defaultValue = defaultValueMap.get(columnName);
					if(defaultValue != null){
						defaultValue = defaultValue.replace("'", "").trim();
						fieldBuilder.append(" = ").append(defaultValue);

					}
					fieldBuilder.append(";\n");

				}



			}

		}catch(Exception e){
			throw new RuntimeException(e);
		}finally{
			JdbcClose.statementResultSet(stmt, result);
		}
		return fieldBuilder.toString();
	}

	/**
	 * 클래스 맴버변수 생성
	 * @param tableName tableName
	 * @return 변수생성값
	 */
	public static String makeObjectValue( String tableName){
		try(Connection conn = ApplicationConnectionPool.getInstance().getCommitConnection()){
			return makeObjectValue(conn, tableName);
		}catch(Exception e){
			throw new RuntimeException(e);
		}
	}

	/**
	 * 데이터가 없을경우에만 insert
	 * @param obj jdbcObject
	 * @return success insert count, fail -1
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
	 * 데이터가 없을경우에만 insert
	 * @param conn Connection
	 * @param obj jdbcObject
	 * @return success insert count, fail -1
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

	public static <T> String getCheckWhere(T obj) throws IllegalAccessException {
		Class<?> objClass = obj.getClass();
		String tableName = TableSql.getTableName(objClass.getAnnotation(Table.class), objClass.getName());

		Field [] fields = getFields(objClass);

		List<Field> pkColumnList = new LinkedList<>();

		//noinspection ForLoopReplaceableByForEach
		for(int i=0 ; i<fields.length ; i++){
			fields[i].setAccessible(true);
			PrimaryKey pk  = fields[i].getAnnotation(PrimaryKey.class);
			if(pk != null){
				pkColumnList.add(fields[i]);
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
			whereBuilder.append(" AND ").append(field.getName()).append("='").append(field.get(obj)).append("'");
		}

		return whereBuilder.substring(4);
	}


	/**
	 * Field []  얻기
	 * @param objClass objClass
	 * @return Field []
	 */
	public static Field [] getFields(Class<?> objClass){
		Field [] fields = FieldUtil.getFieldArrayToParents(objClass);
		if(fields == null || fields.length ==0){
			throw new FieldNullException(objClass.getName());
		}
		return fields;
	}
}