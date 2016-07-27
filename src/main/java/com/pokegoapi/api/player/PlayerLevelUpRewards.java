package com.pokegoapi.api.player;

import POGOProtos.Inventory.Item.ItemAwardOuterClass;
import POGOProtos.Inventory.Item.ItemIdOuterClass;
import POGOProtos.Networking.Responses.LevelUpRewardsResponseOuterClass.LevelUpRewardsResponse;
import lombok.Data;

import java.util.Collections;
import java.util.List;

/**
 * A data class containing the results of a trainer level up. This includes a list of items received for this level up,
 * a list of items which were unlocked by this level up (for example razz berries)
 * and the status of these level up results.
 * If the rewards for this level up have been
 * accepted in the past the status will be ALREADY_ACCEPTED, if this level up has not yet been achieved
 * by the player it will be NOT_UNLOCKED_YET otherwise it will be NEW.
 *
 * @author Alex Schlosser
 */
@Data
public class PlayerLevelUpRewards {
	private final Status status;
	private final List<ItemAwardOuterClass.ItemAward> rewards;
	private final List<ItemIdOuterClass.ItemId> unlockedItems;


	/**
	 * Create new empty result object with the specified status.
	 *
	 * @param status the status of this result
	 */
	public PlayerLevelUpRewards(final Status status) {
		this.status = status;
		this.rewards = Collections.emptyList();
		this.unlockedItems = Collections.emptyList();
	}

	public enum Status {
		ALREADY_ACCEPTED, NEW, NOT_UNLOCKED_YET
	}

	/**
	 * Create a new result object based on a server response
	 *
	 * @param response the response which contains the request results
	 */
	public PlayerLevelUpRewards(final LevelUpRewardsResponse response) {
		this.rewards = response.getItemsAwardedList();
		this.unlockedItems = response.getItemsUnlockedList();
		this.status = (rewards.isEmpty() ? Status.ALREADY_ACCEPTED : Status.NEW);
	}
}
