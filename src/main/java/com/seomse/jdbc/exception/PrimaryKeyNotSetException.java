


package com.seomse.jdbc.exception;
/** 
 * <pre>
 *  파 일 명 : PrimaryKeyNotSetException.java
 *  설    명 : 기본키가 클래스에 설정되어 있지않을때 발생하는 예외
 *         
 *  작 성 자 : macle
 *  작 성 일 : 2017.09
 *  버    전 : 1.0
 *  수정이력 : 
 *  기타사항 :
 * </pre>
 * @author Copyrights 2017 by ㈜모아라. All right reserved.
 */
public class PrimaryKeyNotSetException  extends RuntimeException{
	
	
	private static final long serialVersionUID = 1450861288962787174L;
	
	private static final String message = "pkColunm add @PrimaryKeyColumn(seq = 1)";
	public PrimaryKeyNotSetException(){
		super(message);
	
	
	
	}
	@Override
	public String getMessage(){
		
		
		return message;
	}

}
