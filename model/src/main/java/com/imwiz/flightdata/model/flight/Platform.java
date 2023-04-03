package com.imwiz.flightdata.model.flight;

import java.util.UUID;

import lombok.Builder;
import lombok.Builder.Default;
import lombok.Getter;
import lombok.ToString;

@Builder
@Getter
@ToString
public class Platform {

	@Default
	private UUID id = UUID.randomUUID();
	private String name;
	
}
