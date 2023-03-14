package com.imwiz.flightdata.model.flight;

import java.util.List;
import java.util.UUID;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Builder
@Getter
@ToString
public class Itinerary {

	private UUID id;
	private String name;
	private List<Waypoint> waypoints;
}
