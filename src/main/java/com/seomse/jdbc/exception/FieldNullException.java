



package com.seomse.jdbc.exception;
/** 
 * <pre>
 *  파 일 명 : FieldNullException.java
 *  설    명 : Object내에 필드가 하나도없을떄의 예외상황
 *         
 *  작 성 자 : macle
 *  작 성 일 : 2017.09
 *  버    전 : 1.0
 *  수정이력 : 
 *  기타사항 :
 * </pre>
 * @author Copyrights 2017 by ㈜섬세한사람들. All right reserved.
 */
public class FieldNullException extends RuntimeException{
	
	
	private static final long serialVersionUID = 6355980120432389512L;
	private String message;
	public FieldNullException(String message){
		super(message + " is filed not found");
		this.message = message + " is filed not found";
	
	}
	@Override
	public String getMessage(){
		
		
		return message;
	}
	
}