package me.nebu.trueEssentials.cmds.homes;

import me.nebu.trueEssentials.Util;
import me.nebu.trueEssentials.playerdata.PlayerData;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.defaults.BukkitCommand;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class HomeCommand implements CommandExecutor {

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

        if (args.length == 0) {
            sender.sendMessage(Util.error("You must provide a home name."));
            return true;
        }

        String homeName = args[0];

        PlayerData playerData = PlayerData.of(player);
        Home home = playerData.getHome(homeName);

        if (home == null) {
            sender.sendMessage(Util.error("This home does not exist."));
            return true;
        }

        player.teleport(home.location());
        player.sendMessage(Util.message("Successfully teleported to home: " + home.name()));

        return true;
    }

}
