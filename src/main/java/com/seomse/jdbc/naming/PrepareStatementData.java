

package com.seomse.jdbc.naming;
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
	 * @return
	 */
	public JdbcDataType getType() {
		return type;
	}
	/**
	 * 데이터 타입 설정
	 * @param type
	 */
	public void setType(JdbcDataType type) {
		this.type = type;
	}
	/**
	 * 데이터 얻기
	 * @return
	 */
	public Object getData() {
		return data;
	}
	/**
	 * 데이터 설정
	 * 
	 * @param data
	 */
	public void setData(Object data) {
		this.data = data;
	}
	
	
}