package me.nebu.trueEssentials.extensions;

import me.nebu.trueEssentials.TrueEssentials;
import me.nebu.trueEssentials.events.ChatHandler;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

public class ChatFormatExtension implements Extension {

    private boolean enabled;
    public String format;

    public ChatFormatExtension() {
        reload();
    }

    @Override
    public void reload() {
        FileConfiguration config = TrueEssentials.getInstance().getConfig();
        ConfigurationSection s = config.getConfigurationSection("settings.extensions.chat-format");

        TrueEssentials instance = TrueEssentials.getInstance();

        if (s == null) {
            instance.getLogger().warning("An error occurred when initializing an extension: " + id());
            disable();
            return;
        }

        this.enabled = s.getBoolean("enabled");
        this.format = s.getString("format");

        if (enabled) {
            instance.getServer().getPluginManager().registerEvents(new ChatHandler(), instance);
        }

        if (enabled) TrueEssentials.getInstance().getLogger().info("Loaded extension: " + id());
    }
    @Override
    public ExtensionName id() { return ExtensionName.CHAT_FORMAT; }
    @Override
    public boolean enabled() { return enabled; }
    @Override
    public void enable() { this.enabled = true; }
    @Override
    public void disable() { this.enabled = false; }
}
