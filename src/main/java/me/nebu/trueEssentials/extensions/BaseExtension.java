package me.nebu.trueEssentials.extensions;

import me.nebu.trueEssentials.TrueEssentials;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

public class BaseExtension implements Extension {
    private final ExtensionName id;
    private boolean enabled;
    private final String configPath;

    public BaseExtension(ExtensionName id, String configPath) {
        this.id = id;
        this.configPath = configPath;
        reload();
    }

    @Override
    public void reload() {
        FileConfiguration config = TrueEssentials.getInstance().getConfig();
        ConfigurationSection s = config.getConfigurationSection("settings.extensions." + configPath);

        TrueEssentials instance = TrueEssentials.getInstance();

        if (s == null) {
            instance.getLogger().warning("An error occurred when initializing an extension: " + id());
            disable();
            return;
        }

        this.enabled = s.getBoolean("enabled");

        if (enabled) TrueEssentials.getInstance().getLogger().info("Loaded extension: " + id());
    }
    @Override
    public ExtensionName id() { return id; }
    @Override
    public boolean enabled() { return enabled; }
    @Override
    public void enable() { this.enabled = true; }
    @Override
    public void disable() { this.enabled = false; }
}
