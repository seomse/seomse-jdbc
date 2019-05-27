

package com.seomse.jdbc.annotation;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
/**
 * <pre>
 *  파 일 명 : Table.java
 *  설    명 : Table에대한 기본옵션을 가지고있는 어노테이션
 *
 *  작 성 자 : macle
 *  작 성 일 : 2017.08
 *  버    전 : 1.0
 *  수정이력 :
 *  기타사항 :
 * </pre>
 * @author Copyrights 2017 by ㈜섬세한사람들. All right reserved.
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface Table {
	
	String EMPTY ="‡EMPTY";
	
	/**
	 * 테이블명
	 * @return 테이블명
	 */
	String name() default EMPTY;
	
	/**
	 * 쿼리 
	 * @return 쿼리
	 */
	String sql() default EMPTY;
	
	/**
	 * 조건
	 * @return 조건
	 */
	 String where()  default EMPTY;
	
	 /**
	  * 정렬
	  * @return 정렬조건
	  */
	 String orderBy() default EMPTY;
	 
	 
	 
	 /**
	  * row가 너무많은경우 최대max개수 지정
	  * @return max개수
	  */
	 int size() default -1;
	 
	 
	 /**
	  * DB에서 데이터를 가져오는 단위수
	  * @return 데이터 단위 개수
	  */
	 int fetchSize() default 10;
	 
}