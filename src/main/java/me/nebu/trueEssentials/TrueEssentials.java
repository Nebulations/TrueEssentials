package me.nebu.trueEssentials;

import me.nebu.trueEssentials.cmds.*;
import me.nebu.trueEssentials.cmds.moderation.*;
import me.nebu.trueEssentials.completers.*;
import me.nebu.trueEssentials.events.JoinAndQuitEvent;
import org.bukkit.Bukkit;
import org.bukkit.command.*;
import org.bukkit.event.Listener;
import org.bukkit.permissions.Permission;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.*;

public final class TrueEssentials extends JavaPlugin {

    private static TrueEssentials instance;
    public static boolean PAPI = true;

    @Override
    public void onEnable() {
        getLogger().info("Initializing plugin");
        TrueEssentials.instance = this;

        Metrics metrics = new Metrics(this, 27001);

        saveDefaultConfig();

        Util.load();

        if (!getServer().getPluginManager().isPluginEnabled("PlaceholderAPI")) {
            getLogger().warning("Failed to load PlaceholderAPI support: The plugin is not present or is not enabled! Falling back to basic placeholder support.");
            PAPI = false;
        } else {
            getLogger().info("PlaceholderAPI detected: Using PAPI placeholders");
        }

        getLogger().info("Loading default commands");
        reg(null, "trueessentials", new TrueEssentialsCommand(), new TrueEssentialsCompleter(), "trueessentials");

        reg(null, "inventorysee", new InventorySeeCommand(), new PlayerCompleter(), "inventorysee");
        reg(null, "enderchest", new EnderchestCommand(), new PlayerCompleter(), "enderchest", "others.enderchest");
        reg(null, "flight", new FlightCommand(), new PlayerCompleter(), "flight", "others.ping");
        reg(null, "kickall", new KickallCommand(), new EmptyCompleter(), "staff.kickall", "exempt.kickall");
        reg(null, "ping", new PingCommand(), new PlayerCompleter(), "ping", "others.ping");
        reg(null, "sudo", new SudoCommand(), new PlayerCompleter(), "sudo");
        reg(null, "trash", new TrashCommand(), new EmptyCompleter(), "trash");
        reg(null, "clearchat", new ClearchatCommand(), new EmptyCompleter(), "staff.clearchat", "exempt.clearchat");

        reg(null, "tpask", new TpaskCommand(), new PlayerCompleter(), "teleport");
        reg(null, "tpaccept", new TpacceptCommand(), new EmptyCompleter());

        reg(null, "gamemode", new GameModeCommand(), new GameModeCompleter(), "gamemode", "others.gamemode");
        reg(null, "gmc", new GameModeShortcutCommands(), new PlayerCompleter());
        reg(null, "gms", new GameModeShortcutCommands(), new PlayerCompleter());
        reg(null, "gma", new GameModeShortcutCommands(), new PlayerCompleter());
        reg(null, "gmsp", new GameModeShortcutCommands(), new PlayerCompleter());

        getLogger().info("Loading extensions");
        Util.loadExtensions();

        getLogger().info("Registering extension related listeners");
        bulkReg(
                new JoinAndQuitEvent(),
                new JoinListener(),
                new ModlogGUIListener()
        );

        getLogger().info("Successfully initialized");
    }

    @Override
    public void onDisable() {
        getLogger().info("Successfully shut down");
    }

    private void bulkReg(Listener ... listeners) {
        Arrays.asList(listeners).forEach(listener -> getServer().getPluginManager().registerEvents(listener, this));
    }

    public void reg(String configPath, String cmd, CommandExecutor executor, TabCompleter completer, String ... permissions) {
        PluginManager manager = getServer().getPluginManager();

        boolean enabled = configPath == null || getConfig().getBoolean("settings.extensions." + configPath + ".enabled");

        if (enabled) {

            for (String permission : permissions) {
                Permission perm = new Permission("trueessentials." + permission);

                if (manager.getPermission(perm.getName()) == null) manager.addPermission(perm);
            }

            TabCompleter finalCompleter = completer == null ? new EmptyCompleter() : completer;

            Objects.requireNonNull(getCommand(cmd)).setExecutor(executor);
            Objects.requireNonNull(getCommand(cmd)).setTabCompleter(finalCompleter);

            if (configPath != null) {
                Util.getCommands().put(cmd, new CommandUtil(cmd, executor, finalCompleter, permissions));
            }
        } else {
            SimpleCommandMap map = (SimpleCommandMap) Bukkit.getCommandMap();

            Objects.requireNonNull(getCommand(cmd)).unregister(map);
            map.getKnownCommands().remove(cmd, getCommand(cmd));
        }
    }

    public static TrueEssentials getInstance() {
        return instance;
    }
}
