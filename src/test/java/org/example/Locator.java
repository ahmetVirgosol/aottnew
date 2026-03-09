package org.example;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.openqa.selenium.By;
import java.io.FileReader;

public class Locator {
    private static JsonObject elements;
    static {
        try (FileReader reader = new FileReader("src/test/resources/value_info/elements.json")) {
            elements = JsonParser.parseReader(reader).getAsJsonObject();
        } catch (Exception e) { e.printStackTrace(); }
    }
    public static By get(String key) {
        JsonObject el = elements.getAsJsonObject(key);
        String type = el.get("type").getAsString();
        String val = el.get("value").getAsString();
        return switch (type.toLowerCase()) {
            case "id" -> By.id(val);
            case "css" -> By.cssSelector(val);
            case "class" -> By.className(val);
            default -> By.xpath(val);
        };
    }
}