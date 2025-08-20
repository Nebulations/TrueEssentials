package me.nebu.trueEssentials.cmds;

import me.nebu.trueEssentials.Util;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class KickallCommand implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args) {
        if (!sender.hasPermission("trueessentials.staff.kickall")) {
            sender.sendMessage(Util.error("You do not have permission to run this command."));
            return true;
        }


        int total = 0;
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (!player.hasPermission("trueessentials.exempt.kickall")) {
                player.kick();
                total+=1;
            }
        }

        String format = total == 1 ? "" : "s";

        sender.sendMessage(Util.message("Successfully kicked out " + total + " player" + format + " from the server."));

        return true;
    }
}
