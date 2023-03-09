package com.imwiz.flightdata.examples.etlsubflow.filters;

import org.springframework.integration.core.GenericSelector;
import org.springframework.stereotype.Component;

/**
 * 
 * The Spring ApplicationContext is where Spring holds all @Bean instance objects for IOC dependency 
 * injection.
 * 
 * @Component tells Spring I am a Bean, but does so at the CLASS level.
 * @Bean is applied on Methods.  @Beans store the result of the method, a.k.a, the construction of a class
 * instance usually.  If we are writing the code, we can return a @Component.
 * 
 * @Service, @Controller and @Repository all are stereotyped versions of @Component
 * @ComponentScan detects and gathers @components into the context.
 * @SpringBootApplication contains @ComponentScan and should be at the root of our application (the main.class)
 * @author curtisbates
 *
 */
@Component
public class IsMultipleOfThreeFilter implements GenericSelector<Integer> {

	@Override
	public boolean accept(Integer number) {
		return number % 3 == 0;
	}

}