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


import POGOProtos.Data.Player.PlayerAvatarOuterClass;
import POGOProtos.Enums.GenderOuterClass;
import POGOProtos.Enums.PokemonIdOuterClass;
import com.pokegoapi.api.PokemonGo;
import com.pokegoapi.api.player.PlayerAvatar;
import com.pokegoapi.api.player.TutorialHandler;
import com.pokegoapi.auth.PtcCredentialProvider;
import com.pokegoapi.exceptions.LoginFailedException;
import com.pokegoapi.exceptions.RemoteServerException;
import com.pokegoapi.exceptions.TutorialCanceledException;
import com.pokegoapi.util.Log;
import okhttp3.OkHttpClient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class TutorialCompletionExample {

	/**
	 * Login and attempt to finish the tutorial
     * @param args args
	 */
	public static void main(String[] args) {
		OkHttpClient http = new OkHttpClient();
		PokemonGo go = new PokemonGo(http);
		try {
			// By default, the avatar, nickname and starter pokemon will be chosen randomly.
			// If you want to set specific values, just override the according methods in TutorialHandler
			// and pass it to login(CredentialsProvider, TutorialHandler).
			// For example:
			TutorialHandler tutorialHandler = new TutorialHandler() {
				@Override
				public boolean acceptTOS() {
					// By default, the TOS will be accepted automatically.
					// If you want to ask the user for permission, do it here.
					return true;
				}

				@Override
				public String chooseNickname() throws TutorialCanceledException {
					BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
					String nickname;

					System.out.println("Please choose a nickname (or enter nothing to cancel the tutorial):");
					try {
						nickname = br.readLine();
					} catch (IOException e) {
						e.printStackTrace();
						throw new TutorialCanceledException(e);
					}

					if(nickname == null || nickname.isEmpty()){
						throw new TutorialCanceledException("The user canceled the tutorial dialog.");
					}
					return nickname;
				}

				@Override
				public void onNicknameAlreadyInUse(String nickname) {
					System.out.println(String.format("%s is already in use.", nickname));
				}

				@Override
				public void onNicknameInvalid(String nickname) {
					System.out.println(String.format("%s is not a valid nickname. " +
							"Nicknames must have between 3 and 15 characters, and must not contain symbols.", nickname));
				}

				@Override
				public PlayerAvatarOuterClass.PlayerAvatar.Builder createAvatar() throws TutorialCanceledException {
					// The following will result in a randomly created avatar of female gender.
					// Alternatively you can create your avatar from scratch by calling
					// PlayerAvatarOuterClass.PlayerAvatar.newBuilder()
					// 		.setGender(gender)
					//		.setHair(intVal)  	// intVal must be >= 0 and < PlayerAvatar.getAvailableHair()
					//		.setHat(intVal)		// intVal must be >= 0 and < PlayerAvatar.getAvailableHats()
					//		...
					return super.createAvatar()
							.setGender(GenderOuterClass.Gender.FEMALE);
				}

				@Override
				public PokemonIdOuterClass.PokemonId chooseStarterPokemon() throws TutorialCanceledException {
					return PokemonIdOuterClass.PokemonId.BULBASAUR;
				}
			};

			go.login(new PtcCredentialProvider(http, ExampleLoginDetails.LOGIN,
					ExampleLoginDetails.PASSWORD), tutorialHandler);

			System.out.println(String.format(
					"Welcome to Pokémon GO, %s!",
					go.getPlayerProfile().getPlayerData().getUsername()));

			// Omitting the tutorial handler will result in a randomly chosen avatar, nickname and starter pokemon
			// if not chosen already. Example:
			// go.login(new PtcCredentialProvider(http, ExampleLoginDetails.LOGIN, ExampleLoginDetails.PASSWORD));

		} catch (LoginFailedException | RemoteServerException e) {
			// failed to login, invalid credentials, auth issue or server issue.
			Log.e("Main", "Failed to login or server issue: ", e);

		}
	}
}
