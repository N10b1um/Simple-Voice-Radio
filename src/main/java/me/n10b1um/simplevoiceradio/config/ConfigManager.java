package me.n10b1um.simplevoiceradio.config;

import me.n10b1um.simplevoiceradio.SimpleVoiceRadio;
import me.n10b1um.simplevoiceradio.model.RadioType;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ConfigManager {
    private final SimpleVoiceRadio plugin;
    private final Map<String, RadioType> blockTypes = new HashMap<>();
    private int defaultRange;

    public ConfigManager(SimpleVoiceRadio plugin) {
        this.plugin = plugin;
        this.plugin.saveDefaultConfig();
        reload();
    }

    public void reload() {
        plugin.reloadConfig();
        FileConfiguration config = plugin.getConfig();
        blockTypes.clear();

        this.defaultRange = config.getInt("settings.default-range", 50);

        loadSection(config, "blocks.transmitters", RadioType.TRANSMITTER);
        loadSection(config, "blocks.receivers", RadioType.RECEIVER);
        // loadSection(config, "blocks.repeaters", RadioType.REPEATER); //TODO

        plugin.getLogger().info("Loaded " + blockTypes.size() + " custom radio blocks from config.");
    }

    private void loadSection(FileConfiguration config, String path, RadioType type) {
        List<String> ids = config.getStringList(path);
        for (String id : ids) {
            blockTypes.put(id, type);
        }
    }

    public RadioType getRadioType(String namespacedId) {
        return blockTypes.get(namespacedId);
    }

    public int getDefaultRange() {
        return defaultRange;
    }
}