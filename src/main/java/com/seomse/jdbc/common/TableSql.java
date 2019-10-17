package com.seomse.jdbc.common;

import com.seomse.jdbc.annotation.Table;

/**
 * <pre>
 *  파 일 명 : TableUtil.java
 *  설    명 : annotation Table 관련 유틸성 클래스
 *
 *  작 성 자 : macle
 *  작 성 일 : 2019.10.17
 *  버    전 : 1.0
 *  수정이력 :
 *  기타사항 :
 * </pre>
 * @author Copyrights 2019 by ㈜섬세한사람들. All right reserved.
 */
public class TableSql {

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

}
