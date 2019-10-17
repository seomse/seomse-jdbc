package com.seomse.jdbc.example.naming;

import com.seomse.jdbc.naming.JdbcNaming;

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

        List<ItemNo> itemNoList = JdbcNaming.getObjList(ItemNo.class);


        for(ItemNo itemNo : itemNoList){
            System.out.println(itemNo.getCD_ITEM() + ", " + itemNo.getNM_ITEM());
        }


    }

}
