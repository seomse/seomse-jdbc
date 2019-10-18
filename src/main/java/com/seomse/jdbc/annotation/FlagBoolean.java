package com.seomse.jdbc.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <pre>
 *  파 일 명 : FlagBoolean.java
 *  설    명 : boolean char(1) 형태의 flag 값일때 활용
 *
 *  작 성 자 : macle
 *  작 성 일 : 2019.10.18
 *  버    전 : 1.0
 *  수정이력 :
 *  기타사항 :
 * </pre>
 * @author Copyrights 2019 by ㈜섬세한사람들. All right reserved.
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface FlagBoolean {
}
