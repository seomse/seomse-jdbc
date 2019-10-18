package com.seomse.jdbc.example.objects;

import com.seomse.jdbc.objects.JdbcObjects;

import java.util.List;

/**
 * <pre>
 *  설    명 : 간단한 select 설명
 *
 *  작 성 자 : macle
 *  작 성 일 : 2019.10.17
 *  버    전 : 1.0
 *  수정이력 :
 *  기타사항 :
 * </pre>
 * @author Copyrights 2019 by ㈜섬세한사람들. All right reserved.
 */
public class SelectExample {

    public static void main(String[] args) {

        List<StockItem> exampleList = JdbcObjects.getObjList(StockItem.class);


        for(StockItem example : exampleList){
            System.out.println(example.getCode() + ", " + example.getName() + ", " + example.isTrade() + "," + example.getMarketType().toString());
        }


//        ExampleItem exampleItem = JdbcObjects.getObj(ExampleItem.class);
//        System.out.println(exampleItem.getCode() + ", " + exampleItem.getName());

    }

}
