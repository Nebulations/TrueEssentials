package me.nebu.trueEssentials.cmds.moderation;

import me.nebu.trueEssentials.Util;
import me.nebu.trueEssentials.extensions.ExtensionName;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

import java.util.Objects;

public class ModlogGUIListener implements Listener {

    @EventHandler
    public void onInventoryClick(InventoryClickEvent e) {
        if (!Objects.requireNonNull(Util.getExtension(ExtensionName.MODERATION)).enabled()) {
            return;
        }

        if (e.getWhoClicked().getOpenInventory().getOriginalTitle().startsWith("History of ")) {
            e.setCancelled(true);
        }
    }

}
