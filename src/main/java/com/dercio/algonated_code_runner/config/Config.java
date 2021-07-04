package com.dercio.algonated_code_runner.config;

import io.vertx.core.json.JsonObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;

public class Config {

    private final JsonObject jsonObject;

    public Config() {
        var inputStream = getClass()
                .getClassLoader()
                .getResourceAsStream("application-conf.json");
        this.jsonObject = new JsonObject(readInputStream(Objects.requireNonNull(inputStream)));
    }

    public JsonObject getScalesServiceConfig() {
        return getServices().getJsonObject("scales-service");
    }

    public JsonObject getTSPServiceConfig() {
        return getServices().getJsonObject("tsp-service");
    }

    public JsonObject getServices() {
        return jsonObject.getJsonObject("services");
    }

    private String readInputStream(InputStream inputStream) {
        try {

            var result = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            for (int length; (length = inputStream.read(buffer)) != -1; ) {
                result.write(buffer, 0, length);
            }
            return result.toString();
        } catch (IOException ioException) {
            return "{}";
        }
    }

}
