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
package com.seomse.jdbc.connection;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.SQLException;

/**
 * ConnectionFactory
 * @author macle
 */
public class ConnectionFactory {



	/**
	 * Connection 생성
	 * @param databaseTypeOrDriverClass String oracle, mssql, db2, unisql, mysql, sqlite, driver class fullPackage.class
	 * @param url String url ex)jdbc:oracle:thin:@127.0.0.1:1521:orcl
	 * @param user String user Id
	 * @param password String user password
	 * @return Connection
	 * @throws ClassNotFoundException ClassNotFoundException
	 * @throws SQLException SQLException
	 * @throws InstantiationException InstantiationException
	 * @throws IllegalAccessException IllegalAccessException
	 */
	public static Connection newConnection(String databaseTypeOrDriverClass, String url, String user, String password) throws ClassNotFoundException, SQLException, InstantiationException, IllegalAccessException{
		Driver driver = newDriver(databaseTypeOrDriverClass);
		return newConnection(driver, url, user, password);

	}


	/**
	 * Connection 생성
	 * @param driver Driver
	 * @param url String url ex)jdbc:oracle:thin:@127.0.0.1:1521:orcl
	 * @param user String user Id
	 * @param password String user password
	 * @return Connection
	 * @throws SQLException SQLException
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
	 * Connection 생성
	 * @param databaseTypeOrDriverClass  String oracle, mssql, db2, unisql, mysql, sqlite, driver class fullPackage.class
	 * @param urlText String url ex)jdbc:oracle:thin:@127.0.0.1:1521:orcl
	 * @return Connection
	 * @throws SQLException SQLException
	 * @throws InstantiationException InstantiationException
	 * @throws IllegalAccessException IllegalAccessException
	 * @throws ClassNotFoundException ClassNotFoundException
	 */
	public  static Connection  newConnection(String databaseTypeOrDriverClass, String urlText) throws SQLException, InstantiationException, IllegalAccessException, ClassNotFoundException{
		Driver driver = newDriver(databaseTypeOrDriverClass);
		return driver.connect(urlText, new java.util.Properties());

	}


	/**
	 * sqlite Connection 생성
	 * @param sqliteDbFilePath String sqlite file path
	 * @return  Connection sqlite Connection
	 * @throws SQLException SQLException
	 * @throws InstantiationException InstantiationException
	 * @throws IllegalAccessException IllegalAccessException
	 * @throws ClassNotFoundException ClassNotFoundException
	 */
	public synchronized static Connection  newConnectionSqlite(String sqliteDbFilePath) throws SQLException, InstantiationException, IllegalAccessException, ClassNotFoundException{
		return newConnection("SQLITE","jdbc:sqlite:"+ sqliteDbFilePath );
	}
	
	


	/**
	 * Driver 생성
	 * @param databaseTypeOrDriverClass String databaseTypeOrDriverClass oracle, mssql, db2, unisql, mysql, sqlite, driver class fullPackage.class
	 * @return Driver
	 * @throws InstantiationException InstantiationException
	 * @throws IllegalAccessException IllegalAccessException
	 * @throws ClassNotFoundException ClassNotFoundException
	 */
	public static Driver newDriver(String databaseTypeOrDriverClass) throws InstantiationException, IllegalAccessException, ClassNotFoundException{
		Driver driver ;
		
		String databaseType = databaseTypeOrDriverClass.toUpperCase().trim();

		if(databaseType.equals("ORACLE")){
			driver = (Driver)Class.forName("oracle.jdbc.driver.OracleDriver").newInstance();
		}else if(databaseType.startsWith("MSSQL") || databaseType.startsWith("MS-SQL")){
			driver = (Driver)Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver").newInstance();
		}else if(databaseType.startsWith("DB2")){
			driver = (Driver)Class.forName("com.ibm.db2.jcc.DB2Driver").newInstance();
		}else if(databaseType.startsWith("UNISQL")){
			driver = (Driver)Class.forName("unisql.jdbc.driver.UniSQLDriver").newInstance();
		}else if(databaseType.startsWith("MYSQL")){
			driver = (Driver)Class.forName("com.mysql.jdbc.Driver").newInstance();
		}else if(databaseType.startsWith("SQLITE")){
			driver = (Driver)Class.forName("org.sqlite.JDBC").newInstance();
		}else if(databaseType.startsWith("TIBERO")){
			driver = (Driver)Class.forName("com.tmax.tibero.jdbc.TbDriver").newInstance();
		}else if(databaseType.startsWith("DRILL")){
			driver = (Driver)Class.forName("org.apache.drill.jdbc.Driver").newInstance();
		}else if(databaseType.startsWith("MARIA")){
			driver = (Driver)Class.forName("org.mariadb.jdbc.Driver").newInstance();
		}else if(databaseType.startsWith("HIVE")){
			driver = (Driver)Class.forName("org.apache.hive.jdbc.HiveDriver").newInstance();
		}
		else{
			 driver = (Driver)Class.forName(databaseTypeOrDriverClass).newInstance();
		}		
		return driver;
	}
}