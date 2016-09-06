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

package com.pokegoapi.api.player;

import POGOProtos.Data.Player.DailyBonusOuterClass;
import POGOProtos.Networking.Requests.Messages.CollectDailyDefenderBonusMessageOuterClass;
import POGOProtos.Networking.Requests.RequestTypeOuterClass;
import POGOProtos.Networking.Responses.CollectDailyBonusResponseOuterClass.CollectDailyBonusResponse;
import com.pokegoapi.api.internal.networking.Networking;
import lombok.Data;
import rx.Observable;
import rx.functions.Func1;

@Data
public class DailyBonus {
	private final Networking networking;
	private DailyBonusOuterClass.DailyBonus proto;

	DailyBonus(Networking networking) {
		this.networking = networking;
	}

	final void update(DailyBonusOuterClass.DailyBonus proto) {
		this.proto = proto;
	}

	public long getNextCollectedTimestampMs() {
		return proto.getNextCollectedTimestampMs();
	}

	public long getNextDefenderBonusCollectTimestampMs() {
		return proto.getNextDefenderBonusCollectTimestampMs();
	}

	/**
	 * Collect daily bonus.
	 * @return Observable with the result.
	 */
	public Observable<CollectDailyBonusResponse.Result> collect() {
		if (getNextDefenderBonusCollectTimestampMs() > System.currentTimeMillis()) {
			return Observable.just(CollectDailyBonusResponse.Result.TOO_SOON);
		}
		return networking.queueRequest(RequestTypeOuterClass.RequestType.COLLECT_DAILY_DEFENDER_BONUS,
				CollectDailyDefenderBonusMessageOuterClass.CollectDailyDefenderBonusMessage.newBuilder().build(),
				CollectDailyBonusResponse.class)
				.map(new Func1<CollectDailyBonusResponse, CollectDailyBonusResponse.Result>() {
					@Override
					public CollectDailyBonusResponse.Result call(CollectDailyBonusResponse collectDailyBonusResponse) {
						return collectDailyBonusResponse.getResult();
					}
				});
	}
}
