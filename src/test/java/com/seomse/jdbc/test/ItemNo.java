
package com.seomse.jdbc.test;

import com.seomse.jdbc.annotation.PrimaryKey;
import com.seomse.jdbc.annotation.Table;
/**
 * <pre>
 *  파 일 명 : ItemNo.java
 *  설    명 : 사용법 설명용객체
 *
 *  작 성 자 : macle(김용수)
 *  작 성 일 : 2017.10
 *  버    전 : 1.0
 *  수정이력 :
 *  기타사항 :
 * </pre>
 * @atuhor Copyrights 2017 by ㈜섬세한사람들. All right reserved.
 */

@Table(name="TB_STOCK_ITEM")
public class ItemNo {
	@PrimaryKey(seq = 1) 
	private String CD_ITEM;
	private String NM_ITEM;
	private String TP_MARKET;
	private String FG_SUSPENSION = "Y"  ;
	private String FG_TRADE = "N"  ;
	private String FG_PREFERRED = "N"  ;
	public String getCD_ITEM() {
		return CD_ITEM;
	}
	public void setCD_ITEM(String cD_ITEM) {
		CD_ITEM = cD_ITEM;
	}
	public String getNM_ITEM() {
		return NM_ITEM;
	}
	public void setNM_ITEM(String nM_ITEM) {
		NM_ITEM = nM_ITEM;
	}
	public String getTP_MARKET() {
		return TP_MARKET;
	}
	public void setTP_MARKET(String tP_MARKET) {
		TP_MARKET = tP_MARKET;
	}
	public String getFG_SUSPENSION() {
		return FG_SUSPENSION;
	}
	public void setFG_SUSPENSION(String fG_SUSPENSION) {
		FG_SUSPENSION = fG_SUSPENSION;
	}
	public String getFG_TRADE() {
		return FG_TRADE;
	}
	public void setFG_TRADE(String fG_TRADE) {
		FG_TRADE = fG_TRADE;
	}
	public String getFG_PREFERRED() {
		return FG_PREFERRED;
	}
	public void setFG_PREFERRED(String fG_PREFERRED) {
		FG_PREFERRED = fG_PREFERRED;
	}
	
	
}
