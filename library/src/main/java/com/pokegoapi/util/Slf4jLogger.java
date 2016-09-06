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

import org.slf4j.LoggerFactory;

/**
 * @author Paul van Assen
 */
public class Slf4jLogger implements Logger {
	@Override
	public void v(String tag, String msg) {
		getLogger(tag).debug(msg);
	}

	@Override
	public void v(String tag, String msg, Throwable tr) {
		getLogger(tag).debug(msg, tr);
	}

	@Override
	public void d(String tag, String msg) {
		getLogger(tag).debug(msg);
	}

	@Override
	public void d(String tag, String msg, Throwable tr) {
		getLogger(tag).debug(msg, tr);
	}

	@Override
	public void i(String tag, String msg) {
		getLogger(tag).info(msg);
	}

	@Override
	public void i(String tag, String msg, Throwable tr) {
		getLogger(tag).info(msg, tr);
	}

	@Override
	public void w(String tag, String msg) {
		getLogger(tag).warn(msg);
	}

	@Override
	public void w(String tag, String msg, Throwable tr) {
		getLogger(tag).warn(msg, tr);
	}

	@Override
	public void w(String tag, Throwable tr) {
		getLogger(tag).warn(tag, tr);
	}

	@Override
	public void e(String tag, String msg) {
		getLogger(tag).error(msg);
	}

	@Override
	public void e(String tag, String msg, Throwable tr) {
		getLogger(tag).error(msg, tr);
	}

	@Override
	public void wtf(String tag, String msg) {
		getLogger(tag).error(msg);
	}

	@Override
	public void wtf(String tag, Throwable tr) {
		getLogger(tag).error(tag, tr);
	}

	@Override
	public void wtf(String tag, String msg, Throwable tr) {
		getLogger(tag).error(msg, tr);
	}

	private org.slf4j.Logger getLogger(String tag) {
		return LoggerFactory.getLogger(tag);
	}
}
