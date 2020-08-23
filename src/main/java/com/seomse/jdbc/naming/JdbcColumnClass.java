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


package com.seomse.jdbc.naming;
/**
 * jdbc column class
 * @author macle
 */
public class JdbcColumnClass {
	private final String columnName;
	private Class<?> classes;
	
	/**
	 * 생성자
	 * @param columnName String 컬럼명
	 * @param classes Class   클래스
	 */
	JdbcColumnClass(String columnName, Class<?> classes){
		this.columnName = columnName;
		this.classes = classes;
	}
	
	/**
	 * 컬럼이름 얻기
	 * @return String Column Name
	 */
	public String getColumnName() {
		return columnName;
	}
	/**
	 * 컬럼 이름에 맞는 클래스를 얻기.
	 * @return  Class  ColumnName To Class
	 */
	public Class<?> getClasses() {
		return classes;
	}

	/**
	 * 클래스 설정
	 * @param classes Class
	 */
	void setClasses(Class<?> classes) {
		this.classes = classes;
	}
	
	
}