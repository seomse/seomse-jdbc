


package com.seomse.jdbc.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
/** 
 * <pre>
 *  파 일 명 : DateTime.java
 *  설    명 : 날짜시간 관련 어노테이션 
 *         
 *  작 성 자 : macle
 *  작 성 일 : 2017.08
 *  버    전 : 1.1
 *  수정이력 :  2020.07.07
 *  기타사항 :
 * </pre>
 * @author Copyrights 2017 ~ 2020 by ㈜섬세한사람들. All right reserved.
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface DateTime {
	boolean isNullable() default true;
}