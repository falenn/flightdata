package com.imwiz.flightdata.examples.etlsubflow;

import java.util.Scanner;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.PropertySource;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@PropertySource("application.properties")
@SpringBootApplication
public class Application implements CommandLineRunner {

	@Autowired
	private ApplicationContext applicationContext;

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		log.info("Start App..");
		
		Scanner scanner = new Scanner(System.in);
		System.out.print("Please enter q and press <enter> to exit the program: ");

		while (true) {
			String input = scanner.nextLine();
			if ("q".equals(input.trim())) {
				break;
			}
		}
		System.exit(0);	
	}
}
