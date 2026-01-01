package me.n10b1um.simplevoiceradio;

import me.n10b1um.simplevoiceradio.listener.RadioBreakListener;
import me.n10b1um.simplevoiceradio.listener.RadioInteractListener;
import me.n10b1um.simplevoiceradio.listener.RadioSetupListener;
import me.n10b1um.simplevoiceradio.manager.RadioManager;
import org.bukkit.plugin.java.JavaPlugin;

public final class SimpleVoiceRadio extends JavaPlugin {
private RadioManager radioManager;

    @Override
    public void onEnable() {
        if (!getDataFolder().exists()) {
            getDataFolder().mkdirs();
        }

        this.radioManager = new RadioManager(this);

        this.radioManager.load();

        getServer().getPluginManager().registerEvents(new RadioSetupListener(radioManager), this);
        getServer().getPluginManager().registerEvents(new RadioInteractListener(radioManager), this);
        getServer().getPluginManager().registerEvents(new RadioBreakListener(radioManager), this);
    }

    @Override
    public void onDisable() {
        if (this.radioManager != null) {
            this.radioManager.save();
        }
    }
}
