package me.nebu.trueEssentials.cmds.moderation;

import me.nebu.trueEssentials.Util;
import me.nebu.trueEssentials.extensions.ExtensionName;
import me.nebu.trueEssentials.extensions.ModerationExtension;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class ModCompleter implements TabCompleter {

    private final ModerationExtension extension = (ModerationExtension) Util.getExtension(ExtensionName.MODERATION);

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args) {
        List<String> completer = new ArrayList<>();

        if (args.length == 1) {
            for (Player player : Bukkit.getOnlinePlayers()) {
                Util.addIfMatches(args[0], player.getName(), completer);
            }
        }

        if (args.length == 2) {
            for (String reason : extension.getRawReasons()) {
                Util.addIfMatches(args[1], reason, completer);
            }
        }

        return completer;
    }
}
