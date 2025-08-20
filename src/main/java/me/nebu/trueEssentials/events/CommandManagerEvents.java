package me.nebu.trueEssentials.events;

import me.nebu.trueEssentials.Util;
import me.nebu.trueEssentials.extensions.CommandManagerExtension;
import me.nebu.trueEssentials.extensions.ExtensionName;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerCommandSendEvent;

public class CommandManagerEvents implements Listener {

    @EventHandler
    public void onCommandSend(PlayerCommandSendEvent e) {
        Player player = e.getPlayer();

        if (player.hasPermission("trueessentials.exempt.command-manager")) return;

        CommandManagerExtension ex = (CommandManagerExtension) Util.getExtension(ExtensionName.COMMAND_MANAGER);
        if (ex == null) return;

        if (ex.isReversed()) {
            e.getCommands().clear();
            e.getCommands().addAll(ex.getCommands());
        } else {
            e.getCommands().removeAll(ex.getCommands());
        }
    }

    @EventHandler
    public void onCommand(PlayerCommandPreprocessEvent e) {
        Player player = e.getPlayer();

        if (player.hasPermission("trueessentials.exempt.command-manager")) return;

        String cmd = e.getMessage().substring(1).split(" ")[0].toLowerCase();

        CommandManagerExtension ex = (CommandManagerExtension) Util.getExtension(ExtensionName.COMMAND_MANAGER);
        if (ex == null) return;

        if (ex.getCommands().contains(cmd)) {
            e.setCancelled(true);
            player.sendMessage(Util.error("This command is disabled."));
        }
    }

}
