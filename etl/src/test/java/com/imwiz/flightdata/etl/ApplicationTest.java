package com.imwiz.flightdata.etl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.fail;

import java.util.Collections;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.PropertySource;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.support.GenericMessage;

import com.imwiz.flightdata.etl.si.serviceactivator.CountDownLatchHandler;
import com.imwiz.flightdata.model.config.KafkaProperties;

import lombok.extern.slf4j.Slf4j;

/**
 * producingChannel -> producingChannelAdapter -> kafka topic ->
 * consumingChannelAdapter -> CountDownLatchHandler
 *
 */
@Slf4j
@PropertySource("test-application.properties")
@EmbeddedKafka(ports = 9092, count = 1, topics = "spring-integration-kafka.t", bootstrapServersProperty = "kafka.bootstrap-servers")
@SpringBootTest
public class ApplicationTest {

	@Autowired
	private ApplicationContext applicationContext;

	@Autowired
	private KafkaProperties props;

	@Autowired
	private MessageChannel producingChannel;

	@Autowired
	private CountDownLatchHandler countDownLatchHandler;
	 
	/**
	 * This test runs with Configuration-based @Bean loading of KafkaConsumingChannelAdapter and KafkaProducingChannelAdapter
	 * 
	 */
	@Test
	public void test() {
		try {

			// Create message headers
			Map<String, Object> headers = Collections.singletonMap(KafkaHeaders.TOPIC, props.getTestTopic());

			log.info("Sending 10 messages");
			for (int i = 0; i < 10; i++) {
				GenericMessage<String> message = new GenericMessage<>("Message: " + i, headers);
				producingChannel.send(message);
				log.info("sent message: " + i);

			}

			countDownLatchHandler.getLatch().await(10000, TimeUnit.MILLISECONDS);
			assertThat(countDownLatchHandler.getLatch().getCount()).isEqualTo(0);

		} catch (Exception e) {
			fail("Error: " + e.getMessage());
		}
		
	}
}
