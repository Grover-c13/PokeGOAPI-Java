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

package com.pokegoapi.auth;

import com.squareup.moshi.Json;
import lombok.Getter;
import lombok.Setter;

public class GoogleAuthJson {
	@Getter
	@Setter
	@Json(name = "device_code")
	String deviceCode;
	@Getter
	@Setter
	@Json(name = "user_code")
	String userCode;
	@Getter
	@Setter
	@Json(name = "verification_url")
	String verificationUrl;
	@Getter
	@Setter
	@Json(name = "expires_in")
	int expiresIn;
	@Getter
	@Setter
	@Json(name = "interval")
	int interval;
}
