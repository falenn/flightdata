package com.imwiz.flightdata.etl.si.channelInterceptor;

import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.support.ChannelInterceptor;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;

public class MicrometerCounterChannelInterceptor implements ChannelInterceptor {

	private final MeterRegistry meterRegistry;
	private final Counter messageReceiveCounter;
	private final Counter messageSentCounter;
	

	public MicrometerCounterChannelInterceptor(MeterRegistry meterRegistry) {

		this.meterRegistry = meterRegistry;
		this.messageReceiveCounter = Counter.builder("message.receive.count")
				.description("Messages received on this channel").register(this.meterRegistry);
		this.messageSentCounter = Counter.builder("message.receive.count")
				.description("Messages sent on this channel").register(this.meterRegistry);
	}

	@Override
	public void postSend(Message<?> message, MessageChannel channel, boolean sent) {
		this.messageSentCounter.increment();
		ChannelInterceptor.super.postSend(message, channel, sent);
	}

	@Override
	public Message<?> postReceive(Message<?> message, MessageChannel channel) {
		this.messageReceiveCounter.increment();
		return ChannelInterceptor.super.postReceive(message, channel);
	}

}
