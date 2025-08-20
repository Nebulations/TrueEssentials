package me.nebu.trueEssentials;

import me.nebu.trueEssentials.cmds.*;
import me.nebu.trueEssentials.cmds.homes.DelHomeCommand;
import me.nebu.trueEssentials.cmds.homes.HomeCommand;
import me.nebu.trueEssentials.cmds.homes.HomesCommand;
import me.nebu.trueEssentials.cmds.homes.SetHomeCommand;
import me.nebu.trueEssentials.cmds.itemediting.LoreCommand;
import me.nebu.trueEssentials.cmds.moderation.*;
import me.nebu.trueEssentials.completers.*;
import org.bukkit.Bukkit;
import org.bukkit.command.*;
import org.bukkit.command.defaults.BukkitCommand;
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

        Util.load();
        saveDefaultConfig();

        if (!getServer().getPluginManager().isPluginEnabled("PlaceholderAPI")) {
            getLogger().warning("Failed to load PlaceholderAPI support: The plugin is not present or is not enabled! Falling back to basic placeholder support.");
            PAPI = false;
        } else {
            getLogger().info("PlaceholderAPI detected: Using PAPI placeholders");
        }


        getLogger().info("Loading extensions");
        Util.loadExtensions();

        getLogger().info("Registering commands");
        reg("trueessentials", new TrueEssentialsCommand(), new TrueEssentialsCompleter(), "trueessentials");

        reg("inventorysee", new InventorySeeCommand(), new PlayerCompleter(), "inventorysee");
        reg("enderchest", new EnderchestCommand(), new PlayerCompleter(), "enderchest", "others.enderchest");
        reg("flight", new FlightCommand(), new PlayerCompleter(), "flight", "others.ping");
        reg("kickall", new KickallCommand(), new EmptyCompleter(), "staff.kickall", "exempt.kickall");
        reg("ping", new PingCommand(), new PlayerCompleter(), "ping", "others.ping");
        reg("sudo", new SudoCommand(), new PlayerCompleter(), "sudo");
        reg("trash", new TrashCommand(), new EmptyCompleter(), "trash");
        reg("clearchat", new ClearchatCommand(), new EmptyCompleter(), "staff.clearchat", "exempt.clearchat");

        reg("tpask", new TpaskCommand(), new PlayerCompleter(), "teleport");
        reg("tpaccept", new TpacceptCommand(), new EmptyCompleter());

        reg("gamemode", new GameModeCommand(), new GameModeCompleter(), "gamemode", "others.gamemode");
        reg("gmc", new GameModeShortcutCommands(), new PlayerCompleter());
        reg("gms", new GameModeShortcutCommands(), new PlayerCompleter());
        reg("gma", new GameModeShortcutCommands(), new PlayerCompleter());
        reg("gmsp", new GameModeShortcutCommands(), new PlayerCompleter());


        getLogger().info("Registering extension related commands");

        // Commands from extensions
        if (Util.SPAWNUTIL != null) {
            reg("spawn", new SpawnCommand(), new EmptyCompleter());
            reg("setspawn", new SetspawnCommand(), new EmptyCompleter());
        } else {
            unreg("spawn", "setspawn");
        }

        if (getConfig().getBoolean("settings.extensions.homes.enabled")) {
            reg("delhome", new DelHomeCommand(), new HomeCompleter());
            reg("homes", new HomesCommand(), new EmptyCompleter());
            reg("sethome", new SetHomeCommand(), new EmptyCompleter());
            reg("home", new HomeCommand(), new HomeCompleter());
        } else {
            unreg("delhome", "homes", "sethome", "home");
        }

        if (getConfig().getBoolean("settings.extensions.moderation.enabled")) {
            reg("ban", new BanCommand(), new ModCompleter());
            reg("warn", new WarnCommand(), new ModCompleter());
            reg("modlogs", new ModlogsCommand(), new PlayerCompleter());

            getServer().getPluginManager().registerEvents(new JoinListener(), this);
            getServer().getPluginManager().registerEvents(new ModlogGUIListener(), this);
        } else {
            unreg("ban", "warn", "modlogs");
        }

        /*if (getConfig().getBoolean("settings.extensions.item-editing.enabled")) {
            reg("lore", new LoreCommand(), new LoreCompleter());
        } else {
            unreg("lore");
        }*/

        getLogger().info("Successfully initialized");
    }

    @Override
    public void onDisable() {
        getLogger().info("Successfully shut down");
    }

    private void reg(String cmd, CommandExecutor executor, TabCompleter completer, String ... permissions) {
        PluginManager manager = getServer().getPluginManager();

        for (String permission : permissions) {
            Permission perm = new Permission("trueessentials." + permission);

            if (manager.getPermission(perm.getName()) == null) {
                manager.addPermission(perm);
            }
        }

        Objects.requireNonNull(getCommand(cmd)).setExecutor(executor);
        Objects.requireNonNull(getCommand(cmd)).setTabCompleter(completer == null ? new EmptyCompleter() : completer);
    }

    private void unreg(String ... cmds) {
        SimpleCommandMap map = (SimpleCommandMap) Bukkit.getCommandMap();

        for (String cmd : cmds) {
            Objects.requireNonNull(getCommand(cmd)).unregister(map);
            map.getKnownCommands().remove(cmd);
        }
    }

    public static TrueEssentials getInstance() {
        return instance;
    }
}
