package com.seomse.jdbc.example.objects;

import com.seomse.jdbc.objects.JdbcObjects;

/**
 * <pre>
 *  파 일 명 : ObjectMake.java
 *  설    명 : 객체 생성 도움 예제
 *
 *  작 성 자 : macle
 *  작 성 일 : 2019.10.17
 *  버    전 : 1.0
 *  수정이력 :
 *  기타사항 :
 * </pre>
 * @author Copyrights 2019 by ㈜섬세한사람들. All right reserved.
 */
public class ObjectMake {

    public static void main(String[] args) {

        String tableName = "TB_STOCK_ITEM";
        System.out.println(JdbcObjects.makeObjectValue(tableName));

    }
}
