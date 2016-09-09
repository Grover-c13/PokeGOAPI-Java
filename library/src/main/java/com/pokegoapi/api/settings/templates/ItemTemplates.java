/*
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.`
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.pokegoapi.api.settings.templates;

import POGOProtos.Enums.BadgeTypeOuterClass.BadgeType;
import POGOProtos.Enums.PokemonIdOuterClass.PokemonId;
import POGOProtos.Enums.PokemonMoveOuterClass.PokemonMove;
import POGOProtos.Inventory.Item.ItemIdOuterClass.ItemId;
import POGOProtos.Networking.Requests.Messages.DownloadItemTemplatesMessageOuterClass.DownloadItemTemplatesMessage;
import POGOProtos.Networking.Requests.RequestTypeOuterClass.RequestType;
import POGOProtos.Networking.Responses.DownloadItemTemplatesResponseOuterClass.DownloadItemTemplatesResponse;
import POGOProtos.Networking.Responses.DownloadItemTemplatesResponseOuterClass.DownloadItemTemplatesResponse.ItemTemplate;
import POGOProtos.Networking.Responses.DownloadItemTemplatesResponseOuterClass.DownloadItemTemplatesResponse.Result;
import POGOProtos.Networking.Responses.DownloadRemoteConfigVersionResponseOuterClass.DownloadRemoteConfigVersionResponse;
import POGOProtos.Settings.Master.BadgeSettingsOuterClass.BadgeSettings;
import POGOProtos.Settings.Master.GymBattleSettingsOuterClass.GymBattleSettings;
import POGOProtos.Settings.Master.ItemSettingsOuterClass.ItemSettings;
import POGOProtos.Settings.Master.MoveSettingsOuterClass.MoveSettings;
import POGOProtos.Settings.Master.PlayerLevelSettingsOuterClass.PlayerLevelSettings;
import POGOProtos.Settings.Master.PokemonSettingsOuterClass.PokemonSettings;
import POGOProtos.Settings.Master.PokemonUpgradeSettingsOuterClass.PokemonUpgradeSettings;
import com.pokegoapi.api.PokemonGo;
import com.pokegoapi.api.pokemon.Evolutions;
import com.pokegoapi.exceptions.request.RequestFailedException;
import com.pokegoapi.main.ServerRequest;
import lombok.Getter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ItemTemplates {
	private final ItemTemplateProvider provider;

	@Getter
	public List<ItemTemplate> templates = new ArrayList<>();
	private Map<PokemonId, PokemonSettings> pokemonSettings = new HashMap<>();
	private Map<PokemonMove, MoveSettings> moveSettings = new HashMap<>();
	private Map<BadgeType, BadgeSettings> badgeSettings = new HashMap<>();
	private Map<ItemId, ItemSettings> itemSettings = new HashMap<>();
	private Map<Float, Double> levelCpMultiplier = new HashMap<>();

	@Getter
	public GymBattleSettings battleSettings;
	@Getter
	public PokemonUpgradeSettings upgradeSettings;

	@Getter
	public Evolutions evolutions;

	private boolean loaded;

	/**
	 * Creates ItemTemplates with the given provider
	 *
	 * @param provider the item template provider
	 */
	public ItemTemplates(ItemTemplateProvider provider) {
		this.provider = provider;
		reloadTemplates();
	}

	/**
	 * Checks if these templates require an update from the game servers
	 *
	 * @param response the remote config version data
	 * @return true if templates should be updated
	 */
	public boolean requiresUpdate(DownloadRemoteConfigVersionResponse response) {
		return provider.getUpdatedTimestamp() < response.getItemTemplatesTimestampMs();
	}

	/**
	 * Updates these {@link ItemTemplates} from the game server
	 *
	 * @param api the current api
	 * @throws RequestFailedException if the page update is not successfully sent
	 */
	public void update(PokemonGo api) throws RequestFailedException {
		updatePage(api, 0, 0, api.currentTimeMillis());
		reloadTemplates();
	}

	/**
	 * Updates {@link ItemTemplate} pages recursively
	 *
	 * @param api the current api
	 * @param page the current page index
	 * @param timestamp the timestamp of this page
	 * @param loadTime the time at which the templates started loading
	 * @throws RequestFailedException if the page update is not successfully sent
	 */
	private void updatePage(PokemonGo api, int page, long timestamp, long loadTime) throws RequestFailedException {
		DownloadItemTemplatesMessage message = DownloadItemTemplatesMessage.newBuilder()
				.setPaginate(true)
				.setPageOffset(page)
				.setPageTimestamp(timestamp)
				.build();
		ServerRequest request = new ServerRequest(RequestType.DOWNLOAD_ITEM_TEMPLATES, message);
		api.requestHandler.sendServerRequests(request, true);
		try {
			DownloadItemTemplatesResponse response = DownloadItemTemplatesResponse.parseFrom(request.getData());
			provider.updateTemplates(response, loadTime);
			if (response.getResult() == Result.PAGE) {
				updatePage(api, response.getPageOffset(), response.getTimestampMs(), loadTime);
			}
		} catch (IOException e) {
			throw new RequestFailedException(e);
		}
	}

	private void reloadTemplates() {
		templates.clear();
		pokemonSettings.clear();
		moveSettings.clear();
		badgeSettings.clear();
		itemSettings.clear();
		for (ItemTemplate template : provider.getTemplates().values()) {
			if (template.hasPokemonSettings()) {
				PokemonSettings pokemonSettings = template.getPokemonSettings();
				this.pokemonSettings.put(pokemonSettings.getPokemonId(), pokemonSettings);
			} else if (template.hasMoveSettings()) {
				MoveSettings moveSettings = template.getMoveSettings();
				this.moveSettings.put(moveSettings.getMovementId(), moveSettings);
			} else if (template.hasBadgeSettings()) {
				BadgeSettings badgeSettings = template.getBadgeSettings();
				this.badgeSettings.put(badgeSettings.getBadgeType(), badgeSettings);
			} else if (template.hasItemSettings()) {
				ItemSettings itemSettings = template.getItemSettings();
				this.itemSettings.put(itemSettings.getItemId(), itemSettings);
			} else if (template.hasBattleSettings()) {
				battleSettings = template.getBattleSettings();
			} else if (template.hasPokemonUpgrades()) {
				upgradeSettings = template.getPokemonUpgrades();
			} else if (template.hasPlayerLevel()) {
				PlayerLevelSettings settings = template.getPlayerLevel();
				List<Float> multipliers = settings.getCpMultiplierList();
				for (int i = 0; i < multipliers.size(); i++) {
					double multiplier = multipliers.get(i);
					levelCpMultiplier.put(i + 1.0F, multiplier);
					double nextMultiplier = multipliers.get(Math.min(multipliers.size() - 1, i + 1));
					double step = ((nextMultiplier * nextMultiplier) - (multiplier * multiplier)) / 2.0F;
					if (i >= 30) {
						step /= 2.0;
					}
					levelCpMultiplier.put(i + 1.5F, Math.sqrt((multiplier * multiplier) + step));
				}
			}
			templates.add(template);
		}
		evolutions = new Evolutions(this);
		loaded = true;
	}

	/**
	 * Gets pokemon settings for the given pokemon
	 *
	 * @param pokemon the pokemon to get settings for
	 * @return the settings
	 */
	public PokemonSettings getPokemonSettings(PokemonId pokemon) {
		return pokemonSettings.get(pokemon);
	}

	/**
	 * Gets move settings for the given move
	 *
	 * @param move the move to get settings for
	 * @return the settings
	 */
	public MoveSettings getMoveSettings(PokemonMove move) {
		return moveSettings.get(move);
	}

	/**
	 * Gets badge settings for the given badge type
	 *
	 * @param badge the badge to get settings for
	 * @return the settings
	 */
	public BadgeSettings getBadgeSettings(BadgeType badge) {
		return badgeSettings.get(badge);
	}

	/**
	 * Gets item settings for the given item type
	 *
	 * @param item the item to get settings for
	 * @return the settings
	 */
	public ItemSettings getItemSettings(ItemId item) {
		return itemSettings.get(item);
	}

	/**
	 * Gets the cp multiplier for the given level
	 *
	 * @param level the level to get from
	 * @return the cp multiplier for this level
	 */
	public double getLevelCpMultiplier(float level) {
		return levelCpMultiplier.get(level);
	}

	/**
	 * @return true if this has loaded yet
	 */
	public boolean hasLoaded() {
		return loaded;
	}
}