package com.imwiz.flightdata.examples.etlsubflow.filters;

import org.springframework.integration.core.GenericSelector;
import org.springframework.stereotype.Component;

@Component
public class RemainderIsOneFilter implements GenericSelector<Integer> {

	@Override
	public boolean accept(Integer number) {
		return number % 3 == 1;
	}
}
