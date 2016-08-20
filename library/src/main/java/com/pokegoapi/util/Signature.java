package com.pokegoapi.util;

import POGOProtos.Networking.Envelopes.RequestEnvelopeOuterClass;
import POGOProtos.Networking.Envelopes.SignatureOuterClass;
import POGOProtos.Networking.Envelopes.Unknown6OuterClass;
import POGOProtos.Networking.Envelopes.Unknown6OuterClass.Unknown6.Unknown2;
import POGOProtos.Networking.Requests.RequestOuterClass;

import com.google.protobuf.ByteString;
import com.pokegoapi.api.PokemonGo;
import com.pokegoapi.api.device.DeviceInfo;
import com.pokegoapi.util.Crypto;

import net.jpountz.xxhash.StreamingXXHash32;
import net.jpountz.xxhash.StreamingXXHash64;
import net.jpountz.xxhash.XXHashFactory;

import java.util.Random;

public class Signature {

	private static DeviceInfo sDeviceInfo;
	private static Random sRandom;

	private static boolean sFirstSensorInfo = true;

	/**
	 * Given a fully built request, set the signature correctly.
	 *
	 * @param api     the api
	 * @param builder the requestenvelop builder
	 */
	public static void setSignature(PokemonGo api, RequestEnvelopeOuterClass.RequestEnvelope.Builder builder) {
		if (builder.getAuthTicket() == null) {
			//System.out.println("Ticket == null");
			return;
		}

		long curTime = api.currentTimeMillis();

		byte[] authTicketBA = builder.getAuthTicket().toByteArray();

		SignatureOuterClass.Signature.Builder sigBuilder = SignatureOuterClass.Signature.newBuilder()
				.setLocationHash1(getLocationHash1(api, authTicketBA, builder))
				.setLocationHash2(getLocationHash2(api, builder))
				.setSessionHash(ByteString.copyFrom(api.getSessionHash()))
				.setTimestamp(api.currentTimeMillis())
				.setTimestampSinceStart(curTime - api.startTime);

		if (sDeviceInfo != null) {
			sigBuilder.setDeviceInfo(sDeviceInfo.getDeviceInfo());
		} else {
			if (api.getDeviceInfo() != null) {
				sigBuilder.setDeviceInfo(api.getDeviceInfo());
			} else {
				sDeviceInfo = new DeviceInfo();
				sigBuilder.setDeviceInfo(sDeviceInfo.getDeviceInfo());
			}
		}

		if (api.getSensorInfo() != null) {
			sigBuilder.setSensorInfo(api.getSensorInfo());
		} else {
			sigBuilder.setSensorInfo(buildSensorInfo(curTime));
		}

		sigBuilder.setActivityStatus(buildActivityStatus());

		for (RequestOuterClass.Request serverRequest : builder.getRequestsList()) {
			byte[] request = serverRequest.toByteArray();
			sigBuilder.addRequestHash(getRequestHash(authTicketBA, request));
		}

		// TODO: Call encrypt function on this
		byte[] uk2 = sigBuilder.build().toByteArray();
		byte[] iv = new byte[32];
		new Random().nextBytes(iv);
		byte[] encrypted = Crypto.encrypt(uk2, iv).toByteBuffer().array();
		Unknown6OuterClass.Unknown6 uk6 = Unknown6OuterClass.Unknown6.newBuilder()
				.setRequestType(6)
				.setUnknown2(Unknown2.newBuilder().setEncryptedSignature(ByteString.copyFrom(encrypted))).build();
		builder.setUnknown6(uk6);
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


	private static int getLocationHash1(PokemonGo api, byte[] authTicket,
										RequestEnvelopeOuterClass.RequestEnvelope.Builder builder) {
		XXHashFactory factory = XXHashFactory.fastestInstance();
		StreamingXXHash32 xx32 = factory.newStreamingHash32(0x1B845238);
		xx32.update(authTicket, 0, authTicket.length);
		byte[] bytes = new byte[8 * 3];

		System.arraycopy(getBytes(api.getLatitude()), 0, bytes, 0, 8);
		System.arraycopy(getBytes(api.getLongitude()), 0, bytes, 8, 8);
		System.arraycopy(getBytes(api.getAltitude()), 0, bytes, 16, 8);

		xx32 = factory.newStreamingHash32(xx32.getValue());
		xx32.update(bytes, 0, bytes.length);
		return xx32.getValue();
	}

	private static int getLocationHash2(PokemonGo api, RequestEnvelopeOuterClass.RequestEnvelope.Builder builder) {
		XXHashFactory factory = XXHashFactory.fastestInstance();
		byte[] bytes = new byte[8 * 3];

		System.arraycopy(getBytes(api.getLatitude()), 0, bytes, 0, 8);
		System.arraycopy(getBytes(api.getLongitude()), 0, bytes, 8, 8);
		System.arraycopy(getBytes(api.getAltitude()), 0, bytes, 16, 8);

		StreamingXXHash32 xx32 = factory.newStreamingHash32(0x1B845238);
		xx32.update(bytes, 0, bytes.length);

		return xx32.getValue();
	}

	private static long getRequestHash(byte[] authTicket, byte[] request) {
		XXHashFactory factory = XXHashFactory.fastestInstance();
		StreamingXXHash64 xx64 = factory.newStreamingHash64(0x1B845238);
		xx64.update(authTicket, 0, authTicket.length);
		xx64 = factory.newStreamingHash64(xx64.getValue());
		xx64.update(request, 0, request.length);
		return xx64.getValue();
	}

	private static SignatureOuterClass.Signature.SensorInfo buildSensorInfo(long startTime) {
		if (sRandom == null) {
			sRandom = new Random();
		}

		SignatureOuterClass.Signature.SensorInfo.Builder sensorInfoBuilder =
				SignatureOuterClass.Signature.SensorInfo.newBuilder();
		if (sFirstSensorInfo) {
			sFirstSensorInfo = false;
			sensorInfoBuilder.setAccelRawX(-1.0 + sRandom.nextDouble())
					.setTimestampSnapshot(System.currentTimeMillis() - startTime)
					.setAccelRawY(-1.0 + sRandom.nextDouble())
					.setAccelRawZ(-1.0 + sRandom.nextDouble())
					.setGyroscopeRawX(-10.0 + sRandom.nextDouble() * 5.0)
					.setGyroscopeRawY(-10.0 + sRandom.nextDouble() * 5.0)
					.setGyroscopeRawZ(-10.0 + sRandom.nextDouble() * 5.0)
					.setAccelNormalizedX(-1.0 + sRandom.nextDouble() * 2.0)
					.setAccelNormalizedY(-1.0 + sRandom.nextDouble() * 2.0)
					.setAccelNormalizedZ(-1.0 + sRandom.nextDouble() * 2.0)
					.setAccelerometerAxes(3);
		} else {
			sensorInfoBuilder.setAccelRawX(-1.0 + sRandom.nextDouble())
					.setTimestampSnapshot(System.currentTimeMillis() - startTime)
					.setMagnetometerX(-1.0 + sRandom.nextDouble() * 2.0)
					.setMagnetometerY(-1.0 + sRandom.nextDouble() * 2.0)
					.setMagnetometerZ(-1.0 + sRandom.nextDouble() * 2.0)
					.setAngleNormalizedX(-55.0 + sRandom.nextDouble() * 110.0)
					.setAngleNormalizedY(-55.0 + sRandom.nextDouble() * 110.0)
					.setAngleNormalizedZ(-55.0 + sRandom.nextDouble() * 110.0)
					.setAccelRawY(-1.0 + sRandom.nextDouble())
					.setAccelRawZ(-1.0 + sRandom.nextDouble())
					.setGyroscopeRawX(-10.0 + sRandom.nextDouble() * 5.0)
					.setGyroscopeRawY(-10.0 + sRandom.nextDouble() * 5.0)
					.setGyroscopeRawZ(-10.0 + sRandom.nextDouble() * 5.0)
					.setAccelNormalizedX(-1.0 + sRandom.nextDouble() * 2.0)
					.setAccelNormalizedY(-1.0 + sRandom.nextDouble() * 2.0)
					.setAccelNormalizedZ(-1.0 + sRandom.nextDouble() * 2.0)
					.setAccelerometerAxes(3);
		}

		SignatureOuterClass.Signature.SensorInfo sensorInfo = sensorInfoBuilder.build();

		Log.e("SensorInfo", String.valueOf(sensorInfo.getTimestampSnapshot()));
		Log.e("SensorInfo", String.valueOf(sensorInfo.getMagnetometerX()));
		Log.e("SensorInfo", String.valueOf(sensorInfo.getMagnetometerY()));
		Log.e("SensorInfo", String.valueOf(sensorInfo.getMagnetometerZ()));
		Log.e("SensorInfo", String.valueOf(sensorInfo.getAngleNormalizedX()));
		Log.e("SensorInfo", String.valueOf(sensorInfo.getAngleNormalizedY()));
		Log.e("SensorInfo", String.valueOf(sensorInfo.getAngleNormalizedZ()));
		Log.e("SensorInfo", String.valueOf(sensorInfo.getAccelRawX()));
		Log.e("SensorInfo", String.valueOf(sensorInfo.getAccelRawY()));
		Log.e("SensorInfo", String.valueOf(sensorInfo.getAccelRawZ()));
		Log.e("SensorInfo", String.valueOf(sensorInfo.getGyroscopeRawX()));
		Log.e("SensorInfo", String.valueOf(sensorInfo.getGyroscopeRawY()));
		Log.e("SensorInfo", String.valueOf(sensorInfo.getGyroscopeRawZ()));
		Log.e("SensorInfo", String.valueOf(sensorInfo.getAccelNormalizedX()));
		Log.e("SensorInfo", String.valueOf(sensorInfo.getAccelNormalizedY()));
		Log.e("SensorInfo", String.valueOf(sensorInfo.getAccelNormalizedZ()));
		Log.e("SensorInfo", String.valueOf(sensorInfo.getAccelerometerAxes()));

		return sensorInfo;
	}

	private static SignatureOuterClass.Signature.ActivityStatus buildActivityStatus() {
		SignatureOuterClass.Signature.ActivityStatus.Builder activityStatusBuilder =
				SignatureOuterClass.Signature.ActivityStatus.newBuilder();
		if (sRandom == null) {
			sRandom = new Random();
		}
		boolean tilting = sRandom.nextInt() % 2 == 0;
		activityStatusBuilder.setStationary(true);
		if (tilting) {
			activityStatusBuilder.setTilting(true);
		}
		SignatureOuterClass.Signature.ActivityStatus activityStatus = activityStatusBuilder.build();
		Log.e("ActivityStatus", String.valueOf(activityStatus.getStationary()));
		Log.e("ActivityStatus", String.valueOf(activityStatus.getTilting()));
		return activityStatus;
	}
}
