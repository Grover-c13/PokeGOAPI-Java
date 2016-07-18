package com.pokegoapi.api;

import lombok.Data;

@Data
public class DailyBonus {
	private long nextCollectionTimestamp;
	private long nextDefenderBonusCollectTimestamp;

}
