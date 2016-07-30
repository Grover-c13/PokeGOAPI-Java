package com.pokegoapi.main;

import com.google.protobuf.InvalidProtocolBufferException;
import com.pokegoapi.exceptions.RemoteServerException;
import lombok.Getter;
import lombok.Setter;

public abstract class Task<I> {
	@Getter
	@Setter
	private boolean done;

	public Task() {
		done = false;
	}

	public abstract void onComplete(I input) throws RemoteServerException, InvalidProtocolBufferException;

}
