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

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class WarnCommand extends Command {

    public WarnCommand() {
        super("warn");
    }

    @Override
    public boolean execute(@NotNull CommandSender sender, @NotNull String label, @NotNull String[] args) {
        if (!sender.hasPermission("trueessentials.warn")) {
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
        long now = System.currentTimeMillis();

        String formattedDate = Util.formatDate(now);

        playerData.warn(reason, sender, now);

        if (target.isOnline()) {
            ((Player) target).kick(extension.formatWarnScreen(reason, sender, formattedDate));
        }

        sender.sendMessage(Util.message("Player " + target.getName() + " has been warned."));

        return true;
    }

    @Override
    public @NotNull List<String> tabComplete(@NotNull CommandSender sender, @NotNull String alias, @NotNull String[] args) throws IllegalArgumentException {
        List<String> completer = new ArrayList<>();

        if (args.length == 1) {
            for (Player player : Bukkit.getOnlinePlayers()) {
                Util.addIfMatches(args[0], player.getName(), completer);
            }
        }

        if (args.length == 2) {
            for (String reason : ((ModerationExtension) Util.getExtension(ExtensionName.MODERATION)).getRawReasons()) {
                Util.addIfMatches(args[1], reason, completer);
            }
        }

        return completer;
    }
}
