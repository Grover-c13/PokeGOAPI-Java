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

package com.pokegoapi.api.player;

import POGOProtos.Data.Player.PlayerAvatarOuterClass;
import POGOProtos.Enums.GenderOuterClass;
import POGOProtos.Enums.PokemonIdOuterClass;
import com.pokegoapi.exceptions.TutorialCanceledException;

import java.security.SecureRandom;
import java.util.Random;

/**
 * Handles the unfinished tutorial.
 */
public abstract class TutorialHandler {
	/**
	 * Whether or not the user wants to accept the Terms of Service of Pokemon GO.
	 * By default, this will return True.
	 * You can override this and for example ask the user.
	 *
	 * @return True if the user accepts, else False.
	 */
	public boolean acceptTOS() {
		return true;
	}

	/**
	 * Which avatar should be given to the player.
	 * By default, this will return a completely random avatar.
	 * You can override this and for example ask the user.
	 *
	 * @return The chosen avatar.
	 * @throws TutorialCanceledException When canceled by the user.
	 */
	public PlayerAvatarOuterClass.PlayerAvatar.Builder createAvatar() throws TutorialCanceledException {
		Random random = new Random();

		final PlayerAvatarOuterClass.PlayerAvatar.Builder playerAvatarBuilder =
				PlayerAvatarOuterClass.PlayerAvatar.newBuilder();
		final boolean female = random.nextInt(100) % 2 == 0;
		if (female) {
			playerAvatarBuilder.setGender(GenderOuterClass.Gender.FEMALE);
		}

		playerAvatarBuilder.setSkin(random.nextInt(PlayerAvatar.getAvailableSkins()))
				.setHair(random.nextInt(PlayerAvatar.getAvailableHair()))
				.setEyes(random.nextInt(PlayerAvatar.getAvailableEyes()))
				.setHat(random.nextInt(PlayerAvatar.getAvailableHats()))
				.setShirt(random.nextInt(PlayerAvatar.getAvailableShirts(
						female ? GenderOuterClass.Gender.FEMALE : GenderOuterClass.Gender.MALE)))
				.setPants(random.nextInt(PlayerAvatar.getAvailablePants(
						female ? GenderOuterClass.Gender.FEMALE : GenderOuterClass.Gender.MALE)))
				.setShoes(random.nextInt(PlayerAvatar.getAvailableShoes()))
				.setBackpack(random.nextInt(PlayerAvatar.getAvailableShoes()));

		return playerAvatarBuilder;
	}

	/**
	 * Which pokemon should be chosen as the players starter pokemon.
	 * By default, this will return a random {@link PokemonIdOuterClass.PokemonId},
	 * chosen between Bulbasaur, Charmander, Squirtle and Pikachu.
	 * You can override this and for example ask the user.
	 *
	 * @return A valid starter pokemon.
	 * @throws TutorialCanceledException When canceled by the user.
	 */
	public PokemonIdOuterClass.PokemonId chooseStarterPokemon() throws TutorialCanceledException {
		Random random = new Random();
		int pokemonId = random.nextInt(4);

		return pokemonId == 1 ? PokemonIdOuterClass.PokemonId.BULBASAUR :
				pokemonId == 2 ? PokemonIdOuterClass.PokemonId.CHARMANDER :
						pokemonId == 3 ? PokemonIdOuterClass.PokemonId.SQUIRTLE
								: PokemonIdOuterClass.PokemonId.PIKACHU;
	}

	/**
	 * Which nickname should be given to the player.
	 * Will be called repeatedly until a valid and unclaimed nickname has been given.
	 * The returned nickname will be checked against {@link PlayerProfile#isNicknameValid(String)}
	 * and if it turns out to be invalid, {@link #onNicknameInvalid(String)} will be called.
	 * If the nickname is already in use, {@link #onNicknameAlreadyInUse(String)} will be called.
	 * By default, this will return a randomly generated nickname with a length between 10 and 14 characters.
	 * You can override this and for example ask the user.
	 *
	 * @return Some nickname.
	 * @throws TutorialCanceledException When canceled by the user.
	 */
	public String chooseNickname() throws TutorialCanceledException {
		final String a = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
		final SecureRandom r = new SecureRandom();
		final int l = new Random().nextInt(15 - 10) + 10;
		StringBuilder sb = new StringBuilder(l);
		for (int i = 0; i < l; i++) {
			sb.append(a.charAt(r.nextInt(a.length())));
		}
		return sb.toString();
	}

	/**
	 * Will be called if the given nickname is invalid.
	 *
	 * @param nickname The invalid nickname.
	 * @see PlayerProfile#isNicknameValid(String)
	 */
	public void onNicknameInvalid(String nickname) {
	}

	/**
	 * Will be called if the given nickname is already in use.
	 *
	 * @param nickname The nickname
	 */
	public void onNicknameAlreadyInUse(String nickname) {
	}
}