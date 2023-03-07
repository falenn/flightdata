package com.imwiz.flightdata.examples.etlsubflow.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.channel.QueueChannel;
import org.springframework.integration.config.EnableIntegration;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@EnableIntegration
@Configuration
public class ChannelConfig {

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

}
