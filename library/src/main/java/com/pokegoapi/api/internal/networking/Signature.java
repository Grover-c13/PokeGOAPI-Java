package com.pokegoapi.api.internal.networking;

import POGOProtos.Networking.Envelopes.RequestEnvelopeOuterClass;
import POGOProtos.Networking.Envelopes.SignatureOuterClass;
import POGOProtos.Networking.Envelopes.Unknown6OuterClass;
import POGOProtos.Networking.Envelopes.Unknown6OuterClass.Unknown6.Unknown2;
import POGOProtos.Networking.Requests.RequestOuterClass;
import POGOProtos.Networking.Requests.RequestTypeOuterClass;
import com.annimon.stream.Stream;
import com.annimon.stream.function.Predicate;
import com.google.protobuf.ByteString;
import com.pokegoapi.api.device.ActivityStatus;
import com.pokegoapi.api.device.DeviceInfo;
import com.pokegoapi.api.device.LocationFixes;
import com.pokegoapi.api.device.SensorInfo;
import com.pokegoapi.api.internal.Location;
import net.jpountz.xxhash.StreamingXXHash32;
import net.jpountz.xxhash.StreamingXXHash64;
import net.jpountz.xxhash.XXHashFactory;

import java.util.Random;

public class Signature {
	private final byte[] sessionHash;
	private final long startTime = System.currentTimeMillis();
	private final Location location;
	private final DeviceInfo deviceInfo;
	private final SensorInfo sensorInfo;
	private final ActivityStatus activityStatus;
	private final LocationFixes locationFixes;

	Signature(Location location, DeviceInfo deviceInfo, SensorInfo sensorInfo, ActivityStatus activityStatus, LocationFixes locationFixes) {
		this.sessionHash = new byte[32];
		new Random().nextBytes(sessionHash);
		this.location = location;
		this.deviceInfo = deviceInfo;
		this.sensorInfo = sensorInfo;
		this.activityStatus = activityStatus;
		this.locationFixes = locationFixes;
	}
	/**
	 * Given a fully built request, set the signature correctly.
	 *
	 * @param builder the requestenvelop builder
	 */
	void setSignature(RequestEnvelopeOuterClass.RequestEnvelope.Builder builder) {
		if (builder.getAuthTicket() == null) {
			//System.out.println("Ticket == null");
			throw new RuntimeException("Missing auth ticket");
		}

		long curTime = System.currentTimeMillis();

		byte[] authTicketBA = builder.getAuthTicket().toByteArray();

		/*
			Todo : reuse this later when we know the input
			byte[] unknown = "b8fa9757195897aae92c53dbcf8a60fb3d86d745".getBytes();
			XXHashFactory factory = XXHashFactory.safeInstance();
			StreamingXXHash64 xx64 = factory.newStreamingHash64(0x88533787);
			xx64.update(unknown, 0, unknown.length);
			long unknown25 = xx64.getValue();
		*/
		boolean getMapRequest = Stream.of(builder.getRequestsList()).filter(new Predicate<RequestOuterClass.Request>() {
			@Override
			public boolean test(RequestOuterClass.Request value) {
				return value.getRequestType() == RequestTypeOuterClass.RequestType.GET_MAP_OBJECTS;
			}
		}).count() > 1;

		SignatureOuterClass.Signature.Builder sigBuilder = SignatureOuterClass.Signature.newBuilder()
				.setLocationHash1(getLocationHash1(authTicketBA))
				.setLocationHash2(getLocationHash2())
				.setSessionHash(ByteString.copyFrom(sessionHash))
				.setTimestamp(curTime)
				.setTimestampSinceStart(curTime - startTime)
				.setDeviceInfo(deviceInfo.getDeviceInfo())
				.setActivityStatus(activityStatus.getActivityStatus())
				.addAllLocationFix(locationFixes.getLocationFixes(location, getMapRequest))
				.setUnknown25(7363665268261373700L);

		SignatureOuterClass.Signature.DeviceInfo deviceInfo = this.deviceInfo.getDeviceInfo();
		if (deviceInfo != null) {
			sigBuilder.setDeviceInfo(deviceInfo);
		}

		SignatureOuterClass.Signature.SensorInfo sensorInfo = this.sensorInfo.getSensorInfo();
		if (sensorInfo != null) {
			sigBuilder.setSensorInfo(sensorInfo);
		}

		for (RequestOuterClass.Request serverRequest : builder.getRequestsList()) {
			byte[] request = serverRequest.toByteArray();
			sigBuilder.addRequestHash(getRequestHash(authTicketBA, request));
		}

		byte[] uk2 = sigBuilder.build().toByteArray();
		byte[] iv = new byte[32];
		new Random().nextBytes(iv);
		byte[] encrypted = Crypto.encrypt(uk2, iv).toByteBuffer().array();
		builder.addUnknown6(Unknown6OuterClass.Unknown6.newBuilder()
				.setRequestType(6)
				.setUnknown2(Unknown2.newBuilder().setEncryptedSignature(ByteString.copyFrom(encrypted))).build());
	}

	private static byte[] getBytes(double input) {
		long rawDouble = Double.doubleToRawLongBits(input);
		return new byte[]{
				(byte) (rawDouble >>> 56),
				(byte) (rawDouble >>> 48),
				(byte) (rawDouble >>> 40),
				(byte) (rawDouble >>> 32),
				(byte) (rawDouble >>> 24),
				(byte) (rawDouble >>> 16),
				(byte) (rawDouble >>> 8),
				(byte) rawDouble
		};
	}


	private int getLocationHash1(byte[] authTicket) {
		XXHashFactory factory = XXHashFactory.fastestInstance();
		StreamingXXHash32 xx32 = factory.newStreamingHash32(0x1B845238);
		xx32.update(authTicket, 0, authTicket.length);
		byte[] bytes = new byte[8 * 3];

		System.arraycopy(getBytes(location.getLatitude()), 0, bytes, 0, 8);
		System.arraycopy(getBytes(location.getLongitude()), 0, bytes, 8, 8);
		System.arraycopy(getBytes(location.getAltitude()), 0, bytes, 16, 8);

		xx32 = factory.newStreamingHash32(xx32.getValue());
		xx32.update(bytes, 0, bytes.length);
		return xx32.getValue();
	}

	private int getLocationHash2() {
		XXHashFactory factory = XXHashFactory.fastestInstance();
		byte[] bytes = new byte[8 * 3];

		System.arraycopy(getBytes(location.getLatitude()), 0, bytes, 0, 8);
		System.arraycopy(getBytes(location.getLongitude()), 0, bytes, 8, 8);
		System.arraycopy(getBytes(location.getAltitude()), 0, bytes, 16, 8);

		StreamingXXHash32 xx32 = factory.newStreamingHash32(0x1B845238);
		xx32.update(bytes, 0, bytes.length);

		return xx32.getValue();
	}

	private static long getRequestHash(byte[] authTicket, byte[] request) {
		XXHashFactory factory = XXHashFactory.safeInstance();
		StreamingXXHash64 xx64 = factory.newStreamingHash64(0x1B845238);
		xx64.update(authTicket, 0, authTicket.length);
		xx64 = factory.newStreamingHash64(xx64.getValue());
		xx64.update(request, 0, request.length);
		return xx64.getValue();
	}
}