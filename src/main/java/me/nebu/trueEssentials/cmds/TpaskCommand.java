package me.nebu.trueEssentials.cmds;

import me.nebu.trueEssentials.TrueEssentials;
import me.nebu.trueEssentials.Util;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;

public class TpaskCommand implements CommandExecutor {

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

        if (args.length == 0) {
            player.sendMessage(Util.error("No player provided."));
            return true;
        }

        Player target = Bukkit.getPlayer(args[0]);

        if (target == null) {
            player.sendMessage(Util.error("The player provided does not exist."));
            return true;
        }

        if (Util.TELEPORTS.containsKey(player)) {
            player.sendMessage(Util.error("You have already sent a teleportation request to " + Util.TELEPORTS.get(player).getName() + "."));
            return true;
        }

        boolean contained = Util.TELEPORTS.containsValue(target);

        if (contained) {
            player.sendMessage(Util.error("This player has already received a teleportation request."));
            return true;
        }

        player.sendMessage(Util.message("Sent teleportation request to " + target.getName() + "."));
        target.sendMessage(Util.message("You have received a teleportation request from " + player.getName() + ". You have 30 seconds to accept it by using /tpaccept."));

        Util.TELEPORTS.put(player, target);

        new BukkitRunnable() {
            @Override
            public void run() {
                if (Util.TELEPORTS.get(player) != null) {
                    Util.TELEPORTS.remove(player);
                    player.sendMessage(Util.error("Your teleportation request has expired."));
                }
            }
        }.runTaskLater(TrueEssentials.getInstance(), 600L);

        return true;
    }
}
