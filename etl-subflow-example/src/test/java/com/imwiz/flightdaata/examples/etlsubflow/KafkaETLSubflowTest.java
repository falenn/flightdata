package com.imwiz.flightdaata.examples.etlsubflow;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;

import com.imwiz.flightdata.examples.etlsubflow.config.AppConfig;
import com.imwiz.flightdata.examples.etlsubflow.config.ChannelConfig;
import com.imwiz.flightdata.examples.etlsubflow.filters.IsMultipleOfThreeFilter;
import com.imwiz.flightdata.examples.etlsubflow.filters.IsRemainderIsOneFilter;
import com.imwiz.flightdata.examples.etlsubflow.filters.IsRemainderTwoFilter;
import com.imwiz.flightdata.examples.etlsubflow.flows.SubFlowsConfiguration;

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
@SpringBootTest
@ContextConfiguration(classes = {
		AppConfig.class,
		SubFlowsConfiguration.class,
		ChannelConfig.class,
		IsMultipleOfThreeFilter.class,
		IsRemainderIsOneFilter.class,
		IsRemainderTwoFilter.class
})
@TestPropertySource(locations = "classpath:test-application.yml")
@DirtiesContext
@EmbeddedKafka(partitions = 1, brokerProperties = { "listeners=PLAINTEXT://localhost:9092", "port=9092"  } )
class KafkaETLSubflowTest {

	
	/**
	 * @TODO create consumer and producer / template.  (after merge)
	 */
	@Test
	void test() {
		fail("Not yet implemented");
	}

}
