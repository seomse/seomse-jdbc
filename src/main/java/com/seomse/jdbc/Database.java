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
import com.seomse.jdbc.exception.JdbcServerTimeException;
import com.seomse.jdbc.exception.NotDbTypeException;
import com.seomse.jdbc.exception.SQLRuntimeException;
import com.seomse.jdbc.sequence.SequenceManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Database system use
 * @author macle
 */
public class Database {

	private static final Logger logger = LoggerFactory.getLogger(Database.class);
	private static final SequenceManager sequenceManager = new SequenceManager();


	/**
	 * 시퀀스 관리자
	 * 기본 DB에서 관리하는 시퀀스 관리자
	 * @return 시퀀스 관리자 얻기
	 */
	public static SequenceManager getSequenceManager() {
		return sequenceManager;
	}

	/**
	 * 시퀀스 값 얻기
	 * @param sequenceName String sequence name
	 * @return String sequence value
	 */
	public static String nextVal(String sequenceName){
		return sequenceManager.nextVal(sequenceName);
	}

	/**
	 * 시퀀스 값 얻기
	 * @param sequenceName String sequence name
	 * @param jdbcType db 유형
	 * @return String sequence value
	 */
	public static String nextVal(String sequenceName, String jdbcType){
		return sequenceManager.nextVal(sequenceName, jdbcType);
	}

	/**
	 * database server time
	 * unix time
	 * @return long
	 */
	public static long getDateTime(){

		return getDateTime(getSysDateQuery(ApplicationConnectionPool.getInstance().getJdbcType()));
		
	}

	/**
	 * time sql
	 * @param dbType String database type
	 * @return String sql
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
	 * database date query
	 * @param dbType String database type
	 * @return String
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
	 * table list sql
	 * @param dbType String database type
	 * @return String sql
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
	 * time get
	 * @param sql String time sql
	 * @return long unix time
	 */
	public static long getDateTime(String sql){

		try(Connection conn =  ApplicationConnectionPool.getInstance().getCommitConnection()){
			return getDateTime(conn, sql);
		}catch(Exception e){
			logger.error(ExceptionUtil.getStackTrace(e));
			return System.currentTimeMillis();
		}

	}

	/**
	 * time get
	 * @param conn Connection
	 * @param sql String sql
	 * @return long unix time
	 * @throws SQLException SQLException
	 */
	public static long getDateTime(Connection conn, String sql) throws SQLException {
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
					return timestamp.getTime();
				}
			}
		}catch(SQLException e){
			throw e;
		}finally{
			JdbcClose.statementResultSet(stmt, result);
		}
		throw new JdbcServerTimeException("date time error sql: " + sql);
	}
	

	/**
	 * 테이블 컬럼 목록 얻기
	 * @param tableName String
	 * @return String []
	 */
	public static String [] getColumnNameArray(String tableName){

		try(Connection conn =  ApplicationConnectionPool.getInstance().getCommitConnection()) {
			return getColumnNameArray(conn, tableName);
		}catch(SQLException e){
			throw new SQLRuntimeException(e);
		}
	}
	

	/**
	 * 테이블 컬럼 목록 얻기
	 * @param conn Connection
	 * @param tableName String
	 * @return String []
	 * @throws SQLException SQLException
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
		
		}catch(SQLException e) {
			throw e;
		}finally{
			JdbcClose.statementResultSet(stmt, result);
		}
		return nameArray;
	}

	/**
	 * 기본키 컬럼정보 얻기
	 * @param tableName String
	 * @return Map PrimaryKeyColumnsForTable
	 */
	public static Map<String, Integer> getPrimaryKeyColumnsForTable( String tableName) {
		try(Connection conn =  ApplicationConnectionPool.getInstance().getCommitConnection()) {
			return getPrimaryKeyColumnsForTable(conn, tableName);
		}catch(SQLException e){
			logger.error(ExceptionUtil.getStackTrace(e));
			throw new SQLRuntimeException(e);
		}
	}

	/**
	 * 기본키 컬럼정보 얻기
	 * @param conn Connection
	 * @param tableName String
	 * @return  Map PrimaryKeyColumnsForTable
	 * @throws SQLException SQLException
	 */
	 public static Map<String, Integer> getPrimaryKeyColumnsForTable(Connection conn, String tableName) throws SQLException {
		Map<String, Integer> pkMap = new HashMap<>();
		 try( ResultSet pkColumns =  conn.getMetaData().getPrimaryKeys(null,null,tableName)){

			 while(pkColumns.next()) {
			    String pkColumnName = pkColumns.getString("COLUMN_NAME");
			    Integer pkPosition = pkColumns.getInt("KEY_SEQ");

			    pkMap.put(pkColumnName, pkPosition);
			 }

		 }
		return pkMap;

	 }

	/**
	 * 기본값 얻기 
	 * @param tableName String
	 * @return Map 컬럼별 기본 값
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

		}else if(dbType.startsWith("maria") || dbType.startsWith("mysql")) {
			List<Map<String,String>> dataList =JdbcQuery.getMapStringList("SELECT COLUMN_NAME, COLUMN_DEFAULT FROM Information_Schema.Columns  WHERE Table_Name = '" + tableName +"'");
			for(Map<String,String> data : dataList){
				String value = data.get("COLUMN_DEFAULT");
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
	 * 연결 유지 쿼리
	 * @return String connection keep sql
	 */
	public static String getConnectionKeepQuery(){

		String dbType = ApplicationConnectionPool.getInstance().getJdbcType();
		dbType = dbType.toLowerCase();

		return getConnectionKeepQuery(dbType) ;
	}

	/**
	 * 연결 유지 쿼리
	 * @param dbType oracle, mysql, maria ....
	 * @return connection keep sql
	 */
	public static String getConnectionKeepQuery(String dbType){
		if(dbType.startsWith("maria") || dbType.startsWith("mysql") ||  dbType.startsWith("mssql") ){
			return "SELECT 1";
		}else{
			return "SELECT 1 FROM DUAL";
				
		}
	}

	/**
	 * 컬럼 목록 얻기
	 * @param resultSet ResultSet
	 * @return String [] ColumnNames
	 * @throws SQLException SQLException
	 */
	public static String [] getColumnNames(ResultSet resultSet) throws SQLException {
		ResultSetMetaData metaData = resultSet.getMetaData();
		int count = metaData.getColumnCount(); //number of column
		String[] columnNames = new String[count];
		for (int i = 1; i <= count; i++){
			columnNames[i-1] = metaData.getColumnLabel(i);
		}
		return columnNames;
	}
//	public static String [] getColumnNames(Connection conn, String tableName) throws SQLException {
//		ResultSetMetaData metaData = resultSet.getMetaData();
//		int count = metaData.getColumnCount(); //number of column
//		String[] columnNames = new String[count];
//		for (int i = 1; i <= count; i++){
//			columnNames[i-1] = metaData.getColumnLabel(i);
//		}
//		return columnNames;
//		return null;
//	}


}