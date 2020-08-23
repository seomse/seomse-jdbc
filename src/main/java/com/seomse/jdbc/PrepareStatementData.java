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
package com.seomse.jdbc;

import com.seomse.jdbc.naming.JdbcDataType;
/**
 * data 유형과  data
 * @author macle
 */
public class PrepareStatementData {
	private JdbcDataType type;
	private Object data;
	/**
	 * 데이터 타입 얻기
	 * @return JdbcDataType
	 */
	public JdbcDataType getType() {
		return type;
	}
	/**
	 * 데이터 타입 설정
	 * @param type JdbcDataType
	 */
	public void setType(JdbcDataType type) {
		this.type = type;
	}
	/**
	 * 데이터 얻기
	 * @return Object 데이터 타입에 유효한 데이터
	 */
	public Object getData() {
		return data;
	}
	/**
	 * 데이터 설정
	 * @param data Object 데이터 타입에 유효한 데이터
	 */
	public void setData(Object data) {
		this.data = data;
	}




}