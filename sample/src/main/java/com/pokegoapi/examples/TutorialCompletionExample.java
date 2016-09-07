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
import com.pokegoapi.api.player.Tutorial;
import com.pokegoapi.auth.PtcCredentialProvider;
import com.pokegoapi.exceptions.LoginFailedException;
import com.pokegoapi.exceptions.RemoteServerException;
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

		// The tutorial provider creates a tutorial and returns it
		// If not set, everything will be set randomly and the TOS of Niantic will be accepted.
		go.setTutorialProvider(tutorialProvider);

		try {
			// Next you can simply login and in case that the tutorial hasn't been finished before, your tutorial provider
			// will be called
			go.login(new PtcCredentialProvider(http, ExampleLoginDetails.LOGIN, ExampleLoginDetails.PASSWORD));
			System.out.println(String.format(
					"Welcome to Pokémon GO, %s!",
					go.getPlayerProfile().getPlayerData().getUsername()));

		} catch (LoginFailedException | RemoteServerException e) {
			// failed to login, invalid credentials, auth issue or server issue.
			Log.e("Main", "Failed to login or server issue: ", e);
		}
	}

	// The nickname dialog handles what should happen in case that a valid nickname has not been entered before.
	// It provides fallback handling if the given nickname was invalid or already in use.
	private static Tutorial.TutorialProvider tutorialProvider = new Tutorial.TutorialProvider() {
		@Override
		public Tutorial getTutorial() throws Tutorial.CanceledException {

			// First we need a tutorial builder.
			// We can either set specific values or let them be generated randomly, which happens by default.
			Tutorial.TutorialBuilder tutorialBuilder = Tutorial.newBuilder();

			// If the TOS have not been accepted before, ask the user if they want to accept them.
			// Note that if you don't set the TOS, they will be accepted automatically.
			if (isAcceptTOSMissing()) {

				boolean accept;

				try {
					// Ask user
					accept = askForTOSAcceptance();
				} catch (IOException e) {
					throw new Tutorial.CanceledException(e);
				}

				tutorialBuilder.setAcceptTOS(accept);
			}

			// If no avatar has been set before, set an avatar
			// Note that if you don't set an avatar, it will be set randomly.
			if (isAvatarMissing()) {

				// Create random avatar and change only the hair
				tutorialBuilder.setPlayerAvatar(
						Tutorial.getRandomPlayerAvatar()
								.setHair(2)
								.build()
				);

				// Or create avatar from scratch
				tutorialBuilder.setPlayerAvatar(
						PlayerAvatarOuterClass.PlayerAvatar.newBuilder()
								.setGender(GenderOuterClass.Gender.MALE)
								.setHair(1)
								.setEyes(1)
								.setBackpack(1)
								.setHat(1)
								.setPants(1)
								.setShirt(1)
								.setShoes(1)
								.setSkin(1)
								.build()
				);
			}

			// If no nickname has been set before, ask the user using the nickname dialog
			// Note that if you don't provide a nickname dialog, a random nickname will be set.
			if (isNicknameMissing()) {
				tutorialBuilder.setNicknameDialog(nicknameDialog);
			}

			// If no starter pokemon has been set before, choose Pikachu
			// Note that if you don't set a starter pokemon, it will be set randomly.
			if (isStarterPokemonMissing()) {
				tutorialBuilder.setStarterPokemon(PokemonIdOuterClass.PokemonId.PIKACHU);
			}

			return tutorialBuilder.build();
		}

		// asks the user for permission before accepting the Niantic TOS
		private boolean askForTOSAcceptance() throws IOException {
			System.out.println("Niantic Terms of Service\r\n" +
					"Please read the TOS on following website: https://www.nianticlabs.com/terms/pokemongo/en\r\n" +
					"Do you accept? (y/n)");

			while (true) {
				BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
				String str = br.readLine();

				if (str.equals("y")) {
					return true;
				} else if (str.equals("n")) {
					return false;
				}
			}
		}
	};

	// The nickname dialog handles what should happen in case that a valid nickname has not been entered before.
	// It provides fallback handling if the given nickname was invalid or already in use.
	private static Tutorial.NicknameDialog nicknameDialog = new Tutorial.NicknameDialog() {

		String enterNickname() throws Tutorial.CanceledException {
			BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
			String nickname;

			try {
				nickname = br.readLine();
			} catch (IOException e) {
				e.printStackTrace();
				throw new Tutorial.CanceledException(e);
			}

			if (nickname.isEmpty()) {
				throw new Tutorial.CanceledException("The user canceled the tutorial dialog.");
			}
			return nickname;
		}

		// This will be called if your user hasn't set a nickname yet
		// Return the nickname they want
		@Override
		public String getNickname() throws Tutorial.CanceledException {
			System.out.println("Please choose a nickname (or enter nothing to cancel the tutorial):");
			return enterNickname();
		}

		// This will be called if your user set a nickname that was invalid or already in use
		// Return another nickname they want
		@Override
		public String getFallback(String nickname, Tutorial.NicknameException cause) throws Tutorial.CanceledException {
			if (cause instanceof Tutorial.NicknameInvalidException) {
				System.out.println(String.format("%s is invalid. " +
						"A valid nickname contains between 3 and 15 characters and no symbols.\r\n" +
						"Please choose a valid nickname (or enter nothing to cancel the tutorial):", nickname));
			} else {
				System.out.println(String.format("%s is already in use.\r\n" +
						"Please choose a different nickname (or enter nothing to cancel the tutorial):", nickname));
			}
			return enterNickname();
		}
	};
}
