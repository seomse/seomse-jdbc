package com.seomse.jdbc.example.objects;

import com.seomse.jdbc.annotation.*;

/**
 * <pre>
 *  파 일 명 : StockItem.java
 *  설    명 : 주식 종목
 *
 *  작 성 자 : macle(김용수)
 *  작 성 일 : 2019.11
 *  버    전 : 1.0
 *  수정이력 :
 *  기타사항 :
 * </pre>
 * @author Copyrights 2019 by ㈜모아라. All right reserved.
 */
@Table(name="TB_STOCK_ITEM")
public class StockItem {
    @PrimaryKey(seq = 1)
    @Column(name = "ITEM_CD")
    private String code;
    @Column(name = "ITEM_NM")
    private String name;
    @Column(name = "ITEM_EN_NM")
    private String englishName;
    @Column(name = "MARKET_TP")
    private MarketType marketType = MarketType.KOSPI;
    @Column(name = "CATEGORY_NM")
    private String category;
    @Column(name = "WICS_NM")
    private String wics;
    @Column(name = "SUMMARY_DS")
    private String summary;

    @FlagBoolean
    @Column(name = "TRADE_FG")
    private boolean isTrade = false;

    @DateTime
    @Column(name = "UPT_LAST_DT")
    private long lastUpdateTime;


    public void setCode(String code) {
        this.code = code;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEnglishName(String englishName) {
        this.englishName = englishName;
    }

    public void setMarketType(MarketType marketType) {
        this.marketType = marketType;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setWics(String wics) {
        this.wics = wics;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public void setTrade(boolean trade) {
        isTrade = trade;
    }

    public void setLastUpdateTime(long lastUpdateTime) {
        this.lastUpdateTime = lastUpdateTime;
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public String getEnglishName() {
        return englishName;
    }

    public MarketType getMarketType() {
        return marketType;
    }

    public String getCategory() {
        return category;
    }

    public String getWics() {
        return wics;
    }

    public String getSummary() {
        return summary;
    }

    public boolean isTrade() {
        return isTrade;
    }

    public long getLastUpdateTime() {
        return lastUpdateTime;
    }
}
