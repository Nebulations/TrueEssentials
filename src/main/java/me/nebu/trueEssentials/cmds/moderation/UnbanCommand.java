package me.nebu.trueEssentials.cmds.moderation;

import me.nebu.trueEssentials.Util;
import me.nebu.trueEssentials.extensions.ExtensionName;
import me.nebu.trueEssentials.extensions.ModerationExtension;
import me.nebu.trueEssentials.playerdata.PlayerData;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class UnbanCommand implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args) {
        if (!sender.hasPermission("trueessentials.unban")) {
            sender.sendMessage(Util.error("You do not have permission to run this command."));
            return true;
        }

        if (args.length == 0) {
            sender.sendMessage(Util.error("No player provided."));
            return true;
        }

        String name = args[0];
        OfflinePlayer target = Bukkit.getOfflinePlayer(name);

        if (!target.hasPlayedBefore()) {
            sender.sendMessage(Util.error("This player does not exist or has not played before."));
            return true;
        }

        PlayerData data = PlayerData.of(target);
        if (!data.isBanned()) {
            sender.sendMessage(Util.error("This player is not banned."));
        }

        data.unban();

        sender.sendMessage(Util.message("Player " + target.getName() + " has been unbanned."));

        return true;
    }
}
