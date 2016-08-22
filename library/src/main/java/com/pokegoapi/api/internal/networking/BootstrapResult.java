package com.pokegoapi.api.internal.networking;

import POGOProtos.Networking.Responses.GetHatchedEggsResponseOuterClass.GetHatchedEggsResponse;
import POGOProtos.Networking.Responses.GetInventoryResponseOuterClass.GetInventoryResponse;
import POGOProtos.Networking.Responses.GetPlayerResponseOuterClass.GetPlayerResponse;
import lombok.Data;

/**
 * @author Paul van Assen
 */
@Data
public class BootstrapResult {
	private final GetPlayerResponse playerResponse;
	private final GetInventoryResponse inventoryResponse;
	private final GetHatchedEggsResponse hatchedEggsResponse;
}
