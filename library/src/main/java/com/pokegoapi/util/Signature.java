package com.pokegoapi.util;

import POGOProtos.Networking.Envelopes.AuthTicketOuterClass;
import POGOProtos.Networking.Envelopes.RequestEnvelopeOuterClass;
import POGOProtos.Networking.Envelopes.SignatureOuterClass;
import POGOProtos.Networking.Envelopes.Unknown6OuterClass;
import POGOProtos.Networking.Requests.RequestOuterClass;
import com.google.protobuf.ByteString;
import com.pokegoapi.api.PokemonGo;
import com.pokegoapi.main.ServerRequest;
import net.jpountz.xxhash.StreamingXXHash32;
import net.jpountz.xxhash.StreamingXXHash64;
import net.jpountz.xxhash.XXHashFactory;

import java.util.Random;

public class Signature {

	/**
	 * Given a fully built request, set the signature correctly.
	 *
	 * @param api the api
	 * @param builder the requestenvelop builder
	 */
	public static void setSignature(PokemonGo api, RequestEnvelopeOuterClass.RequestEnvelope.Builder builder) {
		if (builder.getAuthTicket() == null) {
			//System.out.println("Ticket == null");
			return;
		}
		byte[] uk22 = new byte[32];
		new Random().nextBytes(uk22);

		long curTime = api.currentTimeMillis();

		byte[] authTicketBA = builder.getAuthTicket().toByteArray();

		SignatureOuterClass.Signature.Builder sigBuilder = SignatureOuterClass.Signature.newBuilder()
				.setLocationHash1(getLocationHash1(api, authTicketBA, builder))
				.setLocationHash2(getLocationHash2(api, builder))
				.setUnk22(ByteString.copyFrom(uk22))
				.setTimestamp(api.currentTimeMillis())
				.setTimestampSinceStart(curTime - api.startTime);


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
				.setUnknown2(Unknown6OuterClass.Unknown6.Unknown2.newBuilder().setUnknown1(ByteString.copyFrom(encrypted))).build();
		builder.addUnknown6(uk6);
	}

	private static byte[] getBytes(double d) {
		long x = Double.doubleToRawLongBits(d);
		return new byte[]{
				(byte) (x >>> 56),
				(byte) (x >>> 48),
				(byte) (x >>> 40),
				(byte) (x >>> 32),
				(byte) (x >>> 24),
				(byte) (x >>> 16),
				(byte) (x >>> 8),
				(byte) x
		};
	}


	static private int getLocationHash1(PokemonGo api, byte[] authTicket, RequestEnvelopeOuterClass.RequestEnvelope.Builder builder) {
		XXHashFactory factory = XXHashFactory.fastestInstance();
		StreamingXXHash32 xx32 = factory.newStreamingHash32(0x1B845238);
		xx32.update(authTicket, 0, authTicket.length);
		xx32 = factory.newStreamingHash32(xx32.getValue());
		byte[] bytes = new byte[8 * 3];

		System.arraycopy(getBytes(api.getLatitude()), 0, bytes, 0, 8);
		System.arraycopy(getBytes(api.getLongitude()), 0, bytes, 8, 8);
		System.arraycopy(getBytes(api.getAltitude()), 0, bytes, 16, 8);

		xx32.update(bytes, 0, bytes.length);
		return xx32.getValue();
	}

	static private int getLocationHash2(PokemonGo api, RequestEnvelopeOuterClass.RequestEnvelope.Builder builder) {
		XXHashFactory factory = XXHashFactory.fastestInstance();
		StreamingXXHash32 xx32 = factory.newStreamingHash32(0x1B845238);
		byte[] bytes = new byte[8 * 3];

		System.arraycopy(getBytes(api.getLatitude()), 0, bytes, 0, 8);
		System.arraycopy(getBytes(api.getLongitude()), 0, bytes, 8, 8);
		System.arraycopy(getBytes(api.getAltitude()), 0, bytes, 16, 8);

		xx32.update(bytes, 0, bytes.length);

		return xx32.getValue();
	}

	static private long getRequestHash(byte[] authTicket, byte[] request) {
		XXHashFactory factory = XXHashFactory.fastestInstance();
		StreamingXXHash64 xx64 = factory.newStreamingHash64(0x1B845238);
		xx64.update(authTicket, 0, authTicket.length);
		xx64 = factory.newStreamingHash64(xx64.getValue());
		xx64.update(request, 0, request.length);
		return xx64.getValue();
	}
}
