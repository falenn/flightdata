package com.imwiz.flightdata.examples.etlsubflow.gateways;

import java.util.Collection;

import org.springframework.integration.annotation.Gateway;
import org.springframework.integration.annotation.MessagingGateway;

@MessagingGateway(name = "subFlowNubmersGateway")
public interface SubFlowNumbersClassifierGateway {

	
	@Gateway(requestChannel = "numbersClassifier.input")
	void classify(Collection<Integer> numbers);
}
