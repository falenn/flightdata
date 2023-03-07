package com.imwiz.flightdata.examples.etlsubflow.filters;

import org.springframework.integration.core.GenericSelector;

public class RemainderIsOne implements GenericSelector<Integer> {

	

	boolean isRemainderTwo(Integer number) {
		return number % 3 == 2;
	}

	@Override
	public boolean accept(Integer number) {
		// TODO Auto-generated method stub
		return number % 3 == 1;
	}
}
