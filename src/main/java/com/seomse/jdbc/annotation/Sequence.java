
package com.seomse.jdbc.annotation;
	
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
/** 
 * <pre>
 *  파 일 명 : Sequence.java
 *  설    명 : jdbc 객체관련 sequence정보
 *  		  
 *  			                  
 *  작 성 자 : macle
 *  작 성 일 : 2017.08
 *  버    전 : 1.0
 *  수정이력 :  
 *  기타사항 :
 * </pre>
 * @author Copyrights 2017 by ㈜섬세한사람들. All right reserved.
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Sequence {
	
	/**
	 * 시퀀스명
	 * @return 시퀀스명
	 */
	String name()	 ;
	
	/**
	 * 앞쪽에 붙는 문자열 
	 * @return 시퀀스 앞 문자
	 */
	String preFix() default "";
	
	 
}