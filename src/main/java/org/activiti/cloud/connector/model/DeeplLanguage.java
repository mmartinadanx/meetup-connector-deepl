package org.activiti.cloud.connector.model;

public enum DeeplLanguage {
	
    ENGLISH("EN"),
    SPANISH("ES"),
    FRENCH("FR"),
    ITALIAN("IT"),
    PORTUGUESE("PT");
    
    private final String code;
    
    private DeeplLanguage(String code) {
      this.code = code;
    }
    
    public static DeeplLanguage getValueIgnoreCase(String input) {
      return valueOf(input.toUpperCase());
    }
    
    public String getCode() {
      return code;
    }	
}
