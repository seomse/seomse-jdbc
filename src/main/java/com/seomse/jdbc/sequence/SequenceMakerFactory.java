

package com.seomse.jdbc.sequence;

import com.seomse.jdbc.exception.NotDbTypeException;
/** 
 * <pre>
 *  파 일 명 : SequenceMakerFactory.java
 *  설    명 : SequenceMaker 팩토리
 *         
 *  작 성 자 : macle
 *  작 성 일 : 2017.09
 *  버    전 : 1.0
 *  수정이력 : 
 *  기타사항 :
 * </pre>
 * @author Copyrights 2017 by ㈜섬세한사람들. All right reserved.
 */
public class SequenceMakerFactory {
	
	
	/**
	 * SequenceMaker 생성
	 * @param dbType database type (oracle, mysql ...)
	 * @return SequenceMaker
	 */
	public static SequenceMaker make(String dbType){
		dbType = dbType.toLowerCase();
		if(dbType.equals("oracle") || dbType.equals("tibero") ){
			return new OracleSequenceMaker();
//		}else if(dbType.equals("mssql")){
//			return new MssqlSequenceMaker();
		}else{
			throw new NotDbTypeException(dbType);
		}
		
		
		
	}
}