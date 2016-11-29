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

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class Utils {

	/**
	 * Converts input streams to byte arrays.
	 *
	 * @param input the input
	 * @param size the size
	 * @return the byte [ ]
	 * @throws IOException the io exception
	 */
	public static byte[] inputStreamToByteArray(InputStream input, int size) throws IOException {
		byte[] buffer = new byte[size];
		int bytesRead;
		ByteArrayOutputStream output = new ByteArrayOutputStream();
		while ((bytesRead = input.read(buffer)) != -1) {
			output.write(buffer, 0, bytesRead);
		}
		return output.toByteArray();
	}

	/**
	 * Appends the given requests to the given array
	 *
	 * @param requests the base array
	 * @param append the requests to append
	 * @return a new array with the appended requests
	 */
	public static PokemonRequest[] appendRequests(PokemonRequest[] requests, PokemonRequest... append) {
		PokemonRequest[] newRequests = new PokemonRequest[requests.length + append.length];
		System.arraycopy(requests, 0, newRequests, 0, requests.length);
		System.arraycopy(append, 0, newRequests, requests.length, append.length);
		return newRequests;
	}

	/**
	 * Checks if the given error is not null, and returns to the AsyncReturn with the given error value.
	 * @param e the exception
	 * @param asyncReturn the callback to return on
	 * @param error the default error return value
	 * @param <T> the return type
	 * @return true if the exception was not null
	 */
	public static <T> boolean callbackException(Exception e, AsyncReturn<T> asyncReturn, T error) {
		if (e != null) {
			asyncReturn.onReceive(error, e);
			return true;
		}
		return false;
	}

	/**
	 * Checks if PokemonResponse contains an error, and completes the callback with the exception.
	 * @param response the response possibly containing an error
	 * @param callback the callback to call if an error occurs
	 * @return true if an error occurred
	 */
	public static boolean callbackException(PokemonResponse response, PokemonCallback callback) {
		if (response.hasErrored()) {
			callback.onCompleted(response.getException());
			return true;
		}
		return false;
	}

	/**
	 * Checks if PokemonResponse contains an error, and returns to the AsyncReturn with the given error value.
	 * @param response the response possibly containing an error
	 * @param asyncReturn the callback to return on
	 * @param error the default error return value
	 * @param <T> the return type
	 * @return true if an error occurred
	 */
	public static <T> boolean callbackException(PokemonResponse response, AsyncReturn<T> asyncReturn, T error) {
		if (response.hasErrored()) {
			asyncReturn.onReceive(error, response.getException());
			return true;
		}
		return false;
	}
}
