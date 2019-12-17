package com.seomse.jdbc.connection;

import com.seomse.commons.utils.ExceptionUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.SQLException;


/**
 * <pre>
 *  파 일 명 : ConnectionPool.java
 *  설    명 : 컨넥션 생성
 *
 *  작 성 자 : macle
 *  작 성 일 : 2019.02
 *  버    전 : 1.0
 *  수정이력 :
 *  기타사항 :
 * </pre>
 * @author Copyrights 2019 by ㈜섬세한사람들. All right reserved.
 */
public class ConnectionPool {



    private static final Logger logger = LoggerFactory.getLogger(ConnectionPool.class);


    private Driver driver;


    private String jdbcType;


    private String url;

    private String user;

    private String password;

    private int count;

    private boolean isAutoCommit;

    private Connection [] connections;



    private boolean isReconnectionErrorLog = true;

    /**
     * 재연결 시도에 따른 에러로그 여부
     * @param reconnectionErrorLog 재연결 시도에 따른 에러로그 출력 여부
     */
    public void setReconnectionErrorLog(boolean reconnectionErrorLog) {
        isReconnectionErrorLog = reconnectionErrorLog;
    }

    /**
     * 생성자
     * @param jdbcType oracle,tibero,mysql,mssql,maria,sqlite,drill,hive
     * @param driverClass unknown type class , known type = null
     * @param user user id
     * @param password user password
     * @param count poll count
     * @param isAutoCommit autoCommit
     */
    public ConnectionPool(String jdbcType, String driverClass, String url, String user, String password, int count, boolean isAutoCommit) throws IllegalAccessException, ClassNotFoundException, InstantiationException, SQLException {


        if(count < 1){

            throw new RuntimeException("count > 0  --> " +  count);
        }

        this.jdbcType = jdbcType;

        this.url = url;
        this.user = user;
        this.password = password;

        this. count = count;

        this.isAutoCommit = isAutoCommit;


        String typeOrDriverClass;

        if(driverClass == null){
            typeOrDriverClass = jdbcType;
        }else{
            typeOrDriverClass = driverClass;
        }

        driver = ConnectionFactory.newDriver(typeOrDriverClass);

        connect();
    }


    private boolean isReconnectionWait = false;

    private long reconnectionTryTime = 60000L;

    /**
     * 연결이 될때까지 대기 여부
     * @return isReconnectionWait
     */
    public boolean isReconnectionWait() {
        return isReconnectionWait;
    }

    /**
     * 재연결 대기 설정
     * @param reconnectionWait 연결대기여부
     */
    public void setReconnectionWait(boolean reconnectionWait) {
        isReconnectionWait = reconnectionWait;
    }

    /**
     * 재연결 시도 타임(1/1000츄) 얻기
     * @return  재연결 시도 타임(1/1000츄)
     */
    public long getReconnectionTryTime() {
        return reconnectionTryTime;
    }

    /**
     * 재연결 시도 타임 설정
     * @param reconnectionTryTime  재연결 시도 타임(1/1000츄)
     */
    public void setReconnectionTryTime(long reconnectionTryTime) {
        this.reconnectionTryTime = reconnectionTryTime;
    }



    private boolean isReconnection = true;


    /**
     * 재연결 여부 설정
     * @param reconnection 재연결
     */
    public void setReconnection(boolean reconnection) {
        isReconnection = reconnection;
    }

    private int validTime = 3000;

    /**
     * 연결유효 체크시간 설정
     * @param validTime 연결 유효 체크 시간
     */
    public void setValidTime(int validTime) {
        this.validTime = validTime;
    }



    private final Object lock = new Object();

    private int connectionIndex = -1;

    public Connection getConnection(){

        synchronized (lock){
            connectionIndex++;
            if(connectionIndex >= connections.length ){
                connectionIndex = 0;
            }

            if(isReconnection){

                if(isReconnectionWait){
                    //연결대기
                    for(;;){
                        try{
                            if(connections[connectionIndex].isValid(validTime)){
                                return  connections[connectionIndex];
                            }
                            logger.debug("reconnection try");
                            if(reconnect()){
                                return  connections[connectionIndex];
                            }


                            Thread.sleep(reconnectionTryTime);
                        }catch(SQLException | InterruptedException e){
                            if(isReconnectionErrorLog){
                                logger.error(ExceptionUtil.getStackTrace(e));
                            }

                        }
                    }

                }else{
                    reconnect();
                    return  connections[connectionIndex];
                }

            }else{
                return connections[connectionIndex];
            }
        }
    }

    private boolean reconnect(){
        //noinspection CatchMayIgnoreException
        try{connections[connectionIndex].close(); connections[connectionIndex]= null;}catch(Exception e){}
        try {
            connections[connectionIndex] = newConnection(isAutoCommit);

            return connections[connectionIndex].isValid(validTime);

        }catch(Exception e){
            if(isReconnectionErrorLog){
                logger.error(ExceptionUtil.getStackTrace(e));
            }
            return false;
        }

    }


    /**
     * 연결
     */
    public void connect() throws SQLException {

        synchronized (lock) {
            if (connections != null) {
                close();
            }
            connections = new Connection[count];
            for (int i = 0; i < count; i++) {
                connections[i] = newConnection(isAutoCommit);
           }
        }
    }


    /**
     * connection 생성
     * @return connection
     */
    public Connection newConnection(boolean isAutoCommit) throws SQLException {

        Connection connection  = ConnectionFactory.newConnection(driver, url, user, password);
        connection.setAutoCommit(isAutoCommit);
        return connection;
    }

    /**
     * auto commit 여부
     * @return isAutoCommit
     */
    @SuppressWarnings("BooleanMethodIsAlwaysInverted")
    public boolean isAutoCommit(){
        return isAutoCommit;
    }

    /**
     * 연결 종료
     */
    public void close(){

        synchronized (lock) {
            if (connections == null) {
                return;
            }

            for (int i = 0; i < connections.length; i++) {
                //noinspection CatchMayIgnoreException
                try {
                    connections[i].close();
                } catch (Exception e) {}
                connections[i] = null;
            }

            connections = null;
        }
    }

    /**
     * jdbc 종류 얻기
     * oracle mysql 등
     * @return jdbc oracle mysql 등
     */
    public String getJdbcType(){
        return jdbcType;
    }


    /**
     * commit 되어 정보가 반영된 connection얻기
     * @return CommitConnection
     */
    public Connection getCommitConnection() throws SQLException {
        Connection conn = getConnection();
        if(!isAutoCommit){
            conn.commit();
        }

        return conn;

    }

    public void commit(Connection conn) throws SQLException {
        if(!isAutoCommit){
            conn.commit();
        }

    }

}