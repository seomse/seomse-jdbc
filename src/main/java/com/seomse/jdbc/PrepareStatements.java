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
