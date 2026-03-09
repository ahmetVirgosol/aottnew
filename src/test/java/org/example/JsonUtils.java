package org.example;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.FileReader;

public class JsonUtils {
    private static JsonObject data;
    static {
        try (FileReader reader = new FileReader("src/test/resources/datas/value.json")) {
            data = JsonParser.parseReader(reader).getAsJsonObject();
        } catch (Exception e) { e.printStackTrace(); }
    }
    public static String getValue(String key) { return data.get(key).getAsString(); }
}