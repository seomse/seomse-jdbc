

package com.seomse.jdbc.naming;
/**
 * <pre>
 *  파 일 명 : JdbcColumnClass.java
 *  설    명 : 컬럼이 사용하는 자바 클래스를 보관
 *
 *  작 성 자 : macle
 *  작 성 일 : 2019.06.06
 *  버    전 : 1.0
 *  수정이력 :
 *  기타사항 :
 * </pre>
 * @author Copyrights 2019 by ㈜섬세한사람들. All right reserved.
 */
public class JdbcColumnClass {
	private final String columnName;
	private Class<?> classes;
	
	/**
	 * 생성자
	 * @param columnName String 컬럼명
	 * @param classes Class<?>   클래스
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
	 * @return  Class<?>  ColumnName To Class
	 */
	public Class<?> getClasses() {
		return classes;
	}

	/**
	 * 클래스 설정
	 * @param classes Class<?> 
	 */
	void setClasses(Class<?> classes) {
		this.classes = classes;
	}
	
	
}