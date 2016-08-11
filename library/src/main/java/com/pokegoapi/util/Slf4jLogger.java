package com.pokegoapi.util;

import org.slf4j.LoggerFactory;

/**
 * @author Paul van Assen
 */
public class Slf4jLogger implements Logger {
    @Override
    public void v(String tag, String msg) {
        getLogger(tag).debug(msg);
    }

    @Override
    public void v(String tag, String msg, Throwable tr) {
        getLogger(tag).debug(msg, tr);
    }

    @Override
    public void d(String tag, String msg) {
        getLogger(tag).debug(msg);
    }

    @Override
    public void d(String tag, String msg, Throwable tr) {
        getLogger(tag).debug(msg, tr);
    }

    @Override
    public void i(String tag, String msg) {
        getLogger(tag).info(msg);
    }

    @Override
    public void i(String tag, String msg, Throwable tr) {
        getLogger(tag).info(msg, tr);
    }

    @Override
    public void w(String tag, String msg) {
        getLogger(tag).warn(msg);
    }

    @Override
    public void w(String tag, String msg, Throwable tr) {
        getLogger(tag).warn(msg, tr);
    }

    @Override
    public void w(String tag, Throwable tr) {
        getLogger(tag).warn(tag, tr);
    }

    @Override
    public void e(String tag, String msg) {
        getLogger(tag).error(msg);
    }

    @Override
    public void e(String tag, String msg, Throwable tr) {
        getLogger(tag).error(msg, tr);
    }

    @Override
    public void wtf(String tag, String msg) {
        getLogger(tag).error(msg);
    }

    @Override
    public void wtf(String tag, Throwable tr) {
        getLogger(tag).error(tag, tr);
    }

    @Override
    public void wtf(String tag, String msg, Throwable tr) {
        getLogger(tag).error(msg, tr);
    }

    private org.slf4j.Logger getLogger(String tag) {
        return LoggerFactory.getLogger(tag);
    }
}
