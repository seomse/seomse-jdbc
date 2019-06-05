


package com.seomse.jdbc.sequence;

import com.seomse.jdbc.JdbcQuery;
/** 
 * <pre>
 *  파 일 명 : OracleSequenceMaker.java
 *  설    명 : Oracle 시퀀스 생성
 *         
 *  작 성 자 : macle
 *  작 성 일 : 2017.09
 *  버    전 : 1.0
 *  수정이력 : 
 *  기타사항 :
 * </pre>
 * @author Copyrights 2017 by ㈜섬세한사람들. All right reserved.
 */
public class OracleSequenceMaker implements SequenceMaker{

	public String nextVal(String sequenceName) {
		return JdbcQuery.getResultOne("SELECT "+sequenceName+".NEXTVAL FROM DUAL");
	}
	

}