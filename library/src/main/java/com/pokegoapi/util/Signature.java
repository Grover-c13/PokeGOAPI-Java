package com.pokegoapi.util;

import POGOProtos.Networking.Envelopes.RequestEnvelopeOuterClass;
import POGOProtos.Networking.Envelopes.SignatureOuterClass;
import POGOProtos.Networking.Envelopes.Unknown6OuterClass;
import POGOProtos.Networking.Envelopes.Unknown6OuterClass.Unknown6.Unknown2;
import POGOProtos.Networking.Requests.RequestOuterClass;

import com.google.protobuf.ByteString;
import com.pokegoapi.api.PokemonGo;
import com.pokegoapi.util.Crypto;

import net.jpountz.xxhash.StreamingXXHash32;
import net.jpountz.xxhash.StreamingXXHash64;
import net.jpountz.xxhash.XXHashFactory;

import java.util.HashSet;
import java.util.Random;

public class Signature {

    private static Random sRandom = new Random();

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

        buildDeviceInfo(sigBuilder, api);
        buildSensorInfo(sigBuilder, api);
        buildLocationFix(sigBuilder, api);
        buildActivityStatus(sigBuilder);

        for (RequestOuterClass.Request serverRequest : builder.getRequestsList()) {
            byte[] request = serverRequest.toByteArray();
            sigBuilder.addRequestHash(getRequestHash(authTicketBA, request));
        }

        SignatureOuterClass.Signature signature = sigBuilder.build();

        // TODO: Call encrypt function on this
        byte[] uk2 = signature.toByteArray();
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

    private static void buildDeviceInfo(SignatureOuterClass.Signature.Builder sigBuilder, PokemonGo api) {
        if (api.deviceInfoId.isEmpty()) {
            return;
        }

        Random deviceRandom = new Random(api.deviceInfoId.hashCode());
        String[][] devices =
                {
                        {"iPad3,1", "iPad", "J1AP"},
                        {"iPad3,2", "iPad", "J2AP"},
                        {"iPad3,3", "iPad", "J2AAP"},
                        {"iPad3,4", "iPad", "P101AP"},
                        {"iPad3,5", "iPad", "P102AP"},
                        {"iPad3,6", "iPad", "P103AP"},

                        {"iPad4,1", "iPad", "J71AP"},
                        {"iPad4,2", "iPad", "J72AP"},
                        {"iPad4,3", "iPad", "J73AP"},
                        {"iPad4,4", "iPad", "J85AP"},
                        {"iPad4,5", "iPad", "J86AP"},
                        {"iPad4,6", "iPad", "J87AP"},
                        {"iPad4,7", "iPad", "J85mAP"},
                        {"iPad4,8", "iPad", "J86mAP"},
                        {"iPad4,9", "iPad", "J87mAP"},

                        {"iPad5,1", "iPad", "J96AP"},
                        {"iPad5,2", "iPad", "J97AP"},
                        {"iPad5,3", "iPad", "J81AP"},
                        {"iPad5,4", "iPad", "J82AP"},

                        {"iPad6,7", "iPad", "J98aAP"},
                        {"iPad6,8", "iPad", "J99aAP"},

                        {"iPhone5,1", "iPhone", "N41AP"},
                        {"iPhone5,2", "iPhone", "N42AP"},
                        {"iPhone5,3", "iPhone", "N48AP"},
                        {"iPhone5,4", "iPhone", "N49AP"},

                        {"iPhone6,1", "iPhone", "N51AP"},
                        {"iPhone6,2", "iPhone", "N53AP"},

                        {"iPhone7,1", "iPhone", "N56AP"},
                        {"iPhone7,2", "iPhone", "N61AP"},

                        {"iPhone8,1", "iPhone", "N71AP"}

                };
        String[] osVersions = {"8.1.1", "8.1.2", "8.1.3", "8.2", "8.3", "8.4", "8.4.1",
                "9.0", "9.0.1", "9.0.2", "9.1", "9.2", "9.2.1", "9.3", "9.3.1", "9.3.2", "9.3.3", "9.3.4"};
        String[] device = devices[deviceRandom.nextInt(devices.length)];

        SignatureOuterClass.Signature.DeviceInfo.Builder deviceInfoBuilder = SignatureOuterClass.Signature.DeviceInfo.newBuilder();
        deviceInfoBuilder.setDeviceId(api.deviceInfoId)
                .setFirmwareType(osVersions[deviceRandom.nextInt(osVersions.length)])
                .setDeviceModelBoot(device[0])
                .setDeviceModel(device[1])
                .setHardwareModel(device[2])
                .setHardwareModel("iPhone OS")
                .setDeviceBrand("Apple")
                .setHardwareManufacturer("Apple");
        sigBuilder.setDeviceInfo(deviceInfoBuilder.build());
    }

    private static void buildSensorInfo(SignatureOuterClass.Signature.Builder sigBuilder, PokemonGo api) {
        SignatureOuterClass.Signature.SensorInfo.Builder sensorInfoBuilder =
                SignatureOuterClass.Signature.SensorInfo.newBuilder();
        if (api.currentTimeMillis() - api.startTime < 0) {
            sensorInfoBuilder.setTimestampSnapshot(api.currentTimeMillis() - api.startTime)
                    .setAccelRawX(0.1 + (0.7 - 0.1) * sRandom.nextDouble())
                    .setAccelRawY(0.1 + (0.8 - 0.1) * sRandom.nextDouble())
                    .setAccelRawZ(0.1 + (0.8 - 0.1) * sRandom.nextDouble())
                    .setGyroscopeRawX(-1.0 + sRandom.nextDouble() * 2.0)
                    .setGyroscopeRawY(-1.0 + sRandom.nextDouble() * 2.0)
                    .setGyroscopeRawZ(-1.0 + sRandom.nextDouble() * 2.0)
                    .setAccelNormalizedX(-1.0 + sRandom.nextDouble() * 2.0)
                    .setAccelNormalizedY(6.0 + (9.0 - 6.0) * sRandom.nextDouble())
                    .setAccelNormalizedZ(-1.0 + (8.0 - (-1.0)) * sRandom.nextDouble())
                    .setAccelerometerAxes(3);
        } else {
            sensorInfoBuilder.setTimestampSnapshot(api.currentTimeMillis() - api.startTime)
                    .setMagnetometerX(-0.7 + sRandom.nextDouble() * 1.4)
                    .setMagnetometerY(-0.7 + sRandom.nextDouble() * 1.4)
                    .setMagnetometerZ(-0.7 + sRandom.nextDouble() * 1.4)
                    .setAngleNormalizedX(-55.0 + sRandom.nextDouble() * 110.0)
                    .setAngleNormalizedY(-55.0 + sRandom.nextDouble() * 110.0)
                    .setAngleNormalizedZ(-55.0 + sRandom.nextDouble() * 110.0)
                    .setAccelRawX(0.1 + (0.7 - 0.1) * sRandom.nextDouble())
                    .setAccelRawY(0.1 + (0.8 - 0.1) * sRandom.nextDouble())
                    .setAccelRawZ(0.1 + (0.8 - 0.1) * sRandom.nextDouble())
                    .setGyroscopeRawX(-1.0 + sRandom.nextDouble() * 2.0)
                    .setGyroscopeRawY(-1.0 + sRandom.nextDouble() * 2.0)
                    .setGyroscopeRawZ(-1.0 + sRandom.nextDouble() * 2.0)
                    .setAccelNormalizedX(-1.0 + sRandom.nextDouble() * 2.0)
                    .setAccelNormalizedY(6.0 + (9.0 - 6.0) * sRandom.nextDouble())
                    .setAccelNormalizedZ(-1.0 + (8.0 - (-1.0)) * sRandom.nextDouble())
                    .setAccelerometerAxes(3);
        }

        sigBuilder.setSensorInfo(sensorInfoBuilder.build());
    }

    private static void buildActivityStatus(SignatureOuterClass.Signature.Builder sigBuilder) {
        SignatureOuterClass.Signature.ActivityStatus.Builder activityStatusBuilder =
                SignatureOuterClass.Signature.ActivityStatus.newBuilder();
        boolean tilting = sRandom.nextInt() % 2 == 0;
        activityStatusBuilder.setStationary(true);
        if (tilting) {
            activityStatusBuilder.setTilting(true);
        }
        sigBuilder.setActivityStatus(activityStatusBuilder.build());
    }

    private static void buildLocationFix(SignatureOuterClass.Signature.Builder sigBuilder, PokemonGo api) {
        // Random a value between 0 and 100 to have a % which we will use to random the numbers of providers
        int pn = sRandom.nextInt(100);
        int nProviders;
        HashSet<String> negativeSnapshotProviders = new HashSet<>();

        if (api.currentTimeMillis() - api.startTime < 0) {
            nProviders = pn < 75 ? 6 : pn < 95 ? 5 : 8;

            if (nProviders != 8) {
                // a 5% chance that the second provider got a negative value else it should be the first only
                int nChanche = sRandom.nextInt(100);
                negativeSnapshotProviders.add(nChanche < 95 ? "0" : "1");
            } else {
                int nChanche = sRandom.nextInt(100);
                if (nChanche >= 50) {
                    negativeSnapshotProviders.add("0");
                    negativeSnapshotProviders.add("1");
                    negativeSnapshotProviders.add("2");
                } else {
                    negativeSnapshotProviders.add("0");
                    negativeSnapshotProviders.add("1");
                }
            }
        } else {
            nProviders = pn < 60 ? 1 : pn < 90 ? 2 : 3;
        }

        for (int i=0;i<nProviders;i++) {
            float latitude = offsetOnLatLong(api.getLatitude(), sRandom.nextInt(100) + 10);
            float longitude = offsetOnLatLong(api.getLongitude(), sRandom.nextInt(100) + 10);
            float altitude = 65;
            float verticalAccuracy = (float) (15 + (23 - 15) * sRandom.nextDouble());

            // Fake errors xD
            if (sRandom.nextInt(100) > 90) {
                latitude = 360;
                longitude = -360;
            }

            // Another fake error
            if (sRandom.nextInt(100) > 90) {
                altitude = (float) (66 + (160 - 66) * sRandom.nextDouble());
            }

            SignatureOuterClass.Signature.LocationFix.Builder locationFixBuilder =
                    SignatureOuterClass.Signature.LocationFix.newBuilder();

            locationFixBuilder.setProvider("fused")
                    .setTimestampSnapshot(negativeSnapshotProviders.contains(String.valueOf(i)) ?
                            sRandom.nextInt(1000) - 3000  : api.currentTimeMillis() - api.startTime)
                    .setLatitude(latitude)
                    .setLongitude(longitude)
                    .setHorizontalAccuracy(-1)
                    .setAltitude(altitude)
                    .setVerticalAccuracy(verticalAccuracy)
                    .setProviderStatus(3)
                    .setLocationType(1);
            sigBuilder.addLocationFix(locationFixBuilder.build());
        }
    }

    private static float offsetOnLatLong(double l, double d) {
        double r = 6378137;
        double dl = d / (r * Math.cos(Math.PI*l/180));
        return (float) (l + dl * 180/ Math.PI);
    }

    private static int getLocationHash1(PokemonGo api, byte[] authTicket,
                                        RequestEnvelopeOuterClass.RequestEnvelope.Builder builder) {
        XXHashFactory factory = XXHashFactory.safeInstance();
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
        XXHashFactory factory = XXHashFactory.safeInstance();
        byte[] bytes = new byte[8 * 3];

        System.arraycopy(getBytes(api.getLatitude()), 0, bytes, 0, 8);
        System.arraycopy(getBytes(api.getLongitude()), 0, bytes, 8, 8);
        System.arraycopy(getBytes(api.getAltitude()), 0, bytes, 16, 8);

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