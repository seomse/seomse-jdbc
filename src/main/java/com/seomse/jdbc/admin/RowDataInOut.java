

package com.seomse.jdbc.admin;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.seomse.commons.utils.ExceptionUtil;
import com.seomse.commons.utils.FileUtil;
import com.seomse.jdbc.Database;
import com.seomse.jdbc.JdbcQuery;
import com.seomse.jdbc.connection.ApplicationConnectionPool;
import com.seomse.jdbc.naming.JdbcMapDataHandler;
import com.seomse.jdbc.naming.JdbcNamingMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * <pre>
 *  파 일 명 : RowDataInOut.java
 *  설    명 : 다른 DB 계정관의 데이터이관 기능
 *
 *  작 성 자 : macle
 *  작 성 일 : 2019.06.07
 *  버    전 : 1.0
 *  수정이력 :
 *  기타사항 :
 * </pre>
 * @author Copyrights 2019 by ㈜섬세한사람들. All right reserved.
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
	 * @param maxDataCount MaxCommit
	 */
	public void setMaxDataCount(int maxDataCount) {
		this.maxDataCount = maxDataCount;
	}


	/**
	 * 테이블파일 홈경로를 설정
	 * @param fileHome FileHome
	 */
	public void setFileHome(String fileHome) {
		this.fileHome = fileHome;
	}

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
	 * 테이블데이터를 파일로 추출한다
	 * @param tableArray tableArray
	 */
	public void dataOut(String [] tableArray){
		ApplicationConnectionPool applicationConnectionPool = ApplicationConnectionPool.getInstance();
		try (Connection conn = applicationConnectionPool.getCommitConnection()) {
			dataOut(conn, tableArray);
		} catch (Exception e) {
			logger.error(ExceptionUtil.getStackTrace(e));
		}
	}

	private int dataCount = 0;
	/**
	 * 테이블데이터를 파일로 추출한다
	 * @param tableArray tableArray
	 */
	public void dataOut(Connection conn, String [] tableArray){

		Gson gson = new Gson();

		try {
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
		}catch(Exception e){
			logger.error(ExceptionUtil.getStackTrace(e));
		}
		logger.info("table out complete");
	}

	/**
	 * 파일로 추출된 데이터를 DB에 추가한다.
	 * @param tableArray tableArray
	 */
	public void dataIn( String [] tableArray){
		ApplicationConnectionPool connectionPool = ApplicationConnectionPool.getInstance();
		try(Connection conn = connectionPool.getConnection()){
			dataIn(conn, tableArray);
			if(!connectionPool.isAutoCommit()){
				conn.commit();
			}
		}catch(Exception e){
			logger.error(ExceptionUtil.getStackTrace(e));
		}
	}

	/**
	 * 파일로 추출된 데이터를 DB에 추가한다.
	 * mysql 의 경우  Connection setAutoCommit 을 false 로 하는게 좋음
	 *
	 * @param tableArray tableArray
	 */
	public void dataIn(Connection conn, String [] tableArray){

		if(tableArray == null){
			List<String> tableList = JdbcQuery.getStringList(Database.getTableListSql(dbType));
			tableArray = tableList.toArray(new String[0]);
		}
		Gson gson = new Gson();
		List<Map<String,Object>> insertList = new ArrayList<>();
		for(String tableName : tableArray){

			logger.info(tableName);
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
			}catch(Exception e){
				logger.error(ExceptionUtil.getStackTrace(e));
			}

			if(insertList.size() > 0){
				dataInsert(conn, insertList);
			}


		}
	}

	private void dataInsert(Connection conn, List<Map<String,Object>> insertList ){
		try {
			JdbcNamingMap.insert(conn, insertList);

			if (!conn.getAutoCommit()) {
				conn.commit();
			}
		}catch(SQLException e){
			logger.error(ExceptionUtil.getStackTrace(e));
		}
		insertList.clear();
	}



	/**
	 * 모든 테이블의 전체데이터를 맞춘다.
	 * @param selectConn 복사대상테이블
	 * @param insertConn 복사테이블
	 */
	public void allTableSync(Connection selectConn, Connection insertConn){
		List<String> tableList = JdbcQuery.getStringList(Database.getTableListSql(dbType));
		tableSync(selectConn, insertConn, tableList.toArray(new String[0]));
	}

	/**
	 * 모든 테이블의 전체데이터를 복사한다
	 * @param selectConn 복사대상테이블
	 * @param insertConn 복사테이블
	 */
	public void allTableCopy(Connection selectConn, Connection insertConn){
		List<String> tableList = JdbcQuery.getStringList(Database.getTableListSql(dbType));
		tableCopy(selectConn, insertConn, tableList.toArray(new String[0]));
	}


	/**
	 * 테이블의 전체데이터를 맞춘다. (truncate 사용)
	 * @param selectConn 복사대상테이블
	 * @param insertConn 복사테이블
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
	 * 테이블의 전체데이터를 맞춘다. (delete 사용)
	 * @param selectConn 복사대상테이블
	 * @param insertConn 복사테이블
	 */
	public  void tableSyncToDelete(Connection selectConn, Connection insertConn, String [] tables){

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
	 * 테이블의 전체데이터를 복사한다
	 * @param selectConn 복사대상테이블
	 * @param insertConn 복사테이블
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
	 * 테이블 복사
	 * @param selectConn selectConn
	 * @param insertConn insertConn
	 * @param table table
	 */
	public void tableCopy(Connection selectConn, final Connection insertConn, String table){
		final List<Map<String, Object>> dataList = new ArrayList<>();

		JdbcMapDataHandler handler = data -> {
			dataList.add(data);
			if(maxDataCount <= dataList.size()){
				insert(insertConn, dataList);
			}
		};

		JdbcNamingMap.receiveData(selectConn, table, null, null, handler);
		if(dataList.size() > 0){
            insert(insertConn, dataList);
		}
	}

	private void insert(Connection conn, List<Map<String, Object>> dataList){
        JdbcNamingMap.insert(conn, dataList);
        try{
            if(!conn.getAutoCommit())
                conn.commit();
        }catch(Exception e){
            logger.error(ExceptionUtil.getStackTrace(e));
        }
        dataList.clear();
    }
}