package me.n10b1um.simplevoiceradio.gui;

import me.n10b1um.simplevoiceradio.model.RadioBlock;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;

public class GuiManager {

    public GuiManager() {}

    public void openRadioMenu(Player player, RadioBlock radio) {
        Inventory gui = Bukkit.createInventory(new RadioHolder(radio), 27, Component.text("Настройка частоты"));

        ItemStack filler = createItem(Material.GRAY_STAINED_GLASS_PANE, " ");
        for (int i = 0; i < 27; i++) {
            gui.setItem(i, filler);
        }

        double freq = radio.getFrequency() / 100.0;
        ItemStack info = createItem(Material.BEACON, "§eРадиостанция",
                "§7Тип: §f" + radio.getType(),
                "§7Частота: §a" + freq + " FM");
        gui.setItem(13, info);
        gui.setItem(11, createItem(Material.RED_STAINED_GLASS_PANE, "§c-1 FM"));

        gui.setItem(15, createItem(Material.LIME_STAINED_GLASS_PANE, "§a+1 FM"));

        player.openInventory(gui);
    }

    private ItemStack createItem(Material mat, String name, String... lore) {
        ItemStack item = new ItemStack(mat);
        ItemMeta meta = item.getItemMeta();
        meta.displayName(Component.text(name));
        if (lore.length > 0) {
            List<@NotNull TextComponent> loreList = Arrays.stream(lore)
                    .map(Component::text).toList();
            meta.lore(loreList);
        }
        item.setItemMeta(meta);
        return item;
    }
}