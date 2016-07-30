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

package com.pokegoapi.api.async;

/**
 * Author: Mustafa Ashurex
 * Created: 7/30/16
 * <p>
 * Class for holding and returning exceptions that arise in an anonymous method.
 * </p>
 */
public class ExceptionBox {
	private Throwable exception;

	public ExceptionBox() {
	}

	public boolean hasException() {
		return getException() != null;
	}

	public Throwable getException() {
		return exception;
	}

	/**
	 * @param throwable The exception to store.
	 */
	public void setException(Throwable throwable) {
		this.exception = throwable;
	}
}
