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


import POGOProtos.Enums.PokemonIdOuterClass;
import POGOProtos.Inventory.Item.ItemIdOuterClass;
import POGOProtos.Networking.Responses.CatchPokemonResponseOuterClass;
import com.pokegoapi.api.PokemonGo;
import com.pokegoapi.api.inventory.ItemBag;
import com.pokegoapi.api.inventory.Pokeball;
import com.pokegoapi.api.map.pokemon.CatchResult;
import com.pokegoapi.api.map.pokemon.CatchablePokemon;
import com.pokegoapi.api.map.pokemon.encounter.EncounterResult;
import com.pokegoapi.api.settings.CatchOptions;
import com.pokegoapi.auth.PtcCredentialProvider;
import com.pokegoapi.exceptions.LoginFailedException;
import com.pokegoapi.exceptions.RemoteServerException;
import com.pokegoapi.main.AsyncReturn;
import com.pokegoapi.main.PokemonCallback;
import com.pokegoapi.main.SyncedReturn;
import com.pokegoapi.util.Log;
import okhttp3.OkHttpClient;

import java.util.List;

public class CatchPokemonAtAreaExample {

	/**
	 * Catches a pokemon at an area.
	 *
	 * @param args args
	 */
	public static void main(String[] args) {
		OkHttpClient http = new OkHttpClient();
		final PokemonGo api = new PokemonGo(http);
		try {
			PtcCredentialProvider provider
					= new PtcCredentialProvider(http, ExampleLoginDetails.LOGIN, ExampleLoginDetails.PASSWORD);
			api.login(provider, new PokemonCallback() {
				@Override
				public void onCompleted(Exception e) {
					if (e != null) {
						Log.e("Main", "Failed to login or server issue: ", e);
					}
					onLogin(api);
				}
			});

		} catch (LoginFailedException | RemoteServerException e) {
			Log.e("Main", "Failed to login to PTC", e);
		}
	}

	public static void onLogin(final PokemonGo api) {
		api.setLocation(-32.058087, 115.744325, 0);

		api.getMap().getCatchablePokemon(new AsyncReturn<List<CatchablePokemon>>() {
			@Override
			public void onReceive(final List<CatchablePokemon> catchablePokemon, Exception e) {
				//Queue task to run on task thread so it doesn't block the request thread
				api.queueTask(new Runnable() {
					@Override
					public void run() {
						System.out.println("Pokemon in area: " + catchablePokemon.size());

						for (final CatchablePokemon catchable : catchablePokemon) {
							SyncedReturn<EncounterResult> syncedEncounter = new SyncedReturn<>();
							catchable.encounterPokemon(syncedEncounter);
							try {
								//Block until encounter
								EncounterResult result = syncedEncounter.get();
								//Can only catch if the encounter was successful
								if (result.wasSuccessful()) {
									catchPokemon(catchable, api);
								}
							} catch (Exception encounterException) {
								Log.e("Main", "Exception while encountering Pokemon!", encounterException);
							}
						}
						System.exit(0);
					}
				});
			}
		});
	}

	private static void catchPokemon(CatchablePokemon catchable, PokemonGo api) {
		System.out.println("Encountered " + catchable.getPokemonId() + " at "
				+ catchable.getLatitude() + ", " + catchable.getLongitude());
		ItemBag bag = api.getInventories().getItemBag();
		boolean hasRazzberry = bag.getItem(ItemIdOuterClass.ItemId.ITEM_RAZZ_BERRY).getCount() > 0;
		CatchOptions options = new CatchOptions(api)
				.withPokeballSelector(new CatchOptions.PokeballSelector() {
					@Override
					public Pokeball select(List<Pokeball> pokeballs, double probability) {
						//Use the lowest tier useable pokeball
						return pokeballs.get(0);
					}
				})
				.useRazzberry(hasRazzberry);
		//Attempts to capture this pokemon. -1 to give no limit for pokeball and razzberry throws
		SyncedReturn<CatchResult> syncedCapture = new SyncedReturn<>();
		catchable.capture(options, syncedCapture, -1, -1);
		try {
			CatchResult catchResult = syncedCapture.get();
			CatchPokemonResponseOuterClass.CatchPokemonResponse.CatchStatus status = catchResult.getStatus();
			if (catchResult.isFailed()) {
				Log.i("Main", "Pokemon capture failed with status: " + status);
			} else {
				PokemonIdOuterClass.PokemonId pokemon = catchable.getPokemonId();
				Log.i("Main", "Catch of " + pokemon + " was successful!");
			}
			Thread.sleep(5000);
		} catch (Exception e) {
			Log.e("Main", "Exception while capturing Pokemon!", e);
		}
	}
}
