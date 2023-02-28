package com.imwiz.flightdata.etl.si.interceptor;

import static org.awaitility.Awaitility.await;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.Test;
import org.springframework.integration.channel.AbstractMessageChannel;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHandler;

import com.imwiz.flightdata.etl.helper.MessageGenerator;

public class LoggingAndCountingChannelInterceptorTest {

	public static final String DIRECT_CHANNEL_NAME = "testChannel";
	public static final int METRICTEST_MESSAGE_COUNT = 5;
	public static final int TEST_LENGTH_SEC = 20;
	
	
	@Test
	public void interceptorTest() {
		final AbstractMessageChannel messageChannel;
		final LoggingAndCountingChannelInterceptor interceptor;
		final List<Message<?>> subscriberReceivedMessages = new CopyOnWriteArrayList<>();
		
		// Create message channels
		messageChannel = new DirectChannel();
		messageChannel.setComponentName(DIRECT_CHANNEL_NAME);
		
		// Create intercepter and add to channel
		interceptor = new LoggingAndCountingChannelInterceptor();
		messageChannel.addInterceptor(interceptor);
		
		// Create subscriber
		final MessageHandler subscriber = subscriberReceivedMessages::add;
		((DirectChannel)messageChannel).subscribe(subscriber);
		
		// Send some messages
		MessageGenerator.sendMessagesWithRandomDelay(messageChannel);
		
		// syncho tool for asynch delay control
		await().atMost(TEST_LENGTH_SEC, TimeUnit.SECONDS).until(() ->
			subscriberReceivedMessages.size() >= METRICTEST_MESSAGE_COUNT);		
	}
}