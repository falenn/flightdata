package com.imwiz.flightdata.model.flight;

import java.time.ZonedDateTime;
import java.util.Enumeration;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Builder
@Getter
@ToString
public class TrackStatus {

	private String trackId;
	private Enumeration<WaypointEnum> status;
	private String note;
	private ZonedDateTime startTime;
	private ZonedDateTime currentTime;
	 
}
