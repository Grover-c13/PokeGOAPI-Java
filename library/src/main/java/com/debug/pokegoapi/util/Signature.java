package com.debug.pokegoapi.util;

import POGOProtos.Networking.Envelopes.RequestEnvelopeOuterClass;
import POGOProtos.Networking.Envelopes.SignatureOuterClass;
import POGOProtos.Networking.Envelopes.Unknown6OuterClass;
import POGOProtos.Networking.Envelopes.Unknown6OuterClass.Unknown6.Unknown2;
import POGOProtos.Networking.Requests.RequestOuterClass;
import POGOProtos.Networking.Requests.RequestTypeOuterClass;
import com.debug.pokegoapi.api.device.ActivityStatus;
import com.debug.pokegoapi.api.device.LocationFixes;
import com.debug.pokegoapi.api.device.SensorInfo;
import com.debug.pokegoapi.main.InfoHolder;
import com.google.protobuf.ByteString;
import com.google.protobuf.TextFormat;
import net.jpountz.xxhash.StreamingXXHash32;
import net.jpountz.xxhash.StreamingXXHash64;
import net.jpountz.xxhash.XXHashFactory;

import java.util.Random;

public class Signature {

	/**
	 * Given a fully built request, set the signature correctly.
	 *
	 * @param api     the api
	 * @param builder the requestenvelop builder
	 */
	public static void setSignature(InfoHolder infoHolder, RequestEnvelopeOuterClass.RequestEnvelope.Builder builder) {
		if (builder.getAuthTicket() == null) {
			//System.out.println("Ticket == null");
			return;
		}

		long currentTime = System.currentTimeMillis();

		byte[] authTicketBA = builder.getAuthTicket().toByteArray();

		/*
			Todo : reuse this later when we know the input
			byte[] unknown = "b8fa9757195897aae92c53dbcf8a60fb3d86d745".getBytes();
			XXHashFactory factory = XXHashFactory.safeInstance();
			StreamingXXHash64 xx64 = factory.newStreamingHash64(0x88533787);
			xx64.update(unknown, 0, unknown.length);
			long unknown25 = xx64.getValue();
		*/

		Random random = new Random();

		SignatureOuterClass.Signature.Builder sigBuilder = SignatureOuterClass.Signature.newBuilder()
				.setLocationHash1(getLocationHash1(infoHolder, authTicketBA))
				.setLocationHash2(getLocationHash2(infoHolder))
				.setSessionHash(ByteString.copyFrom(infoHolder.getSessionHash()))
				.setTimestamp(System.currentTimeMillis())
				.setTimestampSinceStart(currentTime - infoHolder.getStartTime())
				.setDeviceInfo(infoHolder.getDeviceInfo())
				.setActivityStatus(ActivityStatus.getDefault(infoHolder, random))
				.addAllLocationFix(LocationFixes.getDefault(infoHolder, builder, currentTime, random))
				.setUnknown25(7363665268261373700L);

		SignatureOuterClass.Signature.SensorInfo sensorInfo = SensorInfo.getDefault(infoHolder, currentTime, random);
		if (sensorInfo != null) {
			sigBuilder.setSensorInfo(sensorInfo);
		}
		boolean map = false;
		for (RequestOuterClass.Request serverRequest : builder.getRequestsList()) {
			byte[] request = serverRequest.toByteArray();
			if (serverRequest.getRequestType() == RequestTypeOuterClass.RequestType.GET_MAP_OBJECTS) {
				map = true;
			}
			sigBuilder.addRequestHash(getRequestHash(authTicketBA, request));
		}
		if(map) {
			System.out.println(TextFormat.printToString(sigBuilder));
		}
		// TODO: Call encrypt function on this
		byte[] uk2 = sigBuilder.build().toByteArray();
		byte[] iv = new byte[32];
		new Random().nextBytes(iv);
		byte[] encrypted = Crypto.encrypt(uk2, iv).toByteBuffer().array();
		Unknown6OuterClass.Unknown6 uk6 = Unknown6OuterClass.Unknown6.newBuilder()
				.setRequestType(6)
				.setUnknown2(Unknown2.newBuilder().setEncryptedSignature(ByteString.copyFrom(encrypted))).build();
		builder.addUnknown6(uk6);
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

	private static int getLocationHash1(InfoHolder infoHolder, byte[] authTicket) {
		XXHashFactory factory = XXHashFactory.safeInstance();
		StreamingXXHash32 xx32 = factory.newStreamingHash32(0x1B845238);
		xx32.update(authTicket, 0, authTicket.length);
		byte[] bytes = new byte[8 * 3];

		System.arraycopy(getBytes(infoHolder.getLatitude()), 0, bytes, 0, 8);
		System.arraycopy(getBytes(infoHolder.getLongitude()), 0, bytes, 8, 8);
		System.arraycopy(getBytes(infoHolder.getAltitude()), 0, bytes, 16, 8);

		xx32 = factory.newStreamingHash32(xx32.getValue());
		xx32.update(bytes, 0, bytes.length);
		return xx32.getValue();
	}

	private static int getLocationHash2(InfoHolder infoHolder) {
		XXHashFactory factory = XXHashFactory.safeInstance();
		byte[] bytes = new byte[8 * 3];

		System.arraycopy(getBytes(infoHolder.getLatitude()), 0, bytes, 0, 8);
		System.arraycopy(getBytes(infoHolder.getLongitude()), 0, bytes, 8, 8);
		System.arraycopy(getBytes(infoHolder.getAltitude()), 0, bytes, 16, 8);

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