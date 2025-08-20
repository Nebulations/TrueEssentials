package me.nebu.trueEssentials.cmds.homes;

import me.nebu.trueEssentials.Util;
import me.nebu.trueEssentials.playerdata.PlayerData;
import org.bukkit.Bukkit;
import org.bukkit.attribute.Attribute;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.defaults.BukkitCommand;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class HomesCommand implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args) {
        if (!sender.hasPermission("trueessentials.homes")) {
            sender.sendMessage(Util.error("You do not have permission to run this command."));
            return true;
        }

        if (!(sender instanceof Player player)) {
            sender.sendMessage(Util.error("This command can only be run by players."));
            return true;
        }

        PlayerData playerData = PlayerData.of(player);

        if (playerData.getHomes().isEmpty()) {
            sender.sendMessage(Util.message("You have no homes created."));
            return true;
        }

        StringBuilder homes = new StringBuilder();

        for (Home home : playerData.getHomes()) {
            homes.append(home.name()).append(", ");
        }

        String homesText = homes.substring(0, homes.length() - 2);
        sender.sendMessage(Util.message("Your homes: " + homesText));

        return true;
    }

}
