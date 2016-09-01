package com.pokegoapi.util;

import com.google.protobuf.ByteString;

import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;

/**
 * Created by rama on 31/08/16.
 */
public abstract class PokeAFunc<T extends com.google.protobuf.GeneratedMessage, K> {


	public abstract K exec(T response);

	public K exec(ByteString data) throws Throwable {
		Class<T> klass = (Class<T>)
				((ParameterizedType) getClass().getGenericSuperclass())
						.getActualTypeArguments()[0];

		Method m = klass.getMethod("parseFrom", ByteString.class);
		T response = (T) m.invoke(null, data);

		return exec(response);
	}
}
