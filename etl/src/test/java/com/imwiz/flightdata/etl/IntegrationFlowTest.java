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
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.integration.kafka.dsl.Kafka;
import org.springframework.integration.kafka.inbound.KafkaMessageDrivenChannelAdapter;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.support.GenericMessage;
import org.springframework.test.context.junit4.SpringRunner;

import com.imwiz.flightdata.etl.channel.CountDownLatchHandler;
import com.imwiz.flightdata.model.config.KafkaProperties;

@EmbeddedKafka(ports = 9092, count = 1, topics = "spring-integration-kafka.t", bootstrapServersProperty = "kafka.bootstrap-servers")
@RunWith(SpringRunner.class)
@SpringBootTest
public class IntegrationFlowTest {

	private static final Logger logger = LoggerFactory.getLogger(IntegrationFlowTest.class);

	// private EmbeddedKafkaBroker kafkaBroker;

	@Autowired
	private ApplicationContext applicationContext;

	@Autowired
	private KafkaProperties props;

	@Autowired
	private KafkaTemplate<String, String> producingKafkaTemplate;

	@Autowired
	private ConsumerFactory<?, ?> consumerFactory;

	@Autowired
	private MessageChannel producingChannel;

	@Autowired
	private MessageChannel consumingChannel;

	@Autowired
	private CountDownLatchHandler latchHandler;

	@Test
	public void testIntegrationFlow() {

		try {
			// start producerflow
			// IntegrationFlow producerFlow = (IntegrationFlow)
			// applicationContext.getBean("kafkaProducerFlow");
			// MessageChannel outChannel = producerFlow.getInputChannel();
			IntegrationFlows.from(producingChannel)
					.handle(Kafka.outboundChannelAdapter(producingKafkaTemplate).topic(props.getTestTopic())).get();

			// start consumerflow
			// IntegrationFlow consumerFlow = (IntegrationFlow)
			// applicationContext.getBean("kafkaConsumerFlow");
			IntegrationFlows
					.from(Kafka.messageDrivenChannelAdapter(consumerFactory,
							KafkaMessageDrivenChannelAdapter.ListenerMode.record, props.getTestTopic()))
					.handle(latchHandler).get();

			// Create message headers
			Map<String, Object> headers = Collections.singletonMap(KafkaHeaders.TOPIC, props.getTestTopic());

			logger.info("Sending 10 messages");
			for (int i = 0; i < 10; i++) {
				GenericMessage<String> message = new GenericMessage<>("Message: " + i, headers);
				producingChannel.send(message);
				logger.info("sent message: " + i);

			}

			latchHandler.getLatch().await(10000, TimeUnit.MILLISECONDS);
			assertThat(latchHandler.getLatch().getCount()).isEqualTo(0);

		} catch (Exception e) {
			fail(e.getMessage());
		}

	}
}
