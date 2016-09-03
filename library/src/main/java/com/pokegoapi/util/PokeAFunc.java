package com.pokegoapi.util;

import com.google.protobuf.ByteString;

import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;

/**
 * Created by rama on 31/08/16.
 */
public abstract class PokeAFunc<T extends com.google.protobuf.GeneratedMessage, K> {

	/**
	 * The abstract internal callback
	 *
	 * @param response the response
	 * @return the object for the callback
     */
	public abstract K exec(T response);

	/**
	 *
	 * @param data the data to be parsed through reflection
	 * @return the object for the callback
	 * @throws Throwable errors
     */
	public K exec(ByteString data) throws Throwable {
		Class<T> klass = (Class<T>)
				((ParameterizedType) getClass().getGenericSuperclass())
						.getActualTypeArguments()[0];

		Method klassMethod = klass.getMethod("parseFrom", ByteString.class);
		T response = (T) klassMethod.invoke(null, data);

		return exec(response);
	}
}
