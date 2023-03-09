package com.imwiz.flightdata.examples.etlsubflow.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.integration.annotation.IntegrationComponentScan;
import org.springframework.integration.config.EnableIntegration;

@Configuration
@EnableIntegration
@IntegrationComponentScan(basePackages = "com.imwiz.flightdata.examples.etlsubflow")
public class AppConfig {

}
