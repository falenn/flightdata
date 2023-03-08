package com.imwiz.flightdaata.examples.etlsubflow;

import static org.junit.Assert.assertNull;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Arrays;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.integration.channel.QueueChannel;
import org.springframework.messaging.Message;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import com.imwiz.flightdata.examples.etlsubflow.config.ChannelConfig;
import com.imwiz.flightdata.examples.etlsubflow.flows.SeparateFlowsConfiguration;
import com.imwiz.flightdata.examples.etlsubflow.gateways.NumbersClassifier;

import lombok.extern.slf4j.Slf4j;

//@ RunWith(SpringJUnit4ClassRunner.class)
@Slf4j
@RunWith(SpringRunner.class)
@ContextConfiguration(classes = { SeparateFlowsConfiguration.class, ChannelConfig.class, NumbersClassifier.class })
public class SeparateFlowTest {

	@Autowired
	private QueueChannel multipleOfThreeChannel;

	@Autowired
	private NumbersClassifier numbersClassifier;

	@Test
	public void whenSendMessagesToMultipleOf3Flow_thenOutputMultiplesOf3() {
		log.info("Begin whenSendMessagesToMultipleOf3Flow_thenOutputMultiplesOf3 Test");
		numbersClassifier.multipleOfThree(Arrays.asList(1, 2, 3, 4, 5, 6));
		Message<?> outMessage = multipleOfThreeChannel.receive(0);
		assertEquals(outMessage.getPayload(), 3);
		outMessage = multipleOfThreeChannel.receive(0);
		assertEquals(outMessage.getPayload(), 6);
		outMessage = multipleOfThreeChannel.receive(0);
		assertNull(outMessage);
	}
}
