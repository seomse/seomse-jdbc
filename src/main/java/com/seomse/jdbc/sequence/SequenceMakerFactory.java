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
package com.seomse.jdbc.sequence;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <pre>
 *  파 일 명 : SequenceMakerFactory.java
 *  설    명 : SequenceMaker 팩토리
 *         
 *  작 성 자 : macle
 *  작 성 일 : 2017.09
 *  버    전 : 1.1
 *  수정이력 :
 *  2019.12.19
 *  maria support
 *  기타사항 :
 * </pre>
 * @author Copyrights 2017 by ㈜섬세한사람들. All right reserved.
 */
public class SequenceMakerFactory {

	private static final Logger logger = LoggerFactory.getLogger(SequenceMakerFactory.class);

	/**
	 * SequenceMaker 생성
	 * @param dbType database type (oracle, mysql ...)
	 * @return SequenceMaker
	 */
	public static SequenceMaker make(String dbType){
		dbType = dbType.toLowerCase();
		if(dbType.equals("oracle") || dbType.equals("tibero") ){
			return new OracleSequenceMaker();
//		}else if(dbType.equals("mssql")){
//			return new MssqlSequenceMaker();
		}else if(dbType.startsWith("maria")){
			return new MariaSequenceMaker();
		}else{
//			throw new NotDbTypeException(dbType);

			logger.error("Not supported SequenceMaker DB type.");
			return new EmptySequenceMaker();
		}
		
		
		
	}
}