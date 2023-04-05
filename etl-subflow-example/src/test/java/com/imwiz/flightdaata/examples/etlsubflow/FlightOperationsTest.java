package com.imwiz.flightdaata.examples.etlsubflow;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;

import com.imwiz.flightdata.examples.etlsubflow.config.AppConfig;
import com.imwiz.flightdata.examples.etlsubflow.config.ChannelConfig;
import com.imwiz.flightdata.examples.etlsubflow.config.KafkaConsumerConfig;
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
 * This App Sets the listener and port to a fixed port. If not, we could allow
 * kafka to start up on a random port and use
 * ${spring.embedded.kafka.bootstrap-servers} env var to connect.
 * 
 * @author curtisbates
 *
 */
@Slf4j
@SpringBootTest()
@Configuration
@ContextConfiguration(classes = { KafkaConsumerConfig.class, KafkaProducerConfig.class, AppConfig.class,
		SubFlowsConfiguration.class, ChannelConfig.class, IsMultipleOfThreeFilter.class, IsRemainderIsOneFilter.class,
		IsRemainderTwoFilter.class,FlightOperationsFlow.class, FlightOperationsGateway.class })

@TestPropertySource(locations = "classpath:test-subflow-application.yml")
@DirtiesContext
@EmbeddedKafka(partitions = 1, brokerProperties = { "listeners=PLAINTEXT://localhost:9092", "port=9092" })
class FlightOperationsTest {

	@Autowired
	FlightOperationsGateway flightOperationsGateway;

	// @Autowired
	// CompositeMeterRegistry compositeMeterRegistry;

	/**
	 * This test sends a message out through a messageGateway. This Gateway - a
	 * 
	 * @throws Exception
	 */
	@Test
	public void testETLSubFlowExecution() throws Exception {
		log.debug("Begin testIntegration");

		// Message<FlightOperations> message = new GenericMessage<>(payload);

		Thread.sleep(2000);

		// Send using the gateway
		int i = 0;
		while (i < 10) {
			FlightOperations payload = generateFlightOperations(i);
			log.debug("Begin sending Flight Op with platform: " + payload.getPlatform().getName());
			flightOperationsGateway.sendFlightOps(payload);
			Thread.sleep(1000);
			i++;
		}
		Thread.sleep(100000L);

	}

	// generate data
	private FlightOperations generateFlightOperations(int droneId) {
		Platform p = Platform.builder().name("drone" + droneId).build();

		List<Waypoint> waypoints = new ArrayList<Waypoint>();
		waypoints.add(Waypoint.builder().coordinates("1").build());
		waypoints.add(Waypoint.builder().coordinates("2").build());
		waypoints.add(Waypoint.builder().coordinates("3").build());
		waypoints.add(Waypoint.builder().coordinates("4").build());

		Itinerary i = Itinerary.builder().name("square test flight").waypoints(waypoints).build();

		FlightOperations fo = FlightOperations.builder().flightName("testflight").platform(p).itinerary(i).build();
		return fo;
	}

}
