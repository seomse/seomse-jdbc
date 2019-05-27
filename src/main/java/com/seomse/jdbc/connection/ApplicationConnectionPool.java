package com.seomse.jdbc.connection;


import com.seomse.commons.config.Config;
import com.seomse.commons.security.login.LoginInfo;
import com.seomse.commons.security.login.LoginSecurity;
import com.seomse.commons.utils.ExceptionUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * <pre>
 *  파 일 명 : ApplicationConnectionPool.java
 *  설    명 : 어플리케이션 기본 connection pool
 *            seomse 설정 활용
 *  작 성 자 : macle
 *  작 성 일 : 2019.02
 *  버    전 : 1.0
 *  수정이력 :
 *  기타사항 :
 * </pre>
 * @atuhor Copyrights 2019 by ㈜섬세한사람들. All right reserved.
 */
public class ApplicationConnectionPool {
    private static final Logger logger = LoggerFactory.getLogger(ApplicationConnectionPool.class);

    private static class Singleton {
        private static final ApplicationConnectionPool instance = new ApplicationConnectionPool();
    }

    /**
     * 싱글인스턴스 얻기
     * @return ApplicationConnectionPool
     */
    public static ApplicationConnectionPool getInstance (){
        return Singleton.instance;
    }




    /**
     * 생성자
     * 싱글톤
     */
    private ApplicationConnectionPool(){

        try{

            setConfigConnectionInfo(false);
        }catch(Exception e){
            logger.error(ExceptionUtil.getStackTrace(e));
        }

    }

    private ConnectionPool connectionPool = null;

    /**
     * ConnectionPool 을 외부에서 만들어서 설정
     * @param connectionPool
     */
    public void setConnectionPool(ConnectionPool connectionPool) {
        this.connectionPool = connectionPool;
    }


    /**
     * 설정정보대로 connection pool 설정
     */
    public void setConfigConnectionInfo(){
        setConfigConnectionInfo(true);
    }

    /**
     * DB접속정보를 seomse_config에서 가져와서 설정
     * isErrorLog는 초기생성자에서 에러를 출력하지않기위한 로그
     * @param isErrorLog
     */
    public void setConfigConnectionInfo(boolean isErrorLog){
        String jdbcType = Config.getConfig("application.jdbc.type");
        String driverClass = Config.getConfig("application.jdbc.driver.class");

        String databaseTypeOrFullPackage = driverClass;
        if(driverClass == null){
            databaseTypeOrFullPackage = jdbcType;
        }

        if(!isConfig(databaseTypeOrFullPackage, "application.jdbc.type or application.jdbc.driver.class", isErrorLog)){
            return ;
        }



        final String connectionPoolCountKey = "application.jdbc.connection.pool.count";
        String connectionPoolCountValue =  Config.getConfig(connectionPoolCountKey);
        if(!isConfig(connectionPoolCountValue, connectionPoolCountKey, isErrorLog)){
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
            if(isErrorLog){
                logger.error("application.jdbc.connection.pool.count is not number");
            }

            return ;
        }

        int connectionPoolCount = Integer.parseInt(connectionPoolCountValue);


        final String urlKey = "application.jdbc.url";
        String url =  Config.getConfig(urlKey);
        if(!isConfig(url, urlKey, isErrorLog)){
            return ;
        }


        final String encryptFlagKey = "application.jdbc.login.encrypt";
        String encryptFlag =  Config.getConfig(encryptFlagKey);
        if(!isConfig(encryptFlag, encryptFlagKey, isErrorLog)){
            return ;
        }


        final String userIdKey = "application.jdbc.user.id";
        String userId =  Config.getConfig(userIdKey);
        if(!isConfig(userId, userIdKey, isErrorLog)){
            return ;
        }

        final String userPasswordKey = "application.jdbc.user.password";
        String userPassword =  Config.getConfig(userPasswordKey);
        if(!isConfig(userPassword, userPasswordKey, isErrorLog)){
            return ;
        }




        url = url.trim();


        encryptFlag = encryptFlag.trim();
        userId = userId.trim();
        userPassword = userPassword.trim();

        encryptFlag = encryptFlag.toUpperCase();
        if("Y".equals(encryptFlag)){
            LoginInfo loginInfo = LoginSecurity.decryption(userId, userPassword);
            userId = loginInfo.getId();
            userPassword = loginInfo.getPassword();
        }



        try {
            connectionPool = new ConnectionPool(jdbcType, driverClass, url, userId, userPassword, connectionPoolCount, Config.getBoolean("application.jdbc.connection.auto.commit.flag" , false));
            connectionPool.setValidTime(Config.getInteger("application.jdbc.connection.valid.time", 3000));
            connectionPool.setReconnection(Config.getBoolean("application.jdbc.reconnection.flag", true));
            connectionPool.setReconnectionTryTime(Config.getLong("application.jdbc.reconnection.try.time", 60000L));
            connectionPool.setReconnectionErrorLog(Config.getBoolean("application.jdbc.reconnection.error.log.flag", true));
        }catch(Exception e ){
            //섬세 설정을 사용하지않을경우 에러를 처리하지않기위한 초기 변수
            if(isErrorLog){
                logger.error(ExceptionUtil.getStackTrace(e));
            }
        }

    }

    /**
     * 설정정보 유효성여부
     * @param value 설정된 값
     * @param keyMessage 설정 키와 관련된 에러 메시지
     * @param isErrorLog 에러로그 출력여부
     * @return 설정값이 있는지 여부
     */
    private boolean isConfig(String value, String keyMessage, boolean isErrorLog){
        if(value == null || "".equals(value.trim())){
            if(isErrorLog){
                logger.error("config key: [" + keyMessage +"] set");
            }
            return false;
        }

        return true;
    }

    /**
     * connection 얻기
     * @return connection
     * @throws SQLException
     */
    public Connection getConnection() throws SQLException {
        return connectionPool.getConnection();
    }

    /**
     * jdbc 종류 얻기
     * oracle mysql 등
     * @return jdbc oracle mysql 등
     */
    public String getJdbcType(){
        return connectionPool.getJdbcType();
    }

    /**
     * connectionPool 얻기
     * @return
     */
    public ConnectionPool getConnectionPool(){
        return connectionPool;
    }


    /**
     * commit 되어 정보가 반영된 connection얻기
     * @return
     * @throws SQLException
     */
    public Connection getCommitConnection() throws SQLException {
        return connectionPool.getCommitConnection();

    }

}
