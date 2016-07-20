package com.pokegoapi.examples;

import POGOProtos.Enums.PokemonIdOuterClass;
import POGOProtos.Networking.Envelopes.RequestEnvelopeOuterClass;
import POGOProtos.Networking.Responses.ReleasePokemonResponseOuterClass;
import com.pokegoapi.api.PokemonGo;
import com.pokegoapi.api.inventory.Pokemon;
import com.pokegoapi.auth.GoogleLogin;
import com.pokegoapi.auth.PTCLogin;
import com.pokegoapi.exceptions.LoginFailedException;
import okhttp3.OkHttpClient;


import java.util.List;

public class TransferOnePidgeyExample
{

	public static void main(String[] args)
	{
		OkHttpClient http = new OkHttpClient();
		RequestEnvelopeOuterClass.RequestEnvelope.AuthInfo auth = null;
		try {
			//auth = new PTCLogin(http).login(ExampleLoginDetails.LOGIN, ExampleLoginDetails.PASSWORD);
			// or google
			auth = new GoogleLogin(http).login("", ""); // currently uses oauth flow so no user or pass needed
			PokemonGo go = new PokemonGo(auth, http);

			List<Pokemon> pidgeys = go.getPokebank().getPokemonByPokemonId(PokemonIdOuterClass.PokemonId.PIDGEY);

			if (pidgeys.size() > 0)
			{
				Pokemon pest = pidgeys.get(0);
				pest.debug();
				ReleasePokemonResponseOuterClass.ReleasePokemonResponse.Result result = pest.transferPokemon();

				System.out.println("Transfered Pidgey result:" + result);
			}
			else
			{
				System.out.println("You have no pidgeys :O");
			}




		} catch (LoginFailedException e) {
			// failed to login, invalid credentials or auth issue.
			e.printStackTrace();
		}
	}
}
