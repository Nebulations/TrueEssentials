package me.nebu.trueEssentials.extensions;

import me.nebu.trueEssentials.TrueEssentials;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

public class SplashScreenExtension implements Extension {

    private boolean enabled;

    public boolean playSound;
    public String soundId;
    public float pitch;
    public float volume;
    public double displayTime;
    public double fadeInTime;
    public double fadeOutTime;
    public String title;
    public String subtitle;

    public SplashScreenExtension() {
        reload();
    }

    @Override
    public void reload() {
        FileConfiguration config = TrueEssentials.getInstance().getConfig();
        ConfigurationSection s = config.getConfigurationSection("settings.extensions.splashscreen");
        if (s == null) {
            TrueEssentials.getInstance().getLogger().warning("An error occurred when initializing an extension: " + ExtensionName.SPAWN);
            disable();
            return;
        }

        this.enabled = s.getBoolean("enabled");
        this.playSound = s.getBoolean("sound");
        this.soundId = s.getString("sound-id");
        this.pitch = (float) s.getDouble("pitch");
        this.volume = (float) s.getDouble("volume");
        this.displayTime = s.getDouble("time");
        this.fadeInTime = s.getDouble("fade-in");
        this.fadeOutTime = s.getDouble("fade-out");
        this.title = s.getString("title");
        this.subtitle = s.getString("subtitle");

        if (enabled) TrueEssentials.getInstance().getLogger().info("Loaded extension: " + id());
    }

    @Override
    public ExtensionName id() { return ExtensionName.SPLASHSCREEN; }
    @Override
    public boolean enabled() { return enabled; }
    @Override
    public void enable() { this.enabled = true; }
    @Override
    public void disable() { this.enabled = false; }
}
