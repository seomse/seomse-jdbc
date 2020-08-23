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
 * PrepareStatementData 를 활용 하는 유틸성 클래서
 * @author macle
 */
public class PrepareStatements {
    /**
     * time 을 활용한 조건 생성
     * @param time long unix time
     * @return Map
     */
    public static Map<Integer, PrepareStatementData> newTimeMap(long time){

        Map<Integer, PrepareStatementData> prepareStatementDataMap = new HashMap<>();
        PrepareStatementData prepareStatementData = new PrepareStatementData();
        prepareStatementData.setData(time);
        prepareStatementData.setType(JdbcDataType.DATE_TIME);
        prepareStatementDataMap.put(1, prepareStatementData);

        return prepareStatementDataMap;

    }
}
