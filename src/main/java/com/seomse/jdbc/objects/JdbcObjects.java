package com.seomse.jdbc.objects;

import com.seomse.commons.packages.classes.field.FieldUtil;
import com.seomse.jdbc.annotation.Column;
import com.seomse.jdbc.annotation.Table;
import com.seomse.jdbc.exception.FieldNullException;
import com.seomse.jdbc.exception.TableNameEmptyException;
import com.seomse.jdbc.naming.PrepareStatementData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.*;

/**
 * <pre>
 *  파 일 명 : JdbcObjects.java
 *  설    명 : Jdbc 자바형객체
 *
 *  작 성 자 : macle
 *  작 성 일 : 2019.10.17
 *  버    전 : 1.0
 *  수정이력 :
 *  기타사항 :
 * </pre>
 * @author Copyrights 2019 by ㈜섬세한사람들. All right reserved.
 */
public class JdbcObjects {

    private static final Logger logger = LoggerFactory.getLogger(JdbcObjects.class);


    public static <T> List<T> getObjList(Connection conn, Class<T> objClass , String sql, String whereValue, String orderByValue, int size, Map<Integer, PrepareStatementData> prepareStatementDataMap) throws IllegalAccessException, SQLException, InstantiationException {

        List<T> resultList = new ArrayList<>();


        Table table = objClass.getAnnotation(Table.class);

        Field[] fields = FieldUtil.getFieldArrayToParents(objClass);


        Map<String, Field> columnFieldMap = new HashMap<>();

        for(Field field: fields){
            Column column = field.getAnnotation(Column.class);

            if(column != null){
                String name = column.name();
                if("".equals(name)){
                    columnFieldMap.put(field.getName(), field);
                }else{
                    columnFieldMap.put(name, field);
                }

            }
        }

        String selectSql;
        if(sql == null) {
            selectSql = getSql(objClass, table, columnFieldMap.keySet(), whereValue, orderByValue);
        }else{
            selectSql = sql;
        }




        return null;

    }

    /**
     * sql 얻기
     * @return sql
     */
    public static <T> String getSql(Class<T> objClass, Table table , Set<String> columnNameSet , String whereValue, String orderByValue){
        StringBuilder sqlBuilder = new StringBuilder();

        String tableSql = table.sql();
        if(tableSql.equals(Table.EMPTY)){

            if(table.name().equals(Table.EMPTY)){
                throw new TableNameEmptyException(objClass.getName());
            }

            if(columnNameSet == null || columnNameSet.size() ==0){
                throw new FieldNullException(objClass.getName());
            }
            StringBuilder fieldBuilder = new StringBuilder();

            for(String columnName : columnNameSet){
                fieldBuilder.append(", ").append(columnName);
            }
            sqlBuilder.append("SELECT ");
            sqlBuilder.append(fieldBuilder.toString().substring(1));
            sqlBuilder.append(" FROM ").append(table.name());
        }else{
            sqlBuilder.append(tableSql);
        }

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
