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
import POGOProtos.Networking.Responses.ClaimCodenameResponseOuterClass.ClaimCodenameResponse.Status;

import java.security.SecureRandom;
import java.util.Random;

public class Tutorial {
	private final AcceptTosInterface acceptTOS;
	private final PlayerAvatarInterface playerAvatar;
	private final StarterPokemonInterface starterPokemon;
	private final NicknameInterface nickname;
	private final NicknameFallbackInterface nicknameFallback;

	/**
	 * Creates a {@link Tutorial}. Null values will be replaced by default values.
	 *
	 * @param acceptTOS        the accept TOS implementation
	 * @param playerAvatar     the player avatar implementation
	 * @param starterPokemon   the starter pokemon implementation
	 * @param nickname         the nickname implementation
	 * @param nicknameFallback the fallback nickname implementation
	 */
	private Tutorial(AcceptTosInterface acceptTOS, PlayerAvatarInterface playerAvatar,
					 StarterPokemonInterface starterPokemon, final NicknameInterface nickname, NicknameFallbackInterface nicknameFallback) {
		this.acceptTOS = acceptTOS != null
				? acceptTOS
				:
				new AcceptTosInterface() {
					@Override
					public boolean acceptTOS() throws CanceledException {
						return true;
					}
				};

		this.playerAvatar = playerAvatar != null
				? playerAvatar
				:
				new PlayerAvatarInterface() {
					@Override
					public PlayerAvatarOuterClass.PlayerAvatar setPlayerAvatar() throws CanceledException {
						return getRandomPlayerAvatar().build();
					}
				};

		this.starterPokemon = starterPokemon != null
				? starterPokemon
				:
				new StarterPokemonInterface() {
					@Override
					public PokemonIdOuterClass.PokemonId setStarterPokemon() throws CanceledException {
						return getRandomStarterPokemon();
					}
				};

		this.nickname = nickname != null
				? nickname
				:
				new NicknameInterface() {
					@Override
					public String setNickname() throws CanceledException {
						return getRandomNickname();
					}
				};

		this.nicknameFallback = nicknameFallback != null
				? nicknameFallback
				:
				new NicknameFallbackInterface() {
					@Override
					public String setNicknameFallback(String nicknameStr, NicknameException cause) throws CanceledException {
						return Tutorial.this.nickname.setNickname();
					}
				};
	}

	/**
	 * Gets a random nickname dialog
	 *
	 * @return a random nickname dialog
	 */
	public static String getRandomNickname() {
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

		playerAvatarBuilder.setSkin(random.nextInt(com.pokegoapi.api.player.PlayerAvatar.getAvailableSkins()))
				.setHair(random.nextInt(com.pokegoapi.api.player.PlayerAvatar.getAvailableHair()))
				.setEyes(random.nextInt(com.pokegoapi.api.player.PlayerAvatar.getAvailableEyes()))
				.setHat(random.nextInt(com.pokegoapi.api.player.PlayerAvatar.getAvailableHats()))
				.setShirt(random.nextInt(com.pokegoapi.api.player.PlayerAvatar.getAvailableShirts(
						female ? GenderOuterClass.Gender.FEMALE : GenderOuterClass.Gender.MALE)))
				.setPants(random.nextInt(com.pokegoapi.api.player.PlayerAvatar.getAvailablePants(
						female ? GenderOuterClass.Gender.FEMALE : GenderOuterClass.Gender.MALE)))
				.setShoes(random.nextInt(com.pokegoapi.api.player.PlayerAvatar.getAvailableShoes()))
				.setBackpack(random.nextInt(com.pokegoapi.api.player.PlayerAvatar.getAvailableShoes()));

		return playerAvatarBuilder;
	}

	/**
	 * Gets whether or not the user accepts the TOS of Niantic.
	 *
	 * @return True if accepting, else False
	 */
	public boolean isAcceptTOS() throws CanceledException {
		return acceptTOS.acceptTOS();
	}

	/**
	 * Gets the player avatar
	 *
	 * @return the player avatar
	 */
	public PlayerAvatarOuterClass.PlayerAvatar getPlayerAvatar() throws CanceledException {
		return playerAvatar.setPlayerAvatar();
	}

	/**
	 * Gets the starter pokemon
	 *
	 * @return the starter pokemon
	 */
	public PokemonIdOuterClass.PokemonId getStarterPokemon() throws CanceledException {
		return starterPokemon.setStarterPokemon();
	}

	/**
	 * Gets the nickname
	 *
	 * @return the nickname
	 */
	public String getNickname() throws CanceledException {
		return nickname.setNickname();
	}

	/**
	 * Gets the fallback nickname
	 *
	 * @return the fallback nickname
	 */
	public String getNicknameFallback(String nickname, NicknameException cause) throws CanceledException {
		return nicknameFallback.setNicknameFallback(nickname, cause);
	}

	/**
	 * Gets a new tutorial builder
	 *
	 * @return the tutorial builder
	 */
	static public TutorialBuilder newBuilder() {
		return new TutorialBuilder();
	}

	public interface AcceptTosInterface {
		boolean acceptTOS() throws CanceledException;
	}

	public interface PlayerAvatarInterface {
		PlayerAvatarOuterClass.PlayerAvatar setPlayerAvatar() throws CanceledException;
	}

	public interface NicknameInterface {
		String setNickname() throws CanceledException;
	}

	public interface NicknameFallbackInterface {
		String setNicknameFallback(String nickname, NicknameException cause) throws CanceledException;
	}

	public interface StarterPokemonInterface {
		PokemonIdOuterClass.PokemonId setStarterPokemon() throws CanceledException;
	}

	/**
	 * Builder for {@link Tutorial}.
	 * Call {@link #build()} to build.
	 */
	public static class TutorialBuilder {
		private AcceptTosInterface acceptTOS;
		private PlayerAvatarInterface playerAvatar;
		private StarterPokemonInterface starterPokemon;
		private NicknameInterface nickname;
		private NicknameFallbackInterface nicknameFallback;

		/**
		 * Creates an instance of {@link TutorialBuilder}.
		 */
		TutorialBuilder() {
		}

		/**
		 * Sets an implementation of {@link AcceptTosInterface}
		 *
		 * @param acceptTOS the implementation
		 * @return the tutorial builder
		 */
		public TutorialBuilder setAcceptTOS(AcceptTosInterface acceptTOS) {
			this.acceptTOS = acceptTOS;
			return this;
		}

		/**
		 * Sets an implementation of {@link PlayerAvatarInterface}
		 *
		 * @param playerAvatar the implementation
		 * @return the tutorial builder
		 */
		public TutorialBuilder setPlayerAvatar(PlayerAvatarInterface playerAvatar) {
			this.playerAvatar = playerAvatar;
			return this;
		}

		/**
		 * Sets an implementation of {@link StarterPokemonInterface}
		 *
		 * @param starterPokemon the implementation
		 * @return the tutorial builder
		 */
		public TutorialBuilder setStarterPokemon(StarterPokemonInterface starterPokemon) {
			this.starterPokemon = starterPokemon;
			return this;
		}

		/**
		 * Sets an implementation of {@link NicknameInterface}
		 *
		 * @param nickname the implementation
		 * @return the tutorial builder
		 */
		public TutorialBuilder setNickname(NicknameInterface nickname) {
			this.nickname = nickname;
			return this;
		}

		/**
		 * Sets an implementation of {@link NicknameFallbackInterface}
		 *
		 * @param nicknameFallback the implementation
		 * @return the tutorial builder
		 */
		public TutorialBuilder setNicknameFallback(NicknameFallbackInterface nicknameFallback) {
			this.nicknameFallback = nicknameFallback;
			return this;
		}

		/**
		 * Builds the {@link Tutorial} based on the values set in this {@link TutorialBuilder}.
		 * Fallback to default values if not set.
		 *
		 * @return the tutorial
		 */
		public Tutorial build() {
			return new Tutorial(acceptTOS, playerAvatar, starterPokemon, nickname, nicknameFallback);
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
		private final Status status;

		public NicknameException(Status status) {
			this.status = status;
		}

		public Status getStatus() {
			return status;
		}

		public boolean isNicknameInvalid() {
			return status == Status.CODENAME_NOT_VALID;
		}

		public boolean isNicknameNotAvailable() {
			return status == Status.CODENAME_NOT_AVAILABLE;
		}
	}
}

