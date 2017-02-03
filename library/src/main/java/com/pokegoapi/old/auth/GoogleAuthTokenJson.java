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

package com.pokegoapi.old.auth;

import com.squareup.moshi.Json;




public class GoogleAuthTokenJson {


	private String error;


	@Json(name = "access_token")
	private String accessToken;


	@Json(name = "token_type")
	private String tokenType;


	@Json(name = "expires_in")
	private int expiresIn;


	@Json(name = "refresh_token")
	private String refreshToken;


	@Json(name = "id_token")
	private String idToken;
}
