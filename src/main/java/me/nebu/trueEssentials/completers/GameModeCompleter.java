package me.nebu.trueEssentials.completers;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class GameModeCompleter implements TabCompleter {

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args) {
        List<String> completer = new ArrayList<>();

        if (args.length == 1) {
            String completed = args[0];

            for (GameMode gameMode : GameMode.values()) {
                if (gameMode.name().toLowerCase().startsWith(completed)) {
                    completer.add(gameMode.name().toLowerCase());
                }
            }

        }

        if (args.length == 2) {
            String playerName = args[1];

            for (Player player : Bukkit.getOnlinePlayers()) {
                if (player.getName().toLowerCase().startsWith(playerName.toLowerCase())) {
                    completer.add(player.getName());
                }
            }
        }

        return completer;
    }
}
