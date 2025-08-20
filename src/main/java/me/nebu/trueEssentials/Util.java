package me.nebu.trueEssentials;

import me.clip.placeholderapi.PlaceholderAPI;
import me.nebu.trueEssentials.events.ChatHandler;
import me.nebu.trueEssentials.events.CommandManagerEvents;
import me.nebu.trueEssentials.events.JoinAndQuitEvent;
import me.nebu.trueEssentials.extensions.*;
import me.nebu.trueEssentials.util.JoinUtil;
import me.nebu.trueEssentials.util.SpawnUtil;
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
    public static SpawnUtil SPAWNUTIL;
    public static JoinUtil JOINUTIL;
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
    }

    public static void loadExtensions() {
        TrueEssentials instance = TrueEssentials.getInstance();
        FileConfiguration config = instance.getConfig();
        Logger logger = instance.getLogger();

        boolean joinQuitMessagesEnabled = config.getBoolean("settings.extensions.join-quit-messages.enabled");
        extensions.add(new BaseExtension(ExtensionName.JOIN_QUIT_MESSAGES, joinQuitMessagesEnabled));

        if (joinQuitMessagesEnabled) {
            logger.info("Loading extension: Join and quit messages");

            String join = Objects.requireNonNull(config.getString("settings.extensions.join-quit-messages.join-message"));
            String quit = Objects.requireNonNull(config.getString("settings.extensions.join-quit-messages.quit-message"));
            String newJoin = Objects.requireNonNull(config.getString("settings.extensions.join-quit-messages.new-message"));

            instance.getServer().getPluginManager().registerEvents(new JoinAndQuitEvent(join, newJoin, quit), instance);
        }

        boolean chatFormat = config.getBoolean("settings.extensions.chat-format.enabled");
        extensions.add(new BaseExtension(ExtensionName.CHAT_FORMAT, chatFormat));

        if (chatFormat) {
            logger.info("Loading extension: Chat format");

            String format = config.getString("settings.extensions.chat-format.format");
            instance.getServer().getPluginManager().registerEvents(new ChatHandler(format), instance);
        }

        boolean spawnEnabled = config.getBoolean("settings.extensions.spawn.enabled");
        extensions.add(new BaseExtension(ExtensionName.SPAWN, spawnEnabled));

        if (spawnEnabled) {
            logger.info("Loading extension: Spawn");


            SPAWNUTIL = new SpawnUtil(
                    config.getBoolean("settings.extensions.spawn.sound"),
                    config.getString("settings.extensions.spawn.sound-id"),
                    (float) config.getDouble("settings.extensions.spawn.pitch"),
                    (float) config.getDouble("settings.extensions.spawn.volume"),
                    config.getInt("settings.extensions.spawn.time"),
                    config.getBoolean("settings.extensions.spawn.cancel-on-damage"),
                    config.getBoolean("settings.extensions.spawn.cancel-on-move")
            );
        }

        boolean splashscreenEnabled = config.getBoolean("settings.extensions.splashscreen.enabled");
        extensions.add(new BaseExtension(ExtensionName.SPLASHSCREEN, splashscreenEnabled));

        if (splashscreenEnabled) {
            logger.info("Loading extension: Splash screen");

            JOINUTIL = new JoinUtil(
                    config.getBoolean("settings.extensions.splashscreen.sound"),
                    config.getString("settings.extensions.splashscreen.sound-id"),
                    (float) config.getDouble("settings.extensions.splashscreen.pitch"),
                    (float) config.getDouble("settings.extensions.splashscreen.volume"),
                    config.getDouble("settings.extensions.splashscreen.time"),
                    config.getDouble("settings.extensions.splashscreen.fade-in"),
                    config.getDouble("settings.extensions.splashscreen.fade-out"),
                    config.getString("settings.extensions.splashscreen.title"),
                    config.getString("settings.extensions.splashscreen.subtitle")
            );
        }

        boolean homesEnabled = config.getBoolean("settings.extensions.homes.enabled");
        extensions.add(new BaseExtension(ExtensionName.HOMES, homesEnabled));

        if (homesEnabled) { logger.info("Loading extension: Homes"); }

        boolean commandManagerEnabled = config.getBoolean("settings.extensions.command-manager.enabled");
        extensions.add(new CommandManagerExtension(ExtensionName.COMMAND_MANAGER, commandManagerEnabled, config));

        if (commandManagerEnabled) {
            logger.info("Loading extension: Command Manager");
            instance.getServer().getPluginManager().registerEvents(new CommandManagerEvents(), instance);
        }

        extensions.add(new ModerationExtension(ExtensionName.MODERATION));

        /*boolean itemEditingEnabled = config.getBoolean("settings.extensions.item-editing.enabled");
        extensions.add(new Extension(ExtensionName.ITEM_EDITING, itemEditingEnabled));

        if (itemEditingEnabled) {
            logger.info("Loading extension: Item editing");
        }*/
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
