package org.activiti.cloud.connector.impl;

import java.util.Collections;
import java.util.Map;

import org.activiti.cloud.api.process.model.IntegrationRequest;
import org.activiti.cloud.api.process.model.IntegrationResult;
import org.activiti.cloud.connector.ConnectorConstants;
import org.activiti.cloud.connector.model.DeeplLanguage;
import org.activiti.cloud.connectors.starter.channels.IntegrationResultSender;
import org.activiti.cloud.connectors.starter.configuration.ConnectorProperties;
import org.activiti.cloud.connectors.starter.model.IntegrationResultBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;


@Component
@EnableBinding({DeeplConnectorChannels.class})
public class DeeplConnector {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(DeeplConnector.class);
	
    @Value("${spring.application.name}")
    private String appName;

    @Autowired
    private ConnectorProperties connectorProperties;
    
    @Autowired
    private DeeplService deeplService;

    private final IntegrationResultSender integrationResultSender;

    public DeeplConnector(IntegrationResultSender integrationResultSender) {
        this.integrationResultSender = integrationResultSender;
    }

    @StreamListener(value = DeeplConnectorChannels.DEEPL_CONNECTOR_CONSUMER)
    public void onTranslationRequest(IntegrationRequest event) {

    	//Read input variables
        final Map<String, Object> inboundVariables = event.getIntegrationContext().getInBoundVariables();      
        final String textToTranslate = (String)inboundVariables.get(ConnectorConstants.INPUT_TEXT);
        final DeeplLanguage language = DeeplLanguage.getValueIgnoreCase((String)inboundVariables.get(ConnectorConstants.LANGUAGE));

        LOGGER.info("Received message textToTranslate={}, language={}", textToTranslate, language);
        
        //Execute business logic
        final String translatedText = deeplService.translate(textToTranslate, language);
 
        LOGGER.info("Translated text={}", translatedText);
        
        //Create response - Set output variables
        Map<String, Object> results = Collections.singletonMap(ConnectorConstants.OUTPUT_TEXT, translatedText);
        Message<IntegrationResult> message = IntegrationResultBuilder
        			.resultFor(event, connectorProperties)
                .withOutboundVariables(results)
                .buildMessage();

        //Send response to Activiti
        integrationResultSender.send(message);
    }
}
