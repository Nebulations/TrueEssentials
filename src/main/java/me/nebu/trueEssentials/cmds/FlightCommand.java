package me.nebu.trueEssentials.cmds;

import me.nebu.trueEssentials.Util;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class FlightCommand implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage(Util.error("This command can only be run by players."));
            return true;
        }

        if (!sender.hasPermission("trueessentials.flight")) {
            sender.sendMessage(Util.error("You do not have permission to run this command."));
            return true;
        }

        if (args.length == 0) {
            player.setAllowFlight(!player.getAllowFlight());
            player.sendMessage(Util.message("Your flight mode has been toggled to " + player.getAllowFlight() + "."));

            return true;
        }

        if (!player.hasPermission("trueessentials.others.flight")) {
            player.sendMessage(Util.error("You can only change your own flight mode."));
            return true;
        }

        Player target = Bukkit.getPlayer(args[0]);

        if (target == null) {
            player.sendMessage(Util.error("The player provided does not exist."));
            return true;
        }

        target.setAllowFlight(!target.getAllowFlight());
        target.sendMessage(Util.message("Your flight mode has been toggled to " + target.getAllowFlight() + "."));
        player.sendMessage(Util.message("Toggled " + Util.formatName(target) + " flight mode to " + target.getAllowFlight()));

        return true;
    }
}
