package me.nebu.trueEssentials.extensions;

import me.nebu.trueEssentials.TrueEssentials;
import me.nebu.trueEssentials.events.JoinAndQuitEvent;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

public class JoinExtension implements Extension {

    private boolean enabled;
    public String joinMessage;
    public String newJoinMessage;
    public String quitMessage;

    public JoinExtension() {
        reload();
    }

    @Override
    public void reload() {
        FileConfiguration config = TrueEssentials.getInstance().getConfig();
        ConfigurationSection s = config.getConfigurationSection("settings.extensions.join-quit-messages");

        TrueEssentials instance = TrueEssentials.getInstance();

        if (s == null) {
            instance.getLogger().warning("An error occurred when initializing an extension: " + ExtensionName.JOIN_QUIT_MESSAGES);
            disable();
            return;
        }

        this.enabled = s.getBoolean("enabled");
        this.joinMessage = s.getString("join-message");
        this.newJoinMessage = s.getString("new-message");
        this.quitMessage = s.getString("quit-message");

        if (enabled) TrueEssentials.getInstance().getLogger().info("Loaded extension: " + id());
    }

    @Override
    public ExtensionName id() { return ExtensionName.JOIN_QUIT_MESSAGES; }
    @Override
    public boolean enabled() { return enabled; }
    @Override
    public void enable() { this.enabled = true; }
    @Override
    public void disable() { this.enabled = false; }
}
