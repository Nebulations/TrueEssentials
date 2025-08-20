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

public class BanCommand implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args) {
        if (!sender.hasPermission("trueessentials.ban")) {
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

        if (args.length == 1) {
            sender.sendMessage(Util.error("No reason provided."));
            return true;
        }

        StringBuilder sb = new StringBuilder();
        for (String param : args) {
            sb.append(param).append(" ");
        }

        String reason = sb.substring(0, sb.length() - 1)
                .substring((target.getName() + " ").length(), sb.length() - 1);

        ModerationExtension extension = (ModerationExtension) Util.getExtension(ExtensionName.MODERATION);
        assert extension != null;

        if (!extension.isReasonValid(reason)) {
            sender.sendMessage(Util.error("The reason provided is invalid."));
            return true;
        }

        PlayerData playerData = PlayerData.of(target);
        playerData.ban(reason, ((sender instanceof Player player) ? player.getUniqueId().toString() : "Console"));

        if (target.isOnline()) {
            //((Player) target).kick(extension.formatBanScreen(playerData));
        }

        sender.sendMessage(Util.message("Player " + target.getName() + " has been banned."));

        return true;
    }
}
