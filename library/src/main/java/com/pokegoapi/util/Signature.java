/*
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.pokegoapi.util;

import POGOProtos.Networking.Envelopes.RequestEnvelopeOuterClass;
import POGOProtos.Networking.Envelopes.SignatureOuterClass;
import POGOProtos.Networking.Platform.PlatformRequestTypeOuterClass;
import POGOProtos.Networking.Platform.Requests.SendEncryptedSignatureRequestOuterClass;
import com.google.protobuf.ByteString;
import com.pokegoapi.api.PokemonGo;
import com.pokegoapi.api.device.LocationFixes;
import com.pokegoapi.exceptions.RemoteServerException;

import java.nio.ByteBuffer;
import java.util.Random;

public class Signature {

	/**
	 * Given a fully built request, set the signature correctly.
	 *
	 * @param api the api
	 * @param builder the requestenvelop builder
	 */
	public static void setSignature(PokemonGo api, RequestEnvelopeOuterClass.RequestEnvelope.Builder builder)
			throws RemoteServerException {

		if (builder.getAuthTicket() == null) {
			return;
		}

		byte[] authTicket = builder.getAuthTicket().toByteArray();
		long currentTime = api.currentTimeMillis();
		long timeSince = currentTime - api.getStartTime();

		Random random = new Random();

		SignatureOuterClass.Signature.Builder sigBuilder;
		sigBuilder = SignatureOuterClass.Signature.newBuilder()
				.setLocationHash1(getLocationHash1(api, authTicket))
				.setLocationHash2(getLocationHash2(api))
				.setTimestamp(currentTime)
				.setTimestampSinceStart(timeSince)
				.setDeviceInfo(api.getDeviceInfo())
				.setActivityStatus(api.getActivitySignature(random))
				.addAllLocationFix(LocationFixes.getDefault(api, builder, currentTime, random))
				.setSessionHash(ByteString.copyFrom(api.getSessionHash()))
				.setUnknown25(Constant.UNK25);

		SignatureOuterClass.Signature.SensorInfo sensorInfo = api.getSensorSignature(currentTime, random);
		if (sensorInfo != null) {
			sigBuilder.addSensorInfo(sensorInfo);
		}

		for (int i = 0; i < builder.getRequestsList().size(); i++) {
			sigBuilder.addRequestHash(getRequestHash(builder.getRequests(i).toByteArray(), authTicket));
		}

		SignatureOuterClass.Signature signature = sigBuilder.build();
		byte[] signatureByteArray = signature.toByteArray();
		byte[] encrypted = Crypto.encrypt(signatureByteArray, timeSince).toByteBuffer().array();

		ByteString signatureBytes = SendEncryptedSignatureRequestOuterClass.SendEncryptedSignatureRequest.newBuilder()
				.setEncryptedSignature(ByteString.copyFrom(encrypted)).build()
				.toByteString();

		RequestEnvelopeOuterClass.RequestEnvelope.PlatformRequest platformRequest = RequestEnvelopeOuterClass
				.RequestEnvelope.PlatformRequest.newBuilder()
				.setType(PlatformRequestTypeOuterClass.PlatformRequestType.SEND_ENCRYPTED_SIGNATURE)
				.setRequestMessage(signatureBytes)
				.build();
		builder.addPlatformRequests(platformRequest);
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

	private static int getLocationHash1(PokemonGo api, byte[] authTicket) {
		byte[] bytes = new byte[24];
		System.arraycopy(getBytes(api.getLatitude()), 0, bytes, 0, 8);
		System.arraycopy(getBytes(api.getLongitude()), 0, bytes, 8, 8);
		System.arraycopy(getBytes(api.getAccuracy()), 0, bytes, 16, 8);
		int seed = NiaHash.hash32(authTicket);
		return NiaHash.hash32Salt(bytes, NiaHash.toBytes(seed));
	}

	private static int getLocationHash2(PokemonGo api) {
		byte[] bytes = new byte[24];
		System.arraycopy(getBytes(api.getLatitude()), 0, bytes, 0, 8);
		System.arraycopy(getBytes(api.getLongitude()), 0, bytes, 8, 8);
		System.arraycopy(getBytes(api.getAccuracy()), 0, bytes, 16, 8);

		return NiaHash.hash32(bytes);
	}

	private static long getRequestHash(byte[] request, byte[] authTicket) {
		byte[] seed = ByteBuffer.allocate(8).putLong(NiaHash.hash64(authTicket)).array();
		return NiaHash.hash64Salt(request, seed);
	}
}