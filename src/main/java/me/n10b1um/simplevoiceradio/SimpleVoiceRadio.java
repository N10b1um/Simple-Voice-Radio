package me.n10b1um.simplevoiceradio;

import me.n10b1um.simplevoiceradio.config.ConfigManager;
import me.n10b1um.simplevoiceradio.gui.GuiManager;
import me.n10b1um.simplevoiceradio.integration.VoiceChatIntegration;
import me.n10b1um.simplevoiceradio.listener.GuiListener;
import me.n10b1um.simplevoiceradio.listener.RadioBreakListener;
import me.n10b1um.simplevoiceradio.listener.RadioInteractListener;
import me.n10b1um.simplevoiceradio.listener.RadioSetupListener;
import me.n10b1um.simplevoiceradio.manager.RadioManager;
import org.bukkit.plugin.java.JavaPlugin;

public final class SimpleVoiceRadio extends JavaPlugin {
    private RadioManager radioManager;
    private static SimpleVoiceRadio instance;
    private ConfigManager configManager;

    @Override
    public void onEnable() {
        instance = this;

        if (!getDataFolder().exists()) {
            getDataFolder().mkdirs();
        }

        this.radioManager = new RadioManager(this);
        this.radioManager.load();
        this.configManager = new ConfigManager(this);
        GuiManager guiManager = new GuiManager();

        getServer().getPluginManager().registerEvents(new RadioSetupListener(radioManager), this);
        getServer().getPluginManager().registerEvents(new RadioInteractListener(radioManager, guiManager), this);
        getServer().getPluginManager().registerEvents(new RadioBreakListener(radioManager), this);
        getServer().getPluginManager().registerEvents(new GuiListener(guiManager), this);

        if (getServer().getPluginManager().getPlugin("voicechat") == null) {
            getLogger().warning("SimpleVoiceChat plugin not found! Radio will not work.");
            return;
        }

        de.maxhenkel.voicechat.api.BukkitVoicechatService service =
                getServer().getServicesManager().load(de.maxhenkel.voicechat.api.BukkitVoicechatService.class);

        if (service == null) {
            getLogger().warning("VoiceChat service not found! (Plugin started, but API is unavailable)");
            return;
        }

        VoiceChatIntegration integration = new VoiceChatIntegration();
        service.registerPlugin(integration);
        getLogger().info("Simple Voice Chat successfully connected!");
    }

    @Override
    public void onDisable() {
        if (this.radioManager != null) {
            this.radioManager.save();
        }
    }

    public RadioManager getRadioManager() {
        return this.radioManager;
    }

    public ConfigManager getConfigManager() {
        return configManager;
    }

    public static SimpleVoiceRadio getInstance() {
        return instance;
    }
}
