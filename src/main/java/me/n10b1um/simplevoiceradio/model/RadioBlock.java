package me.n10b1um.simplevoiceradio.model;

import org.bukkit.Location;

public class RadioBlock {
    private Location location;
    private String serverId;
    private RadioType type;
    private int frequency;
    private int range;

    public void setFrequency(int frequency) {
        this.frequency = frequency;
    }

    public RadioBlock(Location location, String serverId, RadioType type, int frequency, int range) {
        this.location = location;
        this.serverId = serverId;
        this.type = type;
        this.frequency = frequency;
        this.range = range;
    }

    public int getRange() {
        return range;
    }

    public int getFrequency() {
        return frequency;
    }

    public RadioType getType() {
        return type;
    }

    public String getServerId() {
        return serverId;
    }

    public Location getLocation() {
        return location;
    }
}