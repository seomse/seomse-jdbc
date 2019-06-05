


package com.seomse.jdbc;

import com.seomse.commons.config.Config;
import com.seomse.jdbc.exception.DaoClassNotFoundException;
/** 
 * <pre>
 *  파 일 명 : JdbcDaoFactory.java
 *  설    명 : Dao 생성 
 *         
 *  작 성 자 : macle
 *  작 성 일 : 2017.09
 *  버    전 : 1.0
 *  수정이력 : 
 *  기타사항 :
 * </pre>
 * @author Copyrights 2017 by ㈜섬세한사람들. All right reserved.
 */
public class JdbcDaoFactory {

	private static final String TYPE_KEY = "application.jdbc.type";

	
	/**
	 * Dao객체 생성
	 * @param daoClass daoClass
	 * @return ImplDaoClass
	 */
	public static <T> T getDao(Class<T> daoClass){
		return getDao(daoClass, Config.getConfig(TYPE_KEY));
	}
	
	/**
	 * Moara에서 사용하는 JDBC Dao를 생성하여 돌려준다.
	 * @param daoClass daoClass
	 * @param dbType dataBaseType
	 * @return ImplDaoClass
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
			//noinspection deprecation
			t = (T) makeDaoClass.newInstance();
		}catch(Exception e){
			throw new DaoClassNotFoundException(	 "Class Not Found  " 
					+ daoClass.getName().substring(0,daoClass.getName().lastIndexOf(".dao.")) + ".jdbc." 
					+ daoClass.getSimpleName() +"Impl" + dbType + "  OR  "  + className);
		}
		return t;
	
	}
	
}