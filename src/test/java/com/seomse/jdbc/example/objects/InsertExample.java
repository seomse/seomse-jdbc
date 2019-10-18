

package com.seomse.jdbc.example.objects;

import com.seomse.jdbc.objects.JdbcObjects;

import java.util.ArrayList;
import java.util.List;

/**
 * <pre>
 *  설    명 : 간단한 insert 설명
 *
 *  작 성 자 : macle
 *  작 성 일 : 2019.10.18
 *  버    전 : 1.0
 *  수정이력 :
 *  기타사항 :
 * </pre>
 * @author Copyrights 2019 by ㈜섬세한사람들. All right reserved.
 */
public class InsertExample {
	public static void main(String [] args){

		List<ExampleItem> itemList =new ArrayList<>();

		ExampleItem exampleItem= new ExampleItem();
		exampleItem.setCode("test5");
		exampleItem.setName("테스트5");
//		itemList.add(exampleItem);
		JdbcObjects.insert(exampleItem);
		
	}
}
