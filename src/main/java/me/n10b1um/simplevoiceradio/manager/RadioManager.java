package me.n10b1um.simplevoiceradio.manager;

import me.n10b1um.simplevoiceradio.SimpleVoiceRadio;
import me.n10b1um.simplevoiceradio.model.RadioBlock;
import me.n10b1um.simplevoiceradio.model.RadioType;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

public class RadioManager {
    private SimpleVoiceRadio plugin;
    private final Map<Location, RadioBlock> activeRadios = new HashMap<>();

    public RadioManager(SimpleVoiceRadio plugin) {
        this.plugin = plugin;
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
}
