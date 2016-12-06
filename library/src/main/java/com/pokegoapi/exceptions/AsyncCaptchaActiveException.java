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

package com.pokegoapi.exceptions;

import lombok.Getter;

/**
 * Exception thrown when a message is requested to send, but a captcha is currently active
 */
public class AsyncCaptchaActiveException extends AsyncPokemonGoException {
	@Getter
	private String captcha;

	public AsyncCaptchaActiveException(String captcha) {
		super("Captcha must be solved before sending messages!");
		this.captcha = captcha;
	}

	public AsyncCaptchaActiveException(Exception exception, String captcha) {
		super("Captcha must be solved before sending messages!", exception);
		this.captcha = captcha;
	}
}
