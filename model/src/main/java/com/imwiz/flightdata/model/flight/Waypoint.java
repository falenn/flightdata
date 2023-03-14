package com.imwiz.flightdata.model.flight;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Builder
@Getter
@ToString
public class Waypoint {

	private String name;
	private String coordinates;
	
}
