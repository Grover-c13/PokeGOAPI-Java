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
import lombok.Getter;

import static sun.audio.AudioDevice.device;

/**
 * Created by fabianterhorst on 08.08.16.
 */

public class DeviceInfo {

	private SignatureOuterClass.Signature.DeviceInfo deviceInfo;
	private SignatureOuterClass.Signature.DeviceInfo.Builder deviceInfoBuilder;

	public DeviceInfo() {
		deviceInfoBuilder = SignatureOuterClass.Signature.DeviceInfo.newBuilder();
	}

	public void setAndroidBoardName(String androidBoardName) {
		deviceInfoBuilder.setAndroidBoardName(androidBoardName);
	}

	public void setAndroidBootloader(String androidBootloader) {
		deviceInfoBuilder.setAndroidBootloader(androidBootloader);
	}

	public void setDeviceBrand(String deviceBrand) {
		deviceInfoBuilder.setDeviceBrand(deviceBrand);
	}

	public void setDeviceId(String deviceId) {
		deviceInfoBuilder.setDeviceId(deviceId);
	}

	public void setDeviceModel(String deviceModel) {
		deviceInfoBuilder.setDeviceModel(deviceModel);
	}

	public void setDeviceModelBoot(String deviceModelBoot) {
		deviceInfoBuilder.setDeviceModelBoot(deviceModelBoot);
	}

	public void setDeviceModelIdentifier(String deviceModelIdentifier) {
		deviceInfoBuilder.setDeviceModelIdentifier(deviceModelIdentifier);
	}

	public void setFirmwareBrand(String firmwareBrand) {
		deviceInfoBuilder.setFirmwareBrand(firmwareBrand);
	}

	public void setFirmwareFingerprint(String firmwareFingerprint) {
		deviceInfoBuilder.setFirmwareFingerprint(firmwareFingerprint);
	}

	public void setFirmwareTags(String firmwareTags) {
		deviceInfoBuilder.setFirmwareTags(firmwareTags);
	}

	public void setFirmwareType(String firmwareType) {
		deviceInfoBuilder.setFirmwareType(firmwareType);
	}

	public void setHardwareManufacturer(String hardwareManufacturer) {
		deviceInfoBuilder.setHardwareManufacturer(hardwareManufacturer);
	}

	public void setHardwareModel(String hardwareModel) {
		deviceInfoBuilder.setHardwareModel(hardwareModel);
	}

	public DeviceInfo(DeviceInfos deviceInfos) {
		deviceInfo = SignatureOuterClass.Signature.DeviceInfo.newBuilder()
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
				.setHardwareModel(deviceInfos.getHardwareModel())
				.build();
	}

	public SignatureOuterClass.Signature.DeviceInfo getDeviceInfo() {
		if(deviceInfoBuilder != null) {
			return deviceInfoBuilder.build();
		}
		return deviceInfo;
	}
}
