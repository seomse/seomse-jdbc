

package com.seomse.jdbc.exception;
/** 
 * <pre>
 *  파 일 명 : DaoClassNotFoundException.java
 *  설    명 : DaoClass 구현체가 없을때
 *         
 *  작 성 자 : macle
 *  작 성 일 : 2017.09
 *  버    전 : 1.0
 *  수정이력 : 
 *  기타사항 :
 * </pre>
 * @author Copyrights 2017 by ㈜모아라. All right reserved.
 */
public class DaoClassNotFoundException  extends RuntimeException{
	/**
	 * 
	 */
	private static final long serialVersionUID = 220903641674816765L;
	
	private String message;
	public DaoClassNotFoundException(String message){
		super(message);
		this.message = message;
	
	}


	@Override
	public String getMessage(){
		
		
		return message;
	}
	
}