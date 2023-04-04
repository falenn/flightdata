package com.imwiz.flightdata.examples.etlsubflow.gateways;

import org.springframework.integration.annotation.Gateway;
import org.springframework.integration.annotation.MessagingGateway;

import com.imwiz.flightdata.model.flight.FlightOperations;

/**
 * Example
 * 
 * package our.businesslogic;

@MessagingGateway
public interface OutboundMessageGateway {
    @Gateway(requestChannel = MessageChannelsConfiguration.OUTBOUND_CHANNEL)
    void sendMarkerMessage(Object markerMessage,
                           @Header(ChannelMessageHeaders.RECIPIENT_ID) String institutionId,
                           @Header(ChannelMessageHeaders.MESSAGE_TYPE) ChannelMessageType channelMessageType);


    @Gateway(requestChannel = MessageChannelsConfiguration.OUTBOUND_CHANNEL)
    void sendOrderMessage(Order message,
                                 @Header(ChannelMessageHeaders.RECIPIENT_ID) String institutionId,
                                 @Header(ChannelMessageHeaders.MESSAGE_TYPE) ChannelMessageType channelMessageType);

}
 * @author curtisbates
 *
 */


@MessagingGateway
public interface FlightOperationsGateway {
	

	/**
	 * Gateway sends to IntegrationFlow using the name of the flow method + ".input"
	 * @param message
	 */
	@Gateway(requestChannel = "sendFlightOperations.input")
	void sendFlightOps(FlightOperations message);

}