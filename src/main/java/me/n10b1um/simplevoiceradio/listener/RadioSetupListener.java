package me.n10b1um.simplevoiceradio.listener;

import me.n10b1um.simplevoiceradio.manager.RadioManager;
import me.n10b1um.simplevoiceradio.model.RadioType;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

public class RadioSetupListener implements Listener {
    private final RadioManager radioManager;

    public RadioSetupListener(RadioManager radioManager) {
        this.radioManager = radioManager;
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        if (event.getAction() != Action.RIGHT_CLICK_BLOCK) return;
        if (event.getHand() != EquipmentSlot.HAND) return;

        Block clickedBlock = event.getClickedBlock();
        if (clickedBlock == null) return;

        switch (clickedBlock.getType()) {
            case JUKEBOX:
                handleTransmitter(event);
                break;
            case BELL:
                setupRadio(event, RadioType.RECEIVER, "receiver");
                break;
            case LIGHTNING_ROD:
                setupRadio(event, RadioType.REPEATER, "repeater");
                break;
            default:
                break;
        }
    }

    private void handleTransmitter(PlayerInteractEvent event) {
        event.setCancelled(true);

        ItemStack item = event.getItem();
        if (item == null || item.getType() != Material.DIAMOND_BLOCK) return;

        setupRadio(event, RadioType.TRANSMITTER, "transmitter");
    }

    private void setupRadio(PlayerInteractEvent event, RadioType type, String typeName) {
        event.setCancelled(true);

        Block block = event.getClickedBlock();
        if (block == null) return;

        Location location = block.getLocation();

        if (radioManager.getRadio(location) != null) return;

        Player player = event.getPlayer();

        radioManager.createRadio(location, type);

        player.sendMessage("§aRadio " + typeName + " successfully created!");
        player.playSound(location, Sound.BLOCK_NOTE_BLOCK_PLING, 1f, 2f);
    }
}