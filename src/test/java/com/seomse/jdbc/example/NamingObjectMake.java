
package com.seomse.jdbc.example;

import com.seomse.commons.config.Config;
import com.seomse.jdbc.naming.JdbcNaming;

/**
 * <pre>
 *  파 일 명 : NamingObjectMaker.java
 *  설    명 : NamingObject 생성
 *
 *  작 성 자 : macle
 *  작 성 일 : 2017.10
 *  버    전 : 1.0
 *  수정이력 :
 *  기타사항 :
 * </pre>
 * @author Copyrights 2017 by ㈜섬세한사람들. All right reserved.
 */
public class NamingObjectMake {

	public static void main(String [] args){

		try{

		String tableName = "TB_STOCK_ITEM";
		System.out.println("@Table(name=\"" +  tableName+ "\")\n");
		System.out.println(JdbcNaming.makeObjectValue(tableName));
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
}
