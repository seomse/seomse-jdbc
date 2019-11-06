
package com.seomse.jdbc.example.naming;

import com.seomse.jdbc.annotation.PrimaryKey;
import com.seomse.jdbc.annotation.Table;

/**
 * <pre>
 *  파 일 명 : ItemNo.java
 *  설    명 : 사용법 설명용객체
 *
 *  작 성 자 : macle
 *  작 성 일 : 2017.10
 *  버    전 : 1.0
 *  수정이력 :
 *  기타사항 :
 * </pre>
 * @author Copyrights 2017 by ㈜섬세한사람들. All right reserved.
 */

@Table(name="TB_STOCK_ITEM")
public class ItemNo {
	@PrimaryKey(seq = 1)
	private String ITEM_CD;
	private String ITEM_NM;


	public String getITEM_CD() {
		return ITEM_CD;
	}

	public void setITEM_CD(String ITEM_CD) {
		this.ITEM_CD = ITEM_CD;
	}

	public String getITEM_NM() {
		return ITEM_NM;
	}

	public void setITEM_NM(String ITEM_NM) {
		this.ITEM_NM = ITEM_NM;
	}
}
