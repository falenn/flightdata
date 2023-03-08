package com.imwiz.flightdata.examples.etlsubflow.flows;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.messaging.MessageChannel;

import com.imwiz.flightdata.examples.etlsubflow.filters.IsRemainderTwoFilter;
import com.imwiz.flightdata.examples.etlsubflow.filters.MultipleOfThreeFilter;
import com.imwiz.flightdata.examples.etlsubflow.filters.RemainderIsOneFilter;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
public class SeparateFlowsConfiguration {

	@Autowired
	private MultipleOfThreeFilter multipleOfThreeFilter;

	@Autowired
	private RemainderIsOneFilter remainderIsOneFilter;

	@Autowired
	private IsRemainderTwoFilter isRemainderTwoFilter;

	@Autowired
	private MessageChannel multipleOfThreeChannel;

	@Autowired
	private MessageChannel remainderIsOneChannel;

	@Autowired
	private MessageChannel remainderIsTwoChannel;

	/**
	 * Subflow Steps:
	 * 
	 * 1. split the incoming message into multiple messages 2. filter message. If
	 * message is multiple of three, then pass to multipleOfThree channel.
	 * 
	 * @return
	 */
	@Bean
	public IntegrationFlow multipleOfThreeFlow() {
		return flow -> flow.split().<Integer>filter(multipleOfThreeFilter).channel(multipleOfThreeChannel);
	}

	@Bean
	public IntegrationFlow remainderIsOneFlow() {
		return flow -> flow.split().<Integer>filter(remainderIsOneFilter).channel(remainderIsOneChannel);
	}

	@Bean
	public IntegrationFlow isRemainderTwoFlow() {
		return flow -> flow.split().<Integer>filter(isRemainderTwoFilter).channel(remainderIsTwoChannel);
	}
}
