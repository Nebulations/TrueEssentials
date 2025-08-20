package me.nebu.trueEssentials.cmds.moderation;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

public class ModlogGUIListener implements Listener {

    @EventHandler
    public void onInventoryClick(InventoryClickEvent e) {
        if (e.getWhoClicked().getOpenInventory().getOriginalTitle().startsWith("History of ")) {
            e.setCancelled(true);
        }
    }

}
