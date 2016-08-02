package com.pokegoapi.util;

import lombok.extern.slf4j.Slf4j;

/**
 * @author Paul van Assen
 */
@Slf4j
public class Slf4jLogger implements Logger {
    @Override
    public void v(String tag, String msg) {
        log.debug(getMessage(tag, msg));
    }

    @Override
    public void v(String tag, String msg, Throwable tr) {
        log.debug(getMessage(tag, msg), tr);
    }

    @Override
    public void d(String tag, String msg) {
        log.debug(getMessage(tag, msg));
    }

    @Override
    public void d(String tag, String msg, Throwable tr) {
        log.debug(getMessage(tag, msg), tr);
    }

    @Override
    public void i(String tag, String msg) {
        log.info(getMessage(tag, msg));
    }

    @Override
    public void i(String tag, String msg, Throwable tr) {
        log.info(getMessage(tag, msg), tr);
    }

    @Override
    public void w(String tag, String msg) {
        log.warn(getMessage(tag, msg));
    }

    @Override
    public void w(String tag, String msg, Throwable tr) {
        log.warn(getMessage(tag, msg), tr);
    }

    @Override
    public void w(String tag, Throwable tr) {
        log.warn(tag, tr);
    }

    @Override
    public void e(String tag, String msg) {
        log.error(getMessage(tag, msg));
    }

    @Override
    public void e(String tag, String msg, Throwable tr) {
        log.error(getMessage(tag, msg), tr);
    }

    @Override
    public void wtf(String tag, String msg) {
        log.error(getMessage(tag, msg));
    }

    @Override
    public void wtf(String tag, Throwable tr) {
        log.error(tag, tr);
    }

    @Override
    public void wtf(String tag, String msg, Throwable tr) {
        log.error(getMessage(tag, msg), tr);
    }

    private String getMessage(String tag, String msg) {
        return tag.concat(" - ").concat(msg);
    }
}
