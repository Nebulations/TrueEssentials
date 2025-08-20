package me.nebu.trueEssentials.extensions;

import org.bukkit.configuration.file.FileConfiguration;

import java.util.*;

public class CommandManagerExtension implements Extension {

    private final ExtensionName id;
    private final boolean enabled;
    private boolean reversed = false;
    private List<String> commands;
    private final FileConfiguration config;

    public CommandManagerExtension(ExtensionName id, boolean enabled, FileConfiguration config) {
        this.id = id;
        this.enabled = enabled;
        this.config = config;

        if (!enabled) return;

        this.reversed = config.getBoolean("settings.extensions.command-manager.reverse");
        this.commands = config.getStringList("settings.extensions.command-manager.commands");
    }

    @Override
    public ExtensionName id() {
        return id;
    }

    @Override
    public boolean enabled() {
        return enabled;
    }

    @Override
    public void reload() {
        if (!enabled) return;

        this.reversed = config.getBoolean("settings.extensions.command-manager.reverse");
        this.commands = config.getStringList("settings.extensions.command-manager.commands");
    }

    public boolean isReversed() {
        return reversed;
    }

    public List<String> getCommands() {
        return commands;
    }
}
