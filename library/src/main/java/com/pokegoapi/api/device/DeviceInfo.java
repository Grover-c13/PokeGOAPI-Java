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

package com.pokegoapi.api.device;

import com.pokegoapi.api.PokemonGo;

import java.util.Random;

import POGOProtos.Networking.Envelopes.SignatureOuterClass;

/**
 * Created by fabianterhorst on 08.08.16.
 */

public class DeviceInfo {

	private SignatureOuterClass.Signature.DeviceInfo.Builder deviceInfoBuilder;

	public DeviceInfo() {
		deviceInfoBuilder = SignatureOuterClass.Signature.DeviceInfo.newBuilder();
	}

	/**
	 * Create a device info with already existing device infos
	 *
	 * @param deviceInfos the device infos interface
	 */
	public DeviceInfo(DeviceInfos deviceInfos) {
		this();
		deviceInfoBuilder
				.setAndroidBoardName(deviceInfos.getAndroidBoardName())
				.setAndroidBootloader(deviceInfos.getAndroidBootloader())
				.setDeviceBrand(deviceInfos.getDeviceBrand())
				.setDeviceId(deviceInfos.getDeviceId())
				.setDeviceModel(deviceInfos.getDeviceModel())
				.setDeviceModelBoot(deviceInfos.getDeviceModelBoot())
				.setDeviceModelIdentifier(deviceInfos.getDeviceModelIdentifier())
				.setFirmwareBrand(deviceInfos.getFirmwareBrand())
				.setFirmwareFingerprint(deviceInfos.getFirmwareFingerprint())
				.setFirmwareTags(deviceInfos.getFirmwareTags())
				.setFirmwareType(deviceInfos.getFirmwareType())
				.setHardwareManufacturer(deviceInfos.getHardwareManufacturer())
				.setHardwareModel(deviceInfos.getHardwareModel());
	}

	private static String bytesToHex(byte[] bytes) {
		char[] hexArray = "0123456789abcdef".toCharArray();
		char[] hexChars = new char[bytes.length * 2];
		for (int index = 0; index < bytes.length; index++) {
			int var = bytes[index] & 0xFF;
			hexChars[index * 2] = hexArray[var >>> 4];
			hexChars[index * 2 + 1] = hexArray[var & 0x0F];
		}
		return new String(hexChars).toLowerCase();
	}


	/**
	 * Gets the default device info for the given api
	 *
	 * @param api the api
	 * @return the default device info for the given api
	 */
	public static DeviceInfo getDefault(PokemonGo api) {
		DeviceInfo deviceInfo = new DeviceInfo();
		Random random = new Random(api.getSeed());
		byte[] bytes = new byte[16];
		random.nextBytes(bytes);
		SignatureOuterClass.Signature.DeviceInfo.Builder builder = deviceInfo.getBuilder();
		builder.setDeviceId(bytesToHex(bytes));
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
		String[] device = devices[random.nextInt(devices.length)];
		builder.setFirmwareType(osVersions[random.nextInt(osVersions.length)])
				.setDeviceModelBoot(device[0])
				.setDeviceModel(device[1])
				.setHardwareModel(device[2])
				.setFirmwareBrand("iPhone OS")
				.setDeviceBrand("Apple")
				.setHardwareManufacturer("Apple");
		return deviceInfo;
	}

	/**
	 * Gets the device info builder
	 *
	 * @return the device info builder
	 */
	public SignatureOuterClass.Signature.DeviceInfo.Builder getBuilder() {
		return deviceInfoBuilder;
	}
}
