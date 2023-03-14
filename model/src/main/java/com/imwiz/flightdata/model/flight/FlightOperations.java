package com.imwiz.flightdata.model.flight;

import java.util.Collection;
import java.util.UUID;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Builder
@Getter
@ToString
public class FlightOperations {

	private UUID id;
	
	private String flightName;
	
	private Platform platform;
	
	
	private Itinerary itinerary;
	
	@Setter
	private Collection<TrackStatus> status;
	
	
	
}
