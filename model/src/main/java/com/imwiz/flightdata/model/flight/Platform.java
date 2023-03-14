package com.imwiz.flightdata.model.flight;

import java.util.UUID;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Builder
@Getter
@ToString
public class Platform {

	
	private UUID id;
	private String name;
	
}
