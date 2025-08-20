package me.nebu.trueEssentials.cmds.utility;

import me.nebu.trueEssentials.Util;
import org.bukkit.Bukkit;
import org.bukkit.attribute.Attribute;
import org.bukkit.command.CommandSender;
import org.bukkit.command.defaults.BukkitCommand;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class FeedCommand extends BukkitCommand {

    public FeedCommand(String name) {
        super(name);
        this.description = "Feed yourself or another player";
        this.usageMessage = "/feed [<player>]";
    }

    @Override
    public boolean execute(@NotNull CommandSender sender, @NotNull String cmd, @NotNull String[] args) {
        if (!sender.hasPermission("trueessentials.feed")) {
            sender.sendMessage(Util.error("You do not have permission to run this command."));
            return true;
        }

        if (args.length == 0) {

            if (!(sender instanceof Player player)) {
                sender.sendMessage(Util.error("A player must be provided in order to run this command here."));
                return true;
            }

            player.setHealth(Objects.requireNonNull(player.getAttribute(Attribute.GENERIC_MAX_HEALTH)).getValue());

            player.sendMessage(Util.message("You have been fed."));

            return true;
        }

        if (!sender.hasPermission("trueessentials.others.feed")) {
            sender.sendMessage(Util.message("You can only feed yourself."));
            return true;
        }

        Player target = Bukkit.getPlayer(args[0]);

        if (target == null) {
            sender.sendMessage(Util.error("The player provided is invalid."));
            return true;
        }

        target.setHealth(Objects.requireNonNull(target.getAttribute(Attribute.GENERIC_MAX_HEALTH)).getValue());
        target.sendMessage(Util.message("You have been fed."));
        sender.sendMessage(Util.message("You have fed " + target.getName() + "."));

        return true;
    }
}
