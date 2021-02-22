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

import com.seomse.jdbc.Database;
import com.seomse.jdbc.PrepareStatementData;
import com.seomse.jdbc.common.JdbcClose;
import com.seomse.jdbc.common.JdbcCommon;
import com.seomse.jdbc.connection.ApplicationConnectionPool;
import com.seomse.jdbc.exception.SQLRuntimeException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.*;

/**
 * naming domain header 를 이용 하여 
 * Map 를 row로 출력하여 사용함
 * db 의 데이터 이간에 활용 됨
 * @author macle
 */
public class JdbcNamingMap {

	private static final Logger logger = LoggerFactory.getLogger(JdbcNamingMap.class);

	public static final String TABLE_NAME = "TABLE_NAME^";

	/**
	 * table 의 모든 데이터 얻기
	 * @param tableName String table name
	 * @return List
	 */
	public static List<Map<String, Object>> getDataList(String tableName) {
		return 	getDataList(tableName, null, null );
	}

	/**
	 * Table 데이터 얻기
	 * @param tableName String table name
	 * @param whereValue String where query
	 * @param prepareStatementDataMap  Map 조건 데이터  date time 같이 database query 가 다른 경우
	 * @return List
	 */
	public static List<Map<String, Object>> getDataList( String tableName, String whereValue, Map<Integer, PrepareStatementData> prepareStatementDataMap)  {

		try(Connection conn = ApplicationConnectionPool.getInstance().getCommitConnection()){

			return getDataList(conn, tableName, whereValue, prepareStatementDataMap);
		}catch(SQLException e){
			throw new SQLRuntimeException(e);
		}
	}

	/**
	 * Table 데이터 얻기
	 * @param conn Connection
	 * @param tableName String table name
	 * @param whereValue String where query
	 * @param prepareStatementDataMap  Map 조건 데이터  date time 같이 database query 가 다른 경우
	 * @return List
	 */
	public static  List<Map<String, Object>> getDataList(Connection conn, String tableName, String whereValue, Map<Integer, PrepareStatementData> prepareStatementDataMap) {
		final List<Map<String, Object>> dataList = new ArrayList<>();

		JdbcMapDataHandler dataHandler = dataList::add;

		receiveData(conn,tableName,whereValue,prepareStatementDataMap,dataHandler);

		return dataList;
	}

	/**
	 * column sql get
	 * @param tableName String table name
	 * @param whereValue String where query
	 * @return String
	 */
	private static String getAllColumnSql(String tableName, String whereValue){


		StringBuilder sqlBuilder = new StringBuilder();
		sqlBuilder.append("SELECT * FROM ").append(tableName);
		if(whereValue != null && !whereValue.trim().equals("")){
			sqlBuilder.append(" WHERE ").append(whereValue);
		}
		return sqlBuilder.toString();
	}



	/**
	 * data list insert
	 * @param dataList List
	 * @return int insert count
	 */
	public static int insert(List<Map<String, Object>> dataList){
		ApplicationConnectionPool connectionPool = ApplicationConnectionPool.getInstance();
		try(Connection conn = connectionPool.getConnection()){

			int result = insert(conn, dataList, "INSERT", true);

			if(!connectionPool.isAutoCommit()) {
				conn.commit();
			}
			return result;
		}catch(SQLException e){
			throw new SQLRuntimeException(e);
		}
	}

	/**
	 * data list insert
	 * @param conn Connection
	 * @param dataList List
	 * @return int upsert count
	 */
	public static int upsert(Connection conn,  List<Map<String, Object>> dataList) {
		return insert(conn, dataList, "UPSERT", true);
	}



	/**
	 * data list insert
	 * @param conn Connection
	 * @param dataList List
	 * @return int insert count
	 */
	public static int insert(Connection conn, List<Map<String, Object>> dataList){
		return insert(conn, dataList, "INSERT", true);
	}




	/**
	 * data list insert
	 * @param conn Connection
	 * @param dataList List
	 * @param insertQueryValue String
	 * @param isClearParameters boolean
	 * @return int insert count
	 */
	public static int insert(Connection conn, List<Map<String, Object>> dataList, String insertQueryValue, boolean isClearParameters)  {

		if(dataList == null || dataList.size() == 0){
			return 0 ;
		}

		Map<String, Object> firstData = dataList.get(0);

		JdbcColumnClass[] nameClassArray = new JdbcColumnClass[firstData.size()-1];

		Set<String> dataKey = firstData.keySet();

		int i=0;

		boolean isTableName = false;

        JdbcNamingDataType jdbcNamingDataType = JdbcNamingDataType.getInstance();

		String tableName =null ;
		for(String columnName  : dataKey){


			if(!isTableName && TABLE_NAME.equals(columnName)){
				isTableName = true;
				tableName = (String)firstData.get(TABLE_NAME);
				continue;
			}
			Class<?> classes ;
			Object obj = firstData.get(columnName);

			if(obj == null){

				for (int j = 1; j <dataList.size() ; j++) {
					Map<String, Object> data = dataList.get(j);
					obj = data.get(columnName);
					if(obj != null){
						break;
					}

				}
			}

            JdbcDataType dataType = jdbcNamingDataType.getType(columnName);

			if( obj != null){
				if(dataType == JdbcDataType.DATE_TIME &&  (obj.getClass() == Long.class || obj.getClass() == Integer.class || obj.getClass() == Double.class || obj.getClass() == Float.class)){
					classes = Timestamp.class;
				}else{
					classes = obj.getClass();
				}
				JdbcColumnClass jdbcNameClass = new JdbcColumnClass(columnName, classes);
				nameClassArray[i] = jdbcNameClass;

			}else{
				JdbcColumnClass jdbcNameClass = new JdbcColumnClass(columnName, null);
				nameClassArray[i] = jdbcNameClass;

			}
			i++;


		}


		int successCount = 0;

		StringBuilder sqlBuilder = new StringBuilder();

		sqlBuilder.append(insertQueryValue).append(" INTO ").append(tableName).append(" (");

		StringBuilder fieldBuilder = new StringBuilder();

		for(i=0 ; i<nameClassArray.length ; i++){
			fieldBuilder.append(", ").append(nameClassArray[i].getColumnName());
		}
		sqlBuilder.append(fieldBuilder.substring(1));
		sqlBuilder.append(") VALUES (");

		fieldBuilder.setLength(0);
		for(i=0 ; i<nameClassArray.length ; i++){
			fieldBuilder.append(", ?");
		}
		sqlBuilder.append(fieldBuilder.substring(1));
		sqlBuilder.append(" )");

		PreparedStatement pstmt = null;

		//noinspection TryFinallyCanBeTryWithResources
		try{
			pstmt = conn.prepareStatement(sqlBuilder.toString());


			for(Map<String, Object> data : dataList){
				for(i=0 ; i<nameClassArray.length ; i++){
					Object object =data.get(nameClassArray[i].getColumnName());


					if(object == null){
						pstmt.setNull(i+1,  Types.NULL);
						continue;
					}

					if(nameClassArray[i].getClasses() == null){
						JdbcDataType dataType = jdbcNamingDataType.getType(nameClassArray[i].getColumnName());

						Class<?> classes ;
						if(dataType == JdbcDataType.DATE_TIME &&  object.getClass() == Long.class){
							classes = Timestamp.class;
						}else{
							classes = object.getClass();
						}
						nameClassArray[i].setClasses(classes);


					}


					try{
						if(nameClassArray[i].getClasses() == String.class){
							pstmt.setString(i+1, (String)object);
						}else if(nameClassArray[i].getClasses() == Timestamp.class){
							long value ;

							if(object.getClass() == Integer.class){
								value = (long) (int) (Integer)object;
							}else if(object.getClass() == Double.class){
								value =(long) (double) (Double)object;
							}else if(object.getClass() == Float.class){
								value =(long) (float) (Float)object;
							}else{
								value =(Long)object;
							}

							Timestamp timeStamp = new Timestamp(value);

							pstmt.setTimestamp(i+1, timeStamp);
						}else if(nameClassArray[i].getClasses() == Long.class){
							pstmt.setLong(i+1, (Long)object);
						}else if(nameClassArray[i].getClasses()  == Integer.class){
							pstmt.setInt(i+1, (Integer)object);
						}else if(nameClassArray[i].getClasses()  == Float.class){
							pstmt.setFloat(i+1, (Float)object);
						}else if(nameClassArray[i].getClasses()  == Double.class){
							pstmt.setDouble(i+1, (Double)object);
						}
					}catch(Exception e){
					    Class<?> objClasses = 	object.getClass();
						if(objClasses == Timestamp.class){
							pstmt.setTimestamp(i+1, (Timestamp)object);
						}else if(objClasses == Long.class){
							pstmt.setLong(i+1, (Long)object);
						}else if(objClasses  == Integer.class){
							pstmt.setInt(i+1, (Integer)object);
						}else if(objClasses  == Float.class){
							pstmt.setFloat(i+1, (Float)object);
						}else if(objClasses == Double.class){
							pstmt.setDouble(i+1, (Double)object);
						}else{
							pstmt.setString(i+1, (String)object);
						}


					}

				}
				pstmt.addBatch();
				if(isClearParameters){
					pstmt.clearParameters();
				}else{
					pstmt.executeBatch();
				}
				successCount++;
			}
			if(isClearParameters){
				pstmt.executeBatch();
			}
		}catch(SQLException e){
			throw new SQLRuntimeException(e);
		}finally{
			//noinspection CatchMayIgnoreException
			try{if(pstmt != null)pstmt.close(); }catch(Exception e){}
		}

		return successCount;
	}

	/**
	 * 뎅이터를 건별로 handler로 전달 받아서 처리
	 * @param conn Connection
	 * @param tableName String
	 * @param whereValue String
	 * @param prepareStatementDataMap Map 조건 데이터  date time 같이 database query 가 다른 경우
	 * @param handler JdbcMapDataHandler
	 */
	public static void receiveData(Connection conn, String tableName, String whereValue, Map<Integer, PrepareStatementData> prepareStatementDataMap
			, JdbcMapDataHandler handler){


		Statement stmt = null;
		ResultSet result = null;

		String sql = getAllColumnSql(tableName, whereValue);

        JdbcNamingDataType jdbcNamingDataType = JdbcNamingDataType.getInstance();

		try{
			if(prepareStatementDataMap != null){
				PreparedStatement pstmt = conn.prepareStatement(sql);
				stmt = pstmt;

				JdbcCommon.setStmt(pstmt, prepareStatementDataMap);

				result = pstmt.executeQuery();
			}else{
				stmt = conn.createStatement();
				result = stmt.executeQuery(sql);
			}
			String [] columnNames = Database.getColumnNames(result);

			while(result.next()){
				Map<String, Object> data = new HashMap<>();
				data.put(TABLE_NAME, tableName);
				for (String columnName : columnNames){


					try{

                        JdbcDataType dataType = jdbcNamingDataType.getType(columnName);

						if(dataType == JdbcDataType.STRING){
							data.put(columnName, result.getString(columnName));
						}else if(dataType == JdbcDataType.DATE_TIME){

							Timestamp timestamp = result.getTimestamp(columnName);
							if(timestamp == null ){
								data.put(columnName, null);
								continue;
							}
							data.put(columnName, timestamp.getTime());

						}else if(dataType == JdbcDataType.LONG){
							long value = result.getLong(columnName);
							if(result.wasNull()){
								data.put(columnName, null);
							}else{
								data.put(columnName, value);
							}


						}else if(dataType == JdbcDataType.DOUBLE){
							double value = 	result.getDouble(columnName);
							if(result.wasNull()){
								data.put(columnName, null);
							}else{
								data.put(columnName, value);
							}

						}else if(dataType == JdbcDataType.INTEGER){
							int value = result.getInt(columnName);
							if(result.wasNull()){
								data.put(columnName, null);
							}else{
								data.put(columnName, value);
							}
						}
					}catch(Exception e){
						data.put(columnName, result.getString(columnName));
					}
				}
				handler.receive(data);
			}
		}catch(SQLException e){
			throw new SQLRuntimeException(e);
		}finally{
			JdbcClose.statementResultSet(stmt,result);

		}
	}

}