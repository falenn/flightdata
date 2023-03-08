package com.imwiz.flightdata.examples.etlsubflow.filters;

import org.springframework.integration.core.GenericSelector;
import org.springframework.stereotype.Component;

@Component
public class MultipleOfThreeFilter implements GenericSelector<Integer> {

	@Override
	public boolean accept(Integer number) {
		return number % 3 == 0;
	}

}