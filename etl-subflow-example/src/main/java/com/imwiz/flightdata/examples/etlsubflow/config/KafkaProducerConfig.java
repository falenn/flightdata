package com.imwiz.flightdata.examples.etlsubflow.config;

import java.util.HashMap;
import java.util.Map;

import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.serializer.JsonSerializer;

import com.imwiz.flightdata.model.flight.FlightOperations;

@Configuration
public class KafkaProducerConfig {

	
	@Bean
	public KafkaTemplate<String, FlightOperations> flightOpsProducingKafkaTemplate() {
		return new KafkaTemplate<String, FlightOperations>(producerFactory());
	}
	
	@Bean
    public DefaultKafkaProducerFactory<String, FlightOperations> producerFactory() {
        DefaultKafkaProducerFactory<String, FlightOperations> factory = new DefaultKafkaProducerFactory<>(producerConfigs());
        factory.setTransactionIdPrefix("my-transaction-");
        return factory;
    }
	
	@Bean
	public Map<String,Object> producerConfigs(){ 
		Map<String, Object> producerProperties = new HashMap<>();
        producerProperties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        producerProperties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        producerProperties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        return producerProperties;
	}
	

}
