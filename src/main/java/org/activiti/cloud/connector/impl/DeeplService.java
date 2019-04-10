package org.activiti.cloud.connector.impl;

import org.activiti.cloud.connector.model.DeeplLanguage;
import org.activiti.cloud.connector.model.DeeplResponse;
import org.activiti.cloud.connector.model.DeeplTranslation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Service
public class DeeplService {
	
    @Value("${deepl.auth-key}")
    private String authKey;

    @Value("${deepl.url}")
    private String deeplUrl;
    
    public String translate(String text, DeeplLanguage language) {
	    	RestTemplate restTemplate = new RestTemplate();

	    	UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(deeplUrl)
	    	        .queryParam("auth_key", authKey)
	    	        .queryParam("source_lang", "EN")
	    	        .queryParam("target_lang", language.getCode())
	    	        .queryParam("text", text);
	    	        
	    	ResponseEntity<DeeplResponse> response = restTemplate
	    	  .exchange(builder.build(false).toUriString(), HttpMethod.GET, null, DeeplResponse.class);
	    			  	    	
	    	if(response.getStatusCode() == HttpStatus.OK) {
	    		DeeplResponse deeplResponse = response.getBody();
	    		return deeplResponse.getTranslations().stream()
	    				.map(DeeplTranslation::getText)
	    				.findFirst()
	    				.orElse("No translation found");
	    	} else {
	    		return "Could not translate the text";
	    	}
    } 
}
