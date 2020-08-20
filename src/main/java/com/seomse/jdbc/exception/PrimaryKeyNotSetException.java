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
 * PrimaryKey annotation 이 생성 되지 않았을 때
 * @author macle
 */
public class PrimaryKeyNotSetException  extends RuntimeException{
	
	
	private static final long serialVersionUID = 1450861288962787174L;
	
	private static final String message = "pkColumn add @PrimaryKeyColumn(seq = 1)";

	/**
	 * 생성자
	 * @param msg exception message
	 */
	public PrimaryKeyNotSetException(String msg){
		super(message+ msg);
	
	
	
	}
	@Override
	public String getMessage(){
		
		
		return message;
	}

}
