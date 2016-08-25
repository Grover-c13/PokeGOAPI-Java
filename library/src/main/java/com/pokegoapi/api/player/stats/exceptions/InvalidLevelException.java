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

package com.pokegoapi.api.player.stats.exceptions;

/**
 * Exception for non valid level
 * 
 * @author gionata-bisciari
 *
 */
public class InvalidLevelException extends Exception {

	private static final long serialVersionUID = 5487201246523954682L;

	public InvalidLevelException() {
		super();
	}

	public InvalidLevelException(String reason) {
		super(reason);
	}

	public InvalidLevelException(Throwable exception) {
		super(exception);
	}
}
