package com.imwiz.flightdata.examples.etlsubflow.flows;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.kafka.dsl.Kafka;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;

import com.imwiz.flightdata.model.flight.FlightOperations;

@Configuration
public class FlightOperationsFlow {

	
	@Value("${kafka.etl-subflow-example.consumer.topic}") 
	String topic;
	
	@Autowired
	KafkaTemplate<String, FlightOperations> flightOpsProducingKafkaTemplate;
	
	@Bean
    public IntegrationFlow sendFlightOperations() {
    		 	
    	return flow -> flow
                .enrichHeaders(headerEnricherSpec -> headerEnricherSpec
                        .header(KafkaHeaders.TOPIC, topic))
                .handle(Kafka.outboundChannelAdapter(flightOpsProducingKafkaTemplate));
    }
}
