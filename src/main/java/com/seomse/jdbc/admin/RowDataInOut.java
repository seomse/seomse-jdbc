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

package com.seomse.jdbc.admin;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.seomse.commons.exception.IORuntimeException;
import com.seomse.commons.utils.ExceptionUtil;
import com.seomse.commons.utils.FileUtil;
import com.seomse.jdbc.Database;
import com.seomse.jdbc.JdbcQuery;
import com.seomse.jdbc.PrepareStatementData;
import com.seomse.jdbc.connection.ApplicationConnectionPool;
import com.seomse.jdbc.exception.SQLRuntimeException;
import com.seomse.jdbc.naming.JdbcMapDataHandler;
import com.seomse.jdbc.naming.JdbcNamingMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 다른 db 계정간의 데이터 전송 이관 기능 지원
 * data 백업, 복원기능 지원 (데이터만 스키마는 안됨)
 *
 * @author macle
 */
public class RowDataInOut {

	private final static Logger logger = LoggerFactory.getLogger(RowDataInOut.class);

	private int maxDataCount = 10000;

	private String fileHome = "tables/";

	private final String charSet ="UTF-8";

	private String dbType = "oracle";

	/**
	 * 생성자
	 */
	public RowDataInOut(){

	}

	/**
	 * 최대 메모리 저장 개수 설정
	 * 최대 건수가 넘어 가면 commit 함
	 * @param maxDataCount int maxDataCount
	 */
	public void setMaxDataCount(int maxDataCount) {
		this.maxDataCount = maxDataCount;
	}


	/**
	 * 테이블 파일 홈 경로를 설정 (dir path)
	 * @param fileHome String FileHome
	 */
	public void setFileHome(String fileHome) {
		this.fileHome = fileHome;
	}


	/**
	 * db 유형 설정
	 * oracle, maria 등
	 * @param dbType String marla db
	 */
	public void setDbType(String dbType) {
		this.dbType = dbType;
	}

	/**
	 * 모든 테이블 데이터를 파일로 추출
	 */
	public void dataOut(){
		List<String> tableList = JdbcQuery.getStringList(Database.getTableListSql(dbType));
		dataOut(tableList.toArray(new String[0])) ;
	}

	/**
	 * 테이블 데이터를 파일로 츠츨
	 * @param tableArray String [] table name array
	 */
	public void dataOut(String [] tableArray){
		ApplicationConnectionPool applicationConnectionPool = ApplicationConnectionPool.getInstance();
		try (Connection conn = applicationConnectionPool.getCommitConnection()) {
			dataOut(conn, tableArray);
		} catch (SQLException e) {
			throw new SQLRuntimeException(e);
		}
	}

	private int dataCount = 0;
	/**
	 * 테이블 데이터를 파일로 츠츨
	 * @param conn Connection
	 * @param tableArray String [] table name array
	 */
	public void dataOut(Connection conn, String [] tableArray){

		Gson gson = new Gson();

		if (tableArray == null) {
			List<String> tableList = JdbcQuery.getStringList(Database.getTableListSql(dbType));
			tableArray = tableList.toArray(new String[0]);
		}

		final StringBuilder outBuilder = new StringBuilder();

		for (String tableName : tableArray) {
			dataCount = 0;
			logger.info("out table: " + tableName);
            final String fileName = fileHome + tableName;
            //파일생성
            FileUtil.fileOutput("", charSet, fileName, false);

			//noinspection Convert2Lambda
			JdbcMapDataHandler handler = new JdbcMapDataHandler() {
                @Override
                public void receive(Map<String, Object> data) {
					dataCount++;
					outBuilder.append(gson.toJson(data)).append("\n");
					if(dataCount >= maxDataCount){
						dataCount = 0;
						FileUtil.fileOutput(outBuilder.toString(), charSet, fileName, true);
						outBuilder.setLength(0);
					}

                }
            };

            JdbcNamingMap.receiveData(conn, tableName, null, null, handler);
			if(dataCount >0){
				FileUtil.fileOutput(outBuilder.toString(), charSet, fileName, true);
				outBuilder.setLength(0);
			}

		}



		logger.info("table out complete");
	}

	/**
	 * 추출된 파일 경로에 있는 모든 파일 insert
	 */
	public void dataIn(){
		dataIn(new File(fileHome).list());
	}

	/**
	 * 추출된 파일중 테이블 이름에 해당하는 데이터를 db 에 insert
	 * @param tableArray String [] table name array
	 */
	public void dataIn( String [] tableArray){
		ApplicationConnectionPool connectionPool = ApplicationConnectionPool.getInstance();
		try(Connection conn = connectionPool.getConnection()){
			dataIn(conn, tableArray);
			if(!connectionPool.isAutoCommit()){
				conn.commit();
			}
		}catch(SQLException e){
			throw new SQLRuntimeException(e);
		}
	}

	/**
	 * 파일로 추출된 데이터를 DB에 추가한다.
	 * mysql 의 경우  Connection setAutoCommit 을 false 로 하는게 좋음
	 * @param conn Connection
	 * @param tableArray String [] table name array
	 */
	public void dataIn(Connection conn, String [] tableArray){

		if(tableArray == null){
			List<String> tableList = JdbcQuery.getStringList(Database.getTableListSql(dbType));
			tableArray = tableList.toArray(new String[0]);
		}
		Gson gson = new Gson();
		List<Map<String,Object>> insertList = new ArrayList<>();
		for(String tableName : tableArray){

			logger.info("data in: " + tableName+"\n");
			String fileName =fileHome+tableName;

			BufferedReader br ;
			String line;
			try {
				br = new BufferedReader(new InputStreamReader(new FileInputStream(fileName), charSet));

				while ((line = br.readLine()) != null) {
					if("".equals(line.trim())){
						continue;
					}
					Map<String, Object> dataMap = gson.fromJson(line, new TypeToken<Map<String, Object>>(){}.getType());
					insertList.add(dataMap);
					if(insertList.size() >= maxDataCount){
						dataInsert(conn, insertList);
					}
				}
			}catch(IOException e){
				throw new IORuntimeException(e);
			}

			if(insertList.size() > 0){
				dataInsert(conn, insertList);
			}


		}
	}

	/**
	 * list data insert
	 * @param conn Connection
	 * @param insertList List insert data list
	 */
	private void dataInsert(Connection conn, List<Map<String,Object>> insertList ){
		try {

			JdbcNamingMap.insert(conn, insertList);

			if (!conn.getAutoCommit()) {
				conn.commit();
			}
		}catch(SQLException e){
			throw new SQLRuntimeException(e);
		}
		insertList.clear();
	}



	/**
	 * 전체 테이블 싱크
	 * @param selectConn Connection select
	 * @param insertConn Connection insert
	 */
	public void allTableSync(Connection selectConn, Connection insertConn){
		List<String> tableList = JdbcQuery.getStringList(Database.getTableListSql(dbType));
		tableSync(selectConn, insertConn, tableList.toArray(new String[0]));
	}

	/**
	 * 전체 테이블 복사
	 * @param selectConn Connection select
	 * @param insertConn Connection insert
	 */
	public void allTableCopy(Connection selectConn, Connection insertConn){
		List<String> tableList = JdbcQuery.getStringList(Database.getTableListSql(dbType));
		tableCopy(selectConn, insertConn, tableList.toArray(new String[0]));
	}


	/**
	 * table sync (truncate 사용)
	 * @param selectConn Connection select
	 * @param insertConn Connection insert
	 * @param tables String [] table name array
	 */
	public void tableSync(Connection selectConn, Connection insertConn, String [] tables){

		for(String table : tables){
			try{
				logger.info(table);
				JdbcQuery.execute(insertConn, "TRUNCATE TABLE " + table);
				tableCopy(selectConn, insertConn, table);

			}catch(Exception e){
				logger.error(ExceptionUtil.getStackTrace(e));
			}
		}
	}

	/**
	 * table sync (delete 사용)
	 * @param selectConn Connection select
	 * @param insertConn Connection insert
	 * @param tables String [] table name array
	 */
	public void tableSyncToDelete(Connection selectConn, Connection insertConn, String [] tables){

		for(String table : tables){
			try{
				logger.info(table);
				JdbcQuery.execute(insertConn, "DELETE FROM " + table);
				tableCopy(selectConn, insertConn, table);

			}catch(Exception e){
				logger.error(ExceptionUtil.getStackTrace(e));
			}
		}

	}

	/**
	 * table copy
	 * @param selectConn Connection select
	 * @param insertConn Connection insert
	 * @param tables String [] table name array
	 */
	public void tableCopy(Connection selectConn, Connection insertConn, String [] tables){

		for(String table : tables){
			try{
				logger.info(table);
				tableCopy(selectConn, insertConn, table);
			}catch(Exception e){
				logger.error(ExceptionUtil.getStackTrace(e));
			}
		}
	}

	/**
	 * table copy
	 * @param selectConn Connection select not null
	 * @param insertConn Connection insert not null
	 * @param table String table name not null
	 */
	public void tableCopy(Connection selectConn, final Connection insertConn, String table){
		tableCopy(selectConn, insertConn, table, null, null);
	}

	/**
	 * table copy
	 * @param selectConn Connection select not null
	 * @param insertConn Connection insert not null
	 * @param table String table name not null
	 * @param whereValue String where null enable
	 * @param prepareStatementDataMap where seq data null enable
	 */
	public void tableCopy(Connection selectConn, final Connection insertConn, String table, String whereValue,  Map<Integer, PrepareStatementData> prepareStatementDataMap){
		final List<Map<String, Object>> dataList = new ArrayList<>();

		JdbcMapDataHandler handler = data -> {
			dataList.add(data);
			if(maxDataCount <= dataList.size()){
				insert(insertConn, dataList);
			}
		};

		JdbcNamingMap.receiveData(selectConn, table, whereValue, prepareStatementDataMap, handler);
		if(dataList.size() > 0){
            insert(insertConn, dataList);
		}
	}

	/**
	 * data insert
	 * @param conn Connection
	 * @param dataList List insert data list
	 */
	private void insert(Connection conn, List<Map<String, Object>> dataList){
        JdbcNamingMap.insert(conn, dataList);
        try{
            if(!conn.getAutoCommit())
                conn.commit();
        }catch(SQLException e){
            throw new SQLRuntimeException(e);
        }
        dataList.clear();
    }
}