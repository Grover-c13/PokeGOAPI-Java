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
	 * Sets AndroidBoardName.
	 *
	 * @param androidBoardName  the AndroidBoardName
	 */
	public void setAndroidBoardName(String androidBoardName) {
		deviceInfoBuilder.setAndroidBoardName(androidBoardName);
	}

	/**
	 * Sets AndroidBootloader.
	 *
	 * @param androidBootloader  the AndroidBootloader
	 */
	public void setAndroidBootloader(String androidBootloader) {
		deviceInfoBuilder.setAndroidBootloader(androidBootloader);
	}

	/**
	 * Sets DeviceBrand.
	 *
	 * @param deviceBrand  the DeviceBrand
	 */
	public void setDeviceBrand(String deviceBrand) {
		deviceInfoBuilder.setDeviceBrand(deviceBrand);
	}

	/**
	 * Sets DeviceId.
	 *
	 * @param deviceId  the DeviceId
	 */
	public void setDeviceId(String deviceId) {
		deviceInfoBuilder.setDeviceId(deviceId);
	}

	/**
	 * Sets DeviceModel.
	 *
	 * @param deviceModel  the DeviceModel
	 */
	public void setDeviceModel(String deviceModel) {
		deviceInfoBuilder.setDeviceModel(deviceModel);
	}

	/**
	 * Sets DeviceModelBoot.
	 *
	 * @param deviceModelBoot  the DeviceModelBoot
	 */
	public void setDeviceModelBoot(String deviceModelBoot) {
		deviceInfoBuilder.setDeviceModelBoot(deviceModelBoot);
	}

	/**
	 * Sets DeviceModelIdentifier.
	 *
	 * @param deviceModelIdentifier  the DeviceModelIdentifier
	 */
	public void setDeviceModelIdentifier(String deviceModelIdentifier) {
		deviceInfoBuilder.setDeviceModelIdentifier(deviceModelIdentifier);
	}

	/**
	 * Sets FirmwareBrand.
	 *
	 * @param firmwareBrand  the FirmwareBrand
	 */
	public void setFirmwareBrand(String firmwareBrand) {
		deviceInfoBuilder.setFirmwareBrand(firmwareBrand);
	}

	/**
	 * Sets FirmwareFingerprint.
	 *
	 * @param firmwareFingerprint  the FirmwareFingerprint
	 */
	public void setFirmwareFingerprint(String firmwareFingerprint) {
		deviceInfoBuilder.setFirmwareFingerprint(firmwareFingerprint);
	}

	/**
	 * Sets FirmwareTags.
	 *
	 * @param firmwareTags  the FirmwareTags
	 */
	public void setFirmwareTags(String firmwareTags) {
		deviceInfoBuilder.setFirmwareTags(firmwareTags);
	}

	/**
	 * Sets FirmwareType.
	 *
	 * @param firmwareType  the FirmwareType
	 */
	public void setFirmwareType(String firmwareType) {
		deviceInfoBuilder.setFirmwareType(firmwareType);
	}

	/**
	 * Sets HardwareManufacturer.
	 *
	 * @param hardwareManufacturer  the HardwareManufacturer
	 */
	public void setHardwareManufacturer(String hardwareManufacturer) {
		deviceInfoBuilder.setHardwareManufacturer(hardwareManufacturer);
	}

	/**
	 * Sets HardwareModel.
	 *
	 * @param hardwareModel  the HardwareModel
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
