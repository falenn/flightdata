package com.imwiz.flightdata.examples.etlsubflow.flows;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.dsl.IntegrationFlow;

import com.imwiz.flightdata.examples.etlsubflow.config.FilterConfig;
import com.imwiz.flightdata.examples.etlsubflow.filters.MultipleOfThree;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
public class SubflowConfiguration {

	
	@Autowired
	private MultipleOfThree multipleOfThree;
	
	/**
	 * Subflow Steps:
	 * 
	 * 1. split the incoming message into multiple messages
	 * 2. filter message.  If message is multiple of three, then pass to multipleOfThree channel.
	 * 
	 * @return
	 */
	@Bean
	public IntegrationFlow multipleOfThreeFlow() {
	    return flow -> flow.split()
	      .<Integer> filter(filterConfig.isMultipleOfThree())
	      .channel("multipleOfThreeChannel");
	}
	
}
