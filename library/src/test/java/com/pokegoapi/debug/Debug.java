package com.pokegoapi.debug;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.io.IOException;
import java.util.Properties;

/**
 * Created by paul on 28-8-2016.
 */
@Slf4j
public class Debug {
    @Test
    public void testMapIssue() throws IOException {
        Properties properties = new Properties();
        properties.load(Debug.class.getResourceAsStream("/local.properties"));
        String token = properties.getProperty("token");
        double lat = Double.valueOf(properties.getProperty("lat"));
        double lng = Double.valueOf(properties.getProperty("lng"));
        //Old.testOld(token, lat, lng);
        New.testNew(token, lat, lng);
    }
}
