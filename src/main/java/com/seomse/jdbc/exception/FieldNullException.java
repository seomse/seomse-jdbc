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