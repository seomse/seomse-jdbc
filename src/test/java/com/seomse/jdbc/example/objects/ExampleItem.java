package com.seomse.jdbc.example.objects;

import com.seomse.jdbc.annotation.Column;
import com.seomse.jdbc.annotation.PrimaryKey;
import com.seomse.jdbc.annotation.Table;

/**
 * <pre>
 *  파 일 명 : RowDataCopy.java
 *  설    명 : data copy example
 *
 *  작 성 자 : macle
 *  작 성 일 : 2019.06.07
 *  버    전 : 1.0
 *  수정이력 :
 *  기타사항 :
 * </pre>
 * @author Copyrights 2019 by ㈜섬세한사람들. All right reserved.
 */
@Table(name="TB_STOCK_ITEM")
public class ExampleItem {
    @PrimaryKey(seq = 1)
    @Column(name = "CD_ITEM")
    private String code;

    @Column(name = "NM_ITEM")
    private String name;


    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
