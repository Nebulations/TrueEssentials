package me.nebu.trueEssentials.cmds;

import me.nebu.trueEssentials.Util;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class EnderchestCommand implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage(Util.error("This command can only be run by players."));
            return true;
        }

        if (!sender.hasPermission("trueessentials.enderchest")) {
            sender.sendMessage(Util.error("You do not have permission to run this command."));
            return true;
        }

        if (args.length == 0) {
            player.openInventory(player.getEnderChest());
            player.sendMessage(Util.message("Opening your ender chest."));

            return true;
        }

        Player target = Bukkit.getPlayer(args[0]);

        if (!player.hasPermission("trueessentials.others.enderchest")) {
            player.sendMessage(Util.error("You can only open your own ender chest."));
            return true;
        }

        if (target == null) {
            player.sendMessage(Util.error("The player provided is invalid."));
            return true;
        }

        player.openInventory(target.getEnderChest());
        player.sendMessage(Util.message("Opening " + Util.formatName(target) + " ender chest."));

        return true;
    }
}
