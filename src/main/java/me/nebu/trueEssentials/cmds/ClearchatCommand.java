package me.nebu.trueEssentials.cmds;

import me.nebu.trueEssentials.Util;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class ClearchatCommand implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args) {
        if (!sender.hasPermission("trueessentials.staff.clearchat")) {
            sender.sendMessage(Util.error("You do not have permission to run this command."));
            return true;
        }

        Component message = Component.newline();
        for (int i = 0; i <= 100; i++) {
            message = message.appendNewline();
        }

        message = message.append(Util.message("The chat was cleared by " + sender.getName() + ".")).append(Component.newline());

        for (Player player : Bukkit.getOnlinePlayers()) {
            if (!player.hasPermission("trueessentials.exempt.clearchat")) {
                player.sendMessage(message);
            }
        }

        sender.sendMessage(Util.message("The chat was cleared successfully."));

        return true;
    }
}
