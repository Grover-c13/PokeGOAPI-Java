package com.pokegoapi.util;

/**
 * Created by Will on 7/20/16.
 */
public interface Logger {

    void v(String tag, String msg);

    void v(String tag, String msg, Throwable t);

    void d(String tag, String msg);

    void d(String tag, String msg, Throwable tr);

    void i(String tag, String msg);

    void i(String tag, String msg, Throwable tr);

    void w(String tag, String msg);

    void w(String tag, String msg, Throwable tr);

    void w(String tag, Throwable tr);

    void e(String tag, String msg);

    void e(String tag, String msg, Throwable tr);

    void wtf(String tag, String msg);

    void wtf(String tag, Throwable tr);

    void wtf(String tag, String msg, Throwable tr);
}
