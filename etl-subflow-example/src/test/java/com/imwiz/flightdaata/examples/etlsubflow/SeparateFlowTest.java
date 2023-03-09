package com.imwiz.flightdaata.examples.etlsubflow;

import static org.junit.Assert.assertNull;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Arrays;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.integration.channel.QueueChannel;
import org.springframework.messaging.Message;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import com.imwiz.flightdata.examples.etlsubflow.config.AppConfig;
import com.imwiz.flightdata.examples.etlsubflow.config.ChannelConfig;
import com.imwiz.flightdata.examples.etlsubflow.filters.IsRemainderTwoFilter;
import com.imwiz.flightdata.examples.etlsubflow.filters.IsMultipleOfThreeFilter;
import com.imwiz.flightdata.examples.etlsubflow.filters.IsRemainderIsOneFilter;
import com.imwiz.flightdata.examples.etlsubflow.flows.SeparateFlowsConfiguration;
import com.imwiz.flightdata.examples.etlsubflow.gateways.SeparateFlowsNumbersClassifierGateway;

import lombok.extern.slf4j.Slf4j;

/**
 * This test loads specific classes for context so as to not load other test elements by just doing a 
 * component scan.  Perhaps examples should be in separate packages...
 * 
 * This class uses a Gateway to provide Java-friendly access to the EIP backend.  Notice, that we load
 * an interface "NumbersClassifierGateway."  When AppConfig.java loads, it has the annotation @IntegrationComponentScan,
 * causing special interfaces to be implemented, in this case by the GatewayFactory.  We don't have to 
 * provide an implementation!
 * 
 * Gateways and Services should be used for request / response interaction.  A Gateway has a ReplyChannel
 * configured dynamically if one is not specified, which also means is has a correlator to pair requests back
 * with responses.
 * 
 * For Async, use a channel adapter instead.  This gateway, however, does work for async send via a nice
 * java api.
 * 
 * This example activates the multiplesOfThreeFlow directly as a method endpoint.
 * 
 * @author curtisbates
 *
 */
@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration(classes = { 
		AppConfig.class,
		SeparateFlowsConfiguration.class, 
		ChannelConfig.class, 
		SeparateFlowsNumbersClassifierGateway.class,
		IsRemainderTwoFilter.class,
		IsMultipleOfThreeFilter.class,
		IsRemainderIsOneFilter.class})
public class SeparateFlowTest {

	@Autowired
	private ApplicationContext applicationContext;
	
	@Autowired
	private QueueChannel multipleOfThreeChannel;

	
	@Test
	public void whenSendMessagesToMultipleOf3Flow_thenOutputMultiplesOf3() {
		log.info("Begin whenSendMessagesToMultipleOf3Flow_thenOutputMultiplesOf3 Test");
		SeparateFlowsNumbersClassifierGateway gateway = 
				(SeparateFlowsNumbersClassifierGateway) applicationContext.getBean("numbersGateway");
		gateway.multipleOfThree(Arrays.asList(1, 2, 3, 4, 5, 6));
		Message<?> outMessage = multipleOfThreeChannel.receive(0);
		assertEquals(outMessage.getPayload(), 3);
		outMessage = multipleOfThreeChannel.receive(0);
		assertEquals(outMessage.getPayload(), 6);
		outMessage = multipleOfThreeChannel.receive(0);
		assertNull(outMessage);
	}
}
