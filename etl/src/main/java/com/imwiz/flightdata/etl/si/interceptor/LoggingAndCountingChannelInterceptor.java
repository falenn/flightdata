package com.imwiz.flightdata.etl.si.interceptor;

import java.text.MessageFormat;
import java.util.Arrays;
import java.util.concurrent.atomic.AtomicInteger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.support.ChannelInterceptor;

public class LoggingAndCountingChannelInterceptor implements ChannelInterceptor {

	// Setup the logger
	private static Logger logger = LoggerFactory.getLogger(LoggingAndCountingChannelInterceptor.class);
	
	// Statistics gathering
	protected AtomicInteger mPreSendMessageCount = new AtomicInteger();
	protected AtomicInteger mPostSendMessageCount = new AtomicInteger();
	
	/**
	 * Intercepts prior to sending to a channel
	 */
	@Override
	public Message<?> preSend(final Message<?> inMessage,
			final MessageChannel inChannel) {
		logMessage("Before message sent", 
				inMessage, 
				inChannel, 
				(Object[]) null);
		
		mPreSendMessageCount.incrementAndGet();
		return inMessage;
	}
	
	@Override
	public void postSend(final Message<?> inMessage, final MessageChannel inChannel, final boolean inSent) {
		logMessage("After Message Sent", 
				inMessage, 
				inChannel, 
				(Object[]) null);
		mPostSendMessageCount.incrementAndGet();		
	}
	
	// Stats access
	public int getPreSendMessageCount() {
		return mPostSendMessageCount.get();
	}
	
	public int getPostSendMessageCount() {
		return mPostSendMessageCount.get();
	}
	
	
	/**
	 * A Fancy channel logger with flexibility for options.
	 * https://www.ivankrizsan.se/2017/12/31/spring-integration-5-message-channels-introduction-and-common-properties/
	 * @param inLogMessage
	 * @param inMessage
	 * @param inMessageChannel
	 * @param inAdditionalMessage Objects as strings to be inserted into the log message or 
	 * left as null.
	 */
	protected void logMessage(final String inLogMessage,
			final Message<?> inMessage,
			final MessageChannel inMessageChannel,
			final Object... inAdditionalMessage) {
		
		if(logger.isInfoEnabled()) {
			final int index = (inAdditionalMessage != null) ? inAdditionalMessage.length : 0;
			
			String theLogMessage = 
					new StringBuilder().append(inLogMessage)
					.append(" Channel: {")
					.append(index)
					.append("}. Payload: {")
					.append(index + 1)
					.append("}")
					.toString();
			
			final Object[] theLogMessageParameters;
			if (inAdditionalMessage != null) {
				theLogMessageParameters = Arrays.copyOf(inAdditionalMessage, inAdditionalMessage.length + 2);
			} else {
				theLogMessageParameters = new Object[2];
			}
			
			theLogMessageParameters[index] =
					(inMessageChannel != null) ? inMessage.toString() : "null message channel";
			theLogMessageParameters[index + 1] =
					(inMessage != null) ? inMessage.getPayload() : "null message";
			theLogMessage = 
					MessageFormat.format(theLogMessage, theLogMessageParameters);
			
			logger.info(theLogMessage);	
		}
	}
	
}
