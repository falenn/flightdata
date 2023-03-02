package com.imwiz.flightdata.etl.config;

import java.util.HashMap;
import java.util.Map;

import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.expression.common.LiteralExpression;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.kafka.outbound.KafkaProducerMessageHandler;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHandler;

import com.imwiz.flightdata.etl.si.interceptor.LoggingAndCountingChannelInterceptor;

@Configuration
public class ProducingChannelConfig {

	private static Logger logger = LoggerFactory.getLogger(ProducingChannelConfig.class);
	
	//This is how we inject values from properties
	@Value("${kafka.bootstrap-servers}")
	private String bootstrapServers;
	
	
	@Bean
	public MessageChannel producingChannel() {
		DirectChannel channel = new DirectChannel();
		channel.addInterceptor(new LoggingAndCountingChannelInterceptor());
		return channel;
	}
	
	@Bean
	@ServiceActivator(inputChannel = "producingChannel")
	public MessageHandler kafkaMessageHandler() {
		logger.debug("Begin aMessageHandler");
		KafkaProducerMessageHandler<String, String> handler = 
				new KafkaProducerMessageHandler<>(kafkaTemplate());
		//this will match up with the GroupId as interpreted by the consumer
		handler.setMessageKeyExpression(new LiteralExpression("kafka-integration"));
		return handler;
	}
	
	@Bean
	public KafkaTemplate<String, String> kafkaTemplate() {
		return new KafkaTemplate<>(producerFactory());
	}
	
	@Bean
	public ProducerFactory<String, String> producerFactory() {
		return new DefaultKafkaProducerFactory<>(producerConfigs());
	}
	
	@Bean
	public Map<String, Object> producerConfigs() {
		Map<String, Object> properties = new HashMap<>();
		properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
		properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
		properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
		//introduce a delay to allow messages to accumulate
		properties.put(ProducerConfig.LINGER_MS_CONFIG, 1);
		
		return properties;
	}
}
