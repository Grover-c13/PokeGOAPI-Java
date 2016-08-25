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

/**
 * Created by Will on 7/20/16.
 */
@SuppressWarnings("checkstyle:methodname")
public interface Logger {

	void v(String tag, String msg);

	void v(String tag, String msg, Throwable tr);

	void d(String tag, String msg);

	void d(String tag, String msg, Throwable tr);

	void i(String tag, String msg);

	void i(String tag, String msg, Throwable tr);

	void w(String tag, String msg);

	void w(String tag, String msg, Throwable tr);

	void w(String tag, Throwable tr);

	void e(String tag, String msg);

	void e(String tag, String msg, Throwable tr);

	void wtf(String tag, String msg);

	void wtf(String tag, Throwable tr);

	void wtf(String tag, String msg, Throwable tr);
}
