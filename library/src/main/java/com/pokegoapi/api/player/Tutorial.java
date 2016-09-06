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

package com.pokegoapi.api.player;

import POGOProtos.Data.Player.PlayerAvatarOuterClass;
import POGOProtos.Enums.GenderOuterClass;
import POGOProtos.Enums.PokemonIdOuterClass;

import java.security.SecureRandom;
import java.util.Random;

public class Tutorial {
	private final boolean acceptTOS;
	private final PlayerAvatarOuterClass.PlayerAvatar playerAvatar;
	private final PokemonIdOuterClass.PokemonId starterPokemon;
	private final NicknameDialog nicknameDialog;

	/**
	 * Creates a {@link Tutorial}. Null values will be replaced by default values.
	 *
	 * @param acceptTOS      accept TOS?
	 * @param playerAvatar   the player avatar
	 * @param starterPokemon the starter pokemon
	 * @param nicknameDialog the nickname dialog
	 */
	public Tutorial(Boolean acceptTOS, PlayerAvatarOuterClass.PlayerAvatar playerAvatar,
					PokemonIdOuterClass.PokemonId starterPokemon, NicknameDialog nicknameDialog) {
		this.acceptTOS = acceptTOS != null ? acceptTOS : true;
		this.playerAvatar = playerAvatar != null ? playerAvatar : getRandomPlayerAvatar().build();
		this.starterPokemon = starterPokemon != null ? starterPokemon : getRandomStarterPokemon();
		this.nicknameDialog = nicknameDialog != null ? nicknameDialog : getRandomNickname();
	}

	/**
	 * Gets a random nickname dialog
	 *
	 * @return a random nickname dialog
	 */
	public static NicknameDialog getRandomNickname() {
		NicknameDialog randomNicknameDialog = new NicknameDialog() {
			@Override
			public String getNickname() {
				final String a = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
				final SecureRandom r = new SecureRandom();
				final int l = new Random().nextInt(15 - 10) + 10;
				StringBuilder sb = new StringBuilder(l);
				for (int i = 0; i < l; i++) {
					sb.append(a.charAt(r.nextInt(a.length())));
				}
				return sb.toString();
			}

			@Override
			public String getFallback(String alreadyUsedNickname, NicknameException lastException) {
				return getNickname();
			}
		};

		return randomNicknameDialog;
	}

	/**
	 * Gets a random starter pokemon
	 *
	 * @return a random starter pokemon
	 */
	public static PokemonIdOuterClass.PokemonId getRandomStarterPokemon() {
		Random random = new Random();
		int pokemonId = random.nextInt(4);

		return pokemonId == 1 ? PokemonIdOuterClass.PokemonId.BULBASAUR :
				pokemonId == 2 ? PokemonIdOuterClass.PokemonId.CHARMANDER :
						pokemonId == 3 ? PokemonIdOuterClass.PokemonId.SQUIRTLE
								: PokemonIdOuterClass.PokemonId.PIKACHU;
	}

	/**
	 * Gets a random player avatar
	 *
	 * @return a random player avatar
	 */
	public static POGOProtos.Data.Player.PlayerAvatarOuterClass.PlayerAvatar.Builder getRandomPlayerAvatar() {
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
	 * Gets whether or not the user accepts the TOS of Niantic.
	 *
	 * @return True if accepting, else False
	 */
	public boolean isAcceptTOS() {
		return acceptTOS;
	}

	/**
	 * Gets the player avatar
	 *
	 * @return the player avatar
	 */
	public PlayerAvatarOuterClass.PlayerAvatar getPlayerAvatar() {
		return playerAvatar;
	}

	/**
	 * Gets the starter pokemon
	 *
	 * @return the starter pokemon
	 */
	public PokemonIdOuterClass.PokemonId getStarterPokemon() {
		return starterPokemon;
	}

	/**
	 * Gets the nickname dialog
	 *
	 * @return the nickname dialog
	 */
	public NicknameDialog getNicknameDialog() {
		return nicknameDialog;
	}

	/**
	 * Interface for nickname dialog
	 */
	public interface NicknameDialog {
		/**
		 * Gets the chosen nickname
		 *
		 * @return a nickname
		 * @throws CanceledException if the nickname dialog gets canceled
		 */
		String getNickname() throws CanceledException;

		/**
		 * Gets a different nickname in case that the previous nickname was invalid or already in use
		 *
		 * @param nickname the nickname, that was invalid or already in use
		 * @param cause    the causing exception. Can be either instanceof Tutorial.NicknameInvalidException
		 *                 or instanceof Tutorial.NicknameNotAvailableException
		 * @return a nickname
		 * @throws CanceledException if the nickname dialog gets canceled
		 */
		String getFallback(String nickname, NicknameException cause) throws CanceledException;
	}

	/**
	 * Builder for {@link Tutorial}.
	 * Call {@link #build()} to build.
	 */
	public static class TutorialBuilder {
		private Boolean acceptTOS;
		private PlayerAvatarOuterClass.PlayerAvatar playerAvatar;
		private PokemonIdOuterClass.PokemonId starterPokemon;
		private NicknameDialog nicknameDialog;

		/**
		 * Creates an instance of {@link TutorialBuilder}.
		 */
		public TutorialBuilder() {
		}

		/**
		 * Sets whether or not the user accepts the TOS of Niantic.
		 * Default: True.
		 *
		 * @param acceptTOS True if accepting, else False.
		 * @return the tutorial builder
		 */
		public TutorialBuilder setAcceptTOS(boolean acceptTOS) {
			this.acceptTOS = acceptTOS;
			return this;
		}

		/**
		 * Sets the player avatar.
		 * Default: a random avatar.
		 *
		 * @param playerAvatar the player avatar
		 * @return the tutorial builder
		 */
		public TutorialBuilder setPlayerAvatar(PlayerAvatarOuterClass.PlayerAvatar playerAvatar) {
			this.playerAvatar = playerAvatar;
			return this;
		}

		/**
		 * Sets the starter pokemon.
		 * Default: a random starter pokemon.
		 *
		 * @param starterPokemon the starter pokemon, must be either Pikachu, Squirtle, Charmander or Bulbasaur.
		 * @return the tutorial builder
		 */
		public TutorialBuilder setStarterPokemon(PokemonIdOuterClass.PokemonId starterPokemon) {
			this.starterPokemon = starterPokemon;
			return this;
		}

		/**
		 * Sets the player's nicknameDialog.
		 * Default: a random nicknameDialog (e.g. kKU8G46FfawQ36).
		 *
		 * @param nicknameDialog the nicknameDialog
		 * @return the tutorial builder
		 */
		public TutorialBuilder setNicknameDialog(NicknameDialog nicknameDialog) {
			this.nicknameDialog = nicknameDialog;
			return this;
		}

		/**
		 * Builds the {@link Tutorial} based on the values set in this {@link TutorialBuilder}.
		 * Fallback to default values if not set.
		 *
		 * @return the tutorial
		 */
		public Tutorial build() {
			return new Tutorial(acceptTOS, playerAvatar, starterPokemon, nicknameDialog);
		}
	}

	/**
	 * Provides a tutorial
	 */
	public abstract static class TutorialProvider {
		private boolean isAcceptTOSMissing;
		private boolean isAvatarMissing;
		private boolean isStarterPokemonMissing;
		private boolean isNicknameMissing;

		/**
		 * Creates a tutorial provider
		 */
		public TutorialProvider() {
		}

		/**
		 * Gets whether or not the TOS have been accepted before
		 *
		 * @return True if TOS have not been accepted before, else False
		 */
		public boolean isAcceptTOSMissing() {
			return isAcceptTOSMissing;
		}

		/**
		 * Gets whether or not the player avatar has been set before
		 *
		 * @return True if player avatar is missing, else False
		 */
		public boolean isAvatarMissing() {
			return isAvatarMissing;
		}

		/**
		 * Gets whether or not the starter pokemon has been set before
		 *
		 * @return True if starter pokemon is missing, else False
		 */
		public boolean isStarterPokemonMissing() {
			return isStarterPokemonMissing;
		}

		/**
		 * Gets whether or not the nickname has been set before
		 *
		 * @return True if nickname is missing, else False
		 */
		public boolean isNicknameMissing() {
			return isNicknameMissing;
		}

		/**
		 * Gets a tutorial that handles the missing options
		 *
		 * @return a tutorial
		 * @throws CanceledException if the tutorial gets canceled
		 */
		public abstract Tutorial getTutorial() throws CanceledException;

		/**
		 * Sets missing options
		 *
		 * @param isAcceptTOSMissing      True if TOS have not been accepted before, else False
		 * @param isAvatarMissing         True if player avatar is missing, else False
		 * @param isStarterPokemonMissing True if starter pokemon is missing, else False
		 * @param isNicknameMissing       True if nickname is missing, else False
		 * @return the tutorial provider
		 */
		public TutorialProvider setMissing(boolean isAcceptTOSMissing, boolean isAvatarMissing,
										boolean isStarterPokemonMissing, boolean isNicknameMissing) {
			this.isAcceptTOSMissing = isAcceptTOSMissing;
			this.isAvatarMissing = isAvatarMissing;
			this.isStarterPokemonMissing = isStarterPokemonMissing;
			this.isNicknameMissing = isNicknameMissing;
			return this;
		}
	}

	public static class CanceledException extends Exception {
		public CanceledException() {
			super();
		}

		public CanceledException(String message) {
			super(message);
		}

		public CanceledException(String message, Throwable cause) {
			super(message, cause);
		}

		public CanceledException(Throwable cause) {
			super(cause);
		}
	}

	public static class NicknameException extends Exception {
		public NicknameException() {
			super();
		}

		public NicknameException(String message) {
			super(message);
		}

		public NicknameException(String message, Throwable cause) {
			super(message, cause);
		}

		public NicknameException(Throwable cause) {
			super(cause);
		}
	}

	public static class NicknameInvalidException extends NicknameException {

	}

	public static class NicknameNotAvailableException extends NicknameException {

	}
}

