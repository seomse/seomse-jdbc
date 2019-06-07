

package com.seomse.jdbc.admin;

import com.seomse.commons.file.FileUtil;
import com.seomse.commons.utils.ExceptionUtil;
import com.seomse.jdbc.Database;
import com.seomse.jdbc.JdbcQuery;
import com.seomse.jdbc.connection.ApplicationConnectionPool;
import com.seomse.jdbc.connection.ConnectionPool;
import com.seomse.jdbc.naming.JdbcMapDataHandler;
import com.seomse.jdbc.naming.JdbcNamingMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * <pre>
 *  파 일 명 : RowDataInOut.java
 *  설    명 : 다른 DB 계정관의 데이터이관 기능
 *
 *  작 성 자 : macle(김용수)
 *  작 성 일 : 2019.06.07
 *  버    전 : 1.0
 *  수정이력 :
 *  기타사항 :
 * </pre>
 * @author Copyrights 2019 by ㈜섬세한사람들. All right reserved.
 */
public class RowDataInOut {

	private final static Logger logger = LoggerFactory.getLogger(RowDataInOut.class);

	private int maxCommit = 10000;

	private String fileHome = "tables/";

	private String charSet ="UTF-8";

	private String dbType = "oracle";

	/**
	 * 생성자
	 */
	public RowDataInOut(){

	}


	/**
	 * 최대커믿개수를 설정한다
	 * @param maxCommit MaxCommit
	 */
	public void setMaxCommit(int maxCommit) {
		this.maxCommit = maxCommit;
	}


	/**
	 * 테이블파일 홈경로를 설정한다.
	 * @param fileHome FileHome
	 */
	public void setFileHome(String fileHome) {
		this.fileHome = fileHome;
	}

	public void setDbType(String dbType) {
		this.dbType = dbType;
	}

	/**
	 * 테이블데이터를 파일로 추출한다
	 * @param tableArray tableArray
	 */
	public void dataOut(String [] tableArray){
		try{
			ConnectionPool connectionPool = ApplicationConnectionPool.getInstance().getConnectionPool();
			Connection conn = connectionPool.getConnection();
			if(!connectionPool.isAutoCommit()){
				conn.commit();
			}
			dataOut(conn, tableArray);

		}catch(Exception e){
			logger.error(ExceptionUtil.getStackTrace(e));
		}
	}
	/**
	 * 테이블데이터를 파일로 추출한다
	 * @param tableArray tableArray
	 */
	public void dataOut(Connection conn, String [] tableArray){
		try {
			if (tableArray == null) {
				List<String> tableList = JdbcQuery.getStringList(Database.getTableListSql(dbType));
				tableArray = tableList.toArray(new String[0]);
			}

			StringBuilder outBuilder = new StringBuilder();

			for (String tableName : tableArray) {

				logger.info("OUT Table: " + tableName);
                String fileName = fileHome + tableName;
                FileUtil.fileOutput("", charSet, fileName, false);
                int count = 0;

                JdbcMapDataHandler handler = new JdbcMapDataHandler() {
                    @Override
                    public void receive(Map<String, Object> data) {
                    }
                };

                JdbcNamingMap.receiveData(conn, tableName, null, null, handler);

//				List<Map<String, Object>> dataList = JdbcNamingMap.getDataList(conn, tableName, null, null);
//				int count = 0;
//				for (Map<String, Object> data : dataList) {
//					count++;
//					outBuilder.append(Config.START_SPLIT + ClassJsonUtil.mapDataToJsonString(data).replace(Config.START_SPLIT, ' ').replace(Config.END_SPLIT, ' ') + Config.END_SPLIT);
//					if (count >= maxCommit) {
//						FileUtil.fileOutput(outBuilder.toString(), charSet, fileName, true);
//						outBuilder.setLength(0);
//						count = 0;
//					}
//				}
//				if (count > 0) {
//					FileUtil.fileOutput(outBuilder.toString(), charSet, fileName, true);
//					outBuilder.setLength(0);
//				}
//
//				dataList.clear();
			}
		}catch(Exception e){
			logger.error(ExceptionUtil.getStackTrace(e));
		}
		logger.info("Table Out Complete");
	}


	/**
	 * 파일로 추출된 데이터를 DB에 추가한다.
	 * @param tableArray tableArray
	 */
	public void dataIn( String [] tableArray){
		try{
			ConnectionPool connectionPool = ApplicationConnectionPool.getInstance().getConnectionPool();
			Connection conn = connectionPool.getConnection();
			if(!connectionPool.isAutoCommit()){
				conn.commit();
			}
			dataIn(conn, tableArray);
		}catch(Exception e){
			logger.error(ExceptionUtil.getStackTrace(e));
		}
	}

	/**
	 * 파일로 추출된 데이터를 DB에 추가한다.
	 * @param tableArray tableArray
	 */
	public void dataIn(Connection conn, String [] tableArray){
//		if(tableArray == null){
//			List<String> tableList = JdbcQuery.getStringList(Database.getTableListSql(dbType));
//			tableArray = tableList.toArray(new String[0]);
//		}
//		try{conn.setAutoCommit(false);}catch(Exception e){logger.error(ExceptionUtil.getStackTrace(e));}
//
//		List<Map<String,Object>> insertList = new ArrayList<>();
//		for(String tableName : tableArray){
//
//			logger.info(tableName);
//			String fileName =fileHome+tableName;
//			InputStreamReader reader = null ;
//
//			try {
//				reader = new InputStreamReader(new FileInputStream(new File(fileName)), charSet);
//				int readData;
//				StringBuilder messageBuilder = new StringBuilder();
//				while((readData = reader.read()) != -1){
//					char message = (char)readData;
//					switch(message){
//					case Config.START_SPLIT:
//						messageBuilder.setLength(0);
//						break;
//					case Config.END_SPLIT:
//
//						Map<String,Object> dataMap = ClassJsonUtil.makeDataMap(messageBuilder.toString());
//						insertList.add(dataMap);
//						if(insertList.size() >= maxCommit){
//							JdbcNamingMap.insert(conn,insertList);
//
//							if(conn.getAutoCommit()){
//
//							}
//
//							try{conn.commit();}catch(Exception e){logger.error(ExceptionUtil.getStackTrace(e));}
//							insertList.clear();
//						}
//						break;
//					default:
//						messageBuilder.append(message);
//					}
//				}
//
//				if(insertList.size() > 0){
//					JdbcNamingMap.insert(conn, insertList);
//					try{conn.commit();}catch(Exception e){logger.error(ExceptionUtil.getStackTrace(e));}
//					insertList.clear();
//				}
//			} catch (Exception e) {
//				logger.error(ExceptionUtil.getStackTrace(e));
//			} finally{
//				//noinspection CatchMayIgnoreException
//				try{if(reader!= null)reader.close(); }catch(Exception e){}
//			}
//		}
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

        JdbcMapDataHandler handler = new JdbcMapDataHandler() {
			@Override
			public void receive(Map<String, Object> data) {
				dataList.add(data);
				if(maxCommit <= dataList.size()){
                    insert(insertConn, dataList);
				}
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