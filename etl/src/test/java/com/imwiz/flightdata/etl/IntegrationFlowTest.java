package com.imwiz.flightdata.etl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.fail;

import java.util.Collections;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.integration.IntegrationMessageHeaderAccessor;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.integration.kafka.dsl.Kafka;
import org.springframework.integration.kafka.dsl.KafkaProducerMessageHandlerSpec;
import org.springframework.integration.kafka.inbound.KafkaMessageDrivenChannelAdapter;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.DefaultKafkaHeaderMapper;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.support.GenericMessage;

import com.imwiz.flightdata.etl.si.serviceactivator.CountDownLatchHandler;
import com.imwiz.flightdata.model.config.KafkaProperties;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
@PropertySource("test-application.properties")
@EmbeddedKafka(ports = 9092, count = 1, topics = "test.integration-flow.t", bootstrapServersProperty = "kafka.bootstrap-servers")
@SpringBootTest
public class IntegrationFlowTest {

	@Value("${kafka.topic.test.integration-flow}")
	private static String testTopic;

	// private EmbeddedKafkaBroker kafkaBroker;
	@Autowired
	private ApplicationContext applicationContext;

	@Autowired
	private KafkaProperties props;

	@Autowired
	private KafkaTemplate<String, String> producingKafkaTemplate;
	
	@Autowired
	private DefaultKafkaHeaderMapper mapper;

	@Autowired
	@Qualifier("myConsumerFactory")
	private ConsumerFactory<?, ?> consumerFactory;
	
	@Autowired
	public ProducerFactory<String, String> producerFactory;

	// @Autowired
	// private ConcurrentMessageListenerContainer<String, String>
	// kafkaListenerContainer;

	@Autowired
	private MessageChannel producingChannel;

	@Autowired
	private CountDownLatchHandler countDownLatchHandler;

	
	/**
	 * KafkaProducerMessageHandlerSpec
	 * 
	 * @param producerFactory
	 * @param topic
	 * @return
	 */
	private KafkaProducerMessageHandlerSpec<String,String, ?> newKafkaMessageHandler(
			ProducerFactory<String, String> producerFactory,
			String topic) {
		return Kafka
				.outboundChannelAdapter(producerFactory)
				.messageKey(m -> m
						.getHeaders()
						.get(IntegrationMessageHeaderAccessor.SEQUENCE_NUMBER))
				.headerMapper(mapper)
				.topicExpression("headers[kafka_topic]?: '" + topic + "'")
				.configureKafkaTemplate(t -> t.id("kafkaTemplate:" + topic));
	}
	/** 
	 * Headers
	 * kafka_messageKey: populate the key of the Kafka Message
	 * kafka_topic: specify topic to publish to
	 * kafka_partitionId: specify partition to write to
	 * 
	 * SpEL is also usable to help apply dynamic values at runtime when probing the request message
	 * 
	 * OutboundChannelAdapter requires a KafkaTemplate which requires a KafkaProducerFactory.
	 * 
	 * When using flow -> flow.from(, we need a messageSource, not a channel.
	 * https://stackoverflow.com/questions/46311035/how-to-configure-a-trigger-in-spring-integration-flow-to-get-value-from-a-method
	 * 
	 */
	@Bean("testTopicProducerFlow")
	public IntegrationFlow testTopicProducerFlow() {
		log.debug("Begin testTopicProducerFlow");
		return flow -> flow.from(producingChannel).handle(
				newKafkaMessageHandler(producerFactory, testTopic),
				e -> e.id("kafkaProducer1")).get();
				
				//Kafka.outboundChannelAdapter(producingKafkaTemplate).messageKey(props.getMessageKey()))
				//.get();
	}

	@Bean("testTopicConsumerFlow")
	public IntegrationFlow testTopicConsumerFlow() {
		log.debug("Begin testTopicConsumerFlow");
		return IntegrationFlows
				.from(Kafka.messageDrivenChannelAdapter(consumerFactory,
						KafkaMessageDrivenChannelAdapter.ListenerMode.record, testTopic))
				.handle(countDownLatchHandler).get();
	}

	@Test
	public void testIntegrationFlow() {

		try {
			//testTopicProducerFlow();
			log.info("Producer flow running...");
			
			log.info("Sending 10 messages");
			// Create message headers
			Map<String, Object> headers = Collections.singletonMap(KafkaHeaders.TOPIC, testTopic);
			for (int i = 0; i < 10; i++) {
				GenericMessage<String> message = new GenericMessage<>("Message: " + i, headers);
				producingChannel.send(message);
				log.info("sent message: " + i);

			}
			// Pause to show that flows are running
			Thread.sleep(5000);
			log.info("Producer flow now running");
			//testTopicConsumerFlow();

			countDownLatchHandler.getLatch().await(10000, TimeUnit.MILLISECONDS);
			assertThat(countDownLatchHandler.getLatch().getCount()).isEqualTo(0);

		} catch (Exception e) {
			fail(e.getMessage());
		}
	}

}
