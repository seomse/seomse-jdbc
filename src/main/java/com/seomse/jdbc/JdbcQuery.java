

package com.seomse.jdbc;

import com.seomse.commons.utils.ExceptionUtil;
import com.seomse.jdbc.common.JdbcClose;
import com.seomse.jdbc.connection.ApplicationConnectionPool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/** 
 * <pre>
 *  파 일 명 : JdbcQuery.java
 *  설    명 : jdbc 쿼리용 클래스
 *         
 *  작 성 자 : macle
 *  작 성 일 : 2017.09
 *  버    전 : 1.2
 *  수정이력 : 2019.02, 2019.06
 *  기타사항 :
 * </pre>
 * @author Copyrights 2017 ~ 2019 by ㈜섬세한사람들. All right reserved.
 */
public class JdbcQuery {
	private static final Logger logger = LoggerFactory.getLogger(JdbcQuery.class);
	
	
	
	/**
	 * 단일결과를 DateTime 의 long 값얻기
	 * Date로 변환방법 new Date(result);
	 * @return DateTime
	 */
	public static Long getResultDateTime(String sql, Long defaultValue) {


		try(Connection conn = ApplicationConnectionPool.getInstance().getCommitConnection()){
			Long result = getResultDateTime(conn, sql);
			if(result == null){
				return defaultValue;
			}

			return result;
		}catch(Exception e){
			logger.error(ExceptionUtil.getStackTrace(e));
			return defaultValue;
		}
	}
	
	

	/**
	 * 단일결과를 DateTime의 long값얻기
	 * Date로 변환방법 new Date(result);
	 * @return DateTime
	 */
	public static Long getResultDateTime(String sql) {

		try(Connection conn = ApplicationConnectionPool.getInstance().getCommitConnection()){
			return getResultDateTime(conn, sql);
		}catch(Exception e){
			throw new RuntimeException(e);
		}

	}
	

	/**
	 * 단일결과를 DateTime의 long값얻기
	 * Date로 변환방법 new Date(result);
	 * @return DateTime
	 */
	public static Long getResultDateTime(Connection conn, String sql) throws SQLException {
		Long resultTime = null;
		
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
					Timestamp timeStamp = result.getTimestamp(columnName);
					resultTime = timeStamp.getTime();			
				}
			}
		}catch(Exception e){
			throw e;
		}finally{
			JdbcClose.statementResultSet(stmt, result);
		}
		
		return resultTime;
	}
	
	
	/**
	 * 단일결과를 Integer형태로 옫기
	 * @return result(Integer)
	 */
	public static Integer getResultInteger(String sql, Integer defaultValue) {

		Integer result = getResultInteger( sql);
		if(result == null){
			return defaultValue;
		}
		
		return 	result;
	}
	
	/**
	 * 단일결과를 Integer 형태로 얻기
	 * @return result(Integer)
	 */
	public static Integer getResultInteger(String sql) {

		try(Connection conn = ApplicationConnectionPool.getInstance().getCommitConnection()){
			String result = getResultOne(conn, sql);
			if (result == null) {
				return null;
			}

			return Integer.parseInt(result);
		}catch(Exception e){
			logger.error(ExceptionUtil.getStackTrace(e));
			return null;
		}
	}
	
	
	/**
	 * 단일결과를 Integer 형태로 얻기
	 * @param conn Connection
	 * @param sql sql
	 * @return result(Integer)
	 */
	public static Integer getResultInteger(Connection conn, String sql) throws SQLException {
		String result = getResultOne(conn, sql);
		if(result == null){
			return null;
		}
		return 	Integer.parseInt(result);
	 }


	/**
	 * 단일결과를 double 형태로 얻기
	 * @param sql sql
	 * @param defaultValue defaultValue
	 * @return result(Double)
	 */
	public static Double getResultDouble(String sql, Double defaultValue) {
		try(Connection conn = ApplicationConnectionPool.getInstance().getCommitConnection()){
			Double result = getResultDouble(conn, sql);
			if (result == null) {
				return defaultValue;
			}

			return result;
		}catch(Exception e){
			logger.error(ExceptionUtil.getStackTrace(e));
			return null;
		}
	}

	/**
	 * 단일결과를 double 형태로 얻기
	 * @param sql sql
	 * @return result(Double)
	 */
	public static Double getResultDouble(String sql) {
		try(Connection conn = ApplicationConnectionPool.getInstance().getCommitConnection()){
			return getResultDouble(conn, sql);
		}catch(Exception e){
			throw new RuntimeException(e);
		}
	}

	/**
	 * 단일결과를 double 형태로 얻기
	 * @param conn Connection
	 * @param sql sql
	 * @return result(Double)
	 */
	public static Double getResultDouble(Connection conn, String sql) throws SQLException {
		Double resultDouble = null;
		
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
				
					resultDouble = result.getDouble(columnName);			
				}
			}

		}catch(Exception e){
			throw e;
		}finally{
			JdbcClose.statementResultSet(stmt, result);
		}
		
		return resultDouble;
	}
	


	/**
	 *  단일결과를 Long형태로 얻기
	 * @param sql sql
	 * @param defaultValue defaultValue
	 * @return result(Long)
	 */
	public static Long getResultLong(String sql, Long defaultValue) {
		try(Connection conn = ApplicationConnectionPool.getInstance().getCommitConnection()){
			Long result = getResultLong(conn, sql);
			if (result == null) {
				return defaultValue;
			}

			return result;
		}catch(Exception e){
			logger.error(ExceptionUtil.getStackTrace(e));
			return defaultValue;
		}
	}
	

	/**
	 * 단일결과를 Long 형태로 얻기
	 * @param sql sql
	 * @return  result(Long)
	 */
	public static Long getResultLong(String sql) {
		try(Connection conn = ApplicationConnectionPool.getInstance().getCommitConnection()){
			return getResultLong(conn, sql);
		}catch(Exception e){
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * 단일결과를 Long형태로 얻기
	 * @return result(Long)
	 */
	public static Long getResultLong(Connection conn, String sql) throws SQLException {
		Long resultLong = null;
		
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
				
					resultLong = result.getLong(columnName);			
				}
			}
		}catch(Exception e){
			throw e;
		}finally{
			JdbcClose.statementResultSet(stmt, result);
		}
		
		return resultLong;
	}
	
	
	
	/**
	 * 단일결과 얻기
	 * @param sql sql
	 *  @param defaultValue 기본값
	 * @return 단일결과 값
	 */
	public static String getResultOne(String sql, String defaultValue){
		try(Connection conn = ApplicationConnectionPool.getInstance().getCommitConnection()){
			String result = getResultOne(conn, sql);
			if (result == null) {
				return defaultValue;
			}

			return result;
		}catch (Exception e){
			logger.error(ExceptionUtil.getStackTrace(e));
			return defaultValue;
		}
	}	
	
	/**
	 * 단일결과 얻기
	 * @param sql sql
	 * @return  단일결과 값
	 */
	public static String getResultOne(String sql){
		try(Connection conn = ApplicationConnectionPool.getInstance().getCommitConnection()){
			return getResultOne(conn, sql);
		}catch(Exception e){
			throw new RuntimeException(e);
		}
	}
	/**
	 * 단일결과 얻기
	 * @param conn Connection
	 * @param sql sql
	 * @return 단일결과 값
	 */
	public static String getResultOne(Connection conn, String sql) throws SQLException {
		String resultValue = null;
		
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
				
					resultValue = result.getString(columnName);			
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
	 * 단일컬럼 결과 리스트 얻기
	 * @param sql sql
	 * @return 단일컬럼 결과 리스트
	 */
	public static List<String> getStringList(String sql){
		try(Connection conn = ApplicationConnectionPool.getInstance().getCommitConnection()){
			return getStringList(conn, sql);
		}catch(Exception e){
			throw new RuntimeException(e);
		}
	}
	
	
	/**
	 * 단일컬럼 결과 리스트
	 * @param conn Connection
	 * @param sql Query
	 * @return 단일컬럼 결과 리스트
	 */
	public static List<String> getStringList(Connection conn, String sql) throws SQLException {
		List<String> resultList = new ArrayList<>();
	
		Statement stmt = null;
		ResultSet result = null;
		try{
			stmt = conn.createStatement();
			result = stmt.executeQuery(sql);
			ResultSetMetaData metaData = result.getMetaData();
			String columnName = metaData.getColumnLabel(1); 	
			while(result.next()){
				resultList.add(result.getString(columnName));
			}
		}catch(Exception e){
			resultList.clear();
			throw e;
		}finally{
			JdbcClose.statementResultSet(stmt, result);
		}
		
		return resultList;
	}

	/**
	 * 지정 된 테이블의 모든컬럼정보를 String 형태로 얻기
	 * Map<String, String> 컬럼, 값
	 * @param tableName tableName
	 * @return MapStringList
	 */
	public static List<Map<String, String>> getAllMapStringList(String tableName){
		try(Connection conn = ApplicationConnectionPool.getInstance().getCommitConnection()){
			return getAllMapStringList(conn, tableName);
		}catch(Exception e){
			throw new RuntimeException(e);
		}
	}

	/**
	 * 지정 된 테이블의 모든컬럼정보를 String 형태로 얻기
	 * Map<String, String> 컬럼, 값
	 * @param conn Connection
	 * @param tableName tableName
	 * @return MapStringList
	 */
	public static List<Map<String, String>> getAllMapStringList(Connection conn, String tableName) throws SQLException {
		String sql = "SELECT * FROM " + tableName;	
		return 	getMapStringList(conn, sql);
	}
	/**
	 * 데이터 베이스 결과를 List에 Map형태로 가져온다 키:컬럼명 내용: 벨류
	 * @param sql sql
	 * @return MapStringList
	 */
	public static List<Map<String, String>> getMapStringList(String sql){

		try(Connection conn = ApplicationConnectionPool.getInstance().getCommitConnection()){
			return getMapStringList(conn, sql);
		}catch(Exception e){
			throw new RuntimeException(e);
		}
	}
	
	
	
	/**
	 * 데이터 베이스 결과를 List 에 Map 형태로 가져온다 키:컬럼명 내용: 벨류
	 * @param conn Connection
	 * @param sql sql
	 * @return MapStringList
	 */
	public static List<Map<String, String>> getMapStringList(Connection conn, String sql) throws SQLException {
		
		List<Map<String, String>> resultMapList = new ArrayList<>();
		
		Statement stmt = null;
		ResultSet result = null;
		
		try{
			stmt = conn.createStatement();
			result = stmt.executeQuery(sql);

			String [] columnNames = getColumnNames(result);

			while(result.next()){
				Map<String, String> resultMap = new HashMap<>();
				for (String columnName : columnNames){
					resultMap.put(columnName, result.getString(columnName));
				}			
				resultMapList.add(resultMap);
			}
		}catch(Exception e){
			resultMapList.clear();
			throw e;
		}finally{
			JdbcClose.statementResultSet(stmt, result);
		}
		
		return resultMapList;
	}



	
	/**
	 * 단일결과를 Map<String, String>형태로 얻기
	 * @param sql sql
	 * @return MapString
	 */
	public static Map<String, String> getMapString( String sql){
		try(Connection conn = ApplicationConnectionPool.getInstance().getCommitConnection()){
			return getMapString(conn, sql);
		}catch(Exception e){
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * 단일결과를 Map<String, String>형태로 얻기
	 * @param conn Connection
	 * @param sql sql
	 * @return MapString
	 */
	public static Map<String, String> getMapString(Connection conn, String sql) throws SQLException {
		Map<String, String> resultMap = new HashMap<>();
		Statement stmt = null;
		ResultSet result = null;
		//noinspection CaughtExceptionImmediatelyRethrown
		try{
			stmt = conn.createStatement();
			result = stmt.executeQuery(sql);
			String [] columnNames = getColumnNames(result);
			if(result.next()){		
				for (String columnName : columnNames){
					resultMap.put(columnName, result.getString(columnName));
				}				
			}else{
				resultMap = null;
			}
		}catch(Exception e){
			throw e;
		}finally{
			JdbcClose.statementResultSet(stmt, result);

		}
		return resultMap;
	}


	public static String [] getColumnNames(ResultSet resultSet) throws SQLException {
		ResultSetMetaData metaData = resultSet.getMetaData();
		int count = metaData.getColumnCount(); //number of column
		String[] columnNames = new String[count];
		for (int i = 1; i <= count; i++){
			columnNames[i-1] = metaData.getColumnLabel(i);
		}
		return columnNames;
	}


	/**
	 * 쿼리에 해당하는 내용을 실행한다.
	 * @param sql 실행쿼리
	 * @return  실패-1
	 */
	public static int execute(String sql){
		ApplicationConnectionPool connectionPool = ApplicationConnectionPool.getInstance();
		try(Connection conn = connectionPool.getCommitConnection()){
			int result =  execute(conn, sql);

			if(!connectionPool.isAutoCommit()){
				conn.commit();
			}
			return result;
		}catch(Exception e){
			logger.error(ExceptionUtil.getStackTrace(e));
			return -1;
		}
	}
	

	
	/**
	 * 쿼리 실행
	 * @param conn DB연결 컨넥션 
	 * @param sql 실행쿼리
	 * @return  실패 -1
	 */
	public static int execute(Connection conn, String sql) throws SQLException {
		PreparedStatement pstmt = null;
		int count ;
		//noinspection CaughtExceptionImmediatelyRethrown
		try{
			pstmt = conn.prepareStatement(sql);			
			count = pstmt.executeUpdate();
			pstmt.close();
			
			pstmt = null;
		
		}catch(Exception e){
			throw e;
		}finally{

			if(pstmt!= null){
				//noinspection CatchMayIgnoreException
				try{pstmt.close(); }catch(Exception e){}
			}
		}
	
		return count;
	}
	
	/**
	 * 프로시져 호출
	 * @param sql 실행쿼리
	 * @return 성공 실패-1
	 */
	public static int callProcedure(String sql){
		ApplicationConnectionPool connectionPool = ApplicationConnectionPool.getInstance();
		try(Connection conn = connectionPool.getCommitConnection()){


			int result = callProcedure(conn, sql);
			if(!connectionPool.isAutoCommit()) {
				conn.commit();
			}
			return result;

		}catch(Exception e){
			logger.error(ExceptionUtil.getStackTrace(e));
			return -1;

		}
	}
	
	/**
	 * 프로시져 호출용
	 * @param conn DB연결 컨넥션 
	 * @param sql 실행쿼리
	 * @return 성공 1 실패 -1
	 */
	public static int callProcedure(Connection conn, String sql) throws SQLException {
		PreparedStatement pstmt = null;
		int count ;
		//noinspection CaughtExceptionImmediatelyRethrown
		try{
			pstmt = conn.prepareCall(sql);
			pstmt.execute();
			pstmt.close();
			pstmt = null;
			count = 1;
		}catch(Exception e){
			throw e;
		}finally{
			if(pstmt!= null){
				//noinspection CatchMayIgnoreException
				try{pstmt.close(); }catch(Exception e){}
			}
		}
		return count;
	}

	
	 /**
	  * RowData 존재여부
	  * @param conn Connection
	  * @param sql sql
	  * @return isRowData
	  */
	 public static boolean isRowData(Connection conn, String sql) throws SQLException {
		String result =  getResultOne(conn, sql);
		return result != null;
	 }
	
	 /**
	  * RowData 존재여부
	  * @param sql sql
	  * @return isRowData
	  */
	 public static boolean isRowData(String sql){
		String result =  getResultOne(sql);
		return result != null;
	 }

	 /**
	  * RowData가 등록인식 될떄까지 기다림
	  * @param sql 쿼리
	  */
	 public static void isRowWait(String sql) {
		 try(Connection conn = ApplicationConnectionPool.getInstance().getCommitConnection()){
			isRowWait(conn, sql, 3, 350);
		}catch(Exception e){
	 		logger.error(ExceptionUtil.getStackTrace(e));
		}
	 }
			 
	 /**
	  * RowData가 등록인식 될떄까지 기다림
	  * @param conn 연결정보
	  * @param sql 쿼리
	  * @param checkCount 최대반복횟수
	  * @param waitTime 기다리는 시간
	  */
	 public static void isRowWait(Connection conn, String sql, int checkCount, long waitTime) throws SQLException {
		 
		 for(int i=0 ; i<checkCount ; i++){
			 
			 if(isRowData(conn, sql)) {
				 break;
			 }
			 	
			 try {
				 Thread.sleep(waitTime);
			 }catch(Exception e) {
				 logger.error(ExceptionUtil.getStackTrace(e));
			 }
			 
		 }	 
	 }
}
