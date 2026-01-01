package me.n10b1um.simplevoiceradio.manager;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import me.n10b1um.simplevoiceradio.SimpleVoiceRadio;
import me.n10b1um.simplevoiceradio.model.RadioBlock;
import me.n10b1um.simplevoiceradio.model.RadioType;
import me.n10b1um.simplevoiceradio.util.GsonHelper;
import org.bukkit.Location;

import java.io.*;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RadioManager {
    private SimpleVoiceRadio plugin;
    private final Map<Location, RadioBlock> activeRadios = new HashMap<>();
    private File saveFile;
    private Gson gson;

    public RadioManager(SimpleVoiceRadio plugin) {
        this.plugin = plugin;
        this.saveFile = new File(plugin.getDataFolder(), "radio.json");
        this.gson = GsonHelper.createGson();
    }

    public void createRadio(Location location, RadioType type) {
        RadioBlock newRadio = new RadioBlock(
                location,
                "local",
                type,
                10000,
                50
        );

        activeRadios.put(location, newRadio);
    }

    public RadioBlock getRadio(Location location) {
        return activeRadios.get(location);
    }

    public void removeRadio(Location location) {
        if (activeRadios.containsKey(location)) {
            activeRadios.remove(location);
            plugin.getLogger().info("Радио удалено на координатах: " + location.toString());
        }
    }

    public void save () {
        if (activeRadios.isEmpty()) return;

        try (Writer writer = new FileWriter(saveFile)) {
            gson.toJson(activeRadios.values(), writer);
            plugin.getLogger().info(activeRadios.size() + " radio devices were successfully saved");
        } catch (IOException e) {
            plugin.getLogger().severe("Failed to save radio devices!");
            e.printStackTrace();
        }
    }

    public void load() {
        try {
            Reader reader = new FileReader(saveFile);
            Type listType = new TypeToken<List<RadioBlock>>(){}.getType();
            java.util.List<RadioBlock> loadedList = gson.fromJson(reader, listType);

            if (loadedList != null) {
                activeRadios.clear();
                for (RadioBlock radio : loadedList) {
                    activeRadios.put(radio.getLocation(), radio);
                }
                plugin.getLogger().info("Loaded " + activeRadios.size() + " radio devices");
            }
        } catch (IOException e) {
            plugin.getLogger().severe("Failed to load radio!");
            e.printStackTrace();
        }
    }
}
