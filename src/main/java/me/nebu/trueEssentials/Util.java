package me.nebu.trueEssentials;

import me.clip.placeholderapi.PlaceholderAPI;
import me.nebu.trueEssentials.cmds.SetspawnCommand;
import me.nebu.trueEssentials.cmds.SpawnCommand;
import me.nebu.trueEssentials.cmds.homes.DelHomeCommand;
import me.nebu.trueEssentials.cmds.homes.HomeCommand;
import me.nebu.trueEssentials.cmds.homes.HomesCommand;
import me.nebu.trueEssentials.cmds.homes.SetHomeCommand;
import me.nebu.trueEssentials.cmds.moderation.*;
import me.nebu.trueEssentials.completers.EmptyCompleter;
import me.nebu.trueEssentials.completers.HomeCompleter;
import me.nebu.trueEssentials.completers.PlayerCompleter;
import me.nebu.trueEssentials.events.CommandManagerEvents;
import me.nebu.trueEssentials.extensions.*;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.PluginCommand;
import org.bukkit.command.SimpleCommandMap;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Util {

    public static Component prefix;
    public static Component primaryColor;
    public static Component secondaryColor;
    public static Component errorColor;
    public static List<Extension> extensions = new ArrayList<>();
    public static HashMap<Player, Player> TELEPORTS = new HashMap<>();

    private static final MiniMessage mm = MiniMessage.miniMessage();
    private static final DateFormat dateFormat = new SimpleDateFormat("MMMMMMMMM dd, yyyy, HH:mm");
    private static final HashMap<String, CommandUtil> commands = new HashMap<>();

    private static FileConfiguration config;

    public static void load() {
        TrueEssentials.getInstance().reloadConfig();
        config = TrueEssentials.getInstance().getConfig();

        // Server messaging configuration
        prefix = Util.colorize((String) getValue("settings.text.prefix"));
        primaryColor = Util.colorize((String) getValue("settings.text.primary-color"));
        secondaryColor = Util.colorize((String) getValue("settings.text.secondary-color"));
        errorColor = Util.colorize((String) getValue("settings.text.error-color"));

        if (extensions.isEmpty()) return;

        extensions.forEach(Extension::reload);
        loadCommands();
    }

    public static void loadExtensions() {
        extensions.add(new JoinExtension());
        extensions.add(new ChatFormatExtension());
        extensions.add(new SpawnExtension());
        extensions.add(new SplashScreenExtension());
        extensions.add(new BaseExtension(ExtensionName.HOMES, "homes"));
        extensions.add(new CommandManagerExtension());
        extensions.add(new ModerationExtension());

        loadCommands();
    }

    private static void loadCommands() {
        TrueEssentials instance = TrueEssentials.getInstance();

        if (Objects.requireNonNull(Util.getExtension(ExtensionName.SPAWN)).enabled()) {
            instance.reg("spawn", "spawn", new SpawnCommand(), new EmptyCompleter());
            instance.reg("spawn", "setspawn", new SetspawnCommand(), new EmptyCompleter());
        } else {
            bulkRemove("spawn", "setspawn");
        }

        if (Objects.requireNonNull(Util.getExtension(ExtensionName.HOMES)).enabled()) {
            instance.reg("homes", "delhome", new DelHomeCommand(), new HomeCompleter());
            instance.reg("homes", "homes", new HomesCommand(), new EmptyCompleter());
            instance.reg("homes", "sethome", new SetHomeCommand(), new EmptyCompleter());
            instance.reg("homes", "home", new HomeCommand(), new HomeCompleter());
        } else {
            bulkRemove("delhome", "homes", "sethome", "home");
        }

        if (Objects.requireNonNull(Util.getExtension(ExtensionName.MODERATION)).enabled()) {
            instance.reg("moderation", "ban", new BanCommand(), new ModCompleter());
            instance.reg("moderation", "warn", new WarnCommand(), new ModCompleter());
            instance.reg("moderation", "modlogs", new ModlogsCommand(), new PlayerCompleter());
            instance.reg("moderation", "unban", new UnbanCommand(), new PlayerCompleter());
        } else {
            bulkRemove("ban", "warn", "modlogs", "unban");
        }
    }

    private static void bulkRemove(String ... commands) {
        SimpleCommandMap map = (SimpleCommandMap) Bukkit.getCommandMap();

        for (String cmd : commands) {
            Objects.requireNonNull(TrueEssentials.getInstance().getCommand(cmd)).unregister(map);
            map.getKnownCommands().remove(cmd);
        }
    }

    private static void restoreVanillaCommands(String ... commands) {
        try {
            for (String cmd : commands) {
                SimpleCommandMap commandMap = (SimpleCommandMap) Bukkit.getServer().getCommandMap();

                Command vanilla = Bukkit.getServer().getCommandMap().getCommand(cmd);

                if (vanilla == null) return;

                commandMap.register("minecraft", vanilla);
            }
        } catch (Exception e) { e.printStackTrace(); }
    }

    public static Component error(String message) {
        return prefix.append(errorColor.append(Component.text(message)));
    }

    public static Component message(String message) {
        return prefix.append(primaryColor.append(Component.text(message)));
    }

    public static String formatName(Player player) {
        String format = player.getName().toLowerCase().endsWith("s") ? "'" : "'s";
        return player.getName() + format;
    }

    public static String formatDate(long time) {
        return dateFormat.format(new Date(time));
    }


    private static Object getValue(String path) {
        Object obj = config.get(path);
        if (obj == null) {
            throw new RuntimeException("Failed to load plugin: Value at path '" + path + "' is invalid.");
        }

        return obj;
    }


    public static HashMap<String, CommandUtil> getCommands() {
        return commands;
    }

    public static long formatDuration(String input) {
        if (input == null || input.isEmpty()) return 0L;

        long totalMillis = 0L;
        Pattern pattern = Pattern.compile("(\\d+)\\s*([dhmsDHMS])");
        Matcher matcher = pattern.matcher(input);

        while (matcher.find()) {
            long value = Long.parseLong(matcher.group(1));
            String unit = matcher.group(2).toLowerCase();

            switch (unit) {
                case "d":
                    totalMillis += value * 24L * 60L * 60L * 1000L;
                    break;
                case "h":
                    totalMillis += value * 60L * 60L * 1000L;
                    break;
                case "m":
                    totalMillis += value * 60L * 1000L;
                    break;
                case "s":
                    totalMillis += value * 1000L;
                    break;
            }
        }

        return totalMillis;
    }


    public static void addIfMatches(String param, String query, List<String> completer) {
        if (query.toLowerCase().startsWith(param.toLowerCase())) {
            completer.add(query);
        }
    }

    public static String formatPlaceholders(Player player, String message) {
        message = message.replace("%player_name%", player.getName())
                .replace("%player_displayname%", PlainTextComponentSerializer.plainText().serialize(player.displayName()));

        return (TrueEssentials.PAPI ? PlaceholderAPI.setPlaceholders(player, message) : message);
    }

    public static Component colorize(String text) {
        Component legacy = LegacyComponentSerializer.legacyAmpersand().deserialize(text);
        return mm.deserialize(mm.serialize(legacy.decoration(TextDecoration.ITALIC, false)).replace("\\", ""));
    }

    public static Extension getExtension(ExtensionName name) {
        for (Extension e : extensions) {
            if (e.id().equals(name)) return e;
        }

        return null;
    }

}
