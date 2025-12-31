package me.n10b1um.simplevoiceradio.listener;

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

    public RadioInteractListener(RadioManager radioManager) {
        this.radioManager = radioManager;
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

        if (player.isSneaking()) {
            changeFrequency(player, radio);
        } else {
            showInfo(player, radio);
        }
    }

    private void showInfo(Player player, RadioBlock radio) {
        player.sendMessage("§8[§bRadio§8] §fПараметры устройства:");

        double visualFreq = radio.getFrequency() / 100.0;
        player.sendMessage("§7- Частота: §e" + visualFreq + " FM");
        player.sendMessage("§7- Тип: §f" + radio.getType());

        player.playSound(player.getLocation(), Sound.BLOCK_LEVER_CLICK, 0.5f, 1f);
    }

    private void changeFrequency(Player player, RadioBlock radio) {
        int newFreq = radio.getFrequency() + 50;

        if (newFreq > 12000) {
            newFreq = 8000;
        }

        radio.setFrequency(newFreq);

        double visualFreq = newFreq / 100.0;
        player.sendMessage("§8[§bRadio§8] §aЧастота изменена: §e" + visualFreq + " FM");
        player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_BIT, 1f, 2f);
    }
}