package me.n10b1um.simplevoiceradio.util;

import com.google.gson.*;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import com.google.gson.Gson;

import java.lang.reflect.Type;

public class GsonHelper {
    public static class  LocationAdapter implements JsonSerializer<Location>, JsonDeserializer<Location> {
        @Override
        public JsonElement serialize(Location src, Type typeOfSrc, JsonSerializationContext context) {
            JsonObject obj = new JsonObject();
            if (src.getWorld() != null) {
                obj.addProperty("world", src.getWorld().getName());
            }
            obj.addProperty("x", src.getBlockX());
            obj.addProperty("y", src.getBlockY());
            obj.addProperty("z", src.getBlockZ());
            return obj;
        }

        @Override
        public Location deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) {
            JsonObject obj = json.getAsJsonObject();
            String worldName = obj.get("world").getAsString();
            double x = obj.get("x").getAsInt();
            double y = obj.get("y").getAsInt();
            double z = obj.get("z").getAsInt();
            return new Location(Bukkit.getWorld(worldName), x, y, z);
        }
    }

    public static Gson createGson() {
        return new GsonBuilder()
                .setPrettyPrinting()
                .registerTypeAdapter(Location.class, new LocationAdapter())
                .create();
    }
}
