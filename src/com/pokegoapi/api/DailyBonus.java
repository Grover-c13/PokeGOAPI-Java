package com.pokegoapi.api;

public class DailyBonus 
{
	private long nextCollectionTimestamp;
	private long nextDefenderBonusCollectTimestamp;
	
	
	public long getNextCollectionTimestamp() {
		return nextCollectionTimestamp;
	}
	public void setNextCollectionTimestamp(long nextCollectionTimestamp) {
		this.nextCollectionTimestamp = nextCollectionTimestamp;
	}
	public long getNextDefenderBonusCollectTimestamp() {
		return nextDefenderBonusCollectTimestamp;
	}
	public void setNextDefenderBonusCollectTimestamp(long nextDefenderBonusCollectTimestamp) {
		this.nextDefenderBonusCollectTimestamp = nextDefenderBonusCollectTimestamp;
	}
	
	
}
