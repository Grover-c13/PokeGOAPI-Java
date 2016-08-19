package com.pokegoapi.util;

import POGOProtos.Networking.Envelopes.RequestEnvelopeOuterClass;
import POGOProtos.Networking.Envelopes.SignatureOuterClass;
import POGOProtos.Networking.Envelopes.Unknown6OuterClass;
import POGOProtos.Networking.Envelopes.Unknown6OuterClass.Unknown6.Unknown2;
import POGOProtos.Networking.Requests.RequestOuterClass;

import com.google.protobuf.ByteString;
import com.pokegoapi.api.PokemonGo;
import com.pokegoapi.util.Crypto;
import com.pokegoapi.util.Log;

import net.jpountz.xxhash.StreamingXXHash32;
import net.jpountz.xxhash.StreamingXXHash64;
import net.jpountz.xxhash.XXHashFactory;

import java.lang.reflect.Method;
import java.util.Random;

public class Signature {

	private static SignatureOuterClass.Signature.DeviceInfo sDeviceInfo;

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

		if (sDeviceInfo == null) {
			if (api.getDeviceInfo() != null) {
				sDeviceInfo = api.getDeviceInfo();
			}

			// fallback
			if (sDeviceInfo == null) {
				sDeviceInfo = buildDeviceInfo();
			}
		}

		Log.e("DeviceInfo", sDeviceInfo.toString());
		sigBuilder.setDeviceInfo(sDeviceInfo);

		SignatureOuterClass.Signature.SensorInfo sensorInfo = api.getSensorInfo();
		if (sensorInfo != null) {
			sigBuilder.setSensorInfo(sensorInfo);
		}

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

	private static SignatureOuterClass.Signature.DeviceInfo buildDeviceInfo() {
		String customId = getUniqueId();
		if (customId.isEmpty()) {
			return SignatureOuterClass.Signature.DeviceInfo.newBuilder().build();
		}

		return SignatureOuterClass.Signature.DeviceInfo.newBuilder()
				.setDeviceId(customId)
				.setAndroidBoardName("shamu")
				.setAndroidBootloader("moto-apq8084-71.15")
				.setDeviceBrand("google")
				.setDeviceModel("shamu")
				.setDeviceModelIdentifier("MRA58R")
				.setDeviceModelBoot("shamu")
				.setHardwareManufacturer("motorola")
				.setHardwareModel("Nexus 6")
				.setFirmwareBrand("shamu")
				.setFirmwareTags("release-keys")
				.setFirmwareType("user")
				.setFirmwareFingerprint("google/shamu/shamu:6.0/MRA58R/2308909:user/release-keys")
				.build();
	}

	/**
	 * @TODO: Something more or something cleaner?
	 *
	 * @return an unique id for this device
	 */
	private static String getUniqueId() {
		try {
			String serial = getDeviceId();
			if (serial.isEmpty()) {
				return serial;
			}
			String[] splittedSerial = serial.split("");
			serial = "";
			int i = 0;
			int k = 0;
			if (serial.length() == 16) {
				// good to go
			}  else if (serial.length() > 16) {
				serial = serial.substring(0, 16);
			} else {
				while (serial.length() != 16) {
					try {
						String s = splittedSerial[i];
						if (isNumber(s)) {
							s = fakeNumber(s);
						}

						serial = serial + s;
						i++;
					} catch (IndexOutOfBoundsException e) {
						i = k;
						k = k == 1 ? 0 : 1;
					}
				}
			}
			return serial.toLowerCase();
		} catch (Exception exception) {
			return "";
		}
	}

	private static String getDeviceId() {
		try {
			Class<?> c = Class.forName("android.os.SystemProperties");
			Method get = c.getMethod("get", String.class, String.class);
			return (String) (get.invoke(c, "ro.serialno", "unknown"));
		} catch (Exception ignored) {
			return "";
		}
	}

	private static String fakeNumber(String s) {
		int i = Integer.valueOf(s);
		switch (i) {
			case 0:
				return String.valueOf(3);
			case 1:
				return String.valueOf(7);
			case 2:
				return String.valueOf(0);
			case 3:
				return String.valueOf(9);
			case 4:
				return String.valueOf(1);
			case 5:
				return String.valueOf(2);
			case 6:
				return String.valueOf(8);
			case 7:
				return String.valueOf(4);
			case 8:
				return String.valueOf(5);
			case 9:
				return String.valueOf(6);
			default:
				return String.valueOf(0);
		}
	}

	private static boolean isNumber(String str) {
		try {
			Double d = Double.parseDouble(str);
			return true;
		} catch(NumberFormatException nfe) {
			return false;
		}
	}
}