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
package com.seomse.jdbc.common;

import com.seomse.jdbc.annotation.Table;
import com.seomse.jdbc.exception.TableNameEmptyException;

/**
 * table annotation 을 활용한 자동 쿼리 생성
 * @author macle
 */
public class TableSql {

    /**
     * table annotation 을 활용하여 생성한 쿼리 얻기 
     * @param table Table annotation
     * @param whereValue String
     * @param orderByValue String
     * @return String query
     */
    public static String getWhereOrderBySql(Table table, String whereValue, String orderByValue){

        StringBuilder sqlBuilder = new StringBuilder();

        if( whereValue != null){
            sqlBuilder.append(" WHERE ").append(whereValue);
        }else{
            String where = table.where();
            if(!where.equals(Table.EMPTY)){
                sqlBuilder.append(" WHERE ").append(where);
            }
        }

        if(orderByValue != null) {
            sqlBuilder.append(" ORDER BY ").append(orderByValue);
        }else {
            String orderBy = table.orderBy();
            if(!orderBy.equals(Table.EMPTY)){
                sqlBuilder.append(" ORDER BY ").append(orderBy);
            }
        }
        return sqlBuilder.toString();
    }

    /**
     * 테이블 명 얻기
     * @param table Table  annotation
     * @param objClassName String class name
     * @return String table name
     */
    public static String getTableName(Table table, String objClassName){
        String tableName = table.name();
        if(tableName.equals(Table.EMPTY)){
            throw new TableNameEmptyException(objClassName);
        }
        return tableName;
    }

}
