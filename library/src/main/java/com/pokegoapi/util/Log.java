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

import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * Created by Will on 7/20/16.
 */
@SuppressWarnings({"checkstyle:methodname", "checkstyle:javadocmethod"})
public class Log {

	private static Logger logger;
	private static Level level = Level.VERBOSE;

	private static Logger getInstance() {
		if (logger == null) {
			logger = new DefaultLogger();
		}
		return logger;
	}

	// Do not call this while logging from a different thread...
	// That's asking for trouble...
	public static void setInstance(Logger logger) {
		Log.logger = logger;
	}

	/**
	 * Sets the level of the Logger. For example, if the level is Error, all
	 * ERROR and ASSERT messages will be logged, but nothing else.
	 *
	 * @param level the level to log at
	 */
	public static void setLevel(Level level) {
		Log.level = level;
	}

	public static void v(String tag, String msg) {
		if (level.level <= Level.VERBOSE.level) {
			getInstance().v(tag, msg);
		}
	}

	public static void v(String tag, String msg, Throwable tr) {
		if (Log.level.level <= Level.VERBOSE.level) {
			getInstance().v(tag, msg, tr);
		}
	}

	public static void d(String tag, String msg) {
		if (Log.level.level <= Level.DEBUG.level) {
			getInstance().d(tag, msg);
		}
	}

	public static void d(String tag, String msg, Throwable tr) {
		if (Log.level.level <= Level.DEBUG.level) {
			getInstance().d(tag, msg, tr);
		}
	}

	public static void i(String tag, String msg) {
		if (Log.level.level <= Level.INFO.level) {
			getInstance().i(tag, msg);
		}
	}

	public static void i(String tag, String msg, Throwable tr) {
		if (Log.level.level <= Level.INFO.level) {
			getInstance().i(tag, msg, tr);
		}
	}

	public static void w(String tag, String msg) {
		if (Log.level.level <= Level.WARN.level) {
			getInstance().w(tag, msg);
		}
	}

	public static void w(String tag, String msg, Throwable tr) {
		if (Log.level.level <= Level.WARN.level) {
			getInstance().w(tag, msg, tr);
		}
	}

	public static void w(String tag, Throwable tr) {
		if (Log.level.level <= Level.WARN.level) {
			getInstance().w(tag, tr);
		}
	}

	public static void e(String tag, String msg) {
		if (Log.level.level <= Level.ERROR.level) {
			getInstance().e(tag, msg);
		}
	}

	public static void e(String tag, String msg, Throwable tr) {
		if (Log.level.level <= Level.ERROR.level) {
			getInstance().e(tag, msg, tr);
		}
	}

	public static void wtf(String tag, String msg) {
		if (Log.level.level <= Level.ASSERT.level) {
			getInstance().wtf(tag, msg);
		}
	}

	public static void wtf(String tag, Throwable tr) {
		if (Log.level.level <= Level.ASSERT.level) {
			getInstance().wtf(tag, tr);
		}
	}

	public static void wtf(String tag, String msg, Throwable tr) {
		if (Log.level.level <= Level.ASSERT.level) {
			getInstance().wtf(tag, msg, tr);
		}
	}

	public enum Level {

		ALL(Integer.MIN_VALUE),
		VERBOSE(2),
		DEBUG(3),
		INFO(4),
		WARN(5),
		ERROR(6),
		ASSERT(7),
		NONE(Integer.MAX_VALUE);

		private int level;

		Level(int level) {
			this.level = level;
		}

	}

	private static final class DefaultLogger extends BaseLogger {
		@Override
		public void log(Level level, String tag, String msg, Throwable tr) {
			String prefix = level.name().substring(0, 1);
			String body = "";
			if (msg != null) {
				body += msg;
			}
			if (tr != null) {
				StringWriter sw = new StringWriter();
				PrintWriter printWriter = new PrintWriter(sw);
				tr.printStackTrace(printWriter);
				body += "\n" + sw.toString();
				// No need to close. No resources taken up
			}
			String result = String.format("%s/%s: %s", prefix, tag, body);
			System.out.println(result);
		}
	}
}
