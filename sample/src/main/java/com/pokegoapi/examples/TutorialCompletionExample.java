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
	 *
	 * @param args args
	 */
	public static void main(String[] args) {
		OkHttpClient http = new OkHttpClient();
		PokemonGo go = new PokemonGo(http);

		/*	With PokemonGo#setTutorial we can define the handling of not yet completed tutorial steps.
			If not set, the nickname, avatar and starter pokemon will be chosen randomly
			and the TOS of Niantic will be accepted automatically. */
		go.setTutorial(buildExampleTutorial());

		try {
			/*	Next you can simply login and in case that the tutorial hasn't been finished before,
				your tutorial provider will be called */
			go.login(new PtcCredentialProvider(http, ExampleLoginDetails.LOGIN, ExampleLoginDetails.PASSWORD));
			System.out.println(String.format(
					"Welcome to Pokémon GO, %s!",
					go.getPlayerProfile().getPlayerData().getUsername()));

		} catch (LoginFailedException | RemoteServerException e) {
			// failed to login, invalid credentials, auth issue or server issue.
			Log.e("Main", "Failed to login or server issue: ", e);
		}
	}

	/**
	 * Here we will build a Tutorial to set in our PokemonGo object.
	 *
	 * @return the tutorial
	 */
	private static Tutorial buildExampleTutorial() {

		/*	First we need a tutorial builder.
			We can either set specific values or let them be generated randomly, which happens by default. */
		return Tutorial.newBuilder()

				/*	If the TOS have not been accepted before, ask the user if they want to accept them.
					By default, the TOS will be accepted. */
				.setAcceptTOS(new Tutorial.AcceptTosInterface() {
					@Override
					public boolean acceptTOS() throws Tutorial.CanceledException {


						return acceptCharFromConsole(
								"Niantic Terms of Service\r\n" +
										"Please read the TOS on following website: https://www.nianticlabs.com/terms/pokemongo/en\r\n" +
										"Do you accept? (y/n)",
								'y', 'n') == 'y';
					}
				})

				/*	If no avatar has been set before, ask the user to create one.
					By default, the avatar will be set randomly. */
				.setPlayerAvatar(new Tutorial.PlayerAvatarInterface() {
					@Override
					public PlayerAvatarOuterClass.PlayerAvatar setPlayerAvatar() throws Tutorial.CanceledException {

						// Example 1: Ask the user
						GenderOuterClass.Gender gender =
								acceptCharFromConsole("Choose a gender [m / f]:", 'm', 'f') == 'm'
										? GenderOuterClass.Gender.MALE
										: GenderOuterClass.Gender.FEMALE;

						return PlayerAvatarOuterClass.PlayerAvatar.newBuilder()
								.setGender(gender)
								.setEyes(acceptNumberFromConsole("Choose eyes [0..%d]:", PlayerAvatar.getAvailableEyes()))
								.setHair(acceptNumberFromConsole("Choose hair [0..%d]:", PlayerAvatar.getAvailableHair()))
								.setBackpack(acceptNumberFromConsole("Choose bag [0..%d]:", PlayerAvatar.getAvailableBags(gender)))
								.setHat(acceptNumberFromConsole("Choose hat [0..%d]:", PlayerAvatar.getAvailableHats()))
								.setPants(acceptNumberFromConsole("Choose pants [0..%d]:", PlayerAvatar.getAvailablePants(gender)))
								.setShirt(acceptNumberFromConsole("Choose shirt [0..%d]:", PlayerAvatar.getAvailableShirts(gender)))
								.setShoes(acceptNumberFromConsole("Choose shoes [0..%d]:", PlayerAvatar.getAvailableShoes()))
								.setSkin(acceptNumberFromConsole("Choose skin [0..%d]:", PlayerAvatar.getAvailableSkins()))
								.build();

						// Example 2: Create a random avatar and change only the hair:
						/*return Tutorial.getRandomPlayerAvatar()
								.setHair(2)
								.build();*/

					}
				})

				/*	If no nickname has been set before, ask the user to choose one.
					By default, the nickname will be generated randomly (e.g. kKU8G46FfawQ36). */
				.setNickname(new Tutorial.NicknameInterface() {
					@Override
					public String setNickname() throws Tutorial.CanceledException {
						return acceptStringFromConsole("Please choose a nickname:");
					}
				})

				/*	If the chosen nickname was invalid or already in use, ask the user to choose a different one
					By default, the fallback will be generated randomly (e.g. kKU8G46FfawQ36). */
				.setNicknameFallback(new Tutorial.NicknameFallbackInterface() {
					@Override
					public String setNicknameFallback(String nickname, Tutorial.NicknameException cause)
							throws Tutorial.CanceledException {

						/*	Determine the cause of the error.
							At this point only 2 types of errors can happen:
							- nickname is invalid
							- nickname is already in use */
						if (cause.isNicknameInvalid()) {
							return acceptStringFromConsole(String.format("%s is invalid. " +
									"A valid nickname contains between 3 and 15 characters and no symbols.\r\n" +
									"Please choose a valid nickname (or enter nothing to cancel the tutorial):", nickname));
						} else {
							return acceptStringFromConsole(String.format("%s is already in use.\r\n" +
									"Please choose a different nickname (or enter nothing to cancel the tutorial):", nickname));
						}

					}
				})

				/*	If no starter pokemon has been set before, ask the user to choose one.
					Note that if you don't set a starter pokemon, it will be set randomly. */
				.setStarterPokemon(new Tutorial.StarterPokemonInterface() {
					@Override
					public PokemonIdOuterClass.PokemonId setStarterPokemon() {
						return PokemonIdOuterClass.PokemonId.PIKACHU;
					}
				})

				/*	When you're done, build it. */
				.build();

	}

	/**
	 * Accepts one of two characters from command line
	 *
	 * @param option1 option 1
	 * @param option2 option 2
	 * @return one of the two options
	 * @throws Tutorial.CanceledException when empty line is received or IOException happens
	 */
	private static char acceptCharFromConsole(String prompt, char option1, char option2) throws Tutorial.CanceledException {
		System.out.println(prompt);
		char c = 0;
		while (c != option1 && c != option2) {
			c = acceptCharFromConsole(null);
		}
		return c;
	}

	/**
	 * Accepts a character from the command line
	 *
	 * @return the character
	 * @throws Tutorial.CanceledException when empty line is received or IOException happens
	 */
	private static char acceptCharFromConsole(String prompt) throws Tutorial.CanceledException {
		if (prompt != null) {
			System.out.println(prompt);
		}

		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		String str;
		try {
			while (true) {
				str = br.readLine();
				if (str.isEmpty()) {
					throw new Tutorial.CanceledException();
				}

				str = str.trim().toLowerCase();

				if (str.length() == 1) {
					return str.charAt(0);
				} else {
					System.out.println("Invalid. Please try again:");
				}
			}
		} catch (IOException e) {
			throw new Tutorial.CanceledException(e);
		}
	}

	/**
	 * Accepts a string from the command line
	 *
	 * @return the string
	 * @throws Tutorial.CanceledException when an empty string is received
	 */
	private static String acceptStringFromConsole(String prompt) throws Tutorial.CanceledException {
		System.out.println(prompt);

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

	/**
	 * Accepts a number from the command line that must be between 0 (inclusive) and available (exclusive)
	 *
	 * @param available the amount of available numbers
	 * @return the number
	 * @throws Tutorial.CanceledException when an empty string is received
	 */
	private static int acceptNumberFromConsole(String prompt, int available) throws Tutorial.CanceledException {
		System.out.println(String.format(prompt, available));

		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		String str;
		int number;

		while (true) {
			try {
				str = br.readLine();
				if (str.isEmpty()) {
					throw new Tutorial.CanceledException();
				}

				number = Integer.parseInt(str);
				if (0 <= number && number < available) {
					return number;
				}
				System.out.println("Too big or too small. Please try again:");
			} catch (NumberFormatException e) {
				System.out.println("Invalid. Please try again:");
			} catch (IOException e) {
				throw new Tutorial.CanceledException(e);
			}
			System.out.println("Invalid. Please try again:");
		}
	}

}
