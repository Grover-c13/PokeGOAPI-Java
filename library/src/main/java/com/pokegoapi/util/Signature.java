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

import POGOProtos.Networking.Envelopes.RequestEnvelopeOuterClass.RequestEnvelope;
import POGOProtos.Networking.Envelopes.SignatureOuterClass;
import POGOProtos.Networking.Platform.PlatformRequestTypeOuterClass.PlatformRequestType;
import POGOProtos.Networking.Platform.Requests.SendEncryptedSignatureRequestOuterClass.SendEncryptedSignatureRequest;
import POGOProtos.Networking.Platform.Requests.UnknownPtr8RequestOuterClass;
import POGOProtos.Networking.Requests.RequestTypeOuterClass.RequestType;
import com.google.protobuf.ByteString;
import com.pokegoapi.api.PokemonGo;
import com.pokegoapi.api.device.LocationFixes;
import com.pokegoapi.api.device.SensorInfo;
import com.pokegoapi.exceptions.request.RequestFailedException;
import com.pokegoapi.util.hash.Hash;
import com.pokegoapi.util.hash.HashProvider;
import com.pokegoapi.util.hash.crypto.Crypto;

import java.util.List;
import java.util.Random;

public class Signature {
	private static final Random RANDOM = new Random();

	/**
	 * Given a fully built request, set the signature correctly.
	 *
	 * @param api the api
	 * @param builder the RequestEnvelope builder
	 * @throws RequestFailedException if an invalid request is sent
	 */
	public static void setSignature(PokemonGo api, RequestEnvelope.Builder builder) throws RequestFailedException {
		boolean usePtr8 = false;
		byte[][] requestData = new byte[builder.getRequestsCount()][];
		for (int i = 0; i < builder.getRequestsCount(); i++) {
			requestData[i] = builder.getRequests(i).toByteArray();
			RequestType requestType = builder.getRequests(i).getRequestType();
			if (requestType == RequestType.GET_PLAYER) {
				usePtr8 |= api.firstGP;
				api.firstGP = false;
			} else if (requestType == RequestType.GET_MAP_OBJECTS) {
				usePtr8 |= !api.firstGMO;
				api.firstGMO = false;
			}
		}
		double latitude = api.latitude;
		double longitude = api.longitude;
		double accuracy = api.accuracy;
		if (Double.isNaN(latitude)) {
			latitude = 0.0;
		}
		if (Double.isNaN(longitude)) {
			longitude = 0.0;
		}
		if (Double.isNaN(accuracy)) {
			accuracy = 0.0;
		}
		byte[] authTicket;
		if (builder.hasAuthTicket()) {
			authTicket = builder.getAuthTicket().toByteArray();
		} else {
			authTicket = builder.getAuthInfo().toByteArray();
		}

		long currentTimeMillis = api.currentTimeMillis();
		byte[] sessionHash = api.sessionHash;
		HashProvider provider = api.hashProvider;
		Hash hash = provider.provide(currentTimeMillis, latitude, longitude, accuracy, authTicket, sessionHash,
				requestData);

		long timeSinceStart = currentTimeMillis - api.startTime;
		SignatureOuterClass.Signature.Builder signatureBuilder = SignatureOuterClass.Signature.newBuilder()
				.setLocationHash1(hash.locationAuthHash)
				.setLocationHash2(hash.locationHash)
				.setSessionHash(ByteString.copyFrom(sessionHash))
				.setTimestamp(currentTimeMillis)
				.setTimestampSinceStart(timeSinceStart)
				.setDeviceInfo(api.getDeviceInfo())
				.addAllLocationFix(LocationFixes.getDefault(api, builder, currentTimeMillis, RANDOM))
				.setActivityStatus(api.getActivitySignature(RANDOM))
				.setUnknown25(provider.getUNK25())
				.setUnknown27(RANDOM.nextInt(59000) + 1000); // Currently random, generation is unknown

		final SignatureOuterClass.Signature.SensorInfo sensorInfo = SensorInfo.getDefault(api, currentTimeMillis,
				RANDOM);

		if (sensorInfo != null)
			signatureBuilder.addSensorInfo(sensorInfo);

		List<Long> requestHashes = hash.requestHashes;
		for (int i = 0; i < builder.getRequestsCount(); i++)
			signatureBuilder.addRequestHash(requestHashes.get(i));

		Crypto crypto = new Crypto();
		SignatureOuterClass.Signature signature = signatureBuilder.build();
		byte[] signatureByteArray = signature.toByteArray();
		byte[] encrypted = crypto.encrypt(signatureByteArray, timeSinceStart);

		ByteString signatureBytes = SendEncryptedSignatureRequest.newBuilder()
				.setEncryptedSignature(ByteString.copyFrom(encrypted)).build()
				.toByteString();

		RequestEnvelope.PlatformRequest signatureRequest = RequestEnvelope.PlatformRequest.newBuilder()
				.setType(PlatformRequestType.SEND_ENCRYPTED_SIGNATURE)
				.setRequestMessage(signatureBytes)
				.build();
		builder.addPlatformRequests(signatureRequest);

		if (usePtr8) {
			ByteString ptr8 = UnknownPtr8RequestOuterClass.UnknownPtr8Request.newBuilder()
					.setMessage("15c79df0558009a4242518d2ab65de2a59e09499")
					.build()
					.toByteString();
			builder.addPlatformRequests(RequestEnvelope.PlatformRequest.newBuilder()
					.setType(PlatformRequestType.UNKNOWN_PTR_8)
					.setRequestMessage(ptr8).build());
		}
	}
}
