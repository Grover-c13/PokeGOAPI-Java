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
import com.google.protobuf.ByteString;
import com.pokegoapi.api.PokemonGo;
import com.pokegoapi.api.device.LocationFixes;
import com.pokegoapi.exceptions.RemoteServerException;
import com.pokegoapi.exceptions.hash.HashException;
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
	 * @throws RemoteServerException if an invalid request is sent
	 * @throws HashException if hashing fails
	 */
	public static void setSignature(PokemonGo api, RequestEnvelope.Builder builder)
			throws RemoteServerException, HashException {
		if (builder.getAuthTicket() == null) {
			return;
		}


		byte[] authTicket = builder.getAuthTicket().toByteArray();

		if (authTicket.length == 0) {
			return;
		}


		byte[][] requestData = new byte[builder.getRequestsCount()][];
		for (int i = 0; i < builder.getRequestsCount(); i++) {
			requestData[i] = builder.getRequests(i).toByteArray();
		}

		double latitude = api.getLatitude();
		double longitude = api.getLongitude();
		double accuracy = api.getAccuracy();

		if (Double.isNaN(latitude)) {
			latitude = 0.0;
		}
		if (Double.isNaN(longitude)) {
			longitude = 0.0;
		}
		if (Double.isNaN(accuracy)) {
			accuracy = 0.0;
		}

		long currentTime = api.currentTimeMillis();
		byte[] sessionHash = api.getSessionHash();
		HashProvider provider = api.getHashProvider();
		Hash hash = provider.provide(currentTime, latitude, longitude, accuracy, authTicket, sessionHash, requestData);
		Crypto crypto = provider.getCrypto();

		long timeSinceStart = currentTime - api.getStartTime();
		SignatureOuterClass.Signature.Builder signatureBuilder = SignatureOuterClass.Signature.newBuilder()
				.setLocationHash1(hash.getLocationAuthHash())
				.setLocationHash2(hash.getLocationHash())
				.setTimestamp(currentTime)
				.setTimestampSinceStart(timeSinceStart)
				.setDeviceInfo(api.getDeviceInfo())
				.setActivityStatus(api.getActivitySignature(RANDOM))
				.addAllLocationFix(LocationFixes.getDefault(api, builder, currentTime, RANDOM))
				.setSessionHash(ByteString.copyFrom(sessionHash))
				.setUnknown25(provider.getUNK25());

		SignatureOuterClass.Signature.SensorInfo sensorInfo = api.getSensorSignature(currentTime, RANDOM);
		if (sensorInfo != null) {
			signatureBuilder.addSensorInfo(sensorInfo);
		}

		List<Long> requestHashes = hash.getRequestHashes();
		for (int i = 0; i < builder.getRequestsCount(); i++) {
			signatureBuilder.addRequestHash(requestHashes.get(i));
		}

		SignatureOuterClass.Signature signature = signatureBuilder.build();
		byte[] signatureByteArray = signature.toByteArray();
		byte[] encrypted = crypto.encrypt(signatureByteArray, timeSinceStart).toByteBuffer().array();

		ByteString signatureBytes = SendEncryptedSignatureRequest.newBuilder()
				.setEncryptedSignature(ByteString.copyFrom(encrypted)).build()
				.toByteString();

		RequestEnvelope.PlatformRequest platformRequest = RequestEnvelope.PlatformRequest.newBuilder()
				.setType(PlatformRequestType.SEND_ENCRYPTED_SIGNATURE)
				.setRequestMessage(signatureBytes)
				.build();
		builder.addPlatformRequests(platformRequest);
	}
}