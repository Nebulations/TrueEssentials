package me.nebu.trueEssentials.extensions;

import me.nebu.trueEssentials.TrueEssentials;
import me.nebu.trueEssentials.Util;
import me.nebu.trueEssentials.playerdata.PlayerData;
import net.kyori.adventure.text.Component;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ModerationExtension implements Extension {
    private boolean enabled;
    private String dateFormat;
    private List<String> reasons;
    private List<String> rawReasons;
    private boolean strictReasonCheck;
    private List<String> warnMessage;
    private List<String> banMessage;
    private final HashMap<Integer, Long> banDurations = new HashMap<>();

    public ModerationExtension() {
        reload();
    }

    @Override
    public ExtensionName id() { return ExtensionName.MODERATION; }
    @Override
    public boolean enabled() { return enabled; }
    @Override
    public void enable() { enabled = true; }
    @Override
    public void disable() { enabled = false; }

    public String getDateFormat() {
        return dateFormat;
    }

    public boolean isReasonValid(String reason) {
        if (!strictReasonCheck) {
            return true;
        }

        return reasons.contains(reason.toLowerCase());
    }

    public List<String> getRawReasons() { return rawReasons; }

    public List<String> getReasons() {
        return reasons;
    }

    public long getBanDuration(int ban) {
        if (ban >= banDurations.size()) {
            return banDurations.get(banDurations.size());
        }

        return banDurations.get(ban+1);
    }

    public Component formatBanScreen(PlayerData data) {
        Component component = Component.empty();

        HashMap<String, String> ban = data.getLatestBan();

        for (String s : banMessage) {
            component = component.append(Util.colorize(
                    s.replace("%reason%", ban.get("reason"))
                            .replace("%who%", ban.get("moderator"))
                            .replace("%punish-date%", ban.get("punish-date"))
                            .replace("%expiration-date%", ban.get("expiration-date"))
                            .replace("%expires%", ban.get("expires"))
            )).append(Component.newline());
        }

        return component;
    }

    public Component formatWarnScreen(String reason, CommandSender moderator, String punishDate) {
        Component component = Component.empty();

        String name = (moderator instanceof Player) ? moderator.getName() : "Console";

        for (String s : warnMessage) {
            component = component.append(Util.colorize(
                    s.replace("%reason%", reason)
                            .replace("%who%", name)
                            .replace("%punish-date%", punishDate)
            )).append(Component.newline());
        }

        return component;
    }


    @Override
    public void reload() {
        FileConfiguration config = TrueEssentials.getInstance().getConfig();

        this.enabled = config.getBoolean("settings.extensions.moderation.enabled");
        this.dateFormat = config.getString("settings.extensions.moderation.date-format");
        this.rawReasons = config.getStringList("settings.extensions.moderation.reasons");

        this.reasons = new ArrayList<>();
        reasons.addAll(rawReasons);
        reasons.replaceAll(String::toLowerCase);

        this.strictReasonCheck = config.getBoolean("settings.extensions.moderation.strict-reason-check");
        this.warnMessage = config.getStringList("settings.extensions.moderation.warn");
        this.banMessage = config.getStringList("settings.extensions.moderation.ban");

        List<String> keys = config.getStringList("settings.extensions.moderation.ban-durations");

        for (int i = 0; i < keys.size(); i++) {
            banDurations.put(i+1, Util.formatDuration(keys.get(i)));
        }

        if (enabled) TrueEssentials.getInstance().getLogger().info("Loaded extension: " + id());
    }
}
