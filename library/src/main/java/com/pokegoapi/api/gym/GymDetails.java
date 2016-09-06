package com.pokegoapi.api.gym;

import POGOProtos.Data.Gym.GymMembershipOuterClass;
import POGOProtos.Data.PokemonDataOuterClass.PokemonData;
import POGOProtos.Networking.Responses.GetGymDetailsResponseOuterClass.GetGymDetailsResponse;
import com.annimon.stream.Collectors;
import com.annimon.stream.Stream;
import com.annimon.stream.function.Function;
import com.google.protobuf.ProtocolStringList;
import com.pokegoapi.exceptions.LoginFailedException;
import com.pokegoapi.exceptions.RemoteServerException;

import java.util.List;

/**
 * Details of a gym
 */
public class GymDetails {
	private final GetGymDetailsResponse getGymDetailsResponse;

	/**
	 * For internal use
	 *
	 * @param getGymDetailsResponse Response
	 */
	GymDetails(GetGymDetailsResponse getGymDetailsResponse) {
		this.getGymDetailsResponse = getGymDetailsResponse;
	}

	public String getName() throws LoginFailedException, RemoteServerException {
		return getGymDetailsResponse.getName();
	}

	public ProtocolStringList getUrlsList() throws LoginFailedException, RemoteServerException {
		return getGymDetailsResponse.getUrlsList();
	}

	public GetGymDetailsResponse.Result getResult() throws LoginFailedException, RemoteServerException {
		return getGymDetailsResponse.getResult();
	}

	public boolean inRange() throws LoginFailedException, RemoteServerException {
		GetGymDetailsResponse.Result result = getResult();
		return (result != GetGymDetailsResponse.Result.ERROR_NOT_IN_RANGE);
	}

	public String getDescription() throws LoginFailedException, RemoteServerException {
		return getGymDetailsResponse.getDescription();
	}


	public List<GymMembershipOuterClass.GymMembership> getGymMembers() {
		return getGymDetailsResponse.getGymState().getMembershipsList();
	}

	/**
	 * Get a list of pokemon defending this gym.
	 *
	 * @return List of pokemon
	 * @throws LoginFailedException  if the login failed
	 * @throws RemoteServerException When a buffer exception is thrown
	 */
	public List<PokemonData> getDefendingPokemon() {
		return Stream.of(getGymMembers()).map(new Function<GymMembershipOuterClass.GymMembership, PokemonData>() {
			@Override
			public PokemonData apply(GymMembershipOuterClass.GymMembership gymMembership) {
				return gymMembership.getPokemonData();
			}
		}).collect(Collectors.<PokemonData>toList());
	}


	public boolean isAttackable() throws LoginFailedException, RemoteServerException {
		return this.getGymMembers().size() != 0;
	}
}
