package me.nebu.trueEssentials.completers;

import me.nebu.trueEssentials.Util;
import me.nebu.trueEssentials.cmds.homes.Home;
import me.nebu.trueEssentials.playerdata.PlayerData;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class BasicCompleters {

    public static List<String> defaultCompleter() {
        return new ArrayList<>();
    }

    public static List<String> playerCompleter(String[] args) {
        List<String> completer = new ArrayList<>();

        String playerName = args[0];

        if (args.length != 1) {
            return completer;
        }

        for (Player player : Bukkit.getOnlinePlayers()) {
            if (player.getName().toLowerCase().startsWith(playerName.toLowerCase())) {
                completer.add(player.getName());
            }
        }

        return completer;
    }

    public static List<String> homesCompleter(CommandSender sender, String[] args) {
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
