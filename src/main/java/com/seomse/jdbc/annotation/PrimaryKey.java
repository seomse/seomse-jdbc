


package com.seomse.jdbc.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
/** 
 * <pre>
 *  파 일 명 : PrimaryKey.java
 *  설    명 : 기본키관련 어노테이션
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
public @interface PrimaryKey {
	
	 int seq() default -1;
	
	 String dateFormatter() default "";
	
	
}