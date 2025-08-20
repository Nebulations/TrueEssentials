package me.nebu.trueEssentials.cmds;

import me.nebu.trueEssentials.Util;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class GameModeShortcutCommands implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage(Util.error("This command can only be run by players."));
            return true;
        }

        String gamemode;

        switch (cmd.getName().toLowerCase()) {
            case "gmc":
                gamemode = "creative";
                break;
            case "gma":
                gamemode = "adventure";
                break;
            case "gms":
                gamemode = "survival";
                break;
            case "gmsp":
                gamemode = "spectator";
                break;
            default: {
                sender.sendMessage(Util.error("An error occurred"));
                return true;
            }
        }

        if (args.length == 0) {
            player.performCommand("gamemode " + gamemode);
        } else {
            player.performCommand("gamemode " + gamemode + " " + args[0]);
        }

        return true;
    }
}
