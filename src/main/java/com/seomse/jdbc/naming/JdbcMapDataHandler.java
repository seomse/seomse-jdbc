package com.seomse.jdbc.naming;


import java.util.Map;

/**
 * <pre>
 *  파 일 명 : JdbcMapDataHandler.java
 *  설    명 : jdbc data 핸들링
 *
 *  작 성 자 : macle
 *  작 성 일 : 2019.06.06
 *  버    전 : 1.0
 *  수정이력 :
 *  기타사항 :
 * </pre>
 * @author Copyrights 2019 by ㈜섬세한사람들. All right reserved.
 */
public interface JdbcMapDataHandler {

    /**
     * jdbc map data receive
     * @param data jdbc map data
     */
    void receive(Map<String, Object> data);
}
