package me.n10b1um.simplevoiceradio.listener;

import me.n10b1um.simplevoiceradio.manager.RadioManager;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockEvent;
import org.bukkit.event.block.BlockExplodeEvent;

public class RadioBreakListener implements Listener {
    private final RadioManager radioManager;

    public RadioBreakListener(RadioManager radioManager) {
        this.radioManager = radioManager;
    }

    @EventHandler
    public void onBreak(BlockBreakEvent event) {
        removeRadioByEvent(event);
    }

    @EventHandler
    public void onExplode(BlockExplodeEvent event) {
        removeRadioByEvent(event);
    }

    private void removeRadioByEvent(BlockEvent event) {
        Block block = event.getBlock();
        Location location = block.getLocation();

        if (radioManager.getRadio(location) == null) return;

        radioManager.removeRadio(location);
    }
}
