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

import lombok.Data;

/**
 * Created by fabianterhorst on 08.08.16.
 */

public interface DeviceInfoProvider {
	Info getInfo();

	@Data
	class Info {
		/**
		 * adb.exe shell getprop ro.product.board
		 *
		 * @return android board name, for example: "angler"
		 */
		private String androidBoardName = "";

		/**
		 * adb.exe shell getprop ro.boot.bootloader
		 *
		 * @return android bootloader, for example: "angler-03.58"
		 */
		private String androidBootloader = "";

		/**
		 * adb.exe shell getprop ro.product.brand
		 *
		 * @return device brand, for example: "google"
		 */
		private String deviceBrand = "";

		/**
		 *  adb.exe shell settings get secure android_id
		 *  UUID.randomUUID().toString
		 *
		 * @return device id, for example: "****************"
		 */
		private String deviceId = "";

		/**
		 *  adb.exe shell getprop ro.product.model
		 *
		 * @return device model, for example: "Nexus 6P"
		 */
		private String deviceModel = "";

		/**
		 *  adb.exe shell getprop ro.product.name
		 *
		 * @return device model identifier, for example: "angler"
		 */
		private String deviceModelIdentifier = "";

		/**
		 *  Always qcom
		 *
		 * @return device boot model, for example: "qcom"
		 */
		private String deviceModelBoot = "";

		/**
		 *  adb.exe shell getprop ro.product.manufacturer
		 *
		 * @return hardware manufacturer, for example: "Huawei"
		 */
		private String hardwareManufacturer = "";

		/**
		 *  adb.exe shell getprop ro.product.model
		 *
		 * @return hardware model, for example: "Nexus 6P"
		 */
		private String hardwareModel = "";

		/**
		 *  adb.exe shell getprop ro.product.name
		 *
		 * @return firmware brand, for example: "angler"
		 */
		private String firmwareBrand = "";

		/**
		 *  adb.exe shell getprop ro.build.tags
		 *
		 * @return firmware tags, for example: "release-keys"
		 */
		private String firmwareTags = "";

		/**
		 *  adb.exe shell getprop ro.build.type
		 *
		 * @return firmware type, for example: "user"
		 */
		private String firmwareType = "";

		/**
		 *  adb.exe shell getprop ro.build.fingerprint
		 *
		 * @return firmware fingerprint, for example: "google/angler/angler:7.0/NPD90G/3051502:user/release-keys"
		 */
		private String firmwareFingerprint = "";
	}
}
