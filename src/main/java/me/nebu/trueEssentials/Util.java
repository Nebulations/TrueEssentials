package me.nebu.trueEssentials;

import me.clip.placeholderapi.PlaceholderAPI;
import me.nebu.trueEssentials.events.CommandManagerEvents;
import me.nebu.trueEssentials.extensions.*;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
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
    }

    public static void loadExtensions() {
        extensions.add(new JoinExtension());
        extensions.add(new ChatFormatExtension());
        extensions.add(new SpawnExtension());
        extensions.add(new SplashScreenExtension());
        extensions.add(new BaseExtension(ExtensionName.HOMES, "homes"));
        extensions.add(new CommandManagerExtension());
        extensions.add(new ModerationExtension());
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
            completer.add(query.toLowerCase());
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
