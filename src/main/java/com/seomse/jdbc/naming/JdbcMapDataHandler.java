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
package com.seomse.jdbc.naming;


import java.util.Map;

/**
 * 한번에 select 된 데이터가 많을때 row 단위로 처리 해야 하는 경우
 * @author macle
 */
public interface JdbcMapDataHandler {

    /**
     * jdbc map data receive
     * @param data Map<String, Object> jdbc map data
     */
    void receive(Map<String, Object> data);
}
