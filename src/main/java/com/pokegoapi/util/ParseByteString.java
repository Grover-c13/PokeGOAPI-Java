package com.pokegoapi.util;

import com.google.protobuf.ByteString;
import com.google.protobuf.InvalidProtocolBufferException;
import com.pokegoapi.exceptions.RemoteServerException;
import rx.functions.Func1;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Created by paul on 30-7-2016.
 */
public class ParseByteString<T> implements Func1<ByteString, T> {

    private final Class<T> clz;

    public ParseByteString(Class<T> clz) {
        this.clz = clz;
    }

    @Override
    @SuppressWarnings("unchecked")
    public T call(ByteString bytes) {
        try {
            Method method = clz.getMethod("parseFrom", ByteString.class);
            return (T)method.invoke(null, bytes);
        } catch (NoSuchMethodException |IllegalAccessException e) {
            throw new RuntimeException(e);
        } catch (InvocationTargetException e) {
            if (e.getCause() instanceof InvalidProtocolBufferException) {
                throw new RemoteServerException(e.getCause());
            }
            else {
                throw new RuntimeException(e);
            }
        }
    }
}
