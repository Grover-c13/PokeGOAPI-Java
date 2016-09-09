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

package com.pokegoapi.util;

import com.pokegoapi.exceptions.AsyncPokemonGoException;
import com.pokegoapi.exceptions.request.RequestFailedException;
import rx.Observable;

public class AsyncHelper {
	/**
	 * Convert an observable to the actual result, recovering the actual exception and throwing that
	 *
	 * @param observable Observable to handle
	 * @param <T> Result type
	 * @return Result of the observable
	 * @throws RequestFailedException if an exception occurred while sending requests
	 */
	public static <T> T toBlocking(Observable<T> observable) throws RequestFailedException {
		try {
			return observable.toBlocking().first();
		} catch (RuntimeException e) {
			if (e.getCause() instanceof RequestFailedException) {
				throw new RequestFailedException(e.getMessage(), e.getCause());
			}
			throw new AsyncPokemonGoException("Unknown exception occurred. ", e);
		}
	}
}
