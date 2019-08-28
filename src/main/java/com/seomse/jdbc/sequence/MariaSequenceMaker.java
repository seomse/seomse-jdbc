package com.seomse.jdbc.sequence;

import com.seomse.jdbc.JdbcQuery;

/**
 * <pre>
 *  파 일 명 : MariaSequenceMaker.java
 *  설    명 : maria db sequence maker
 *
 *  작 성 자 : macle
 *  작 성 일 : 2019.09
 *  버    전 : 1.0
 *  수정이력 :
 *  기타사항 :
 * </pre>
 * @author Copyrights 2019 by ㈜섬세한사람들. All right reserved.
 */
public class MariaSequenceMaker implements SequenceMaker{

    @Override
    public String nextVal(String sequenceName) {
        return JdbcQuery.getResultOne("SELECT NEXT VALUE FOR " + sequenceName);
    }

}