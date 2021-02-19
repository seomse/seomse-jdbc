/*
 * Copyright (C) 2021 Seomse Inc.
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

package com.seomse.jdbc.sequence;

import java.util.HashMap;
import java.util.Map;

/**
 * 시퀀스 관리자
 * @author macle
 */
public class SequenceManager {


    private final Map<String, SequenceMaker> dbTypeMap = new HashMap<>();

    private final Object lock = new Object();

    /**
     * 시퀀스 값 얻기
     * @param sequenceName String sequence name
     * @param jdbcType db 유형
     * @return String sequence value
     */
    public String nextVal(String sequenceName, String jdbcType){
        SequenceMaker sequenceMaker =  dbTypeMap.get(jdbcType);

        if(sequenceMaker == null){
            synchronized (lock){
                sequenceMaker =  dbTypeMap.get(jdbcType);
                if(sequenceMaker == null){
                    sequenceMaker =  SequenceMakerFactory.make(jdbcType);
                    dbTypeMap.put(jdbcType, sequenceMaker);
                }
            }
        }


        return sequenceMaker.nextVal(sequenceName);

    }


    private SequenceMaker defaultMaker = null;

    /**
     * 기본 생성기 설정
     * @param jdbcType db 유형
     */
    public void setDefaultMaker(String jdbcType) {
        this.defaultMaker = SequenceMakerFactory.make(jdbcType);
    }

    /**
     * 시퀀스 값 얻기
     * @param sequenceName String sequence name
     * @return String sequence value
     */
    public String nextVal(String sequenceName){
        return defaultMaker.nextVal(sequenceName);
    }

}
