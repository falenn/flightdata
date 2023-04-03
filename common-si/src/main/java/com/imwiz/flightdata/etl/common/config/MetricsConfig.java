package com.imwiz.flightdata.etl.common.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.actuate.autoconfigure.metrics.export.prometheus.PrometheusMetricsExportAutoConfiguration.PrometheusScrapeEndpointConfiguration;
import org.springframework.boot.actuate.metrics.export.prometheus.PrometheusScrapeEndpoint;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import io.micrometer.core.instrument.composite.CompositeMeterRegistry;
import io.micrometer.core.instrument.config.MeterFilter;
import io.micrometer.prometheus.PrometheusConfig;
import io.micrometer.prometheus.PrometheusMeterRegistry;

@Configuration
@Import(PrometheusScrapeEndpointConfiguration.class)
public class MetricsConfig {

	
	@Value("${application}")
	private String application;
	
	
	/**
	 * Although we could use SimpleMeterRegistry, it is better to use one that is aligned
	 * with our monitoring backend.
	 * CompositeMeterRegistry allows us to publish to multiple monitoring backends simultaneously.
	 * This is probably the best approach.
	 * 
	 * @param meterRegistry
	 * @param tagsProvider
	 * @return
	 */
	
	@Bean
	CompositeMeterRegistry compositeMeterRegistry() {
		CompositeMeterRegistry registry = new CompositeMeterRegistry();
		registry.add(prometheusMeterRegistry());
		return registry;
	}
	
	@Bean
	PrometheusMeterRegistry prometheusMeterRegistry() {
		PrometheusMeterRegistry registry = new PrometheusMeterRegistry(prometheusConfig());
		registry.config().meterFilter(MeterFilter.acceptNameStartsWith("message."));
		return registry;
	}
	
	
	/**
	 * Implement PrometheusConfig
	 * @return
	 */
	@Bean
	PrometheusConfig prometheusConfig() {
		PrometheusConfig prometheusConfig = PrometheusConfig.DEFAULT; 
		
		return prometheusConfig;
	}
	
}
