package com.pokegoapi.api.player;

import lombok.Data;

@Data
public class DailyBonus {
	
	private long nextCollectionTimestamp;
	private long nextDefenderBonusCollectTimestamp;
}
