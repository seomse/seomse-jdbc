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
 * table name annotation 이 없을떄
 * @author macle
 */
public class TableNameEmptyException extends RuntimeException{
	

	private static final long serialVersionUID = -6317055714276205170L;
	private final String message;
	/**
	 * 생성자
	 * @param message String exception message
	 */
	public TableNameEmptyException(String message){
		super(message);
		this.message = message + " class table Name Empty";
	}
	@Override
	public String getMessage(){
		return message;
	}

}