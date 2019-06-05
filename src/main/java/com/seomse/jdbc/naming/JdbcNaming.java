

package com.seomse.jdbc.naming;

import com.seomse.commons.packages.classes.field.FieldUtil;
import com.seomse.commons.utils.ExceptionUtil;
import com.seomse.commons.utils.sort.QuickSortList;
import com.seomse.jdbc.Database;
import com.seomse.jdbc.JdbcClose;
import com.seomse.jdbc.annotation.DateTime;
import com.seomse.jdbc.annotation.PrimaryKey;
import com.seomse.jdbc.annotation.Sequence;
import com.seomse.jdbc.annotation.Table;
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
 *  버    전 : 1.3
 *  수정이력 :  2017.11, 2018.04, 2018.07
 *  기타사항 :
 * </pre>
 * @author Copyrights 2017 ~ 2018 by ㈜섬세한사람들. All right reserved.
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
	 * @return jdbc객체리스트
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
	 * @return jdbc객체리스트
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
			return getObjList(ApplicationConnectionPool.getInstance().getConnection(), objClass, null, null, null, -1, null);
		}catch(Exception e){
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * 객체결과 리스트 얻기
	 * @param objClass 객체 클래스
	 * @param whereValue 조건문
	 * @return jdbc객체리스트
	 */
	public static <T> List<T> getObjList(Class<T> objClass , String whereValue){
		try {
			return getObjList(ApplicationConnectionPool.getInstance().getConnection(), objClass, null, whereValue, null, -1, null);
		}catch(Exception e){
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * 객체결과 리스트 얻기
	 * @param objClass 객체 클래스
	 * @param whereValue 조건문
	 * @param size 건수제한
	 * @return jdbc객체리스트
	 */
	public static <T> List<T> getObjList(Class<T> objClass , String whereValue, int size){
		try {
			return getObjList(ApplicationConnectionPool.getInstance().getConnection(),  objClass, null, whereValue, null, size, null);
		}catch(Exception e){
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * 객체결과 리스트 얻기
	 * @param objClass 객체 클래스
	 * @param whereValue 조건문
	 * @param orderByValue 정렬문
	 * @return jdbc객체리스트
	 */
	public static <T> List<T> getObjList(Class<T> objClass , String whereValue, String orderByValue){
		try {
			return getObjList(ApplicationConnectionPool.getInstance().getConnection(),  objClass, null, whereValue, orderByValue, -1, null);
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
			return getObjList(ApplicationConnectionPool.getInstance().getConnection(),  objClass, null, whereValue, orderByValue, size, null);
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
			return getObjList(ApplicationConnectionPool.getInstance().getConnection(),  objClass, null, whereValue, null, -1, prepareStatementDataMap);
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
			return getObjList(ApplicationConnectionPool.getInstance().getConnection(),  objClass, sql, whereValue, null, -1, prepareStatementDataMap);
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
			return getObjList(ApplicationConnectionPool.getInstance().getConnection(),  objClass, sql, null, null, -1, null);
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

		Field [] fields = FieldUtil.getFieldArrayToParents(objClass);
		String makeSql;
		if(sql == null) {
			makeSql = getSql(objClass, table, fields, whereValue, orderByValue);
		}else{
			makeSql = sql;
		}
		
		boolean isPrepareStatement = false;

		if(prepareStatementDataMap != null){
			isPrepareStatement = true;					
		}
	
		Statement stmt = null;
		ResultSet result = null;
		
		
		if(size == -1) {
			size = table.size();	
		}

		//noinspection CaughtExceptionImmediatelyRethrown
		try{
			if(isPrepareStatement){
				PreparedStatement pstmt = conn.prepareStatement(makeSql);
				stmt = pstmt;

				setStmt(pstmt,prepareStatementDataMap);
				result = pstmt.executeQuery();
			}else{
				stmt = conn.createStatement();	
				result = stmt.executeQuery(makeSql);
			}
			
			int fetchSize = table.fetchSize(); 
			if(fetchSize > 0){
				stmt.setFetchSize(table.fetchSize());
				result.setFetchSize(table.fetchSize());
			}
			if(size == -1){
				while(result.next()){
					//noinspection deprecation
					T resultObj = objClass.newInstance();
				
					setFieldsValue(result, fields, resultObj);
					
					resultList.add(resultObj);
				}	
			}else{
				int checkCount = 0;
				while(result.next()){

					//noinspection deprecation
					T resultObj = objClass.newInstance();
				

					setFieldsValue(result, fields, resultObj);
						
						
					
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

		if( whereValue != null){
			sqlBuilder.append(" WHERE ").append(whereValue);
		}else{
			String where = table.where();
			if(!where.equals(Table.EMPTY)){
				sqlBuilder.append(" WHERE ").append(where);
			}
		}

		if(orderByValue != null) {
			sqlBuilder.append(" ORDER BY ").append(orderByValue);
		}else {
			String orderBy = table.orderBy();
			if(!orderBy.equals(Table.EMPTY)){
				sqlBuilder.append(" ORDER BY ").append(orderBy);
			}
		}
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
			return getObj(ApplicationConnectionPool.getInstance().getConnection(), objClass, null, null, null, null);
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
			return getObj(ApplicationConnectionPool.getInstance().getConnection(), objClass, null, whereValue, null, null);
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
			return getObj(ApplicationConnectionPool.getInstance().getConnection(), objClass, null, whereValue, orderByValue, null);
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
			return getObj(ApplicationConnectionPool.getInstance().getConnection(), objClass, null, whereValue, null, prepareStatementDataMap);
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
			return getObj(ApplicationConnectionPool.getInstance().getConnection(), objClass, sql, whereValue, null, prepareStatementDataMap);
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
			return getObj(ApplicationConnectionPool.getInstance().getConnection(),  objClass, sql, null, null, null);
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
			
			if(prepareStatementDataMap != null){
				PreparedStatement pstmt = conn.prepareStatement(makeSql);
				stmt = pstmt;

				setStmt(pstmt,prepareStatementDataMap);
				result = pstmt.executeQuery();				
			}else{
				stmt = conn.createStatement();	
				result = stmt.executeQuery(makeSql);
			}
			result.setFetchSize(2);
			if(result.next()){
				//noinspection deprecation
				resultObj = objClass.newInstance();

				setFieldsValue(result, fields, resultObj);
			}	
		}catch(Exception e){
			throw e;
		}finally{
			JdbcClose.statementResultSet(stmt, result);
		}
		
		return resultObj;	
	}
	
	
	/**
	 * 필드에 값 설정
	 * @param result ResultSet
	 * @param fields Field []
	 * @param resultObj Object
	 */
	private static void setFieldsValue(ResultSet result, Field [] fields, Object resultObj ) throws IllegalArgumentException, IllegalAccessException, SQLException{
		
		for(Field field : fields){
			field.setAccessible(true);
		
			
			DateTime dateTime =  field.getAnnotation(DateTime.class);
			if(dateTime == null){
				Class<?> classType  = field.getType();
				if(classType == String.class){
					field.set(resultObj, result.getString(field.getName()));
				}else if(classType == Long.class){	
					try{
						long value =  result.getLong(field.getName());
						if(result.wasNull()){
							field.set(resultObj, null);
						}else{
							field.set(resultObj, value);
						}
						
					}catch(Exception e){
						String value =  result.getString(field.getName());
						if(result.wasNull()){
							field.set(resultObj, null);
						}else{
							field.set(resultObj, Long.parseLong(value));
						}
					}
				
				}else if(classType == Integer.class){	
				
					try{
						int value =  result.getInt(field.getName());
						if(result.wasNull()){
							field.set(resultObj, null);
						}else{
							field.set(resultObj, value);
						}
						
					}catch(Exception e){
						String value =  result.getString(field.getName());
						if(result.wasNull()){
							field.set(resultObj, null);
						}else{
							field.set(resultObj, Integer.parseInt(value));
						}
					}
						
					
				}else if(classType == Float.class){
					
					try{
						float value =  result.getFloat(field.getName());
						if(result.wasNull()){
							field.set(resultObj, null);
						}else{
							field.set(resultObj, value);
						}
						
					}catch(Exception e){
						String value =  result.getString(field.getName());
						if(result.wasNull()){
							field.set(resultObj, null);
						}else{
							field.set(resultObj, Float.parseFloat(value));
						}
					}
					
				}else if(classType == Double.class){				
					
					try{
						double value =  result.getDouble(field.getName());
						if(result.wasNull()){
							field.set(resultObj, null);
						}else{
							field.set(resultObj, value);
						}
						
					}catch(Exception e){
						String value =  result.getString(field.getName());
						if(result.wasNull()){
							field.set(resultObj, null);
						}else{
							field.set(resultObj, Double.parseDouble(value));
						}
					}
				}
			}else{
				
				try{
					Timestamp timeStamp = result.getTimestamp(field.getName());
					if(timeStamp == null){
						field.set(resultObj, null);
					}else{
						field.set(resultObj, timeStamp.getTime());						
					}
				}catch(Exception e){
					Object value = result.getObject(field.getName());
					if( value == null){
						field.set(resultObj, null);
					}else{
						if(value.getClass() == Long.class){
							field.set(resultObj, value);			
						}else{
							field.set(resultObj, Long.parseLong(value.toString()));
						}
						
					}
					
					
				}
			}
			
		}
			

	}
	
	
	/**
	 * obj를 이용한 데이터 upsert
	 * @param objClassList 객체리시트
	 * @return fail -1
	 */
	public static <T> int upsert( List<T> objClassList){
		try {
			return insert(ApplicationConnectionPool.getInstance().getConnection(), objClassList, "UPSERT", true);
		}catch(Exception e){
			logger.error(ExceptionUtil.getStackTrace(e));
			return -1;

		}
	}
	

	/**
	 * obj를 이용한 데이터 upsert
	 * @param objClassList 객체리시트
	 * @param isClearParameters ClearParameters 여부
	 * @return fail -1
	 */
	public static <T> int upsert( List<T> objClassList,   boolean isClearParameters){
		try {
			return insert(ApplicationConnectionPool.getInstance().getConnection(), objClassList, "UPSERT", isClearParameters);
		}catch(Exception e){
			logger.error(ExceptionUtil.getStackTrace(e));
			return -1;

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
		try {
			return insert(ApplicationConnectionPool.getInstance().getConnection(), objClassList, "INSERT", true);
		}catch(Exception e){
			logger.error(ExceptionUtil.getStackTrace(e));
			return -1;
		}
	}
	

	/**
	 * obj를 이용한 데이터 insert
	 * @param objClassList 객체리시트
	 * @param isClearParameters ClearParameters 여부
	 * @return fail -1
	 */
	public static <T> int insert( List<T> objClassList,   boolean isClearParameters){
		try {
			return insert(ApplicationConnectionPool.getInstance().getConnection(), objClassList , "INSERT", isClearParameters);
		}catch(Exception e){
			logger.error(ExceptionUtil.getStackTrace(e));
			return -1;
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
		
		Table table = objClass.getAnnotation(Table.class);
		
		String tableName = table.name();
		if(tableName.equals(Table.EMPTY)){
			throw new TableNameEmptyException(objClass.getName());
		}
		
		Field [] fields = FieldUtil.getFieldArrayToParents(objClass);
		
		if(fields == null || fields.length ==0){
			throw new FieldNullException(objClass.getName());
		}
		
		int successCount = -1;
		
		StringBuilder sqlBuilder = new StringBuilder();
	
		sqlBuilder.append(insertQueryValue + " INTO " + tableName +" (");
		
		StringBuilder fieldBuilder = new StringBuilder();
				
		for(int i=0 ; i<fields.length ; i++){
			fieldBuilder.append(", " + fields[i].getName());
		}
		sqlBuilder.append(fieldBuilder.toString().substring(1));		
		sqlBuilder.append(") VALUES (");
		
		
		fieldBuilder.setLength(0);
		for(int i=0 ; i<fields.length ; i++){
			fieldBuilder.append(", ?");
		}
		sqlBuilder.append(fieldBuilder.toString().substring(1));	
		sqlBuilder.append(" )");
		
		
		PreparedStatement pstmt = null;
		
		try{
			pstmt = conn.prepareStatement(sqlBuilder.toString());
			
			for(T vo : objClassList){
				
				for(int i=0 ; i<fields.length ; i++){
					fields[i].setAccessible(true);
					Object object = fields[i].get(vo);
					
					if(object == null){
						
						Sequence sequence = fields[i].getAnnotation(Sequence.class);
						if(sequence == null){
							pstmt.setNull(i+1,  java.sql.Types.NULL);				
						}else{
							
							String nextVal = Database.nextVal(sequence.name());
							nextVal =  sequence.preFix() + nextVal;
							
							fields[i].set(vo, nextVal);
							pstmt.setString(i+1, nextVal);
							
						}
	
					}else{
						DateTime dateColumn =  fields[i].getAnnotation(DateTime.class);
						
						if(dateColumn == null){
//							String value = (String)object;
//							pstmt.setString(i+1, value);	
							
							
							
							if(object.getClass() == String.class){
								pstmt.setString(i+1, (String)object);	
							}else if(object.getClass() == Long.class){
								pstmt.setLong(i+1, (Long)object);	
							}else if(object.getClass() == Integer.class){
								pstmt.setInt(i+1, (Integer)object);	
							}else if(object.getClass() == Float.class){
								pstmt.setFloat(i+1, (Float)object);	
							}else if(object.getClass() == Double.class){
								pstmt.setDouble(i+1, (Double)object);	
							}
							
						}else{
							Long value = (Long)object;
						
							Timestamp timeStamp = new Timestamp(value);
							pstmt.setTimestamp(i+1, timeStamp);
						}
					}	
						
				}
				
				pstmt.addBatch();
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
			logger.error(sqlBuilder.toString());
			logger.error(ExceptionUtil.getStackTrace(e));
		}finally{

			//noinspection CatchMayIgnoreException
			try{pstmt.close(); }catch(Exception e){}
		} 
				
		return successCount;
	}
	

	/**
	 * 있으면 업데이트 없으면 추가
	 * @param obj
	 * @param isNullUpdate null column null update flag
	 * @return fail -1
	 */
	public static <T> int insertOrUpdate(T obj, boolean isNullUpdate){

		try{
			return insertOrUpdate(ApplicationConnectionPool.getInstance().getConnection(), obj, isNullUpdate);
		}catch(Exception e){
			logger.error(ExceptionUtil.getStackTrace(e));
			return -1;
		}

	}
	
	
	/**
	 * 있으면 업데이트 없으면 추가
	 * @param conn
	 * @param obj
	 * @param isNullUpdate
	 * @return 실패 -1
	 */
	public static <T> int insertOrUpdate(Connection conn, Object obj , boolean isNullUpdate ){
			
		Class<?> objClass =  obj.getClass();
		Table table = objClass.getAnnotation(Table.class);
		
		String tableName = table.name();
		if(tableName.equals(Table.EMPTY)){
			throw new TableNameEmptyException(objClass.getName());
		}
		
		Field [] fields = FieldUtil.getFieldArrayToParents(objClass);
		
		List<Field> pkColumnList = new LinkedList<Field>();
				
		if(fields == null || fields.length ==0){
			throw new FieldNullException(objClass.getName());
		}
		
		int successCount = -1;
		
			
		
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
	
		}
		
		if(pkColumnList.size() ==0){
			throw new PrimaryKeyNotSetException();
		}
		
		
		int [] seqArray = new int[pkColumnList.size()];
		for(int i=0; i<seqArray.length ;i++){
				
			Field field = pkColumnList.get(i);
			field.setAccessible(true);
				
			seqArray[i] = field.getAnnotation(PrimaryKey.class).seq();		
		}
		
		if(seqArray.length> 1){
			QuickSortList sortList = new QuickSortList(pkColumnList);
			sortList.sortAsc(seqArray);
//			new QuickSortList(pkColumnList , seqArray, true);		
		}
		
		try{

			StringBuilder whereBuilder = new StringBuilder();
			for(int i= 0 ; i < seqArray.length ; i++){
				Field field = pkColumnList.get(i);
				field.setAccessible(true);
				whereBuilder.append(" AND " + field.getName() +"='"  +field.get(obj) +"'"  );
			}
			
			Object checkVo = getObj(conn, obj.getClass(), null, whereBuilder.substring(4), null, null);
			if(checkVo == null){
				successCount =insert(conn, obj);
			}else{
				successCount = update(conn, obj, isNullUpdate); 
			}
		}
		catch(Exception e){
			logger.error(ExceptionUtil.getStackTrace(e));
		}
				
		return successCount;
		
	}
	
	
	/**
	 * 객체를이용한 자동 upsert 
	 * @param conn
	 * @param obj
	 * @return fail -1
	 */
	public static <T> int upsert(Connection conn, T obj){
		return insert(conn, obj, "UPSERT");
	}

	/**
	 * 객체를이용한 자동 upsert 
	 * @param obj
	 * @return success 1, fail -1
	 */
	public static <T> int upsert(T obj){
		try {
			return insert(ApplicationConnectionPool.getInstance().getConnection(), obj, "UPSERT");
		}catch(Exception e){
			logger.error(ExceptionUtil.getStackTrace(e));
			return -1;
		}
	}

	
	/**
	 * 객체를이용한 자동 insert 
	 * @param obj
	 * @return success 1, fail -1
	 */
	public static <T> int insert(T obj){
		try {
			return insert(ApplicationConnectionPool.getInstance().getConnection(), obj, "INSERT");
		}catch(Exception e){
			logger.error(ExceptionUtil.getStackTrace(e));
			return -1;
		}
	}
	
	/**
	 * 객체를이용한 자동 insert 
	 * @param conn
	 * @param obj
	 * @return success 1, fail -1
	 */
	public static <T> int insert(Connection conn, T obj){
		return insert(conn, obj, "INSERT");
	}
	
	/**
	 * 객체를이용한 자동 insert 
	 * @param conn
	 * @param obj
	 * @return success 1, fail -1
	 */
	public static <T> int insert(Connection conn, T obj, String insertQueryValue){
		
		Class<?> objClass = obj.getClass();
		
		
		Table table = objClass.getAnnotation(Table.class);
		
		String tableName = table.name();
		if(tableName.equals(Table.EMPTY)){
			throw new TableNameEmptyException(objClass.getName());
		}
		
		Field [] fields = FieldUtil.getFieldArrayToParents(objClass);
		
		if(fields == null || fields.length ==0){
			throw new FieldNullException(objClass.getName());
		}
		
		int successCount = -1;
		
		StringBuilder sqlBuilder = new StringBuilder();
	
		sqlBuilder.append(insertQueryValue + " INTO " + tableName +" (");
		
		StringBuilder fieldBuilder = new StringBuilder();
		
		for(int i=0 ; i<fields.length ; i++){
			
			fieldBuilder.append(", " + fields[i].getName());
		}
		sqlBuilder.append(fieldBuilder.toString().substring(1));		
		sqlBuilder.append(") VALUES (");
		
		
		fieldBuilder.setLength(0);
		for(int i=0 ; i<fields.length ; i++){
			fieldBuilder.append(", ?");
		}
		sqlBuilder.append(fieldBuilder.toString().substring(1));	
		sqlBuilder.append(" )");
		
		
		PreparedStatement pstmt = null;
		
		try{
			pstmt = conn.prepareStatement(sqlBuilder.toString());
				
			for(int i=0 ; i<fields.length ; i++){
				fields[i].setAccessible(true);
				Object object = fields[i].get(obj);
				if(object == null){
					
					Sequence sequence = fields[i].getAnnotation(Sequence.class);
					if(sequence == null){
						pstmt.setNull(i+1,  java.sql.Types.NULL);				
					}else{
						
						String nextVal = Database.nextVal(sequence.name());
					
						nextVal =  sequence.preFix() + nextVal;
						
						fields[i].set(obj, nextVal);
						pstmt.setString(i+1, nextVal);
						
					}

				}else{
					
					
					DateTime dateTime =  fields[i].getAnnotation(DateTime.class);
					if(dateTime == null){
//						String value = (String)object;
//						pstmt.setString(i+1, value);	
						
						if(object.getClass() == String.class){
							pstmt.setString(i+1, (String)object);	
						}else if(object.getClass() == Long.class){
							pstmt.setLong(i+1, (Long)object);	
						}else if(object.getClass() == Integer.class){
							pstmt.setInt(i+1, (Integer)object);	
						}else if(object.getClass() == Float.class){
							pstmt.setFloat(i+1, (Float)object);	
						}else if(object.getClass() == Double.class){
							pstmt.setDouble(i+1, (Double)object);	
						}
						
					}else{
						Long value = (Long)object;
					
						Timestamp timeStamp = new Timestamp(value);
						pstmt.setTimestamp(i+1, timeStamp);
					}
					
				}	
						
			}
				
			pstmt.addBatch();
			pstmt.clearParameters();	
			
			pstmt.executeBatch();
			successCount = 1;
		}catch(Exception e){
			logger.error(ExceptionUtil.getStackTrace(e));
		}finally{
			try{pstmt.close(); pstmt = null; }catch(Exception e){}
		} 
				
		return successCount;
	}
	
	
	
	/**
	 * 객체를 이용한 update
	 * @param conn
	 * @param obj
	 * @param isNullUpdate
	 * @return  success 1, fail -1
	 */
	public static <T> int update(Connection conn,T obj , boolean isNullUpdate ){
			
		Class<?> objClass = obj.getClass();
		Table table = objClass.getAnnotation(Table.class);
		
		String tableName = table.name();
		if(tableName.equals(Table.EMPTY)){
			throw new TableNameEmptyException(objClass.getName());
		}
		
		Field [] fields = FieldUtil.getFieldArrayToParents(objClass);
		
		List<Field> pkColumnList = new LinkedList<Field>();
		
		
		
		if(fields == null || fields.length ==0){
			throw new FieldNullException(objClass.getName());
		}
		
		int successCount = -1;
		
		StringBuilder sqlBuilder = new StringBuilder();
	
		sqlBuilder.append("UPDATE " +tableName + " SET "  );
		
		StringBuilder fieldBuilder = new StringBuilder();
		
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
	
			fieldBuilder.append(", " + fields[i].getName() + "=?");
		}
		
		if(pkColumnList.size() ==0){
			throw new PrimaryKeyNotSetException();
		}
		int [] seqArray = new int[pkColumnList.size()];
		for(int i=0; i<seqArray.length ;i++){
			
			Field field = pkColumnList.get(i);
			field.setAccessible(true);
			
			seqArray[i] = field.getAnnotation(PrimaryKey.class).seq();		
		}
		
	
		
		sqlBuilder.append(fieldBuilder.toString().substring(1));		
		sqlBuilder.append(" WHERE ");
		
		fieldBuilder.setLength(0);
		int pkColumnSize =pkColumnList.size();
		
		if(pkColumnSize> 1){
			QuickSortList sortList = new QuickSortList(pkColumnList);
			sortList.sortAsc(seqArray);
		}
		
		for(int i= 0 ; i < seqArray.length ; i++){
			Field field = pkColumnList.get(i);
			fieldBuilder.append(" AND " + field.getName() +"=?" );
		}
		sqlBuilder.append(fieldBuilder.substring(4));
		PreparedStatement pstmt = null;
		
		try{
			pstmt = conn.prepareStatement(sqlBuilder.toString());
				
			int index = 0;
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
					
				
					pstmt.setNull(index+1,  java.sql.Types.NULL);				
					

				}else{
					
					
					DateTime dateTime =  fields[i].getAnnotation(DateTime.class);
					if(dateTime == null){
//						String value = (String)object;
//						pstmt.setString(i+1, value);	
						
						if(object.getClass() == String.class){
							pstmt.setString(index+1, (String)object);	
						}else if(object.getClass() == Long.class){
							pstmt.setLong(index+1, (Long)object);	
						}else if(object.getClass() == Integer.class){
							pstmt.setInt(index+1, (Integer)object);	
						}else if(object.getClass() == Float.class){
							pstmt.setFloat(index+1, (Float)object);	
						}else if(object.getClass() == Double.class){
							pstmt.setDouble(index+1, (Double)object);	
						}
						
					}else{
						Long value = (Long)object;
					
						Timestamp timeStamp = new Timestamp(value);
						pstmt.setTimestamp(index+1, timeStamp);
					}
					
				}	
				index++;
						
			}
				
			for(int i= 0 ; i < seqArray.length ; i++){
				Field field = pkColumnList.get(i);
				Object object = field.get(obj);
				DateTime dateTime =  field.getAnnotation(DateTime.class);
				if(dateTime == null){
//					String value = (String)object;
//					pstmt.setString(i+1, value);	
					
					if(object.getClass() == String.class){
						pstmt.setString(index+1, (String)object);	
					}else if(object.getClass() == Long.class){
						pstmt.setLong(index+1, (Long)object);	
					}else if(object.getClass() == Integer.class){
						pstmt.setInt(index+1, (Integer)object);	
					}else if(object.getClass() == Float.class){
						pstmt.setFloat(index+1, (Float)object);	
					}else if(object.getClass() == Double.class){
						pstmt.setDouble(index+1, (Double)object);	
					}
					
				}else{
					Long value = (Long)object;
				
					Timestamp timeStamp = new Timestamp(value);
					pstmt.setTimestamp(index+1, timeStamp);
				}
				index++;
			}
			
			pstmt.addBatch();
			pstmt.clearParameters();

			pstmt.executeBatch();
			successCount = 1;
		}catch(Exception e){
			logger.error(ExceptionUtil.getStackTrace(e));
		}finally{
			try{pstmt.close(); pstmt = null; }catch(Exception e){}
		} 
				
		return successCount;
	}

	
	/**
	 * 클래스 맴버변수 생성
	 * @param tableName
	 * @return 변수생성값
	 */
	public static String makeObjectValue( String tableName) throws SQLException {
		return makeObjectValue(ApplicationConnectionPool.getInstance().getConnection(), tableName);
	}
	
	/**
	 * 클래스 맴버변수 생성
	 * @param conn
	 * @param tableName
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
			result = stmt.executeQuery("SELECT * FROM " + tableName);
			ResultSetMetaData metaData = result.getMetaData();
			int count = metaData.getColumnCount(); //number of column
		
			for (int i = 1; i <= count; i++){
		
				String columnName = metaData.getColumnLabel(i);
				
				Integer pkSeq = pkMap.get(columnName);
				if(pkSeq != null){
					fieldBuilder.append("@PrimaryKey(seq = " +pkSeq +")\n");
				}
				
				
				JdbcDataType dataType = jdbcNamingDataType.getType(columnName);
				
				if(dataType == JdbcDataType.STRING){
					
					
					fieldBuilder.append("private String " + columnName ); 	
				
					String defaultValue = defaultValueMap.get(columnName);
					if(defaultValue != null){
						fieldBuilder.append( " = " +defaultValue.replace("'","\"") ); 	
					}
					fieldBuilder.append(";\n"); 	
					
					
				}else if(dataType == JdbcDataType.DATE_TIME ){
					fieldBuilder.append("@DateTime\nprivate Long " +  columnName ); 
					
					
					String defaultValue = defaultValueMap.get(columnName);
					if(defaultValue != null){
						if(defaultValue.replace("'", "").toUpperCase().trim().equals("SYSDATE")){
							fieldBuilder.append( " = System.currentTimeMillis()" );
						}
					}
					fieldBuilder.append(";\n"); 	
					
				}else if(dataType == JdbcDataType.LONG ){
					fieldBuilder.append("private Long " +  columnName ); 
					
					String defaultValue = defaultValueMap.get(columnName);
					if(defaultValue != null){
						defaultValue = defaultValue.replace("'", "").trim();
						fieldBuilder.append( " = " + defaultValue);
						
					}
					fieldBuilder.append(";\n"); 	
					
				}else if(dataType == JdbcDataType.DOUBLE ){					
					fieldBuilder.append("private Double " +  columnName); 
					
					String defaultValue = defaultValueMap.get(columnName);
					if(defaultValue != null){
						defaultValue = defaultValue.replace("'", "").trim();
						fieldBuilder.append( " = " + defaultValue);
						if( defaultValue.indexOf(".") == -1){
							fieldBuilder.append(".0");
						}
						
						
					}
					fieldBuilder.append(";\n"); 	
					
				}else if(dataType == JdbcDataType.INTEGER 
						){
					fieldBuilder.append("private Integer " +  columnName ); 
					
					String defaultValue = defaultValueMap.get(columnName);
					if(defaultValue != null){
						defaultValue = defaultValue.replace("'", "").trim();
						fieldBuilder.append( " = " + defaultValue);
						
					}
					fieldBuilder.append(";\n"); 	
					
				}
				

			
			}
		
		}catch(Exception e){
			logger.error(ExceptionUtil.getStackTrace(e));
		}finally{
			try{if(result!=null)result.close(); result=null; }catch(Exception e){}
			try{if(stmt!=null)stmt.close(); stmt=null; }catch(Exception e){}
		}
		return fieldBuilder.toString();
	}
	
	/**
	 * 데이터가 없을경우에만 insert
	 * @param obj
	 * @return success insert count, fail -1
	 */
	public static <T> int insertIfNoData(T obj){
		try {
			return insertIfNoData(ApplicationConnectionPool.getInstance().getConnection(), obj);
		}catch (Exception e){
			logger.error(ExceptionUtil.getStackTrace(e));
			return -1;
		}
	}
	/**
	 * 데이터가 없을경우에만 insert
	 * @param conn
	 * @param obj
	 * @return success insert count, fail -1
	 */
	public static <T> int insertIfNoData(Connection conn,T obj){
		Class<?> objClass = obj.getClass();
		Table table = objClass.getAnnotation(Table.class);
		
		String tableName = table.name();
		if(tableName.equals(Table.EMPTY)){
			throw new TableNameEmptyException(objClass.getName());
		}
		
		Field [] fields = FieldUtil.getFieldArrayToParents(objClass);
		
		List<Field> pkColumnList = new LinkedList<Field>();
				
		if(fields == null || fields.length ==0){
			throw new FieldNullException(objClass.getName());
		}
		
		int successCount = -1;
		
			
		
		for(int i=0 ; i<fields.length ; i++){
			fields[i].setAccessible(true);
			PrimaryKey  pk  = fields[i].getAnnotation(PrimaryKey.class);
			if(pk != null){
				pkColumnList.add(fields[i]);
				continue;
			}
		}
		
		if(pkColumnList.size() ==0){
			throw new PrimaryKeyNotSetException();
		}
		
		
		int [] seqArray = new int[pkColumnList.size()];
		for(int i=0; i<seqArray.length ;i++){
				
			Field field = pkColumnList.get(i);
			field.setAccessible(true);
				
			seqArray[i] = field.getAnnotation(PrimaryKey.class).seq();		
		}
			
		
		if(seqArray.length> 1){
			new QuickSortList(pkColumnList , seqArray, true);		
		}
		
		try{

			StringBuilder whereBuilder = new StringBuilder();
			for(int i= 0 ; i < seqArray.length ; i++){
				Field field = pkColumnList.get(i);
				field.setAccessible(true);
				whereBuilder.append(" AND " + field.getName() +"='"  +field.get(obj) +"'"  );
			}
			
			
			Object checkVo = getObj(conn, obj.getClass(), whereBuilder.substring(4));
			if(checkVo == null){
				successCount =insert(conn, obj);
			}
		}
		catch(Exception e){
			logger.error(ExceptionUtil.getStackTrace(e));
		}
				
		return successCount;
	}
}
