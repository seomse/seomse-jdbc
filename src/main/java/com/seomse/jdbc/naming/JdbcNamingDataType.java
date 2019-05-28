
package com.seomse.jdbc.naming;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.seomse.commons.config.Config;
import com.seomse.commons.config.ConfigObserver;
import com.seomse.commons.utils.string.StringArray;
/** 
 * <pre>
 *  파 일 명 : JdbcNamingDataType.java
 *  설    명 : Jdbc명명 규칙의 데이터 유형 
 *         
 *  작 성 자 : macle
 *  작 성 일 : 2017.09
 *  버    전 : 1.0
 *  수정이력 : 
 *  기타사항 :
 * </pre>
 * @atuhor Copyrights 2017 by ㈜섬세한사람들. All right reserved.
 */

public class JdbcNamingDataType {
	private static final Logger logger = LoggerFactory.getLogger(JdbcNamingDataType.class);	
	
	private static class Singleton {
		private static final JdbcNamingDataType instance = new JdbcNamingDataType();
	}
	
	/**
	 * 싱글턴 인스턴스 얻기
	 * @return
	 */
	public static JdbcNamingDataType getInstance(){
		return Singleton.instance;
	}
	
	/**
	 * 앞 우선순위여부 
	 * false일경우 뒤 우선순위
	 * 우선순위는 앞, 뒤 헤더조건을 모두 사용할 경우 
	 */
	private boolean isFrontPriority = true;
	
	private boolean isFront = false;	
	private boolean isBack = false;
	
	private JdbcDataType defaultDataType = JdbcDataType.STRING;
	
	private Map<JdbcDataType, TypeAndHeader> typeHeaderMap;
	
	// null 방지 
	private TypeAndHeader [] sortArray = new TypeAndHeader[0];
	
	private Map<String, JdbcDataType> typeKeyMap;
	
	private String [] typeKeyArray = {
			"string"
			,"double"
			,"integer"
			,"long"
			,"datetime"
	};
	
	
	/**
	 * 생성자
	 */
	private JdbcNamingDataType(){
		
		typeKeyMap = new HashMap<>();
		typeKeyMap.put("string", JdbcDataType.STRING);
		typeKeyMap.put("double", JdbcDataType.DOUBLE);
		typeKeyMap.put("integer", JdbcDataType.INTEGER);
		typeKeyMap.put("long", JdbcDataType.LONG);
		typeKeyMap.put("datetime", JdbcDataType.DATE_TIME);
		
		final String headerPositionKey = "application.jdbc.naming.header.position";
		setHeaderPosition(Config.getConfig(headerPositionKey,"front"));
		
		final String defaultKey = "application.jdbc.naming.default";
		setDefaultKey(Config.getConfig(defaultKey,"string"));
		
		
		typeHeaderMap = new HashMap<>();
		JdbcDataType [] typeArray =JdbcDataType.values();
		for(JdbcDataType dataType : typeArray){
			typeHeaderMap.put(dataType, new TypeAndHeader(dataType));
		}
		
		for(String typeKey : typeKeyArray){
			setTypeHeader(typeKey, Config.getConfig("application.jdbc.naming." + typeKey) );
		}

		
		final String seqKey = "application.jdbc.naming.seq";
		String seqValue = Config.getConfig(seqKey);
		if(seqValue != null){
			setSeq(seqValue);
		}
		
		
		
		
		ConfigObserver configObserver = new ConfigObserver() {
			
			public void updateConfig(Map<String, String> configMap) {
				String value = configMap.get(headerPositionKey);
				if(value != null){
					setHeaderPosition(value);
				}
				value = configMap.get(defaultKey);
				if(value != null){
					setDefaultKey(value);
				}
				
				
				for(String typeKey : typeKeyArray){
					setTypeHeader(typeKey, configMap.get("application.jdbc.naming." + typeKey) );
				}
				

				String seqValue = configMap.get(seqKey);
				if(seqValue != null){
					setSeq(seqValue);
				}
			}
		};
		
		Config.addObserver(configObserver);
	}
	
	
	private void setHeaderPosition(String value){

		
		String checkValue = value.toLowerCase();
		int frontIndex = checkValue.indexOf("front");
		int backIndex = checkValue.indexOf("back");
		
		if(frontIndex != -1){
			isFront = true;
		}else{
			isFront = false;
		}
		if(backIndex != -1){
			isBack = true;
		}else{
			isBack = false;
		}
		
		if(isFront && isBack){
			if(frontIndex < backIndex){
				isFrontPriority = true;
			}else{
				isFrontPriority = false;
			}
		}
	}
	

	private void setDefaultKey(String value){
		String checkValue = value.toLowerCase().trim();
		JdbcDataType type = typeKeyMap.get(checkValue);
		
		if(type != null){
			defaultDataType = type;
		}else{
			logger.error("Config check 'application.jdbc.naming.default' value in (string, double, integer, long, datetime) ");
			defaultDataType = JdbcDataType.STRING;
		}
		
	}
	
	private void setTypeHeader(String typeKey, String value){
		
		JdbcDataType type  = typeKeyMap.get(typeKey);

		TypeAndHeader typeAndHeader = typeHeaderMap.get(type);
		if(value == null){
			typeAndHeader.setHeaderArray(StringArray.EMPTY_STRING_ARRAY);
			return ;
		}
		value = value.trim();
		if("".equals(value)){
			typeAndHeader.setHeaderArray(StringArray.EMPTY_STRING_ARRAY);
			return ;
		}
	
		typeAndHeader.setHeaderArray(value.split(","));
		
	}
	
	private void setSeq(String value){
		value = value.trim().toLowerCase().replace(" ", "");
		
		String [] keyArray = value.split(",");
		
		for(String keyCheck : keyArray){
			if(typeKeyMap.containsKey(keyCheck)){
				continue;
			}
			
			logger.error("Config check 'application.jdbc.naming.seq' value in (string,double,integer,long,datetime) error -> "  + keyCheck);
		}
		
		
		TypeAndHeader [] sortArray = new TypeAndHeader[keyArray.length];
		for(int i=0 ; i<sortArray.length ; i++){
			sortArray[i] = typeHeaderMap.get(typeKeyMap.get(keyArray[i]));
		}
		
		this.sortArray = sortArray;

	}
	
	private class TypeAndHeader{
		private String [] headerArray = StringArray.EMPTY_STRING_ARRAY;
		private JdbcDataType dataType;
		private TypeAndHeader(JdbcDataType jdbcDataType){
			dataType = jdbcDataType;
		}
		
		private void setHeaderArray(String [] array){
			headerArray = array;
		}
	}
	
	
	public JdbcDataType getType(String columnName){
		if(isFront && isBack){
			if(isFrontPriority){
				JdbcDataType type = front(columnName);
				if(type != null){
					return type;
				}
				type = back(columnName);
				if(type != null){
					return type;
				}
				
			}else{
				JdbcDataType type = back(columnName);
				if(type != null){
					return type;
				}
				type = front(columnName);
				if(type != null){
					return type;
				}
			}
			
		}else if(isFront){
			JdbcDataType type = front(columnName);
			if(type != null){
				return type;
			}
		}else if(isBack){
			JdbcDataType type = back(columnName);
			if(type != null){
				return type;
			}
		}
		return defaultDataType;
	}

	
	
	private JdbcDataType front(String columnName){
		
		TypeAndHeader [] sortArray =this.sortArray ;
		for(int i=0 ; i<sortArray.length ; i++){
			String [] headerArray = sortArray[i].headerArray;
			
			for(int j=0 ; j<headerArray.length ; j++){
				if(columnName.startsWith(headerArray[j]+ "_") ){
					return sortArray[i].dataType;
				}
			}
		}
		
		return null;
	}
	
	private JdbcDataType back(String columnName){
		
		TypeAndHeader [] sortArray =this.sortArray ;
		for(int i=0 ; i<sortArray.length ; i++){
			String [] headerArray = sortArray[i].headerArray;
			
			for(int j=0 ; j<headerArray.length ; j++){
				if(columnName.endsWith("_" + headerArray[j]) ){
					return sortArray[i].dataType;
				}
			}
		}
		
		return null;
	}
	
	
	public static void main(String [] args){
		
		System.out.println(JdbcNamingDataType.getInstance().getType("PRC_AA"));
	}
}