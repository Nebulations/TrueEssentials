package me.nebu.trueEssentials.cmds;

import me.nebu.trueEssentials.TrueEssentials;
import me.nebu.trueEssentials.Util;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.defaults.BukkitCommand;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class SetspawnCommand implements CommandExecutor {


    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args) {
        if (!sender.hasPermission("trueessentials.setspawn")) {
            sender.sendMessage(Util.error("You do not have permission to run this command."));
            return true;
        }

        if (!(sender instanceof Player player)) {
            sender.sendMessage(Util.error("This command can only be run by players."));
            return true;
        }

        Location location = player.getLocation();
        TrueEssentials.getInstance().getConfig().set("storage.spawn-location", location);
        TrueEssentials.getInstance().saveConfig();

        player.sendMessage(Util.message(String.format("New spawn defined at %s, %s, %s, in world %s.",
                Math.round(location.getX()),
                Math.round(location.getY()),
                Math.round(location.getZ()),
                location.getWorld().getName().toLowerCase()
        )));


        return true;
    }
}
