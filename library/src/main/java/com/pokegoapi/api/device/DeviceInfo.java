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

import java.util.Random;
import java.util.UUID;

import POGOProtos.Networking.Envelopes.SignatureOuterClass;

/**
 * Created by fabianterhorst on 08.08.16.
 */

public class DeviceInfo {

	public static final DeviceInfo DEFAULT = new DeviceInfo() {
		{
			String uuid = UUID.randomUUID().toString();
			setDeviceId(uuid);
			Random random = new Random(uuid.hashCode());
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
			setFirmwareType(osVersions[random.nextInt(osVersions.length)]);
			String[] device = devices[random.nextInt(devices.length)];
			setDeviceModelBoot(device[0]);
			setDeviceModel(device[1]);
			setHardwareModel(device[2]);
			setFirmwareBrand("iPhone OS");
			setDeviceBrand("Apple");
			setHardwareManufacturer("Apple");
		}
	};

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

	/**
	 * Sets AndroidBoardName
	 *
	 * <pre>
	 * {@code deviceInfo.setAndroidBoardName(Build.BOARD);}
	 * </pre>
	 *
	 * @param androidBoardName AndroidBoardName, for example: "angler"
	 */
	public void setAndroidBoardName(String androidBoardName) {
		deviceInfoBuilder.setAndroidBoardName(androidBoardName);
	}

	/**
	 * Sets AndroidBootloader
	 *
	 * <pre>
	 * {@code deviceInfo.setAndroidBootloader(Build.BOOTLOADER);}
	 * </pre>
	 *
	 * @param androidBootloader AndroidBootloader, for example: "angler-03.58"
	 */
	public void setAndroidBootloader(String androidBootloader) {
		deviceInfoBuilder.setAndroidBootloader(androidBootloader);
	}

	/**
	 * Sets DeviceBrand
	 *
	 * <pre>
	 * {@code deviceInfo.setDeviceBrand(Build.BRAND);}
	 * </pre>
	 *
	 * @param deviceBrand DeviceBrand, for example: "google"
	 */
	public void setDeviceBrand(String deviceBrand) {
		deviceInfoBuilder.setDeviceBrand(deviceBrand);
	}

	/**
	 * Sets DeviceId
	 *
	 * <pre>
	 * {@code deviceInfo.setDeviceId(UUID.randomUUID().toString());}
	 * </pre>
	 *
	 * @param deviceId DeviceId, for example: "****************"
	 */
	public void setDeviceId(String deviceId) {
		deviceInfoBuilder.setDeviceId(deviceId);
	}

	/**
	 * Sets DeviceModel
	 *
	 * <pre>
	 * {@code deviceInfo.setDeviceModel(Build.MODEL);}
	 * </pre>
	 *
	 * @param deviceModel DeviceModel, for example: "Nexus 6P"
	 */
	public void setDeviceModel(String deviceModel) {
		deviceInfoBuilder.setDeviceModel(deviceModel);
	}

	/**
	 * Sets DeviceModelBoot
	 *
	 * <pre>
	 * {@code deviceInfo.setDeviceModelBoot("qcom");}
	 * </pre>
	 *
	 * @param deviceModelBoot DeviceModelBoot, for example: "qcom"
	 */
	public void setDeviceModelBoot(String deviceModelBoot) {
		deviceInfoBuilder.setDeviceModelBoot(deviceModelBoot);
	}

	/**
	 * Sets DeviceModelIdentifier
	 *
	 * <pre>
	 * {@code deviceInfo.setDeviceModelIdentifier(Build.PRODUCT);}
	 * </pre>
	 *
	 * @param deviceModelIdentifier DeviceModelIdentifier, for example: "angler"
	 */
	public void setDeviceModelIdentifier(String deviceModelIdentifier) {
		deviceInfoBuilder.setDeviceModelIdentifier(deviceModelIdentifier);
	}

	/**
	 * Sets FirmwareBrand
	 *
	 * <pre>
	 * {@code deviceInfo.setFirmwareBrand(Build.PRODUCT);}
	 * </pre>
	 *
	 * @param firmwareBrand FirmwareBrand, for example: "angler"
	 */
	public void setFirmwareBrand(String firmwareBrand) {
		deviceInfoBuilder.setFirmwareBrand(firmwareBrand);
	}

	/**
	 * Sets FirmwareFingerprint
	 *
	 * <pre>
	 * {@code deviceInfo.setFirmwareFingerprint(Build.FINGERPRINT);}
	 * </pre>
	 *
	 * @param firmwareFingerprint FirmwareFingerprint,
	 *                            for example: "google/angler/angler:7.0/NPD90G/3051502:user/release-keys"
	 */
	public void setFirmwareFingerprint(String firmwareFingerprint) {
		deviceInfoBuilder.setFirmwareFingerprint(firmwareFingerprint);
	}

	/**
	 * Sets FirmwareTags
	 *
	 * <pre>
	 * {@code deviceInfo.setFirmwareTags(Build.TAGS);}
	 * </pre>
	 *
	 * @param firmwareTags FirmwareTags, for example: "release-keys"
	 */
	public void setFirmwareTags(String firmwareTags) {
		deviceInfoBuilder.setFirmwareTags(firmwareTags);
	}

	/**
	 * Sets FirmwareType
	 *
	 * <pre>
	 * {@code deviceInfo.setFirmwareType(Build.TYPE);}
	 * </pre>
	 *
	 * @param firmwareType FirmwareType, for example: "user"
	 */
	public void setFirmwareType(String firmwareType) {
		deviceInfoBuilder.setFirmwareType(firmwareType);
	}

	/**
	 * Sets HardwareManufacturer
	 *
	 * <pre>
	 * {@code deviceInfo.setHardwareManufacturer(Build.MANUFACTURER);}
	 * </pre>
	 *
	 * @param hardwareManufacturer HardwareManufacturer, for example: "Huawei"
	 */
	public void setHardwareManufacturer(String hardwareManufacturer) {
		deviceInfoBuilder.setHardwareManufacturer(hardwareManufacturer);
	}

	/**
	 * Sets HardwareModel
	 *
	 * <pre>
	 * {@code deviceInfo.setHardwareModel(Build.HARDWARE);}
	 * </pre>
	 *
	 * @param hardwareModel HardwareModel, for example: "Nexus 6P"
	 */
	public void setHardwareModel(String hardwareModel) {
		deviceInfoBuilder.setHardwareModel(hardwareModel);
	}

	/**
	 * Gets DeviceInfo.
	 *
	 * @return DeviceInfo
	 */
	public SignatureOuterClass.Signature.DeviceInfo getDeviceInfo() {
		return deviceInfoBuilder.build();
	}
}
