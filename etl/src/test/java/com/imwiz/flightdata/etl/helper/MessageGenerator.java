package com.imwiz.flightdata.etl.helper;

import java.util.Random;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.support.MessageBuilder;


public class MessageGenerator {

	public static final int MAX_MESSAGES = 10;
	public static final int TIME_DELAY_MAX_SEC = 5;
	public static final int TIME_DELAY_MIN_SEC = 1;
	
	private static final Random random = new Random(System.currentTimeMillis());
	
	protected static Logger logger = LoggerFactory.getLogger(MessageGenerator.class);
	
	/**
	 * Sends messages to the provided channel
	 * @param inChannel
	 * @return
	 */
	public static void sendMessagesWithRandomDelay(MessageChannel inChannel) {
		
		int index = 0;
		while(index < MAX_MESSAGES) {
			
			pause();
			
			Message<String> m = MessageBuilder.withPayload("Test message; " + index).build();
			inChannel.send(m);
			index++;
		}
		logger.debug("Done");
	}
	
	// pause random number of seconds
	private static void pause() {
		
		int value = random.nextInt(TIME_DELAY_MAX_SEC - TIME_DELAY_MIN_SEC + 1) + TIME_DELAY_MIN_SEC;
		
		try {
			logger.debug("Sleeping " + value + " seconds");
			TimeUnit.SECONDS.sleep(value);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			logger.error(e.getStackTrace().toString());
		}
		
	}
}
