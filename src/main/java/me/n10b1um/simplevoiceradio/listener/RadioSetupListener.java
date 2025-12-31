package me.n10b1um.simplevoiceradio.listener;

import me.n10b1um.simplevoiceradio.manager.RadioManager;
import me.n10b1um.simplevoiceradio.model.RadioType;
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
        if (clickedBlock == null || clickedBlock.getType() != Material.JUKEBOX) return;

        event.setCancelled(true);

        ItemStack item = event.getItem();
        if (item == null || item.getType() != Material.DIAMOND_BLOCK || radioManager.getRadio(clickedBlock.getLocation()) != null) return;

        Player player = event.getPlayer();

        radioManager.createRadio(clickedBlock.getLocation(), RadioType.TRANSMITTER);
        player.sendMessage("§aРадио-передатчик успешно создан!");
        player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 1f, 2f);

    }
}
