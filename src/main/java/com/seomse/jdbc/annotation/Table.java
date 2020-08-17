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

package com.seomse.jdbc.annotation;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
/**
 * 테이블 지정
 * 필수 조건
 * @author macle
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface Table {
	
	String EMPTY ="‡EMPTY";
	
	/**
	 * 테이블명
	 * @return String 테이블명
	 */
	String name() default EMPTY;
	
	/**
	 * 쿼리 
	 * @return String 쿼리
	 */
	String sql() default EMPTY;
	
	/**
	 * 조건
	 * @return String 조건
	 */
	String where()  default EMPTY;
	
	 /**
	  * 정렬
	  * @return String 정렬조건
	  */
	 String orderBy() default EMPTY;
	 

	 /**
	  * row가 너무많은경우 최대max개수 지정
	  * @return int max개수
	  */
	 int size() default -1;
	 
	 
	 /**
	  * DB에서 데이터를 가져오는 단위수
	  * @return int 데이터 단위 개수
	  */
	 int fetchSize() default 10;
	 
}