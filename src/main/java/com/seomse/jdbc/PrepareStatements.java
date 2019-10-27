package com.seomse.jdbc;

import com.seomse.jdbc.naming.JdbcDataType;

import java.util.HashMap;
import java.util.Map;

/**
 * <pre>
 *  파 일 명 : PrepareStatements.java
 *  설    명 : PrepareStatement 용 유틸성 클래스
 *
 *  작 성 자 : macle
 *  작 성 일 : 2019.10.27
 *  버    전 : 1.0
 *  수정이력 :
 *  기타사항 :
 * </pre>
 * @author Copyrights 2019 by ㈜섬세한사람들. All right reserved.
 */
public class PrepareStatements {
    public static Map<Integer, PrepareStatementData> newTimeMap(long time){

        Map<Integer, PrepareStatementData> prepareStatementDataMap = new HashMap<>();
        PrepareStatementData prepareStatementData = new PrepareStatementData();
        prepareStatementData.setData(time);
        prepareStatementData.setType(JdbcDataType.DATE_TIME);
        prepareStatementDataMap.put(1, prepareStatementData);

        return prepareStatementDataMap;

    }
}
