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

	/**
	 * Set AndroidBoardName
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
	 * Set AndroidBootloader
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
	 * Set DeviceBrand
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
	 * Set DeviceId
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
	 * Set DeviceModel
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
	 * Set DeviceModelBoot
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
	 * Set DeviceModelIdentifier
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
	 * Set FirmwareBrand
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
	 * Set FirmwareFingerprint
	 *
	 * <pre>
	 * {@code deviceInfo.setFirmwareFingerprint(Build.FINGERPRINT);}
	 * </pre>
	 *
	 * @param firmwareFingerprint FirmwareFingerprint, for example: "google/angler/angler:7.0/NPD90G/3051502:user/release-keys"
	 */
	public void setFirmwareFingerprint(String firmwareFingerprint) {
		deviceInfoBuilder.setFirmwareFingerprint(firmwareFingerprint);
	}

	/**
	 * Set FirmwareTags
	 *
	 * <pre>
	 * {@code deviceInfo.setFirmwareTags(Build.TAGS);}
	 * </pre>
	 *
	 * @param firmwareTags FirmwareTags, for example: "google/angler/angler:7.0/NPD90G/3051502:user/release-keys"
	 */
	public void setFirmwareTags(String firmwareTags) {
		deviceInfoBuilder.setFirmwareTags(firmwareTags);
	}

	/**
	 * Set FirmwareType
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
	 * Set HardwareManufacturer
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
	 * Set HardwareModel
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
