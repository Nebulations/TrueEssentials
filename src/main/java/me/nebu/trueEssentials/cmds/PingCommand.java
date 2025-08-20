package me.nebu.trueEssentials.cmds;

import me.nebu.trueEssentials.Util;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class PingCommand implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args) {
        if (!sender.hasPermission("trueessentials.ping")) {
            sender.sendMessage(Util.error("You do not have permission to run this command."));
            return true;
        }

        if (args.length == 0) {

            if (!(sender instanceof Player player)) {
                sender.sendMessage(Util.error("You must specify a player to run this command here."));
                return true;
            }

            player.sendMessage(Util.message("Your ping: " + player.getPing() + "ms."));
            return true;
        }

        if (!sender.hasPermission("trueessentials.others.ping")) {
            sender.sendMessage(Util.error("You can only view your own ping."));
            return true;
        }

        Player target = Bukkit.getPlayer(args[0]);

        if (target == null) {
            sender.sendMessage(Util.error("The player provided does not exist."));
            return true;
        }

        sender.sendMessage(Util.message(Util.formatName(target) + " ping: " + target.getPing() + "ms."));

        return true;
    }
}
