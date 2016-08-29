package com.debug.pokegoapi.main;

import POGOProtos.Networking.Envelopes.AuthTicketOuterClass.AuthTicket;
import POGOProtos.Networking.Envelopes.RequestEnvelopeOuterClass.RequestEnvelope.AuthInfo;
import POGOProtos.Networking.Envelopes.SignatureOuterClass.Signature.DeviceInfo;
import com.debug.pokegoapi.api.device.ActivityStatus;
import com.debug.pokegoapi.api.device.LocationFixes;
import com.debug.pokegoapi.api.device.SensorInfo;
import lombok.Data;

/**
 * @author Paul van Assen
 */
@Data
public class InfoHolder {
	private final long startTime = System.currentTimeMillis();
	private double latitude;
	private double longitude;
	private double altitude;
	private AuthInfo authInfo;
	private byte[] sessionHash;
	private DeviceInfo deviceInfo;
	private ActivityStatus activityStatus;
	private LocationFixes locationFixes;
	private SensorInfo sensorInfo;
	private AuthTicket authTicket;
	private String urlEndpoint;
}
