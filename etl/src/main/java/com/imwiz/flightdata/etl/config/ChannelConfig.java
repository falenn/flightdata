package com.imwiz.flightdata.etl.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.channel.PublishSubscribeChannel;
import org.springframework.messaging.MessageChannel;

import com.imwiz.flightdata.etl.si.channelInterceptor.LoggingAndCountingChannelInterceptor;
import com.imwiz.flightdata.etl.si.serviceactivator.CountDownLatchHandler;

@Configuration
public class ChannelConfig {

	
	@Bean
	public MessageChannel consumingChannel() {
		DirectChannel channel = new DirectChannel();
		channel.addInterceptor(new LoggingAndCountingChannelInterceptor());
		return channel;
	}
	
	@Bean
	public DirectChannel producingChannel() {
		DirectChannel channel = new DirectChannel();
		channel.addInterceptor(new LoggingAndCountingChannelInterceptor());
		return channel;
	}
	
	@Bean
	MessageChannel inDataChannel() {
		return new DirectChannel();
	}
	
	@Bean
	public MessageChannel pubSubFileChannel() {
	    return new PublishSubscribeChannel();
	}
	
	
	/**
	 * This consumes from the consumingChannel
	 * 
	 * @return
	 */
	@Bean
	@ServiceActivator(inputChannel = "consumingChannel")
	public CountDownLatchHandler countDownLatchHandler() {
		return new CountDownLatchHandler();
	}
}
