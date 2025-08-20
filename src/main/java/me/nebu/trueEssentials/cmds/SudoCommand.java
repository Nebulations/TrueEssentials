package me.nebu.trueEssentials.cmds;

import me.nebu.trueEssentials.Util;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class SudoCommand implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args) {
        if (!sender.hasPermission("trueessentials.sudo")) {
            sender.sendMessage(Util.error("You do not have permission to run this command."));
            return true;
        }

        if (args.length == 0) {
            sender.sendMessage(Util.error("No player provided."));
            return true;
        }

        Player target = Bukkit.getPlayer(args[0]);

        if (target == null) {
            sender.sendMessage(Util.error("The player provided does not exist."));
            return true;
        }

        if (args.length == 1) {
            sender.sendMessage(Util.error("No command provided."));
            return true;
        }

        StringBuilder targetCmdBuilder = new StringBuilder();
        for (int i = 1; i < args.length; i++) {
            targetCmdBuilder.append(args[i]).append(" ");
        }

        String targetCmd = targetCmdBuilder.substring(0, targetCmdBuilder.length() - 1);

        target.performCommand(targetCmd);
        sender.sendMessage(Util.message("Forced " + target.getName() + " to run command: /" + targetCmd));

        return true;
    }
}
