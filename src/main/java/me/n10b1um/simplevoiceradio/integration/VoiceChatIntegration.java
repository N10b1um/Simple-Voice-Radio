package me.n10b1um.simplevoiceradio.integration;

import de.maxhenkel.voicechat.api.ServerPlayer;
import de.maxhenkel.voicechat.api.VoicechatApi;
import de.maxhenkel.voicechat.api.VoicechatPlugin;
import de.maxhenkel.voicechat.api.VoicechatServerApi;
import de.maxhenkel.voicechat.api.events.EventRegistration;
import de.maxhenkel.voicechat.api.events.MicrophonePacketEvent;
import de.maxhenkel.voicechat.api.events.VoicechatServerStartedEvent;
import de.maxhenkel.voicechat.api.packets.MicrophonePacket;
import me.n10b1um.simplevoiceradio.SimpleVoiceRadio;
import me.n10b1um.simplevoiceradio.manager.RadioManager;
import org.bukkit.entity.Player;

import javax.annotation.Nullable;
import java.util.Set;

public class VoiceChatIntegration implements VoicechatPlugin {
    private static VoicechatServerApi serverApi;
    private static VoicechatApi api;

    public VoiceChatIntegration() {
    }

    @Override
    public String getPluginId() {
        return "simple_voice_radio";
    }

    @Override
    public void initialize(VoicechatApi api) {
        VoiceChatIntegration.api = api;
        SimpleVoiceRadio.getInstance().getLogger().info("Simple Voice Chat API initialized.");
    }

    @Override
    public void registerEvents(EventRegistration registration) {
        registration.registerEvent(VoicechatServerStartedEvent.class, this::onServerStarted);
        registration.registerEvent(MicrophonePacketEvent.class, this::onMicrophone);
    }

    private void onServerStarted(VoicechatServerStartedEvent event) {
        serverApi = event.getVoicechat();
        SimpleVoiceRadio.getInstance().getLogger().info("VoicechatServerApi captured!");
    }

    private void onMicrophone(MicrophonePacketEvent event) {
        if (event.getSenderConnection() == null) return;
        ServerPlayer vcPlayer = event.getSenderConnection().getPlayer();
        Object platformPlayer = vcPlayer.getPlayer();

        if (!(platformPlayer instanceof Player player)) {
            return;
        }

        SimpleVoiceRadio plugin = SimpleVoiceRadio.getInstance();
        RadioManager radioManager = plugin.getRadioManager();

        Set<Integer> frequencies = radioManager.getNearbyRadioFrequencies(player);
        if (frequencies.isEmpty()) return;

        MicrophonePacket packet = event.getPacket();
        for(Integer freq : frequencies) {
            radioManager.broadcastsSound(packet, freq, player);
        }
    }

    @Nullable
    public static VoicechatServerApi getServerApi() {
        return serverApi;
    }

    @Nullable
    public static VoicechatApi getApi() {
        return api;
    }
}