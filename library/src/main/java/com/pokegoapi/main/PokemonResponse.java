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

package com.pokegoapi.main;

import com.google.protobuf.ByteString;
import lombok.Getter;

public class PokemonResponse {
	@Getter
	private final ByteString responseData;
	@Getter
	private final Exception exception;

	private PokemonResponse(ByteString responseData, Exception exception) {
		this.responseData = responseData;
		this.exception = exception;
	}

	/**
	 * Checks if this response is an error.
	 *
	 * @return if this response contains an exception
	 */
	public boolean hasErrored() {
		return this.exception != null;
	}

	public static PokemonResponse getError(Exception exception) {
		return new PokemonResponse(null, exception);
	}

	public static PokemonResponse getResult(ByteString result) {
		return new PokemonResponse(result, null);
	}
}
