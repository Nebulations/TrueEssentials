package me.nebu.trueEssentials.completers;

import me.nebu.trueEssentials.Util;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class TrueEssentialsCompleter implements TabCompleter {

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args) {
        List<String> completer = new ArrayList<>();

        if (args.length == 1) {
            String param = args[0];

            Util.addIfMatches(param, "reload", completer);
            Util.addIfMatches(param, "version", completer);
            Util.addIfMatches(param, "extensions", completer);
        }

        return completer;
    }
}
