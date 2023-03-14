package com.imwiz.flightdata.model.flight;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Builder
@Getter
@ToString
public class Track {

	private String trackId;
	private Waypoint stratingWaypoint;
	private Waypoint destinationWaypoint;
	private String note;
}
