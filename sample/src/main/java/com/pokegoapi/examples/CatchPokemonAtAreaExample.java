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


import POGOProtos.Networking.Envelopes.RequestEnvelopeOuterClass;
import com.pokegoapi.api.PokemonGo;
import com.pokegoapi.api.map.MapObjects;
import com.pokegoapi.api.map.pokemon.CatchResult;
import com.pokegoapi.api.map.pokemon.CatchablePokemon;
import com.pokegoapi.api.map.pokemon.encounter.EncounterResult;
import com.pokegoapi.api.settings.AsyncCatchOptions;
import com.pokegoapi.auth.PtcCredentialProvider;
import com.pokegoapi.exceptions.LoginFailedException;
import com.pokegoapi.exceptions.RemoteServerException;
import com.pokegoapi.util.PokeCallback;
import okhttp3.OkHttpClient;

import java.util.Collection;

public class CatchPokemonAtAreaExample {

	/**
	 * Catches a pokemon at an area.
	 *
	 * @param args args
	 */
	public static void main(String[] args) {
		OkHttpClient http = new OkHttpClient();
		RequestEnvelopeOuterClass.RequestEnvelope.AuthInfo auth = null;
		final PokemonGo go = new PokemonGo(http);

		try {
			go.login(new PtcCredentialProvider(http, ExampleLoginDetails.LOGIN,
					ExampleLoginDetails.PASSWORD), new PokeCallback<Void>() {
				@Override
				public void onResponse(Void result) {
					catchPokemon(go);
				}
			});
		} catch (LoginFailedException e) {
			e.printStackTrace();
		} catch (RemoteServerException e) {
			e.printStackTrace();
		}


	}

	private static void catchPokemon(final PokemonGo go) {
		go.setLocation(-32.058087, 115.744325, 0);

		go.getMap().getMapObjects(new PokeCallback<MapObjects>() {
			@Override
			public void onResponse(MapObjects result) {
				Collection<CatchablePokemon> catchablePokemon = result.getAllCatchablePokemons();
				System.out.println("Pokemon in area:" + catchablePokemon.size());

				for (final CatchablePokemon cp : catchablePokemon) {
					// You need to Encounter first.
					cp.encounterPokemon(new PokeCallback<EncounterResult>() {
						@Override
						public void onResponse(EncounterResult encResult) {
							// if encounter was succesful, catch
							if (encResult.wasSuccessful()) {
								System.out.println("Encounted:" + cp.getPokemonId());
								AsyncCatchOptions options = new AsyncCatchOptions(go);
								options.useRazzberries(true);

								cp.catchPokemon(options, new PokeCallback<CatchResult>() {
									@Override
									public void onResponse(CatchResult result) {
										System.out.println("CatchResult: " + result.getStatus());
									}
								});

							}
						}
					});
				}
			}
		});

	}
}
