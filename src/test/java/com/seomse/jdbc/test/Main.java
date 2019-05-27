

package com.seomse.jdbc.test;

import com.seomse.jdbc.naming.JdbcNaming;
/**
 * <pre>
 *  설    명 : 간단한 insert 설명
 *
 *  작 성 자 : macle
 *  작 성 일 : 2017.09
 *  버    전 : 1.0
 *  수정이력 :
 *  기타사항 :
 * </pre>
 * @author Copyrights 2017 by ㈜섬세한사람들. All right reserved.
 */
public class Main {
	public static void main(String [] args){
		ItemNo itemNo= new ItemNo();
		itemNo.setCD_ITEM("test");
		JdbcNaming.insert(itemNo);
		

		
		
	}
}
