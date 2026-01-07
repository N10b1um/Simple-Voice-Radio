package me.n10b1um.simplevoiceradio.manager;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import de.maxhenkel.voicechat.api.Position;
import de.maxhenkel.voicechat.api.VoicechatApi;
import de.maxhenkel.voicechat.api.VoicechatConnection;
import de.maxhenkel.voicechat.api.VoicechatServerApi;
import de.maxhenkel.voicechat.api.packets.LocationalSoundPacket;
import de.maxhenkel.voicechat.api.packets.MicrophonePacket;
import me.n10b1um.simplevoiceradio.SimpleVoiceRadio;
import me.n10b1um.simplevoiceradio.integration.VoiceChatIntegration;
import me.n10b1um.simplevoiceradio.model.RadioBlock;
import me.n10b1um.simplevoiceradio.model.RadioType;
import me.n10b1um.simplevoiceradio.util.GsonHelper;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.io.*;
import java.lang.reflect.Type;
import java.util.*;

public class RadioManager {
    private final SimpleVoiceRadio plugin;
    private final Map<Location, RadioBlock> activeRadios = new HashMap<>();
    private final File saveFile;
    private final Gson gson;

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
            plugin.getLogger().info("Radio device removed from : " + location.toString());
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
        if (!saveFile.exists()) return;
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

    public Set<Integer> getNearbyRadioFrequencies(Player player) {
        Set<Integer> frequencies = new HashSet<>();

        for (Map.Entry<Location, RadioBlock> entry : activeRadios.entrySet()) {
            RadioBlock radio = entry.getValue();
            if (radio.getType() == RadioType.TRANSMITTER &&
                    entry.getKey().getWorld().equals(player.getWorld()) &&
                    entry.getKey().distance(player.getLocation()) <= 5) { // TODO: config distance

                frequencies.add(radio.getFrequency());
            }
        }
        return frequencies;
    }

    //todo: add repeaters logic
    public void broadcastsSound(MicrophonePacket packet, int freq, Player speaker) {
        for (Player listener : org.bukkit.Bukkit.getOnlinePlayers()) {
            if (listener.getUniqueId().equals(speaker.getUniqueId())) continue;

            Location bestSource = null;
            double bestDistSq = Double.MAX_VALUE;
            double maxDist = 32.0;

            for (RadioBlock radio : activeRadios.values()) {
                if (radio.getType() != RadioType.RECEIVER) continue;
                if (radio.getFrequency() != freq) continue;
                if (!radio.getLocation().getWorld().equals(listener.getWorld())) continue;

                double distSq = radio.getLocation().distanceSquared(listener.getLocation());

                if (distSq < (maxDist * maxDist) && distSq < bestDistSq) {
                    bestDistSq = distSq;
                    bestSource = radio.getLocation();
                }
            }

            if (bestSource != null) {
                sendAudioPacket(listener, packet, bestSource);
            }
        }
    }

    private void sendAudioPacket(Player listener, MicrophonePacket sourcePacket, Location sourceLocation) {
        VoicechatApi api = VoiceChatIntegration.getApi();
        VoicechatServerApi serverApi = VoiceChatIntegration.getServerApi();
        VoicechatConnection connection;
        if (serverApi == null) return;
        connection = serverApi.getConnectionOf(listener.getUniqueId());
        if (connection == null) return;
        Position position;
        if (api == null) return;
        position = api.createPosition(
                sourceLocation.getX(),
                sourceLocation.getY(),
                sourceLocation.getZ()
                );

        LocationalSoundPacket packet = sourcePacket.locationalSoundPacketBuilder()
                .position(position)
                .distance(32f)      //todo add to config
                .build();
        serverApi.sendLocationalSoundPacketTo(connection, packet);
    }
}
