package com.imwiz.flightdata.model;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.imwiz.flightdata.model.flight.FlightOperations;
import com.imwiz.flightdata.model.flight.Itinerary;
import com.imwiz.flightdata.model.flight.Platform;
import com.imwiz.flightdata.model.flight.TrackStatus;
import com.imwiz.flightdata.model.flight.Waypoint;

import lombok.extern.slf4j.Slf4j;

@Slf4j
class TestLombok {
	
	private ObjectMapper mapper;
	
	@BeforeEach
	private void setup() {
		mapper = new ObjectMapper();
	}

	
	@Test
	public void testUserBuilder() {
		log.debug("Begin builderTest");
		User.UserBuilder builder = User.builder();
		User user = builder.firstName("Test").build();
		assertEquals(user.getFirstName(), "Test");
	}
	
	
	@Test
	public void testFlightOperations() {
		log.debug("Begin testFlightOperations");
		
		List<Waypoint> waypoints = new ArrayList<Waypoint>();
		waypoints.add(Waypoint.builder().name("corner1").coordinates("coord1").build());
		waypoints.add(Waypoint.builder().name("corner2").coordinates("coord2").build());
		waypoints.add(Waypoint.builder().name("corner3").coordinates("coord3").build());
		waypoints.add(Waypoint.builder().name("home").coordinates("coord4").build());
		
		
		
		Itinerary itinerary = Itinerary.builder()
				.id(UUID.randomUUID())
				.name("Survey in 1sq mile")
				.waypoints(waypoints)
				.build();
		
		FlightOperations flightOps = FlightOperations.builder()
				.id(UUID.randomUUID())
				.flightName("Test flight 1")
				.platform(Platform.builder().id(UUID.randomUUID()).name("Drone1").build())
				.itinerary(itinerary)
				.build();
		
		try {
			log.debug("FlightOps 1: " + mapper.writerWithDefaultPrettyPrinter().writeValueAsString(flightOps));
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}


}
