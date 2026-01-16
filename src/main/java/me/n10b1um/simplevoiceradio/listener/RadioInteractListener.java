package me.n10b1um.simplevoiceradio.listener;

import me.n10b1um.simplevoiceradio.gui.GuiManager;
import me.n10b1um.simplevoiceradio.manager.RadioManager;
import me.n10b1um.simplevoiceradio.model.RadioBlock;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;

public class RadioInteractListener implements Listener {

    private final RadioManager radioManager;
    private final GuiManager guiManager;
    public RadioInteractListener(RadioManager radioManager, GuiManager guiManager) {
        this.radioManager = radioManager;
        this.guiManager = guiManager;
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        if (event.getAction() != Action.RIGHT_CLICK_BLOCK) return;
        if (event.getHand() != EquipmentSlot.HAND) return;

        Block block = event.getClickedBlock();
        if (block == null) return;

        RadioBlock radio = radioManager.getRadio(block.getLocation());
        if (radio == null) return;

        Player player = event.getPlayer();
        event.setCancelled(true);

        guiManager.openRadioMenu(player, radio);
    }
}