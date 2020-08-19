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
 *  파 일 명 : TableNameEmptyException.java
 *  설    명 : 클래에서 Table 어노테이션의 name을 사용해야하는데 테이블네임설정이 되어있지않으면 발생하는 예외상황
 *         
 *  작 성 자 : macle
 *  작 성 일 : 2017.09
 *  버    전 : 1.0
 *  수정이력 : 
 *  기타사항 :
 * </pre>
 * @author Copyrights 2017 by ㈜섬세한사람들. All right reserved.
 */
public class TableNameEmptyException extends RuntimeException{
	

	private static final long serialVersionUID = -6317055714276205170L;
	private String message;
	public TableNameEmptyException(String message){
		super(message);
		this.message = message + " class table Name Empty";
	
	}
	@Override
	public String getMessage(){
		
		
		return message;
	}

}