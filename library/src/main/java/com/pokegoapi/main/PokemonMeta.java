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

package com.pokegoapi.main;

import POGOProtos.Enums.BadgeTypeOuterClass.BadgeType;
import POGOProtos.Enums.PokemonIdOuterClass.PokemonId;
import POGOProtos.Enums.PokemonMoveOuterClass.PokemonMove;
import POGOProtos.Inventory.Item.ItemIdOuterClass.ItemId;
import POGOProtos.Networking.Responses.DownloadItemTemplatesResponseOuterClass.DownloadItemTemplatesResponse;
import POGOProtos.Networking.Responses.DownloadItemTemplatesResponseOuterClass.DownloadItemTemplatesResponse.ItemTemplate;
import POGOProtos.Networking.Responses.DownloadRemoteConfigVersionResponseOuterClass.DownloadRemoteConfigVersionResponse;
import POGOProtos.Settings.Master.BadgeSettingsOuterClass.BadgeSettings;
import POGOProtos.Settings.Master.GymBattleSettingsOuterClass.GymBattleSettings;
import POGOProtos.Settings.Master.ItemSettingsOuterClass.ItemSettings;
import POGOProtos.Settings.Master.MoveSettingsOuterClass.MoveSettings;
import POGOProtos.Settings.Master.PokemonSettingsOuterClass.PokemonSettings;
import POGOProtos.Settings.Master.PokemonUpgradeSettingsOuterClass.PokemonUpgradeSettings;
import com.google.protobuf.ByteString;
import com.pokegoapi.api.pokemon.Evolutions;
import com.pokegoapi.api.pokemon.PokemonCpUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PokemonMeta {
	public static final List<ItemTemplate> templates = new ArrayList<>();
	public static final Map<PokemonId, PokemonSettings> pokemonSettings = new HashMap<>();
	public static final Map<PokemonMove, MoveSettings> moveSettings = new HashMap<>();
	public static final Map<BadgeType, BadgeSettings> badgeSettings = new HashMap<>();
	public static final Map<ItemId, ItemSettings> itemSettings = new HashMap<>();

	public static GymBattleSettings battleSettings;
	public static PokemonUpgradeSettings upgradeSettings;

	private static long timestamp;

	static {
		try {
			File templatesFile = Utils.getTempFile("templates");
			File timestampFile = Utils.getTempFile("timestamp");
			if (timestampFile.exists() && templatesFile.exists()) {
				BufferedReader reader = new BufferedReader(new FileReader(timestampFile));
				String line;
				while ((line = reader.readLine()) != null) {
					try {
						timestamp = Long.parseLong(line);
						break;
					} catch (NumberFormatException e) {
						continue;
					}
				}
				reader.close();
				ByteString data = ByteString.readFrom(new FileInputStream(templatesFile));
				update(data, false);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Checks the version of item templates from the given response
	 *
	 * @param response the response to check from
	 * @return if an update is required
	 * @throws IOException if the timestamp fails to be written
	 */
	public static boolean checkVersion(DownloadRemoteConfigVersionResponse response) throws IOException {
		boolean changed = response.getItemTemplatesTimestampMs() > timestamp;
		if (changed) {
			timestamp = response.getItemTemplatesTimestampMs();
			PrintWriter out = new PrintWriter(new FileWriter(Utils.createTempFile("timestamp")));
			out.print(timestamp);
			out.close();
		}
		return changed;
	}

	/**
	 * Updates the PokemonMeta from the response to DownloadItemTemplatesResponse and caches it
	 *
	 * @param data the data from the response
	 * @param write if this should write the data to the cache
	 * @throws IOException if writing fails
	 */
	public static void update(ByteString data, boolean write) throws IOException {
		DownloadItemTemplatesResponse templatesResponse = DownloadItemTemplatesResponse.parseFrom(data);
		if (write) {
			data.writeTo(new FileOutputStream(Utils.createTempFile("templates")));
		}
		List<ItemTemplate> templates = templatesResponse.getItemTemplatesList();
		PokemonMeta.templates.clear();
		PokemonMeta.templates.addAll(templates);
		for (ItemTemplate template : templates) {
			if (template.hasPokemonSettings()) {
				PokemonSettings pokemonSettings = template.getPokemonSettings();
				PokemonMeta.pokemonSettings.put(pokemonSettings.getPokemonId(), pokemonSettings);
			} else if (template.hasMoveSettings()) {
				MoveSettings moveSettings = template.getMoveSettings();
				PokemonMeta.moveSettings.put(moveSettings.getMovementId(), moveSettings);
			} else if (template.hasBadgeSettings()) {
				BadgeSettings badgeSettings = template.getBadgeSettings();
				PokemonMeta.badgeSettings.put(badgeSettings.getBadgeType(), badgeSettings);
			} else if (template.hasItemSettings()) {
				ItemSettings itemSettings = template.getItemSettings();
				PokemonMeta.itemSettings.put(itemSettings.getItemId(), itemSettings);
			} else if (template.hasBattleSettings()) {
				battleSettings = template.getBattleSettings();
			} else if (template.hasPokemonUpgrades()) {
				upgradeSettings = template.getPokemonUpgrades();
			}
		}
		Evolutions.initialize(templates);
		PokemonCpUtils.initialize(templates);
	}

	/**
	 * Gets pokemon settings for the given pokemon
	 *
	 * @param pokemon the pokemon to get settings for
	 * @return the settings
	 */
	public static PokemonSettings getPokemonSettings(PokemonId pokemon) {
		return pokemonSettings.get(pokemon);
	}

	/**
	 * Gets move settings for the given move
	 *
	 * @param move the move to get settings for
	 * @return the settings
	 */
	public static MoveSettings getMoveSettings(PokemonMove move) {
		return moveSettings.get(move);
	}

	/**
	 * Gets badge settings for the given badge type
	 *
	 * @param badge the badge to get settings for
	 * @return the settings
	 */
	public static BadgeSettings getBadgeSettings(BadgeType badge) {
		return badgeSettings.get(badge);
	}

	/**
	 * Gets item settings for the given item type
	 *
	 * @param item the item to get settings for
	 * @return the settings
	 */
	public static ItemSettings getItemSettings(ItemId item) {
		return itemSettings.get(item);
	}
}