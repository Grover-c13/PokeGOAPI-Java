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

import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLStreamHandler;
import java.net.URLStreamHandlerFactory;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class CaptchaSolveHelper {
	public static final String USER_AGENT =
			"Mozilla/5.0 (Windows NT 10.0; WOW64) "
					+ "AppleWebKit/537.36 (KHTML, like Gecko) "
					+ "Chrome/54.0.2840.99 Safari/537.36";

	private static final List<Listener> LISTENERS = new ArrayList<Listener>();

	static {
		URL.setURLStreamHandlerFactory(new URLStreamHandlerFactory() {
			@Override
			public URLStreamHandler createURLStreamHandler(String protocol) {
				if (protocol.equals("unity")) {
					return new URLStreamHandler() {
						@Override
						protected URLConnection openConnection(URL url) throws IOException {
							return new URLConnection(url) {
								@Override
								public void connect() throws IOException {
									String token = url.toString().split(Pattern.quote(":"))[1];
									for (Listener listener : LISTENERS) {
										listener.onTokenReceived(token);
									}
								}
							};
						}
					};
				}
				return null;
			}
		});
	}

	/**
	 * Registers the given captcha token listener.
	 *
	 * @param listener the listener to register
	 */
	public static void registerListener(Listener listener) {
		LISTENERS.add(listener);
	}

	/**
	 * Removes the given captcha token listener.
	 *
	 * @param listener the listener to remove
	 */
	public static void removeListener(Listener listener) {
		LISTENERS.remove(listener);
	}

	public interface Listener {
		void onTokenReceived(String token);
	}
}
