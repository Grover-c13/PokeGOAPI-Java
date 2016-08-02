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

public class ResultOrException {
	@Getter
	private final ByteString result;
	@Getter
	private final Exception exception;

	private ResultOrException(ByteString result, Exception exception) {
		this.result = result;
		this.exception = exception;
	}

	public static ResultOrException getError(Exception exception) {
		return new ResultOrException(null, exception);
	}

	public static ResultOrException getResult(ByteString result) {
		return new ResultOrException(result, null);
	}
}
