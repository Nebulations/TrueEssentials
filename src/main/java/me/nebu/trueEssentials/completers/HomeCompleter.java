package me.nebu.trueEssentials.completers;

import me.nebu.trueEssentials.Util;
import me.nebu.trueEssentials.cmds.homes.Home;
import me.nebu.trueEssentials.playerdata.PlayerData;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class HomeCompleter implements TabCompleter {

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args) {
        List<String> completer = new ArrayList<>();

        if (!(sender instanceof Player player)) return completer;

        PlayerData playerData = PlayerData.of(player);

        if (args.length == 1) {
            String param = args[0];

            for (Home home : playerData.getHomes()) {
                Util.addIfMatches(param, home.name(), completer);
            }
        }

        return completer;
    }
}
