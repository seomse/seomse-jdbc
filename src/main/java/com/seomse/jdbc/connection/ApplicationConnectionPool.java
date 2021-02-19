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


import com.seomse.commons.config.Config;
import com.seomse.commons.exception.ReflectiveOperationRuntimeException;
import com.seomse.commons.utils.ExceptionUtil;
import com.seomse.cypto.LoginCrypto;
import com.seomse.jdbc.Database;
import com.seomse.jdbc.exception.SQLRuntimeException;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
/**
 * default application connection pool
 * @author macle
 */
public class ApplicationConnectionPool {

    private static final Logger logger = LoggerFactory.getLogger(ApplicationConnectionPool.class);

    private static class Singleton {
        private static final ApplicationConnectionPool instance = new ApplicationConnectionPool().configBuild();
    }

    /**
     * 싱글인스턴스 얻기
     * @return ApplicationConnectionPool Singleton instance
     */
    public static ApplicationConnectionPool getInstance (){
        return Singleton.instance;
    }


    /**
     * 생성자
     * 따로 생성해서 사용할 수 있음
     */
    public ApplicationConnectionPool(){

    }

    /**
     * Singleton instance 생성
     * @return ApplicationConnectionPool 
     */
    public ApplicationConnectionPool configBuild(){
        try{
            setConfigConnectionInfo();
        }catch(Exception e){
            logger.error(ExceptionUtil.getStackTrace(e));
        }
        return this;
    }

    private long connectionTimeOut = 30000L;
    private long validationTimeOut = 5000L;


    private boolean isConnectionWait = false;

    private long connectionWaitTryTime = 10000L;

    private boolean isAutoCommit = false;
    private int connectionPoolCount = 10;

    private DataSource dataSource;

    private String jdbcType;

    private String url;
    private String userId;
    private String password;
    private String databaseTypeOrFullPackage;

    /**
     * DB접속정보를 seomse_config에서 가져와서 설정
     * isErrorLog 는 초기생성자에서 에러를 출력하지않기위한 로그
     */
    public void setConfigConnectionInfo(){
        jdbcType = Config.getConfig("application.jdbc.type");

        if(jdbcType != null){
            Database.getSequenceManager().setDefaultMaker(jdbcType);
        }

        String driverClass = Config.getConfig("application.jdbc.driver.class");

        databaseTypeOrFullPackage = driverClass;
        if(driverClass == null){
            databaseTypeOrFullPackage = jdbcType;
        }

        if(!isConfig(databaseTypeOrFullPackage, "application.jdbc.type or application.jdbc.driver.class")){
            return ;
        }



        final String connectionPoolCountKey = "application.jdbc.connection.pool.count";
        String connectionPoolCountValue =  Config.getConfig(connectionPoolCountKey);
        if(!isConfig(connectionPoolCountValue, connectionPoolCountKey)){
            return ;
        }

        connectionPoolCountValue = connectionPoolCountValue.trim();

        boolean isNumber = true;
        int length = connectionPoolCountValue.length();
        for (int i=0; i < length ; i++){
            char chk = connectionPoolCountValue.charAt(i);
            if(chk>57 || chk<48){
                isNumber = false;
                break;
            }
        }

        if(!isNumber){

            logger.error("application.jdbc.connection.pool.count is not number");


            return ;
        }

        connectionPoolCount = Integer.parseInt(connectionPoolCountValue, 10);


        final String urlKey = "application.jdbc.url";
        url =  Config.getConfig(urlKey);
        if(!isConfig(url, urlKey)){
            return ;
        }


        final String encryptFlagKey = "application.jdbc.login.encrypt";
        String encryptFlag =  Config.getConfig(encryptFlagKey);
        if(!isConfig(encryptFlag, encryptFlagKey)){
            return ;
        }


        final String userIdKey = "application.jdbc.user.id";
        userId =  Config.getConfig(userIdKey);
        if(!isConfig(userId, userIdKey)){
            return ;
        }

        final String userPasswordKey = "application.jdbc.user.password";
        password =  Config.getConfig(userPasswordKey);
        if(!isConfig(password, userPasswordKey)){
            return ;
        }

        url = url.trim();

        encryptFlag = encryptFlag.trim();
        userId = userId.trim();
        password = password.trim();

        encryptFlag = encryptFlag.toUpperCase();
        if("Y".equals(encryptFlag)){
            String [] loginInfos = LoginCrypto.decryption(userId,password);
            userId = loginInfos[0];
            password = loginInfos[1];
        }


        isConnectionWait = Config.getBoolean("application.jdbc.connection.wait.flag", false);
        connectionWaitTryTime = Config.getLong("application.jdbc.connection.wait.try.time", 10000L);


        isAutoCommit = Config.getBoolean("application.jdbc.connection.auto.commit.flag" , true);

        connectionTimeOut = Config.getLong("application.jdbc.connection.time.out", 30000L);
        validationTimeOut = Config.getLong("application.jdbc.connection.valid.time.out", 5000L);

        try {
            setDataSource();
        }catch(Exception e ){
            //섬세 설정을 사용하지않을경우 에러를 처리하지않기위한 초기 변수

            logger.error(ExceptionUtil.getStackTrace(e));

        }
    }

    /**
     * data source set
     * Hikari
     */
    public void setDataSource(){
        HikariConfig config = new HikariConfig();

        config.setJdbcUrl(url);
        config.setUsername(userId);
        config.setPassword(password);
        config.setAutoCommit(isAutoCommit);
        config.setConnectionTimeout(connectionTimeOut);
        config.setValidationTimeout(validationTimeOut);
        config.setMaximumPoolSize(connectionPoolCount);
        dataSource =  new HikariDataSource(config);
    }

    /**
     * 설정정보 유효성여부
     * @param value String 설정된 값
     * @param keyMessage String 설정 키와 관련된 에러 메시지
     * @return boolean 설정값이 있는지 여부
     */
    @SuppressWarnings("BooleanMethodIsAlwaysInverted")
    private boolean isConfig(String value, String keyMessage){
        if(value == null || "".equals(value.trim())){

            logger.error("config key: [" + keyMessage +"] set");

            return false;
        }

        return true;
    }

    /**
     * connection 얻기
     * @return Connection connection pool
     * @throws SQLException SQLException
     */
    public Connection getConnection() throws SQLException {

        if(isConnectionWait){
            for(;;){
                try{
                    Connection conn = dataSource.getConnection();
                    if(conn != null){
                        return conn;
                    }
                }catch(SQLException e){
                    logger.trace(ExceptionUtil.getStackTrace(e));
                }

                try {
                    //noinspection BusyWait
                    Thread.sleep(connectionWaitTryTime);
                } catch (InterruptedException e) {
                    logger.error(ExceptionUtil.getStackTrace(e));
                }
            }
        }else{
            return dataSource.getConnection();
        }

    }

    /**
     * jdbc 종류 얻기
     * oracle mysql 등
     * @return String jdbc type oracle mysql 등
     */
    public String getJdbcType(){
        return jdbcType;
    }

    /**
     * 최신 결과가 반영된 commit connection 얻기
     * @return Connection
     * @throws SQLException SQLException
     */
    public Connection getCommitConnection() throws SQLException {
        Connection connection = getConnection();
        if(!isAutoCommit){
            connection.commit();
        }
        return connection;
    }

    /**
     * auto commit 여부 얻기
     * @return boolean auto commit flag
     */
    @SuppressWarnings("BooleanMethodIsAlwaysInverted")
    public boolean isAutoCommit() {
        return isAutoCommit;
    }

    /**
     * 연결정보로 connection 을 생성해서 활용할떄
     * @param isAutoCommit boolean  auto commit 여부
     * @return Connection
     */
    public Connection newConnection(boolean isAutoCommit){
        try {
            Connection connection = ConnectionFactory.newConnection(databaseTypeOrFullPackage, url, userId, password);
            connection.setAutoCommit(isAutoCommit);
            return connection;
        }catch(SQLException e){
            throw new SQLRuntimeException(e);
        } catch (ReflectiveOperationException e) {
            throw new ReflectiveOperationRuntimeException(e);
        }
    }

    /**
     * connectionTimeOut set milli second
     * @param connectionTimeOut long time out (milli second)
     */
    public void setConnectionTimeOut(long connectionTimeOut) {
        this.connectionTimeOut = connectionTimeOut;
    }

    /**
     * validationTimeOut set
     * @param validationTimeOut long time out (milli second)
     */
    public void setValidationTimeOut(long validationTimeOut) {
        this.validationTimeOut = validationTimeOut;
    }

    /**
     * 컨넥션이 될떄 까지 기다 릴지 여부
     * @param connectionWait boolean
     */
    public void setConnectionWait(boolean connectionWait) {
        isConnectionWait = connectionWait;
    }

    /**
     * 컨넥션 재연결 반복 시도 주기
     * @param connectionWaitTryTime long  (milli second)
     */
    public void setConnectionWaitTryTime(long connectionWaitTryTime) {
        this.connectionWaitTryTime = connectionWaitTryTime;
    }

    /**
     * auto commit set
     * @param autoCommit boolean
     */
    public void setAutoCommit(boolean autoCommit) {
        isAutoCommit = autoCommit;
    }

    /**
     * connection count set
     * @param connectionPoolCount int
     */
    public void setConnectionPoolCount(int connectionPoolCount) {
        this.connectionPoolCount = connectionPoolCount;
    }

    /**
     * jdbc type (oracle, maria, etc ...)
     * @param jdbcType String
     */
    public void setJdbcType(String jdbcType) {
        this.jdbcType = jdbcType;
    }

    /**
     * url set
     * @param url String
     */
    public void setUrl(String url) {
        this.url = url;
    }

    /**
     * user id set
     * @param userId String
     */
    public void setUserId(String userId) {
        this.userId = userId;
    }

    /**
     * password set
     * @param password String
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * 알맞는 db 유형이 아닐때
     * 유명하지 않은 DB일때 직접 전체 경로 설정
     * @param databaseTypeOrFullPackage String
     */
    public void setDatabaseTypeOrFullPackage(String databaseTypeOrFullPackage) {
        this.databaseTypeOrFullPackage = databaseTypeOrFullPackage;
    }
}
