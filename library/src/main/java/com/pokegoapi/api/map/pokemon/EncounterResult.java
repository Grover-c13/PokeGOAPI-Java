package com.pokegoapi.api.map.pokemon;

import POGOProtos.Networking.Responses.DiskEncounterResponseOuterClass.DiskEncounterResponse;
import POGOProtos.Networking.Responses.EncounterResponseOuterClass.EncounterResponse.Status;
import POGOProtos.Networking.Responses.IncenseEncounterResponseOuterClass.IncenseEncounterResponse;
import lombok.Getter;

public enum EncounterResult {
	ERROR(0),
	SUCCESS(1),
	NOT_FOUND(2),
	CLOSED(3),
	POKEMON_FLED(4),
	NOT_IN_RANGE(5),
	ALREADY_HAPPENED(6),
	INVENTORY_FULL(7),
	UNRECOGNISED(-1);

	@Getter
	private int value;

	EncounterResult(int value) {
		this.value = value;
	}

	/**
	 * @param status the status to convert from
	 * @return EncounterResult from Status
	 */
	public static EncounterResult from(Status status) {
		switch (status) {
			case ENCOUNTER_ERROR:
				return ERROR;
			case ENCOUNTER_SUCCESS:
				return SUCCESS;
			case ENCOUNTER_NOT_FOUND:
				return NOT_FOUND;
			case ENCOUNTER_CLOSED:
				return CLOSED;
			case ENCOUNTER_POKEMON_FLED:
				return POKEMON_FLED;
			case ENCOUNTER_NOT_IN_RANGE:
				return NOT_IN_RANGE;
			case ENCOUNTER_ALREADY_HAPPENED:
				return ALREADY_HAPPENED;
			case POKEMON_INVENTORY_FULL:
				return INVENTORY_FULL;
			default:
				return UNRECOGNISED;
		}
	}

	/**
	 * @param result the result to convert from
	 * @return EncounterResult from Result
	 */
	public static EncounterResult from(DiskEncounterResponse.Result result) {
		switch (result) {
			case SUCCESS:
				return SUCCESS;
			case NOT_AVAILABLE:
				return NOT_FOUND;
			case ENCOUNTER_ALREADY_FINISHED:
				return CLOSED;
			case NOT_IN_RANGE:
				return NOT_IN_RANGE;
			case POKEMON_INVENTORY_FULL:
				return INVENTORY_FULL;
			default:
				return UNRECOGNISED;
		}
	}

	/**
	 * @param result the result to convert from
	 * @return EncounterResult from Result
	 */
	public static EncounterResult from(IncenseEncounterResponse.Result result) {
		switch (result) {
			case INCENSE_ENCOUNTER_SUCCESS:
				return SUCCESS;
			case POKEMON_INVENTORY_FULL:
				return INVENTORY_FULL;
			case INCENSE_ENCOUNTER_NOT_AVAILABLE:
				return NOT_FOUND;
			default:
				return UNRECOGNISED;
		}
	}
}
