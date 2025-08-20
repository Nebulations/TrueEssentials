package me.nebu.trueEssentials.cmds;

import me.nebu.trueEssentials.Util;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class GameModeCommand implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args) {
        if (!sender.hasPermission("trueessentials.gamemode")) {
            sender.sendMessage(Util.error("You do not have permission to run this command."));
            return true;
        }

        if (args.length == 0) {
            sender.sendMessage(Util.error("No game mode provided."));
            return true;
        }

        GameMode gameMode;

        try {
            gameMode = GameMode.valueOf(args[0].toUpperCase());
        } catch (Exception e) {
            sender.sendMessage(Util.error("Invalid game mode."));
            return true;
        }

        if (args.length > 1) {

            if (!sender.hasPermission("trueessentials.others.gamemode")) {
                sender.sendMessage(Util.error("You can only change your own game mode."));
                return true;
            }

            Player target = Bukkit.getPlayer(args[1]);

            if (target == null) {
                sender.sendMessage(Util.error("The player provided does not exist."));
                return true;
            }

            target.setGameMode(gameMode);
            sender.sendMessage(Util.message("Updated " + Util.formatName(target) + " game mode to " + gameMode.name().toLowerCase() + "."));
            target.sendMessage(Util.message("Your game mode has been updated to " + gameMode.name().toLowerCase() + "."));
        } else {
            if (!(sender instanceof Player player)) {
                sender.sendMessage(Util.error("This command requires a player to run here."));
                return true;
            }

            player.setGameMode(gameMode);
            player.sendMessage(Util.message("Your game mode has been updated to " + gameMode.name().toLowerCase() + "."));
        }

        return true;
    }
}
