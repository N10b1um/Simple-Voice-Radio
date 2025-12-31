package me.n10b1um.simplevoiceradio.listener;

import me.n10b1um.simplevoiceradio.manager.RadioManager;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

//TODO УДАЛЯТЬ РАДИО ИЗ ПЯМЯТИ ПРИ ВЗРЫВАХ
public class RadioBreakListener implements Listener {
    private RadioManager radioManager;

    public RadioBreakListener(RadioManager radioManager) {
        this.radioManager = radioManager;
    }

    @EventHandler
    public void onBreak(BlockBreakEvent event) {
        Block block = event.getBlock();
        Location location = block.getLocation();

        if (radioManager.getRadio(location) == null) return;

        radioManager.removeRadio(location);
    }
}
