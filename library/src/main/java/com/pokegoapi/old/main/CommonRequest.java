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

package com.pokegoapi.old.main;

import POGOProtos.Networking.Requests.RequestTypeOuterClass.RequestType;
import com.google.protobuf.ByteString;
import com.google.protobuf.InvalidProtocolBufferException;
import com.pokegoapi.old.api.PokemonGo;
import com.pokegoapi.old.exceptions.CaptchaActiveException;
import com.pokegoapi.network.LoginFailedException;
import com.pokegoapi.network.RemoteServerException;

public interface CommonRequest {
	ServerRequest create(PokemonGo api, RequestType requestType);

	void parse(PokemonGo api, ByteString data, RequestType requestType)
			throws InvalidProtocolBufferException, CaptchaActiveException, RemoteServerException, LoginFailedException;
}