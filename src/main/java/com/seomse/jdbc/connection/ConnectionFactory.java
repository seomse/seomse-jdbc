
package com.seomse.jdbc.connection;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.SQLException;

/** 
 * <pre>
 *  파 일 명 : ConnectionFactory.java
 *  설    명 : 컨넥션 생성
 *         
 *  작 성 자 : macle
 *  작 성 일 : 2017.07
 *  버    전 : 1.1
 *  수정이력 : 2019.02
 *  기타사항 :
 * </pre>
 * @atuhor Copyrights 2017, 2019 by ㈜섬세한사람들. All right reserved.
 */
public class ConnectionFactory {


	/**
	 * Connection 생성
	 * @param databaseTypeOrDriverClass oracle, mssql, db2, unisql, mysql, sqlite, driver class fullPackage.class
	 * @param url url ex)jdbc:oracle:thin:@127.0.0.1:1521:orcl
	 * @param user user Id
	 * @param password user password
	 * @return Connection
	 * @throws ClassNotFoundException
	 * @throws SQLException 
	 * @throws IllegalAccessException 
	 * @throws InstantiationException 
	 */
	public static Connection newConnection(String databaseTypeOrDriverClass, String url, String user, String password) throws ClassNotFoundException, SQLException, InstantiationException, IllegalAccessException{
		Driver driver = newDriver(databaseTypeOrDriverClass);
		return newConnection(driver, url, user, password);
//	      DriverManager.registerDriver(driver);	
//		return  DriverManager.getConnection(url, user, password);		
	}


	/**
	 * Connection 생성
	 * @param driver
	 * @param url  url ex)jdbc:oracle:thin:@127.0.0.1:1521:orcl
	 * @param user user Id
	 * @param password  user password
	 * @return Connection
	 * @throws SQLException
	 */
	public static Connection newConnection(Driver driver, String url, String user, String password) throws SQLException {
		java.util.Properties info = new java.util.Properties();
		if (user != null) {

			info.put("user", user);
		}
		if (password != null) {
			info.put("password", password);
		}
		return driver.connect(url, info);

	}
	
	/**
	 *Connection 생성
	 * @param databaseTypeOrDriverClass oracle, mssql, db2, unisql, mysql, sqlite, driver class fullPackage.class
	 * @param urlText urlText
	 * @return 연결정보
	 * @throws SQLException
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 * @throws ClassNotFoundException
	 */
	public  static Connection  newConnection(String databaseTypeOrDriverClass, String urlText) throws SQLException, InstantiationException, IllegalAccessException, ClassNotFoundException{
		Driver driver = newDriver(databaseTypeOrDriverClass);
		return driver.connect(urlText, new java.util.Properties());
//		DriverManager.registerDriver(driver);	
//		return DriverManager.getConnection(urlText);
	}

	/**
	 * sqlite Connection 생성
	 * @param sqliteDbFilePath
	 * @return
	 * @throws SQLException
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 * @throws ClassNotFoundException
	 */
	public synchronized static Connection  newConnectionSqlite(String sqliteDbFilePath) throws SQLException, InstantiationException, IllegalAccessException, ClassNotFoundException{
		return newConnection("SQLITE","jdbc:sqlite:"+ sqliteDbFilePath );
	}
	
	

	/**
	 * Driver 생성
	 * @param databaseTypeOrDriverClass oracle, mssql, db2, unisql, mysql, sqlite, driver class fullPackage.class
	 * @return
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 * @throws ClassNotFoundException
	 */
	public  static Driver newDriver(String databaseTypeOrDriverClass) throws InstantiationException, IllegalAccessException, ClassNotFoundException{
		Driver driver ;
		
		String databaseType = databaseTypeOrDriverClass.toUpperCase().trim();

		//noinspection IfCanBeSwitch
		if(databaseType.equals("ORACLE")){
			//noinspection deprecation
			driver = (Driver)Class.forName("oracle.jdbc.driver.OracleDriver").newInstance();
		}else if(databaseType.equals("MSSQL") || databaseType.equals("MS-SQL")){
			//noinspection deprecation
			driver = (Driver)Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver").newInstance();
		}else if(databaseType.equals("DB2")){
			//noinspection deprecation
			driver = (Driver)Class.forName("com.ibm.db2.jcc.DB2Driver").newInstance();	
		}else if(databaseType.equals("UNISQL")){
			//noinspection deprecation
			driver = (Driver)Class.forName("unisql.jdbc.driver.UniSQLDriver").newInstance();	
		}else if(databaseType.equals("MYSQL")){
			//noinspection deprecation
			driver = (Driver)Class.forName("com.mysql.jdbc.Driver").newInstance();	
		}else if(databaseType.equals("SQLITE")){
			//noinspection deprecation
			driver = (Driver)Class.forName("org.sqlite.JDBC").newInstance();		
		}else if(databaseType.equals("TIBERO")){
			//noinspection deprecation
			driver = (Driver)Class.forName("com.tmax.tibero.jdbc.TbDriver").newInstance();	
		}else if(databaseType.equals("DRILL")){
			//noinspection deprecation
			driver = (Driver)Class.forName("org.apache.drill.jdbc.Driver").newInstance();	
		}else if(databaseType.equals("MARIA") || databaseType.equals("MARIADB")){
			//noinspection deprecation
			driver = (Driver)Class.forName("org.mariadb.jdbc.Driver").newInstance();	
		}else if(databaseType.equals("HIVE")){
			//noinspection deprecation
			driver = (Driver)Class.forName("org.apache.hive.jdbc.HiveDriver").newInstance();
		}
		else{
			//noinspection deprecation
			 driver = (Driver)Class.forName(databaseTypeOrDriverClass).newInstance();
		}		
		return driver;
	}
}