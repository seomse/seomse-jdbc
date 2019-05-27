


package com.seomse.jdbc.sequence;
/** 
 * <pre>
 *  파 일 명 : SequenceMaker.java
 *  설    명 : 시퀀스 생성
 *         
 *  작 성 자 : macle
 *  작 성 일 : 2017.09
 *  버    전 : 1.0
 *  수정이력 : 
 *  기타사항 :
 * </pre>
 * @author Copyrights 2017 by ㈜섬세한사람들. All right reserved.
 */
public interface SequenceMaker {
	/**
	 * 시퀀스값 얻기
	 * @return 시퀀스값 (next)
	 */
	String nextVal(String sequenceName);
}