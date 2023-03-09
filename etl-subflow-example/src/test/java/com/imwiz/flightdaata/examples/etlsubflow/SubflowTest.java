package com.imwiz.flightdaata.examples.etlsubflow;

import static org.junit.Assert.assertNull;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
import com.imwiz.flightdata.examples.etlsubflow.filters.IsMultipleOfThreeFilter;
import com.imwiz.flightdata.examples.etlsubflow.filters.IsRemainderIsOneFilter;
import com.imwiz.flightdata.examples.etlsubflow.filters.IsRemainderTwoFilter;
import com.imwiz.flightdata.examples.etlsubflow.flows.SubFlowsConfiguration;
import com.imwiz.flightdata.examples.etlsubflow.gateways.SubFlowNumbersClassifierGateway;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@SpringBootTest
@RunWith(SpringRunner.class)
@ContextConfiguration(classes = {
		AppConfig.class,
		SubFlowsConfiguration.class,
		ChannelConfig.class,
		IsMultipleOfThreeFilter.class,
		IsRemainderIsOneFilter.class,
		IsRemainderTwoFilter.class
})
public class SubflowTest {

	@Autowired
	private ApplicationContext applicationContext;
	
	/**
	 * QueueChannel is needed to call receive on.  Messages are stored
	 */
	@Autowired
	private QueueChannel multipleOfThreeChannel;
	
	@Autowired
	private QueueChannel remainderIsOneChannel;
	
	@Autowired
	private QueueChannel remainderIsTwoChannel;
	
	@Test
	public void testSubflow() {
		log.debug("Begin testSubflow");
		
		SubFlowNumbersClassifierGateway gateway = 
				(SubFlowNumbersClassifierGateway) applicationContext.getBean("subFlowNubmersGateway");
		gateway.classify(Arrays.asList(1, 2, 3, 4, 5, 6));
		
		// multipleOfThree - should be 3,6
		Message<?> outMessage = multipleOfThreeChannel.receive(0);
		assertEquals(outMessage.getPayload(), 3);
		outMessage = multipleOfThreeChannel.receive(0);
		assertEquals(outMessage.getPayload(), 6);
		outMessage = multipleOfThreeChannel.receive(0);
		assertNull(outMessage);
		
		List<Integer> ans = new ArrayList<Integer>();
		// remainderIsOne with %3 - should be 1,4
		while(remainderIsOneChannel.getQueueSize() > 0) {
			outMessage = remainderIsOneChannel.receive();
			ans.add((Integer)outMessage.getPayload());
			log.info("remainderIsOne: " + outMessage.getPayload());
		}
		assertEquals(5, addList(ans));

		ans.clear();
		
		// remainderIsTwo with %3 - should be 2,5
		while(remainderIsTwoChannel.getQueueSize() > 0) {
			outMessage = remainderIsTwoChannel.receive();
			ans.add((Integer)outMessage.getPayload());
			log.info("remainderIsTwo: " + outMessage.getPayload());
		}
		assertEquals(7, addList(ans));

		
		
		
	}
	
	/*
	 * Helper class
	 */
	private Integer addList(List<Integer> list) {
		Integer ans = 0;
		for (Integer integer : list) {
			ans += integer;
		} 
		return ans;
	}
}
