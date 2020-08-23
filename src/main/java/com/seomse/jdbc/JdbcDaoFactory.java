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

import com.seomse.commons.config.Config;
import com.seomse.jdbc.exception.DaoClassNotFoundException;
/**
 * JdbcDaoFactory
 * @author macle
 */
public class JdbcDaoFactory {

	private static final String TYPE_KEY = "application.jdbc.type";


	/**
	 * dao implement get
	 * @param daoClass  daoClass
	 * @param <T> ImplDaoClass
	 * @return T ImplDaoClass
	 */
	public static <T> T getDao(Class<T> daoClass){
		return getDao(daoClass, Config.getConfig(TYPE_KEY));
	}

	/**
	 * dao implement get
	 * @param daoClass Class  daoClass
	 * @param dbType string dataBaseType
	 * @param <T> ImplDaoClass
	 * @return T ImplDaoClass
	 */
	@SuppressWarnings("unchecked")
	public static <T> T getDao(Class<T> daoClass, String dbType){
	
		dbType = dbType.toLowerCase();
		dbType = Character.toUpperCase(dbType.charAt(0)) + dbType.substring(1);
	
		String className = daoClass.getName().substring(0,daoClass.getName().lastIndexOf(".dao.")) + ".jdbc." 
						+ daoClass.getSimpleName() +"Impl" + dbType;
		
		
		Class<?> makeDaoClass ;
		try {
			makeDaoClass =Class.forName(className);			
		} catch (ClassNotFoundException e) {
			className = daoClass.getName().substring(0,daoClass.getName().lastIndexOf(".dao.")) + ".jdbc." 
					+ daoClass.getSimpleName() +"Impl";
			try{
				makeDaoClass =Class.forName(className);
			}catch(ClassNotFoundException makeE){
			
				throw new DaoClassNotFoundException(	 "Class Not Found  " 
						+ daoClass.getName().substring(0,daoClass.getName().lastIndexOf(".dao.")) + ".jdbc." 
						+ daoClass.getSimpleName() +"Impl" + dbType + "  OR  "  + className);
			}
		}
		T t ;
		try{

			t = (T) makeDaoClass.newInstance();
		}catch(Exception e){
			throw new DaoClassNotFoundException(	 "Class Not Found  " 
					+ daoClass.getName().substring(0,daoClass.getName().lastIndexOf(".dao.")) + ".jdbc." 
					+ daoClass.getSimpleName() +"Impl" + dbType + "  OR  "  + className);
		}
		return t;
	
	}
	
}