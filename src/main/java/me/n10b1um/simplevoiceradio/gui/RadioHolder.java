package me.n10b1um.simplevoiceradio.gui;

import me.n10b1um.simplevoiceradio.model.RadioBlock;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.jetbrains.annotations.NotNull;

public class RadioHolder implements InventoryHolder {
    private final RadioBlock radio;

    public RadioHolder(RadioBlock radio) {
        this.radio = radio;
    }

    public RadioBlock getRadio() {
        return radio;
    }

    @Override
    public @NotNull Inventory getInventory() {
        return null;
    }
}