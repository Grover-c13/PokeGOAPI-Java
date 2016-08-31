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

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by iGio90 on 29/08/16.
 */

public class ClientInterceptor implements Interceptor {

	@Override
	public Response intercept(Interceptor.Chain chain) throws IOException {
		Request originalRequest = chain.request();
		Request requestWithUserAgent = originalRequest.newBuilder()
				.removeHeader("User-Agent")
				.addHeader("User-Agent", "Niantic App")
				.build();

		return chain.proceed(requestWithUserAgent);
	}
}
