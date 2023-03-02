package com.imwiz.flightdata.etl;

import java.util.Scanner;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.AbstractApplicationContext;

import com.imwiz.flightdata.etl.config.AppConfig;

@SpringBootApplication
@PropertySource("classpath:values.properties")
public class App {

	public static void main(String[] args) {

		AbstractApplicationContext context = 
				new AnnotationConfigApplicationContext(AppConfig.class);
		context.registerShutdownHook();

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
