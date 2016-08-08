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

/**
 * Created by fabianterhorst on 08.08.16.
 */

public interface DeviceInfos {
	/* adb.exe shell getprop ro.product.board */
	String getAndroidBoardName();

	/* adb.exe shell getprop ro.boot.bootloader */
	String getAndroidBootloader();

	/* adb.exe shell getprop ro.product.brand */
	String getDeviceBrand();

	/* adb.exe shell settings get secure android_id */
	String getDeviceId();

	/* adb.exe shell getprop ro.product.model */
	String getDeviceModel();

	/* adb.exe shell getprop ro.product.name */
	String getDeviceModelIdentifier();

	/* qcom */
	String getDeviceModelBoot();

	/* adb.exe shell getprop ro.product.manufacturer */
	String getHardwareManufacturer();

	/* adb.exe shell getprop ro.product.model */
	String getHardwareModel();

	/* adb.exe shell getprop ro.product.name */
	String getFirmwareBrand();

	/* adb.exe shell getprop ro.build.tags */
	String getFirmwareTags();

	/* adb.exe shell getprop ro.build.type */
	String getFirmwareType();

	/* adb.exe shell getprop ro.build.fingerprint */
	String getFirmwareFingerprint();
}
