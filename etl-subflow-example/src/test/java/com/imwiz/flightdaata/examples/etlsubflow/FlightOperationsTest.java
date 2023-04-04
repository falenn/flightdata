package com.imwiz.flightdaata.examples.etlsubflow;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.integration.kafka.dsl.Kafka;
import org.springframework.integration.kafka.inbound.KafkaMessageDrivenChannelAdapter.ListenerMode;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.listener.ContainerProperties.AckMode;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.support.serializer.JsonSerializer;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.messaging.MessageChannel;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;

import com.imwiz.flightdata.examples.etlsubflow.config.AppConfig;
import com.imwiz.flightdata.examples.etlsubflow.config.ChannelConfig;
import com.imwiz.flightdata.examples.etlsubflow.config.KafkaProducerConfig;
import com.imwiz.flightdata.examples.etlsubflow.filters.IsMultipleOfThreeFilter;
import com.imwiz.flightdata.examples.etlsubflow.filters.IsRemainderIsOneFilter;
import com.imwiz.flightdata.examples.etlsubflow.filters.IsRemainderTwoFilter;
import com.imwiz.flightdata.examples.etlsubflow.flows.FlightOperationsFlow;
import com.imwiz.flightdata.examples.etlsubflow.flows.SubFlowsConfiguration;
import com.imwiz.flightdata.examples.etlsubflow.gateways.FlightOperationsGateway;
import com.imwiz.flightdata.model.flight.FlightOperations;
import com.imwiz.flightdata.model.flight.Itinerary;
import com.imwiz.flightdata.model.flight.Platform;
import com.imwiz.flightdata.model.flight.Waypoint;

import lombok.extern.slf4j.Slf4j;


/**
 * https://www.baeldung.com/spring-boot-kafka-testing
 * 
 * This App Sets the listener and port to a fixed port.  If not,
 * we could allow kafka to start up on a random port and use 
 * ${spring.embedded.kafka.bootstrap-servers} env var to connect.
 * @author curtisbates
 *
 */
@Slf4j
@SpringBootTest()
@Configuration 
@ContextConfiguration(classes = {
		KafkaProducerConfig.class,
		FlightOperationsFlow.class,
		AppConfig.class,
		SubFlowsConfiguration.class,
		ChannelConfig.class,
		IsMultipleOfThreeFilter.class,
		IsRemainderIsOneFilter.class,
		IsRemainderTwoFilter.class,
		FlightOperationsGateway.class
})

@TestPropertySource(locations = "classpath:test-subflow-application.yml")
@DirtiesContext
@EmbeddedKafka(partitions = 1, brokerProperties = { "listeners=PLAINTEXT://localhost:9092", "port=9092"  } )
class FlightOperationsTest {

	/**
	 * Kafka Setup
	 */
	@Value("${kafka.etl-subflow-example.consumer.auto-offset-reset}")
	private String kafkaAutoOffset;
	
	@Value("${kafka.etl-subflow-example.consumer.group-id}")
	private String kafkaGroupId;
	
	@Value("${kafka.etl-subflow-example.consumer.topic}")
	private String kafkaInTopic;
	
	@Value("${kafka.broker.bootstrap-servers}")
	private String kafkaBootstrapServers;
	
	@Autowired
	FlightOperationsGateway flightOperationsGateway;

	
	//@Autowired
	//CompositeMeterRegistry compositeMeterRegistry; 
	
	
	@Bean
    private MessageChannel flightOpIn() {
		DirectChannel channel = new DirectChannel();
		//channel.addInterceptor(new MicrometerCounterChannelInterceptor(compositeMeterRegistry));
		return (MessageChannel)channel;
	}
 
	/**
	 * Enables an integrationFlow that activates on creation, consuming from 
	 * the kafka topic and sending to "inputChannel"
	 * @param topic
	 * @param consumerFactory
	 * @return
	 */
    @Bean
    public IntegrationFlow listeningFromKafkaFlow(
    		@Value("${kafka.etl-subflow-example.consumer.topic}") String topic,
            ConsumerFactory<?, FlightOperations> consumerFactory) {
        return IntegrationFlows.from(
        		Kafka.messageDrivenChannelAdapter(consumerFactory, ListenerMode.record, topic)
                	.configureListenerContainer(c -> c.ackMode(AckMode.RECORD))
                	.retryTemplate(new RetryTemplate())
                	.filterInRetry(true))
                .channel("flightOpIn")
                .get();
    }
    
    
    /**
	 * Create the factory for the consumer
	 * 
	 * @return
	 */
	@Bean
	public ConsumerFactory<?, ?> consumerFactory() {
		return new DefaultKafkaConsumerFactory<>(consumerConfigs());
	}
	
	/**
	 * Defining the props for this config locally
	 * 
	 * @return
	 */
	@Bean
	public Map<String, Object> consumerConfigs() {
		Map<String, Object> properties = new HashMap<>();
		properties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
		properties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
		properties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
		properties.put(ConsumerConfig.GROUP_ID_CONFIG, kafkaGroupId);
		// automatically reset the offset to the earliest offset
		properties.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, kafkaAutoOffset);
		return properties;
	}
	
		 @Test
	    public void testETLSubFlowExecution() throws Exception {
	    	log.debug("Begin testIntegration");
	        FlightOperations payload = generateFlightOperations();
	        //Message<FlightOperations> message = new GenericMessage<>(payload);

	        //Send using the gateway
	        flightOperationsGateway.sendFlightOps(payload);
	        
	        
	        Thread.sleep(100000L);
	        
	    }
	    
	    //generate data
	    private FlightOperations generateFlightOperations() {
	    	Platform p = Platform.builder()
	    			.name("drone1")
	    			.build();
	    	
	    	List<Waypoint>  waypoints = new ArrayList<Waypoint>();
	    	waypoints.add(Waypoint.builder().coordinates("1").build());
	    	waypoints.add(Waypoint.builder().coordinates("2").build());
	    	waypoints.add(Waypoint.builder().coordinates("3").build());
	    	waypoints.add(Waypoint.builder().coordinates("4").build());
	    			
	    	
	    	Itinerary i = Itinerary.builder()
	    			.name("square test flight")
	    			.waypoints(waypoints)
	    			.build();
	    	
	    	FlightOperations fo = FlightOperations.builder()
	    			.flightName("testflight")
	    			.platform(p)
	    			.itinerary(i)
	    			.build();
	    	return fo;
	    }

}
