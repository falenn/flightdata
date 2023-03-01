package com.imwiz.flightdata.etl;

import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.junit4.SpringRunner;

import com.imwiz.flightdata.etl.channel.CountDownLatchHandler;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ApplicationTest {

	private static final Logger logger = LoggerFactory.getLogger(ApplicationTest.class);
	
	@Autowired
	private ApplicationContext applicationContext;
	
	@Autowired 
	private CountDownLatchHandler countDownLatchHandler;
	
	
}
