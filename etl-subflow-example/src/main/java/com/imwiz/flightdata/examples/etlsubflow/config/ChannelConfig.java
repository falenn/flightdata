package com.imwiz.flightdata.examples.etlsubflow.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.channel.QueueChannel;
import org.springframework.integration.config.EnableIntegration;
import org.springframework.integration.dsl.Pollers;
import org.springframework.integration.scheduling.PollerMetadata;
import org.springframework.messaging.MessageChannel;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@EnableIntegration
@Configuration
public class ChannelConfig {

	// Used for subflow example
	@Bean
	QueueChannel multipleOfThreeChannel() {
		return new QueueChannel();
	}

	@Bean
	QueueChannel remainderIsOneChannel() {
		return new QueueChannel();
	}

	@Bean
	QueueChannel remainderIsTwoChannel() {
		return new QueueChannel();
	}

	@Bean
	QueueChannel numbersClassifierChannel() {
		return new QueueChannel();
	}

	@Bean
	MessageChannel flightOpInChannel() {
		QueueChannel channel = new QueueChannel(10);
		// channel.addInterceptor(new
		// MicrometerCounterChannelInterceptor(compositeMeterRegistry));
		return (MessageChannel) channel;
	}

	/**
	 * Used to support the polling of the QueueChannel
	 * @return
	 */
	@Bean
	public ThreadPoolTaskExecutor taskExecutor() {
		ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
		executor.setCorePoolSize(5);
		executor.setMaxPoolSize(10);
		executor.setQueueCapacity(25);
		executor.initialize();
		return executor;
	}

	@Bean(name = PollerMetadata.DEFAULT_POLLER)
	public PollerMetadata poller() {
		return Pollers.fixedRate(500).maxMessagesPerPoll(5).taskExecutor(taskExecutor()).get();
	}

}
