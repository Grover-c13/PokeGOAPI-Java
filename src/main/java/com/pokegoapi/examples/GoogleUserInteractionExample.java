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

package com.pokegoapi.examples;


import com.pokegoapi.auth.GoogleUserCredentialProvider;
import com.pokegoapi.exceptions.LoginFailedException;
import com.pokegoapi.exceptions.RemoteServerException;
import okhttp3.OkHttpClient;


import java.util.Scanner;

public class GoogleUserInteractionExample {


	public static void main(String[] args) {
		OkHttpClient http = new OkHttpClient();
		GoogleUserCredentialProvider provider = null;
		try {

			provider = new GoogleUserCredentialProvider(http);
			System.out.println("Please go to " + provider.LOGIN_URL);
			System.out.println("Enter authorisation code:");
			Scanner sc = new Scanner(System.in);
			String access = sc.nextLine();
			provider.login(access);
			System.out.println("Refresh token:" + provider.getRefreshToken());
		} catch (LoginFailedException | RemoteServerException e) {
			e.printStackTrace();
		}

	}
}
