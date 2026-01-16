package me.n10b1um.simplevoiceradio.listener;

import me.n10b1um.simplevoiceradio.gui.GuiManager;
import me.n10b1um.simplevoiceradio.gui.RadioHolder;
import me.n10b1um.simplevoiceradio.model.RadioBlock;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;

public class GuiListener implements Listener {

    private final GuiManager guiManager;

    public GuiListener(GuiManager guiManager) {
        this.guiManager = guiManager;
    }

    @EventHandler
    public void onClick(InventoryClickEvent event) {
        Inventory inv = event.getInventory();
        if (!(inv.getHolder() instanceof RadioHolder holder)) {
            return;
        }

        event.setCancelled(true);

        if (event.getCurrentItem() == null) return;
        Player player = (Player) event.getWhoClicked();
        RadioBlock radio = holder.getRadio();
        Material mat = event.getCurrentItem().getType();

        int currentFreq = radio.getFrequency();
        int change = 0;

        if (mat == Material.RED_STAINED_GLASS_PANE) change = -100;
        else if (mat == Material.LIME_STAINED_GLASS_PANE) change = 100;

        if (change == 0) return;

        int newFreq = currentFreq + change;

        if (newFreq < 0) newFreq = 0;
        if (newFreq > 10000) newFreq = 10000;

        radio.setFrequency(newFreq);

        player.playSound(player.getLocation(), Sound.UI_BUTTON_CLICK, 1f, 1f);

        guiManager.openRadioMenu(player, radio);
    }
}