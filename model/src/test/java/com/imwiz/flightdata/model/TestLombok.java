package com.imwiz.flightdata.model;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class TestLombok {

	private static Logger logger = LoggerFactory.getLogger(TestLombok.class);

	@Test
	public void builderTest() {
		logger.debug("Begin builderTest");
		User.UserBuilder builder = User.builder();
		User user = builder.firstName("Test").build();
		assertEquals(user.getFirstName(), "Test");
	}

}
