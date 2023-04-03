package com.imwiz.flightdata.examples.etlsubflow;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.PropertySource;


@PropertySource("classpath:application.yml")
@SpringBootApplication
public class Application  {

	
	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

	/*
	 * @Override public void run(String... args) throws Exception {
	 * log.info("Start App..");
	 * 
	 * Scanner scanner = new Scanner(System.in);
	 * System.out.print("Please enter q and press <enter> to exit the program: ");
	 * 
	 * while (true) { String input = scanner.nextLine(); if
	 * ("q".equals(input.trim())) { break; } } System.exit(0); }
	 */
}
