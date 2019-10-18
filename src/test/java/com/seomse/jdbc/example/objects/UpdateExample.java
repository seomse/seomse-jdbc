package com.seomse.jdbc.example.objects;

import com.seomse.jdbc.objects.JdbcObjects;

/**
 * <pre>
 *  파 일 명 : UpdateExample.java
 *  설    명 : 간단한 업데이트 예제
 *
 *  작 성 자 : macle
 *  작 성 일 : 2019.10.18
 *  버    전 : 1.0
 *  수정이력 :
 *  기타사항 :
 * </pre>
 * @author Copyrights 2019 by ㈜섬세한사람들. All right reserved.
 */
public class UpdateExample {
    public static void main(String [] args){
        ExampleItem exampleItem= new ExampleItem();
        exampleItem.setCode("test5");
        exampleItem.setName("테스트5uuu");
//        JdbcObjects.insertOrUpdate(exampleItem, false);
        JdbcObjects.update(exampleItem, false);
    }
}
