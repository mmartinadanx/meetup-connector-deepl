package org.activiti.cloud.connector.impl;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.messaging.SubscribableChannel;

public interface DeeplConnectorChannels {

    String DEEPL_CONNECTOR_CONSUMER = "deeplConnectorConsumer";

    @Input(DEEPL_CONNECTOR_CONSUMER)
    SubscribableChannel deeplConnectorConsumer();
}
