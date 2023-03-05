package com.imwiz.flightdata.model.config;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class KafkaProperties {

	private String bootstrapServers;
	private String consumerConfigAutoOffsetResetConfig;
	private String testTopic;
	private String messageKey;
}
