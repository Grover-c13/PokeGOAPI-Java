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

package com.pokegoapi.api.inventory;

import POGOProtos.Enums.PokemonFamilyIdOuterClass;
import POGOProtos.Enums.PokemonIdOuterClass.PokemonId;
import POGOProtos.Inventory.EggIncubatorOuterClass;
import POGOProtos.Inventory.InventoryItemDataOuterClass;
import POGOProtos.Inventory.InventoryItemOuterClass;
import POGOProtos.Inventory.Item.ItemDataOuterClass.ItemData;
import POGOProtos.Inventory.Item.ItemIdOuterClass.ItemId;
import POGOProtos.Networking.Requests.Messages.GetInventoryMessageOuterClass.GetInventoryMessage;
import POGOProtos.Networking.Requests.RequestTypeOuterClass;
import POGOProtos.Networking.Responses.GetInventoryResponseOuterClass;
import POGOProtos.Networking.Responses.GetInventoryResponseOuterClass.GetInventoryResponse;
import com.google.protobuf.InvalidProtocolBufferException;
import com.pokegoapi.api.PokemonGo;
import com.pokegoapi.api.async.AsyncDataObject;
import com.pokegoapi.api.pokemon.EggPokemon;
import com.pokegoapi.api.pokemon.Pokemon;
import com.pokegoapi.exceptions.LoginFailedException;
import com.pokegoapi.exceptions.RemoteServerException;
import com.pokegoapi.main.ServerRequest;
import com.pokegoapi.util.Log;
import lombok.Getter;
import rx.Observable;

import java.util.ArrayList;
import java.util.List;


public class Inventories extends AsyncDataObject<Inventories> {

	private static final String TAG = Inventories.class.getSimpleName();

	@Getter
	private ItemBag itemBag;
	@Getter
	private PokeBank pokebank;
	@Getter
	private CandyJar candyjar;
	@Getter
	private Pokedex pokedex;
	@Getter
	private List<EggIncubator> incubators;
	@Getter
	private Hatchery hatchery;

	private long lastInventoryUpdate = 0;


	/**
	 * Creates Inventories and initializes content.
	 *
	 * @param api PokemonGo api
	 * @throws LoginFailedException  the login failed exception
	 * @throws RemoteServerException the remote server exception
	 */
	public Inventories(PokemonGo api) {
		super(api);
		itemBag = new ItemBag(api);
		pokebank = new PokeBank(api);
		candyjar = new CandyJar(api);
		pokedex = new Pokedex(api);
		incubators = new ArrayList<>();
		hatchery = new Hatchery(api);
	}


	/**
	 * Update Inventories from server data.
	 *
	 * @return The updated Inventories data.
	 * @see #refreshData(boolean)
	 * @see #refreshDataSync(boolean)
	 * @deprecated Use one of the refreshData methods instead.
	 */
	public Inventories updateInventories() {
		try {
			return refreshDataSync(false);
		} catch (Exception ex) {
			Log.e(TAG, "Error refreshing Inventories synchronously: " + ex.getMessage());
		}

		return this;
	}

	/**
	 * Update Inventories from server data.
	 *
	 * @return The updated Inventories data.
	 * @see #refreshData(boolean)
	 * @see #refreshDataSync(boolean)
	 * @deprecated Use one of the refreshData methods instead.
	 */
	public Inventories updateInventories(boolean forceRefresh) {
		try {
			return refreshDataSync(forceRefresh);
		} catch (Exception ex) {
			Log.e(TAG, "Error refreshing Inventories synchronously: " + ex.getMessage());
		}

		return this;
	}

	private ServerRequest buildServerRequest(final boolean forceUpdate)
	{
		if (forceUpdate) {
			lastInventoryUpdate = 0;
			itemBag.reset(getApi());
			pokebank.reset(getApi());
			candyjar.reset(getApi());
			pokedex.reset(getApi());
			incubators = new ArrayList<>();
			hatchery.reset(getApi());
		}

		GetInventoryMessage invReqMsg =
				GetInventoryMessage.newBuilder().setLastTimestampMs(lastInventoryUpdate).build();
		return new ServerRequest(RequestTypeOuterClass.RequestType.GET_INVENTORY, invReqMsg);
	}

	public Observable<Inventories> refreshData(){ return refreshData(false); }

	/**
	 * Updates the inventories with latest data asynchronously.
	 *
	 * @param forceUpdate force update if true.
	 * @return An {@link Observable} Inventories.
	 */
	public Observable<Inventories> refreshData(final boolean forceUpdate) {
		final ServerRequest serverRequest = buildServerRequest(forceUpdate);
		return sendAsyncServerRequests(serverRequest).cast(Inventories.class);
	}

	@Override
	public Inventories refreshDataSync() throws LoginFailedException, RemoteServerException {
		return refreshDataSync(false);
	}

	/**
	 * Updates the inventories with latest data synchronously.
	 *
	 * @param forceUpdate force update if true.
	 * @return An {@link Observable} Inventories.
	 * @throws LoginFailedException  the login failed exception
	 * @throws RemoteServerException the remote server exception
	 */
	public Inventories refreshDataSync(boolean forceUpdate) throws LoginFailedException, RemoteServerException {
		ServerRequest[] serverRequests = getApi().getRequestHandler().sendServerRequests(buildServerRequest(forceUpdate));
		return updateInstanceData(serverRequests);
	}

	/**
	 * Update this instance data with the ServerRequest and Response data.
	 *
	 * @param requests  The server request data.
	 * @throws RemoteServerException If server errors occured.
	 */
	@Override
	protected synchronized Inventories updateInstanceData(final ServerRequest... requests)
			throws LoginFailedException, RemoteServerException {

		final ServerRequest request = requests[0];
		GetInventoryResponse inventoryResponse = null;
		try {
			inventoryResponse = GetInventoryResponseOuterClass.GetInventoryResponse.parseFrom(request.getData());
		} catch (InvalidProtocolBufferException ex) {
			return refreshDataSync(true);
		}

		for (InventoryItemOuterClass.InventoryItem inventoryItem : inventoryResponse.getInventoryDelta()
				.getInventoryItemsList()) {

			InventoryItemDataOuterClass.InventoryItemData itemData = inventoryItem.getInventoryItemData();

			// hatchery
			if (itemData.getPokemonData().getPokemonId() == PokemonId.MISSINGNO
					&& itemData.getPokemonData().getIsEgg()) {
				hatchery.addEgg(new EggPokemon(itemData.getPokemonData()));
			}

			// pokebank
			if (itemData.getPokemonData().getPokemonId() != PokemonId.MISSINGNO) {
				pokebank.addPokemon(new Pokemon(getApi(), inventoryItem.getInventoryItemData().getPokemonData()));
			}

			// items
			if (itemData.getItem().getItemId() != ItemId.UNRECOGNIZED
					&& itemData.getItem().getItemId() != ItemId.ITEM_UNKNOWN) {
				ItemData item = itemData.getItem();
				itemBag.addItem(new Item(item));
			}

			// candyjar
			if (itemData.getCandy().getFamilyId() != PokemonFamilyIdOuterClass.PokemonFamilyId.UNRECOGNIZED
					&& itemData.getCandy().getFamilyId() != PokemonFamilyIdOuterClass.PokemonFamilyId.FAMILY_UNSET) {
				candyjar.setCandy(itemData.getCandy().getFamilyId(), itemData.getCandy().getCandy());
			}
			// player stats
			if (itemData.hasPlayerStats()) {
				getApi().getPlayerProfile().setStats(itemData.getPlayerStats());
			}

			// pokedex
			if (itemData.hasPokedexEntry()) {
				pokedex.add(itemData.getPokedexEntry());
			}

			if (itemData.hasEggIncubators()) {
				for (EggIncubatorOuterClass.EggIncubator incubator : itemData.getEggIncubators()
						.getEggIncubatorList()) {
					incubators.add(new EggIncubator(getApi(), incubator));
				}
			}

			lastInventoryUpdate = getApi().currentTimeMillis();
		}

		return this;
	}

	public synchronized Inventories getInstance(){ return this; }
}
