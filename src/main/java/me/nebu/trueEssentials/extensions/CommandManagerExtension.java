package me.nebu.trueEssentials.extensions;

import me.nebu.trueEssentials.TrueEssentials;
import me.nebu.trueEssentials.events.ChatHandler;
import me.nebu.trueEssentials.events.CommandManagerEvents;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.*;

public class CommandManagerExtension implements Extension {

    private boolean enabled;
    private boolean reversed = false;
    private List<String> commands;

    public CommandManagerExtension() {
        reload();
    }

    @Override
    public ExtensionName id() { return ExtensionName.COMMAND_MANAGER; }
    @Override
    public boolean enabled() { return enabled; }
    @Override
    public void enable() { enabled = true; }
    @Override
    public void disable() { enabled = false; }

    @Override
    public void reload() {
        FileConfiguration config = TrueEssentials.getInstance().getConfig();
        ConfigurationSection s = config.getConfigurationSection("settings.extensions.command-manager");

        TrueEssentials instance = TrueEssentials.getInstance();

        if (s == null) {
            instance.getLogger().warning("An error occurred when initializing an extension: " + id());
            disable();
            return;
        }

        this.enabled = s.getBoolean("enabled");
        this.reversed = config.getBoolean("settings.extensions.command-manager.reverse");
        this.commands = config.getStringList("settings.extensions.command-manager.commands");

        if (enabled) {
            instance.getServer().getPluginManager().registerEvents(new CommandManagerEvents(), instance);
        }

        if (enabled) TrueEssentials.getInstance().getLogger().info("Loaded extension: " + id());
    }

    public boolean isReversed() {
        return reversed;
    }

    public List<String> getCommands() {
        return commands;
    }
}
