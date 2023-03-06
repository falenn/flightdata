package com.imwiz.flightdata.etl.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.integration.config.EnableIntegration;

import com.imwiz.flightdata.model.config.KafkaProperties;

@Configuration 
@PropertySource("classpath:values.properties")
@EnableIntegration
public class PropertyConfig {

	
	@Value("${kafka.bootstrap-servers}")
	public String bootstrapServers;
	
	@Value("${kafka.topic.spring-integration-kafka}")
	public String springIntegrationKafkaTopic;
	
	@Bean
	public KafkaProperties consumerConfigKafkaProps() {
		return KafkaProperties.builder()
				.bootstrapServers(bootstrapServers)
				.consumerConfigAutoOffsetResetConfig("earliest")
				.testTopic(springIntegrationKafkaTopic)
				.messageKey("kafka-integration")
				.build();
	}
}
