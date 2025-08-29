package me.nebu.trueEssentials.cmds;

import me.nebu.trueEssentials.TrueEssentials;
import me.nebu.trueEssentials.Util;
import me.nebu.trueEssentials.extensions.Extension;
import me.nebu.trueEssentials.extensions.ExtensionName;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public class TrueEssentialsCommand implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args) {
        if (!sender.hasPermission("trueessentials.trueessentials")) {
            sender.sendMessage(Util.error("You do not have permission to run this command."));
            return true;
        }

        if (args.length == 0) {
            sender.sendMessage(Util.error("This command requires at least 1 argument."));
            return true;
        }

        String param = args[0];

        if (param.equalsIgnoreCase("reload")) {
            TrueEssentials.getInstance().reloadConfig();
            Util.load();

            sender.sendMessage(Util.message("Successfully reloaded the configuration file and extensions."));
        } else if (param.equalsIgnoreCase("version")) {
            sender.sendMessage(Util.message("Installed version: " + TrueEssentials.getInstance().getPluginMeta().getVersion()));
        } else if (param.equalsIgnoreCase("extensions")) {
            sender.sendMessage(Component.newline().append(Util.message("Available extensions:")
                    .appendNewline()
                    .append(formatExtension(ExtensionName.JOIN_QUIT_MESSAGES, "Join and quit messages"))
                    .append(formatExtension(ExtensionName.SPAWN, "Spawn"))
                    .append(formatExtension(ExtensionName.SPLASHSCREEN, "Splash screen"))
                    .append(formatExtension(ExtensionName.CHAT_FORMAT, "Chat formatting"))
                    .append(formatExtension(ExtensionName.HOMES, "Homes"))
                    .append(formatExtension(ExtensionName.COMMAND_MANAGER, "Command Manager"))
                    .append(formatExtension(ExtensionName.MODERATION, "Moderation Utilities"))
            ));
        } else {
            sender.sendMessage(Util.error("The provided argument is invalid."));
        }

        return true;
    }

    private Component formatExtension(ExtensionName id, String name) {
        boolean enabled = false;

        for (Extension e : Util.extensions) {
            if (e.id().equals(id)) {
                enabled = e.enabled();
            }
        }

        char icon = enabled ? '✔' : '❌';
        NamedTextColor color = enabled ? NamedTextColor.GREEN : NamedTextColor.RED;

        return Util.secondaryColor
                .append(Component.text("| ").decorate(TextDecoration.BOLD))
                .append(Util.primaryColor.append(Component.text(name + ": ")))
                .append(Component.text(icon).color(color).decorate(TextDecoration.BOLD))
                .appendNewline();
    }
}
