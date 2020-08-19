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
 * <pre>
 *  파 일 명 : PrepareStatementData.java
 *  설    명 : JDBC에서 사용하는 데이터 타입
 *         
 *  작 성 자 : macle
 *  작 성 일 : 2017.09
 *  버    전 : 1.0
 *  수정이력 : 
 *  기타사항 :
 * </pre>
 * @author Copyrights 2017 by ㈜섬세한사람들. All right reserved.
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
	 * @return 데이터 타입에 유효한 데이터
	 */
	public Object getData() {
		return data;
	}
	/**
	 * 데이터 설정
	 * @param data 데이터 타입에 유효한 데이터
	 */
	public void setData(Object data) {
		this.data = data;
	}




}