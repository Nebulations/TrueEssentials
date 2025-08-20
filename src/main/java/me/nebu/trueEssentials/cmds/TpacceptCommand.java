package me.nebu.trueEssentials.cmds;

import me.nebu.trueEssentials.Util;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class TpacceptCommand implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args) {
        if (!sender.hasPermission("trueessentials.teleport")) {
            sender.sendMessage(Util.error("You do not have permission to run this command."));
            return true;
        }

        if (!(sender instanceof Player player)) {
            sender.sendMessage(Util.error("This command can only be run by players."));
            return true;
        }

        boolean contained = Util.TELEPORTS.containsValue(player);

        if (!contained) {
            player.sendMessage(Util.error("You have no pending teleportation requests."));
            return true;
        }

        Player target = null;
        for (Player p : Util.TELEPORTS.keySet()) {
            if (Util.TELEPORTS.get(p).equals(player)) {
                target = Util.TELEPORTS.get(p);
            }
        }

        if (target == null) {
            player.sendMessage(Util.error("An error occurred when processing your teleportation request."));
            return true;
        }

        target.sendMessage(Util.message(player.getName() + " has accepted your teleportation request."));
        player.sendMessage(Util.message("You have accepted " + Util.formatName(target) + " teleportation request."));

        target.teleport(player);

        Util.TELEPORTS.remove(target);

        return true;
    }
}
