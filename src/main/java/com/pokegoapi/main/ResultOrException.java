package com.pokegoapi.main;

import com.google.protobuf.ByteString;
import lombok.Data;
import lombok.Getter;

public class ResultOrException {
    @Getter
    private final ByteString result;
    @Getter
    private final Exception exception;

    private ResultOrException(ByteString result, Exception exception) {
        this.result = result;
        this.exception = exception;
    }

    public static ResultOrException getError(Exception exception) {
        return new ResultOrException(null, exception);
    }

    public static ResultOrException getResult(ByteString result) {
        return new ResultOrException(result, null);
    }
}
