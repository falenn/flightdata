package com.imwiz.flightdata.etl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.fail;

import java.util.Collections;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.support.GenericMessage;
import org.springframework.test.context.junit4.SpringRunner;

import com.imwiz.flightdata.etl.channel.CountDownLatchHandler;
import com.imwiz.flightdata.model.config.KafkaProperties;

/**
 * producingChannel -> producingChannelAdapter -> kafka topic ->
 * consumingChannelAdapter -> CountDownLatchHandler
 *
 */
@EmbeddedKafka(ports = 9092, count = 1, topics = "spring-integration-kafka.t", bootstrapServersProperty = "kafka.bootstrap-servers")
@RunWith(SpringRunner.class)
@SpringBootTest
public class ApplicationTest {

	private static final Logger logger = LoggerFactory.getLogger(ApplicationTest.class);

	// private EmbeddedKafkaBroker kafkaBroker;

	@Autowired
	private ApplicationContext applicationContext;

	@Autowired
	private KafkaProperties props;

	@Autowired
	private MessageChannel producingChannel;

	@Autowired
	private CountDownLatchHandler countDownLatchHandler;
	 

	@Test
	public void test() {
		try {

			// Create message headers
			Map<String, Object> headers = Collections.singletonMap(KafkaHeaders.TOPIC, props.getTestTopic());

			logger.info("Sending 10 messages");
			for (int i = 0; i < 10; i++) {
				GenericMessage<String> message = new GenericMessage<>("Message: " + i, headers);
				producingChannel.send(message);
				logger.info("sent message: " + i);

			}

			countDownLatchHandler.getLatch().await(10000, TimeUnit.MILLISECONDS);
			assertThat(countDownLatchHandler.getLatch().getCount()).isEqualTo(0);

		} catch (Exception e) {
			fail("Error: " + e.getMessage());
		}
		
	}
}
