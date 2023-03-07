package com.imwiz.flightdata.etl.si.channeladapter;

import java.util.HashMap;
import java.util.Map;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.kafka.inbound.KafkaMessageDrivenChannelAdapter;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.listener.ConcurrentMessageListenerContainer;
import org.springframework.kafka.listener.ContainerProperties;
import org.springframework.messaging.MessageChannel;

import com.imwiz.flightdata.model.config.KafkaProperties;

@Configuration
public class KafkaConsumingChannelAdapter {

	private static Logger logger = LoggerFactory.getLogger(KafkaConsumingChannelAdapter.class);

	@Autowired
	private KafkaProperties props;

	@Autowired
	private MessageChannel consumingChannel;

	/**
	 * Defining the props for this config locally
	 * 
	 * @return
	 */
	@Bean
	public Map<String, Object> consumerConfigs() {
		Map<String, Object> properties = new HashMap<>();
		properties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, props.getBootstrapServers());
		properties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
		properties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
		properties.put(ConsumerConfig.GROUP_ID_CONFIG, props.getMessageKey());
		// automatically reset the offset to the earliest offset
		properties.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, props.getConsumerConfigAutoOffsetResetConfig());
		return properties;
	}

	/**
	 * KafkaMessageDrivenChannelAdapter consumes from a kafka topic and writes to
	 * the configured Spring Integration {@code}MessageChannel
	 * 
	 * @param outputChannel
	 * @return
	 */
	@Bean
	public KafkaMessageDrivenChannelAdapter<String, String> kafkaMessageDrivenChannelAdapter() {
		logger.debug("Begin kafkaMessageDrivenChannelAdapter");
		KafkaMessageDrivenChannelAdapter<String, String> kafkaMessageDrivenChannelAdapter = new KafkaMessageDrivenChannelAdapter<>(
				kafkaListenerContainer());
		kafkaMessageDrivenChannelAdapter.setOutputChannel(consumingChannel);
		return kafkaMessageDrivenChannelAdapter;
	}

	/**
	 * KafkaListenerContainer designates the Kafka Topic to consume from
	 * 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@Bean
	public ConcurrentMessageListenerContainer<String, String> kafkaListenerContainer() {
		return (ConcurrentMessageListenerContainer<String, String>) new ConcurrentMessageListenerContainer<>(
				consumerFactory(), new ContainerProperties(props.getTestTopic()));
	}

	/**
	 * Create the factory for the consumer
	 * 
	 * @return
	 */
	@Bean(name = "myConsumerFactory")
	public ConsumerFactory<?, ?> consumerFactory() {
		return new DefaultKafkaConsumerFactory<>(consumerConfigs());
	}
}