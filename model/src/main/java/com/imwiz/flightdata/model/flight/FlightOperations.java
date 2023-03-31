package com.imwiz.flightdata.model.flight;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Collection;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.ser.ZonedDateTimeSerializer;

import lombok.Builder;
import lombok.Builder.Default;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.jackson.Jacksonized;

@Builder
@Getter
@ToString
@Jacksonized()
public class FlightOperations {

	private UUID id;
	
	@Default
	@JsonSerialize(using = ZonedDateTimeSerializer.class)
	@JsonFormat(pattern = "YYYY-MM-DD HH:mm:ss z")
	private ZonedDateTime createdDate = ZonedDateTime.of(LocalDateTime.now(),ZoneId.of("GMT"));
	
	private String flightName;
	
	private Platform platform;
	
	
	private Itinerary itinerary;
	
	@Setter
	private Collection<TrackStatus> status;
	
	
	
}
