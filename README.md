# seomse-jdbc

# 개발환경
- open jdk 1.8

# 개요
oracle, mysql 계열의 DB를 지원 하기 위해 개발 되었습니다.

개발할 당시 oracle 용을 먼저 개발 하고 mysql, maria를 지원하는 형태로 진행 하였기 때문에 maria 에서의 문제점이 발생 하면 아래 communication 경로에 제보해주시면 수정 하겠습니다.

 
한번 작성한 코드로 oracle, mysql 계열 에서 동작 하고 두 DB간의 데이터 이관 등의 작업을 지원 합니다 
 - 모든 DB를 지원 하고자 시작 하였지만 현시점에서의 목적 DB는 oracle, tibero, mysql, mariadb 등의 관련 계열 입니다.

domain header 를 정의 하고 설계 하는 국내 환경 에서 복잡 하지 않은 쿼리를 사용 하는 

back단 응용 프로 그램 에서의 사용이 편리 합니다.

복잡한 join을 활용해야하는 front 에서는 적합 하지 않을 수 있습니다.

domain header 참조
- https://docs.google.com/spreadsheets/d/13T8OR02ESmGTsD6uAI5alYPdRg6ekrfnVnkCdqpoAvE/edit#gid=1439308631

관련 방식으로 진행되는 프로젝트 seomse-system 내용을 보면 내용을 이해할 수 있습니다.
- https://github.com/seomse/seomse-system

# 구성
### admin 
- domain header DB에 관해서 서로 다른 any db 간의 데이터 이관을 지원 합니다.
- data backup 을 지원 합니다 (row data 만 지원 스키마는 지원 하지 못함)
- data 복원 (row data 만 지원 스키마는 지원 하지 못함)
- 단 이때 backup 된 데이터는 db 종류에 상관 없이 복원 가능
- scr > test > com.seomse.jdbc.example.admin.RowDataCopy 참조
- scr > test > com.seomse.jdbc.example.admin.RowDataOut 참조


### no (database naming object)
- 아래와 같은 방식에서의 select insert update 지원
- 변수명은 table column 명과 같음
- 아래 코드를 자동으로 생성해주는 유틸성 클래스 제공
- scr > test > com.seomse.jdbc.example.naming.NamingObjectMake 참조

```java
@Table(name="T_STOCK_ITEM")
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
```

### jdbc obj

- 아래와 같은 방식에서의 select insert update 지원
- 테이블명만 입력하면 @PrimaryKey(seq = 1) 와 같은 정보를 자동 으로 생성 하는 방식 지원
- scr > test > com.seomse.jdbc.example.objects.ObjectMake 참조
```java
@Table(name="B_STOCK_ITEM")
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
}
```

### query
- query(sql)를 활용한 다양한 유틸성 클래스 지원 
- com.seomse.jdbc.JdbcQuery
- public static List<Map<String, String>> getMapStringList(String sql);
- public static String getResultOne(String sql)
- 위와 같은 유틸성 method 지원

# gradle
implementation 'com.seomse.jdbc:seomse-jdbc:0.9.2'

# etc
https://mvnrepository.com/artifact/com.seomse.jdbc/seomse-jdbc/0.9.2


# communication
### blog, homepage
- seomse.tistory.com
- www.seomse.com
- seomse.com

### 카카오톡 오픈톡
 - https://open.kakao.com/o/g6vzOKqb

### 슬랙 slack
- https://seomse.slack.com/

### email (협업, 외주)
 - comseomse@gmail.com