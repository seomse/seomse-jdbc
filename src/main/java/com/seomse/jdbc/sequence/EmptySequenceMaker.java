package com.seomse.jdbc.sequence;
/**
 * <pre>
 *  파 일 명 : EmptySequenceMaker.java
 *  설    명 : 시퀀스값을 돌려주지 못하는 시퀀스메이커
 *            지원하지 못하는 데이터베이스일떄 생성된다
 *
 *  작 성 자 : macle
 *  작 성 일 : 2017.09
 *  버    전 : 1.0
 *  수정이력 :
 *  기타사항 :
 * </pre>
 * @author Copyrights 2017 by ㈜섬세한사람들. All right reserved.
 */
public class EmptySequenceMaker implements SequenceMaker {
    @Override
    public String nextVal(String sequenceName) {
        return null;
    }
}
