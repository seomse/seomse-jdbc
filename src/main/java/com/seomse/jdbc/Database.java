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
package com.seomse.jdbc;

import com.seomse.commons.utils.ExceptionUtil;
import com.seomse.jdbc.common.JdbcClose;
import com.seomse.jdbc.connection.ApplicationConnectionPool;
import com.seomse.jdbc.exception.NotDbTypeException;
import com.seomse.jdbc.sequence.SequenceMaker;
import com.seomse.jdbc.sequence.SequenceMakerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/** 
 * <pre>
 *  파 일 명 : Database.java
 *  설    명 : DB의 정보활용 클래스
 *         
 *  작 성 자 : macle
 *  작 성 일 : 2017.09
 *  버    전 : 1.3
 *  수정이력 : 2019.02, 2019.06, 2019.11
 *  기타사항 :
 * </pre>
 * @author Copyrights 2017 ~ 2019 by ㈜섬세한사람들. All right reserved.
 */
public class Database {

	private static final Logger logger = LoggerFactory.getLogger(Database.class);

	private static final SequenceMaker sequenceMaker = SequenceMakerFactory.make(ApplicationConnectionPool.getInstance().getJdbcType());


	/**
	 * 시퀀스 값 얻기
	 * @param sequenceName 시퀀스 명
	 * @return 스퀀스값
	 */
	public static String nextVal(String sequenceName){
		return sequenceMaker.nextVal(sequenceName);
	}
	
	
	/**
	 * DB 서버 시간정보 얻기
	 * @return DB 서버 시간정보
	 */
	public static Long getDateTime(){


		return getDateTime(getSysDateQuery(ApplicationConnectionPool.getInstance().getJdbcType()));
		
	}

	/**
	 * DB유형에 맞는 시간을 가져올수 있는 쿼리목록
	 * @param dbType db 유형
	 * @return db 서버 시간 얻는 쿼리
	 */
	public static String getSysDateQuery(String dbType){
		dbType = dbType.toLowerCase();
		if(dbType.startsWith("oracle") || dbType.startsWith("tibero")){
			return "SELECT SYSTIMESTAMP FROM DUAL";
		}else if(dbType.startsWith("mysql") || dbType.startsWith("maria")){
			return "SELECT now()";
		}else if(dbType.startsWith("mssql") || dbType.startsWith("ms-sql")){
			return "SELECT GETDATE()";
		}
		
		
		throw new NotDbTypeException(dbType);
	}
	
	/**
	 * DB 유형에 맞는 Date 호출 값 얻기
	 * @param dbType db 유형
	 * @return date 가져오는 select
	 */
	public static String getSysDateName(String dbType){
		dbType = dbType.toLowerCase();
		//noinspection
		if(dbType.startsWith("oracle") || dbType.startsWith("tibero") ){
			return "SYSDATE";
		}else if(dbType.startsWith("mysql") || dbType.startsWith("maria")){
			return "now()";
		}else if(dbType.startsWith("mssql")  || dbType.startsWith("ms-sql")){
			return "GETDATE()";
		}
		
		throw new NotDbTypeException(dbType);
	}

	/**
	 * 테이블 리스트를 조회하는 sql 얻기
	 * @param dbType dbType db 유형
	 * @return sql
	 */
	public static String getTableListSql(String dbType){

		//noinspection
		if(dbType.startsWith("oracle") || dbType.startsWith("tibero") ){
			return "SELECT TABLE_NAME FROM USER_TABLES";
		}else if(dbType.startsWith("mysql")|| dbType.startsWith("maria")){
			return "SHOW TABLES";
		}else if(dbType.startsWith("mssql") || dbType.startsWith("ms-sql")){
			return "SELECT TABLE_NAME FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_TYPE = 'BASE TABLE'";
		}

		throw new NotDbTypeException(dbType);
	}

	

	/**
	 * 시간정보 얻기
	 * @param sql 시간 select 쿼리
	 */
	public static Long getDateTime(String sql){

		try(Connection conn =  ApplicationConnectionPool.getInstance().getCommitConnection()){
			return getDateTime(conn, sql);
		}catch(Exception e){
			logger.error(ExceptionUtil.getStackTrace(e));
			return System.currentTimeMillis();
		}

	}
	
	/**
	 * sql에 대한 결과를 dateTime(timestamp) 형태로 받는다.
	 * @param conn 연결
	 * @param sql 쿼리
	 * @return 시간
	 */
	public static Long getDateTime(Connection conn, String sql) throws SQLException {
		Long resultValue = null;
		
		Statement stmt = null;
		ResultSet result = null;

		//noinspection CaughtExceptionImmediatelyRethrown
		try{
			stmt = conn.createStatement();
			result = stmt.executeQuery(sql);
			ResultSetMetaData metaData = result.getMetaData();
			int count = metaData.getColumnCount(); //number of column
			if(count > 0 ){
				String columnName = metaData.getColumnLabel(1); 		
				if(result.next()){
					Timestamp timestamp = result.getTimestamp(columnName);
					if(timestamp != null)
						resultValue = timestamp.getTime();			
				}
			}
		}catch(Exception e){
			throw e;
		}finally{
			JdbcClose.statementResultSet(stmt, result);
		}
		
		return resultValue;
	}
	

	/**
	 * 테이블의 컬럼명 목록 얻기
	 * @param tableName 테이블명
	 * @return 컬럼명 배열
	 */
	public static String [] getColumnNameArray(String tableName){

		try(Connection conn =  ApplicationConnectionPool.getInstance().getCommitConnection()) {
			return getColumnNameArray(conn, tableName);
		}catch(Exception e){
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * 테이블의 컬럼명 목록 얻기
	 * @param conn DB연결 컨넥션
	 * @param tableName 테이블 네임
	 * @return 컬럼명 배열
	 */
	public static String [] getColumnNameArray(Connection conn, String tableName) throws SQLException {
		Statement stmt = null;
		ResultSet result = null;
		String [] nameArray ;
		//noinspection CaughtExceptionImmediatelyRethrown
		try{
			stmt = conn.createStatement();

			//noinspection SqlDialectInspection,SqlNoDataSourceInspection
			result = stmt.executeQuery("SELECT * FROM " + tableName);
			ResultSetMetaData metaData = result.getMetaData();
			int count = metaData.getColumnCount(); //number of column
			
			nameArray = new String [count];
			for (int i = 1; i <= count; i++){
				nameArray[i -1] = metaData.getColumnLabel(i);
			}
		
		}catch(Exception e) {
			throw e;
		}finally{
			JdbcClose.statementResultSet(stmt, result);
		}
		return nameArray;
	}
	
	/**
	 * 기본키 컬럼정보 얻기
	 * @param tableName tableName
	 * @return PrimaryKeyColumnsForTable
	 */
	public static Map<String, Integer> getPrimaryKeyColumnsForTable( String tableName) {
		try(Connection conn =  ApplicationConnectionPool.getInstance().getCommitConnection()) {
			return getPrimaryKeyColumnsForTable(conn, tableName);
		}catch(SQLException e){
			logger.error(ExceptionUtil.getStackTrace(e));
			throw new RuntimeException(e);
		}
	}

	/**
	 * 기본키 컬럼정보 얻기
	 * @param conn Connection
	 * @param tableName tableName
	 * @return PrimaryKeyColumnsForTable
	 */
	 public static Map<String, Integer> getPrimaryKeyColumnsForTable(Connection conn, String tableName) throws SQLException {
		 ResultSet pkColumns= null;
		 Map<String, Integer> pkMap = new HashMap<>();
		 //noinspection CaughtExceptionImmediatelyRethrown
		 try{
			 pkColumns= conn.getMetaData().getPrimaryKeys(null,null,tableName);

			 while(pkColumns.next()) {
			    String pkColumnName = pkColumns.getString("COLUMN_NAME");
			    Integer pkPosition = pkColumns.getInt("KEY_SEQ");

			    pkMap.put(pkColumnName, pkPosition);
			 }

		 }catch(SQLException e){
			throw e;
		 }finally{
			 //noinspection CatchMayIgnoreException
			 try{if(pkColumns!=null)pkColumns.close(); }catch(Exception e){}
		 }

		return pkMap;

	 }

	 

	 /**
	  * 테이블의 컬럼별 디폴트값을 가져온다.
	  * @param tableName tableName
	  * @return DefaultValue
	  */
	 public static Map<String, String> getDefaultValue(String tableName){
		 //혹시 받는쪽에서 데이터변환코딩을 할 수 있으므로 디폴트값이 없어도 빈객체를 생성해서 돌려준다.
		
		 String dbType = ApplicationConnectionPool.getInstance().getJdbcType();
		 dbType = dbType.toLowerCase();
		 Map<String,String> defaultMap = new HashMap<>();
		
		if(dbType.equals("oracle") || dbType.equals("tibero") ){

			List<Map<String,String>> dataList =JdbcQuery.getMapStringList("SELECT COLUMN_NAME, DATA_DEFAULT FROM USER_TAB_COLS  WHERE TABLE_NAME = '" + tableName +"'");
			for(Map<String,String> data : dataList){
				String value = data.get("DATA_DEFAULT");
				
				if(value == null){
					continue;
				}
				String columnName  = data.get("COLUMN_NAME");
				if(columnName == null){
					continue;
				}
				
				defaultMap.put(columnName, value);
				
			}
			
		}else{
			throw new NotDbTypeException(dbType);
		}

		return defaultMap;
	 }

	 /**
	  * 연결유지 쿼리를 돌려준다.
	  * @return ConnectionKeepQuery
	  */
	 public static String getConnectionKeepQuery(){
		String sql  ;
			
		String dbType = ApplicationConnectionPool.getInstance().getJdbcType();
		dbType = dbType.toLowerCase();
			
		if(dbType.startsWith("maria") || dbType.startsWith("mysql") ||  dbType.startsWith("mssql") ){
			sql = "SELECT 1";
		}else{
			sql = "SELECT 1 FROM DUAL";
				
		}
		
		return sql;
	 }
	 


}