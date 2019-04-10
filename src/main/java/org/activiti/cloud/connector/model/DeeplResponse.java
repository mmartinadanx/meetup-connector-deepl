package org.activiti.cloud.connector.model;

import java.util.List;

public class DeeplResponse {
	
	private List<DeeplTranslation> translations;
	
	public DeeplResponse() {
		super();
	}
	
	public List<DeeplTranslation> getTranslations() {
		return translations;
	}
}
