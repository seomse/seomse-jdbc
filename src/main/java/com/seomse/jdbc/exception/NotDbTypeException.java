

package com.seomse.jdbc.exception;
/** 
 * <pre>
 *  파 일 명 : NotDbTypeException.java
 *  설    명 : DB 타입이 정의되지 않았을때
 *         
 *  작 성 자 : macle
 *  작 성 일 : 2017.09
 *  버    전 : 1.0
 *  수정이력 : 
 *  기타사항 :
 * </pre>
 * @atuhor Copyrights 2017 by ㈜섬세한사람들. All right reserved.
 */
public class NotDbTypeException extends RuntimeException{
	
	

	private static final long serialVersionUID = -4829387423444122854L;
	
	private String message;
	
	
	public NotDbTypeException(String message){
		super(message + " (db type is not defined)");
		this.message = message + " (db type is not defined)";
	
	}
	@Override
	public String getMessage(){
		
		
		return message;
	}
}