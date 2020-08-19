/*
 * Copyright (C) 2020 Seomse Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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
	
	private static final String message = "pkColumn add @PrimaryKeyColumn(seq = 1)";

	/**
	 * 생성자
	 * @param msg 메시지
	 */
	public PrimaryKeyNotSetException(String msg){
		super(message+ msg);
	
	
	
	}
	@Override
	public String getMessage(){
		
		
		return message;
	}

}
