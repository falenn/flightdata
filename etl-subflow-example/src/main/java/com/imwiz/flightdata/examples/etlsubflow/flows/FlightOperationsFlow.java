package com.imwiz.flightdata.examples.etlsubflow.flows;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.integration.kafka.dsl.Kafka;
import org.springframework.integration.kafka.inbound.KafkaMessageDrivenChannelAdapter.ListenerMode;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.listener.ContainerProperties.AckMode;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.retry.support.RetryTemplate;

import com.imwiz.flightdata.model.flight.FlightOperations;

import lombok.extern.slf4j.Slf4j;

@Configuration
@Slf4j
public class FlightOperationsFlow {

	
	@Value("${kafka.etl-subflow-example.consumer.topic}") 
	String topic;
	
	@Autowired
	KafkaTemplate<String, FlightOperations> flightOpsProducingKafkaTemplate;
	
	@Autowired
	ConsumerFactory<?,?> consumerFactory;
	
	@Autowired
    MessageChannel flightOpInChannel;
	
	/**
	 * Exposes a subflow via the conventional "sendFlightOperations.input" channel based on method name of the subflow
	 * Subflow publishes the message to kafka, such that:
	 * 
	 * sendFlightOperations -> (kafka)subflow.in
	 * 
	 * @return
	 */
	@Bean
    public IntegrationFlow sendFlightOperations() {	 	
    	return flow -> flow
                .enrichHeaders(headerEnricherSpec -> headerEnricherSpec
                        .header(KafkaHeaders.TOPIC, topic))
                .handle(Kafka.outboundChannelAdapter(flightOpsProducingKafkaTemplate));
    }
	
	/**
	 * Enables an integrationFlow that activates on creation, consuming from 
	 * the kafka topic and sending to "inputChannel"
	 * 
	 * (kafka)subflow.in -> receiveFlightOperations -> (queuechannel) flightOpIn 
	 * 
	 * @param topic
	 * @param consumerFactory
	 * @return
	 */
    @SuppressWarnings("unchecked")
	@Bean
    public IntegrationFlow receiveFlightOperations(
    		@Value("${kafka.etl-subflow-example.consumer.topic}") String topic,
            ConsumerFactory<?, ?> consumerFactory) {
        return IntegrationFlows.from(
        		Kafka.messageDrivenChannelAdapter(consumerFactory, ListenerMode.record, topic)
                	.configureListenerContainer(c -> c.ackMode(AckMode.RECORD))
                	.retryTemplate(new RetryTemplate())
                	.filterInRetry(true))
        		.enrichHeaders(h -> h
        				.headerExpression("platform", "payload.platform.name"))
        		.handle(m -> recordPlatform((Message<FlightOperations>) m))
                .channel(flightOpInChannel)
                .get();
    }
    
    /**
     * 
     * (queuechannel) flightOpIn -> processFlightOperations -> * 
     * @return
     */
    @SuppressWarnings("unchecked")
	@Bean
    public IntegrationFlow processFlightOperations() {
    	return IntegrationFlows.from(flightOpInChannel)
    			.handle(m -> recordPlatform((Message<FlightOperations>) m))
    			.get();
    }
    
    
    private Message<?> recordPlatform(Message<FlightOperations> message) {
    	log.debug("recordPlatform platform name: " + message.getPayload().getPlatform().getName());
    	return message;
    }
}
