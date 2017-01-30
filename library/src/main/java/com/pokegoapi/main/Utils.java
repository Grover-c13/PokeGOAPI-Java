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
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

public class Utils {
	private static final File TEMP_DIR = new File(System.getProperty("java.io.tmpdir"), "pokego-java");

	static {
		TEMP_DIR.mkdir();
	}

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
	public static ServerRequest[] appendRequests(ServerRequest[] requests, ServerRequest... append) {
		ServerRequest[] newRequests = new ServerRequest[requests.length + append.length];
		System.arraycopy(requests, 0, newRequests, 0, requests.length);
		System.arraycopy(append, 0, newRequests, requests.length, append.length);
		return newRequests;
	}

	/**
	 * Creates a temp file at the given location
	 *
	 * @param name the name of the file
	 * @return the temp file
	 * @throws IOException if there is a problem while creating the file
	 */
	public static File createTempFile(String name) throws IOException {
		final File temp = Utils.getTempFile(name);
		if (!temp.getParentFile().exists()) {
			temp.getParentFile().mkdirs();
		}
		if (!(temp.exists()) && !(temp.createNewFile())) {
			throw new IOException("Could not create temp file: " + temp.getAbsolutePath());
		}
		return temp;
	}

	/**
	 * Gets the temp file at a given location without creating it
	 *
	 * @param name the name of this file
	 * @return the requested temp file
	 */
	public static File getTempFile(String name) {
		return new File(TEMP_DIR, name);
	}
}
