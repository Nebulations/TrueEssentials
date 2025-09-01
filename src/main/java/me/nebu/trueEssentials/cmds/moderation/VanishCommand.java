package me.nebu.trueEssentials.cmds.moderation;

import me.nebu.trueEssentials.Util;
import me.nebu.trueEssentials.extensions.ExtensionName;
import me.nebu.trueEssentials.extensions.JoinExtension;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class VanishCommand extends Command {

    public VanishCommand() {
        super("vanish");
    }

    @Override
    public boolean execute(@NotNull CommandSender sender, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage(Util.error("This command can only be run by players."));
            return true;
        }

        if (!player.hasPermission("trueessentials.vanish")) {
            player.sendMessage(Util.error("You do not have permission to run this command."));
            return true;
        }

        JoinExtension joinExtension = (JoinExtension) Util.getExtension(ExtensionName.JOIN_QUIT_MESSAGES);
        assert joinExtension != null;

        boolean invis = player.isInvisible();
        player.sendMessage(Util.message(invis ? "You are no longer vanished." : "You are now vanished."));
        player.setInvisible(!invis);
        player.setGameMode(invis ? GameMode.CREATIVE : GameMode.SPECTATOR);

        var x = invis ? Util.getVanishedPlayers().remove(player) : Util.getVanishedPlayers().add(player);
        Util.updateVanished();

        if (!joinExtension.enabled()) {
            String state = invis ? "joined" : "left";
            Bukkit.broadcast(Component.text(player.getName() + " has " + state + " the game").color(NamedTextColor.YELLOW));
        } else {
            String msg = invis ? joinExtension.joinMessage : joinExtension.quitMessage;
            Bukkit.broadcast(Util.colorize(Util.formatPlaceholders(player, msg)));
        }

        return true;
    }

    @Override
    public @NotNull List<String> tabComplete(@NotNull CommandSender sender, @NotNull String alias, @NotNull String[] args) throws IllegalArgumentException {
        return new ArrayList<>();
    }
}
