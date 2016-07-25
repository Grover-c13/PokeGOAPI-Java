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

import static com.pokegoapi.util.Log.Level.ASSERT;
import static com.pokegoapi.util.Log.Level.DEBUG;
import static com.pokegoapi.util.Log.Level.ERROR;
import static com.pokegoapi.util.Log.Level.INFO;
import static com.pokegoapi.util.Log.Level.VERBOSE;
import static com.pokegoapi.util.Log.Level.WARN;

import com.pokegoapi.util.Log.Level;


/**
 * Created by Will on 7/20/16.
 */
@SuppressWarnings("checkstyle:methodname")
public abstract class BaseLogger implements Logger {

	@Override
	public void v(String tag, String msg) {
		log(VERBOSE, tag, msg, null);
	}

	@Override
	public void v(String tag, String msg, Throwable tr) {
		log(VERBOSE, tag, msg, tr);
	}

	@Override
	public void d(String tag, String msg) {
		log(DEBUG, tag, msg, null);
	}

	@Override
	public void d(String tag, String msg, Throwable tr) {
		log(DEBUG, tag, msg, tr);
	}

	@Override
	public void i(String tag, String msg) {
		log(INFO, tag, msg, null);
	}

	@Override
	public void i(String tag, String msg, Throwable tr) {
		log(INFO, tag, msg, tr);
	}

	@Override
	public void w(String tag, String msg) {
		log(WARN, tag, msg, null);
	}

	@Override
	public void w(String tag, String msg, Throwable tr) {
		log(WARN, tag, msg, tr);
	}

	@Override
	public void w(String tag, Throwable tr) {
		log(WARN, tag, null, tr);
	}

	@Override
	public void e(String tag, String msg) {
		log(ERROR, tag, msg, null);
	}

	@Override
	public void e(String tag, String msg, Throwable tr) {
		log(ERROR, tag, msg, tr);
	}

	@Override
	public void wtf(String tag, String msg) {
		log(ASSERT, tag, msg, null);
	}

	@Override
	public void wtf(String tag, Throwable tr) {
		log(ASSERT, tag, null, tr);
	}

	@Override
	public void wtf(String tag, String msg, Throwable tr) {
		log(ASSERT, tag, msg, tr);
	}

	public abstract void log(Level level, String tag, String msg, Throwable tr);
}
