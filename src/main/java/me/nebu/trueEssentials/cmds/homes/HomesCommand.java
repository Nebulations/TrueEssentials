package me.nebu.trueEssentials.cmds.homes;

import me.nebu.trueEssentials.Util;
import me.nebu.trueEssentials.completers.BasicCompleters;
import me.nebu.trueEssentials.playerdata.PlayerData;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class HomesCommand extends Command {

    public HomesCommand() {
        super("homes");
    }

    @Override
    public boolean execute(@NotNull CommandSender sender, @NotNull String label, @NotNull String[] args) {
        if (!sender.hasPermission("trueessentials.homes")) {
            sender.sendMessage(Util.error("You do not have permission to run this command."));
            return true;
        }

        if (!(sender instanceof Player player)) {
            sender.sendMessage(Util.error("This command can only be run by players."));
            return true;
        }

        PlayerData playerData = PlayerData.of(player);

        if (playerData.getHomes().isEmpty()) {
            sender.sendMessage(Util.message("You have no homes created."));
            return true;
        }

        StringBuilder homes = new StringBuilder();

        for (Home home : playerData.getHomes()) {
            homes.append(home.name()).append(", ");
        }

        String homesText = homes.substring(0, homes.length() - 2);
        sender.sendMessage(Util.message("Your homes: " + homesText));

        return true;
    }

    @Override
    public @NotNull List<String> tabComplete(@NotNull CommandSender sender, @NotNull String alias, @NotNull String[] args) throws IllegalArgumentException {
        return BasicCompleters.homesCompleter(sender, args);
    }
}
