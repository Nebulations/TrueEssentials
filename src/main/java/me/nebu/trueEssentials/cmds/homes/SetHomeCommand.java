package me.nebu.trueEssentials.cmds.homes;

import me.nebu.trueEssentials.TrueEssentials;
import me.nebu.trueEssentials.Util;
import me.nebu.trueEssentials.playerdata.PlayerData;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.defaults.BukkitCommand;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Set;

public class SetHomeCommand implements CommandExecutor {

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
            sender.sendMessage(Util.error("You must provide a name for your home."));
            return true;
        }

        String homeName = args[0];

        PlayerData playerData = PlayerData.of(player);

        if (playerData.getHome(homeName) != null) {
            sender.sendMessage(Util.error("You already have a home with this name."));
            return true;
        }

        Home home = new Home(homeName, player.getLocation());

        int homes = playerData.getHomes().size();

        FileConfiguration config = TrueEssentials.getInstance().getConfig();
        ConfigurationSection section = config.getConfigurationSection("settings.extensions.homes.limits");

        if (section == null) {
            sender.sendMessage(Util.error("An error occurred: No permissions defined. Contact a server administrator."));
            return true;
        }

        Set<String> keys = section.getKeys(false);

        int maxHomes = 0;
        for (String key : keys) {
            int limit = config.getInt("settings.extensions.homes.limits." + key);

            if (maxHomes >= limit) continue;

            if (!player.hasPermission("trueessentials.homes.limit." + key)) continue;

            maxHomes = limit;
        }

        if (homes >= maxHomes) {
            player.sendMessage(Util.error("You have reached your home limit. (" + maxHomes + ")"));
            return true;
        }

        playerData.addHome(home);
        playerData.save();

        player.sendMessage(Util.message("Successfully created a new home: " + homeName));

        return true;
    }

}
