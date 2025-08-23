package me.nebu.trueEssentials.extensions;

import me.nebu.trueEssentials.TrueEssentials;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

public class SpawnExtension implements Extension {

    private boolean enabled;

    public boolean soundAfterTeleport;
    public String soundId;
    public float pitch;
    public float volume;
    public int time;
    public boolean cancelOnDamage;
    public boolean cancelOnMove;

    public SpawnExtension() {
        reload();
    }

    @Override
    public ExtensionName id() { return ExtensionName.SPAWN; }
    @Override
    public boolean enabled() { return enabled; }
    @Override
    public void enable() { enabled = true; }
    @Override
    public void disable() { enabled = false; }

    @Override
    public void reload() {
        FileConfiguration config = TrueEssentials.getInstance().getConfig();
        ConfigurationSection s = config.getConfigurationSection("settings.extensions.spawn");
        if (s == null) {
            TrueEssentials.getInstance().getLogger().warning("An error occurred when initializing an extension: " + ExtensionName.SPAWN);
            disable();
            return;
        }

        this.enabled = s.getBoolean("enabled");
        this.soundAfterTeleport = s.getBoolean("sound");
        this.soundId = s.getString("sound-id");
        this.pitch = (float) s.getDouble("pitch");
        this.volume = (float) s.getDouble("volume");
        this.time = s.getInt("time");
        this.cancelOnDamage = s.getBoolean("cancel-on-damage");
        this.cancelOnMove = s.getBoolean("cancel-on-move");

        if (enabled) TrueEssentials.getInstance().getLogger().info("Loaded extension: " + id());
    }
}
