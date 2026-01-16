package me.n10b1um.simplevoiceradio.listener;

import dev.lone.itemsadder.api.CustomBlock;
import me.n10b1um.simplevoiceradio.SimpleVoiceRadio;
import me.n10b1um.simplevoiceradio.config.ConfigManager;
import me.n10b1um.simplevoiceradio.manager.RadioManager;
import me.n10b1um.simplevoiceradio.model.RadioType;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;


public class RadioSetupListener implements Listener {
    private final RadioManager radioManager;
    private final ConfigManager configManager;
    public RadioSetupListener(RadioManager radioManager) {
        this.radioManager = radioManager;
        this.configManager = SimpleVoiceRadio.getInstance().getConfigManager();
    }
@EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        String blockId = event.getBlock().getType().toString();
//        CustomBlock customBlock = CustomBlock.byAlreadyPlaced(event.getBlock());  //todo: integrate itemsAdder
//        if (customBlock != null) {
//            blockId = customBlock.getNamespacedID();
//        } else {
//            return;
//        }
        if(blockId == null) return;

        RadioType type = configManager.getRadioType(blockId);
        System.out.println("blockId: " + blockId);
        System.out.println("type: " + type);
        if (type == null) return;

        Location location = event.getBlock().getLocation();
        Player player = event.getPlayer();

        radioManager.createRadio(location, type);
        player.sendMessage("§aRadio " + type + " successfully created!");   //todo remove garbage log
        player.playSound(location, Sound.BLOCK_NOTE_BLOCK_PLING, 1f, 2f);
    }
}