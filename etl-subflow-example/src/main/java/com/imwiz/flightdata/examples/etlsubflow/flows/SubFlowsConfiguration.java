package com.imwiz.flightdata.examples.etlsubflow.flows;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.dsl.IntegrationFlow;

import com.imwiz.flightdata.examples.etlsubflow.filters.IsMultipleOfThreeFilter;
import com.imwiz.flightdata.examples.etlsubflow.filters.IsRemainderIsOneFilter;
import com.imwiz.flightdata.examples.etlsubflow.filters.IsRemainderTwoFilter;

import lombok.extern.slf4j.Slf4j;

/**
 * This Flow uses a publishSubscribeChannel and multiple subscriptions of subflows to manage 
 * the three numbersClassifier routing.  This is different from the gateway approach,
 * where the caller selected the flow to activate as a method call. 
 * 
 * 
 * @Configuration is used to make this @Bean declaration discoverable via componentScan, hence added
 * to the applicationContext.
 * 
 * @author curtisbates
 *
 */
@Slf4j
@Configuration
public class SubFlowsConfiguration {

	
	@Autowired
	IsMultipleOfThreeFilter isMultipleOfThree;
	
	@Autowired
	IsRemainderIsOneFilter isRemainederOne;
	
	@Autowired
	IsRemainderTwoFilter isRemainderTwo;
	
	/**
	 * This integration flow controls 3 subflows.  These subflows are anonymous, meaning they
	 * cannot be independently addressed.
	 * @return
	 */
	@Bean
	public IntegrationFlow numbersClassifier() {
		log.debug("Begin classify flow");
		return flow -> flow.split()
				.publishSubscribeChannel(subscription ->
						subscription
						.subscribe(subflow -> subflow
								.<Integer> filter(isMultipleOfThree)
								.channel("multipleOfThreeChannel"))
						.subscribe(subflow -> subflow
								.<Integer> filter(isRemainederOne)
								.channel("remainderIsOneChannel"))
						.subscribe(subflow -> subflow
								.<Integer> filter(isRemainderTwo)
								.channel("remainderIsTwoChannel")));
						
						
						
						
				
	}
	
}
