package com.imwiz.flightdata.examples.etlsubflow.gateways;

import java.util.Collection;

import org.springframework.integration.annotation.Gateway;
import org.springframework.integration.annotation.MessagingGateway;


@MessagingGateway
public interface NumbersClassifier {

	/**
	 * ".input" added to indicate lambda-based flow resolution
	 * 
	 * @TODO more on this...
	 * @param numbers
	 */
	@Gateway(requestChannel = "multipleOfThreeFlow.input")
	void multipleOfThree(Collection<Integer> numbers);

	@Gateway(requestChannel = "remainderIsOneFlow.input")
	void remainderIsOne(Collection<Integer> numbers);

	@Gateway(requestChannel = "remainderIsTwoFlow.input")
	void remainderIsTwo(Collection<Integer> numbers);

}