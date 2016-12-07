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

import POGOProtos.Enums.GenderOuterClass.Gender;
import com.pokegoapi.api.PokemonGo;
import com.pokegoapi.api.listener.TutorialListener;
import com.pokegoapi.api.player.Avatar;
import com.pokegoapi.api.player.PlayerAvatar;
import com.pokegoapi.api.pokemon.StarterPokemon;
import com.pokegoapi.auth.PtcCredentialProvider;
import com.pokegoapi.exceptions.CaptchaActiveException;
import com.pokegoapi.exceptions.LoginFailedException;
import com.pokegoapi.exceptions.RemoteServerException;
import com.pokegoapi.util.Log;
import okhttp3.OkHttpClient;

public class TutorialHandleExample {

	/**
	 * Goes through the tutorial with custom responses.
	 *
	 * @param args args
	 */
	public static void main(String[] args) {
		OkHttpClient http = new OkHttpClient();
		final PokemonGo api = new PokemonGo(http);
		try {
			PtcCredentialProvider provider
					= new PtcCredentialProvider(http, ExampleConstants.LOGIN, ExampleConstants.PASSWORD);
			// Add listener to listen for all tutorial related events, must be registered before login is called,
			// otherwise it will not be used
			api.addListener(new TutorialListener() {
				@Override
				public String claimName(PokemonGo api, String lastFailure) {
					//Last attempt to set a codename failed, set a random one by returning null
					if (lastFailure != null) {
						System.out.println("Codename \"" + lastFailure + "\" is already taken. Using random name.");
						return null;
					}
					System.out.println("Selecting codename");
					//Set the PTC name as the POGO username
					return ExampleConstants.LOGIN;
				}

				@Override
				public StarterPokemon selectStarter(PokemonGo api) {
					//Catch Charmander as your starter pokemon
					System.out.println("Selecting starter pokemon");
					return StarterPokemon.CHARMANDER;
				}

				@Override
				public PlayerAvatar selectAvatar(PokemonGo api) {
					System.out.println("Selecting player avatar");
					return new PlayerAvatar(
							Gender.FEMALE,
							Avatar.Skin.YELLOW.id(),
							Avatar.Hair.BLACK.id(),
							Avatar.FemaleShirt.BLUE.id(),
							Avatar.FemalePants.BLACK_PURPLE_STRIPE.id(),
							Avatar.FemaleHat.BLACK_YELLOW_POKEBALL.id(),
							Avatar.FemaleShoes.BLACK_YELLOW_STRIPE.id(),
							Avatar.Eye.BROWN.id(),
							Avatar.FemaleBackpack.GRAY_BLACK_YELLOW_POKEBALL.id());
				}
			});
			api.login(provider);
		} catch (LoginFailedException | RemoteServerException | CaptchaActiveException e) {
			Log.e("Main", "Failed to login!", e);
		}
	}
}