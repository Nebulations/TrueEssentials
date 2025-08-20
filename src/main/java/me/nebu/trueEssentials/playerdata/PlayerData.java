package me.nebu.trueEssentials.playerdata;

import me.nebu.trueEssentials.TrueEssentials;
import me.nebu.trueEssentials.Util;
import me.nebu.trueEssentials.cmds.homes.Home;
import me.nebu.trueEssentials.extensions.ExtensionName;
import me.nebu.trueEssentials.extensions.ModerationExtension;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

public class PlayerData {

    public static PlayerData of(Player player) {
        return new PlayerData(player);
    }
    public static PlayerData of(OfflinePlayer offlinePlayer) {
        return new PlayerData(Objects.requireNonNull(offlinePlayer.getPlayer()));
    }


    private final File dataFile;
    private final YamlConfiguration data;

    private final Player player;
    private List<Home> homes;

    private PlayerData(Player player) {
        this.player = player;
        String path = TrueEssentials.getInstance().getDataFolder().getAbsolutePath() + "/playerdata/" + player.getUniqueId() + ".yml";

        dataFile = new File(path);

        if (!dataFile.exists()) {
            if (!dataFile.getParentFile().exists()) {
                dataFile.getParentFile().mkdirs();
            }

            try { dataFile.createNewFile();
            } catch (IOException e) { e.printStackTrace(); }
        }

        data = YamlConfiguration.loadConfiguration(dataFile);

        if (Objects.requireNonNull(Util.getExtension(ExtensionName.HOMES)).enabled()) {
            homes = new ArrayList<>();
            ConfigurationSection homes = data.getConfigurationSection("homes");
            if (homes != null) {
                Set<String> keys = homes.getKeys(false);

                for (String key : keys) {
                    this.homes.add(new Home(key, data.getLocation("homes." + key)));
                }
            }
        }
    }

    public Player getPlayer() {
        return player;
    }

    // Homes
    public List<Home> getHomes() {
        return homes;
    }

    public void addHome(Home home) {
        homes.add(home);

        data.set("homes." + home.name(), home.location());
    }

    public void removeHome(String name) {
        homes.removeIf(home -> home.name().equals(name));
    }

    public Home getHome(String name) {
        for (Home home : homes) {
            if (home.name().equals(name)) return home;
        }

        return null;
    }

    // Moderation
    public boolean isBanned() {
        boolean exists = data.isBoolean("moderation.banned");

        if (!exists) return false;

        long expires = data.getLong("moderation.expires");

        Date expiresDate = new Date(expires);

        Date now = new Date(System.currentTimeMillis());

        if (now.after(expiresDate)) {
            data.set("moderation.expires", null);
            data.set("moderation.banned", null);

            save();

            return false;
        }

        return data.getBoolean("moderation.banned");
    }

    public HashMap<String, String> getLatestBan() {
        ConfigurationSection section = data.getConfigurationSection("moderation.bans");
        assert section != null;

        List<String> keys = new ArrayList<>(section.getKeys(false));
        String id = keys.get(keys.size()-1);

        String query = "moderation.bans." + id + ".";

        HashMap<String, String> result = new HashMap<>();

        String reason = data.getString(query + "reason");
        String mod = data.getString(query + "moderator");
        long punishTime = data.getLong(query + "date");
        long expires = data.getLong(query + "expires");
        long timeLeft = Math.abs(expires - System.currentTimeMillis());

        DateFormat format = new SimpleDateFormat("MMMMMMMMM dd, yyyy, HH:mm");
        Date date = new Date(punishTime);
        Date expiresDate = new Date(expires);

        ModerationExtension extension = (ModerationExtension) Util.getExtension(ExtensionName.MODERATION);
        assert extension != null;

        String f = extension.getDateFormat();

        long days = timeLeft / (1000L * 60 * 60 * 24);
        long hours = (timeLeft / (1000L * 60 * 60)) % 24;
        long minutes = (timeLeft / (1000L * 60)) % 60;
        long seconds = (timeLeft / 1000L) % 60;

        String finalFormat = f.replace("%days%", String.valueOf(days))
                .replace("%hours%", String.valueOf(hours))
                .replace("%minutes%", String.valueOf(minutes))
                .replace("%seconds%", String.valueOf(seconds));

        result.put("reason", reason);
        result.put("moderator", mod);
        result.put("punish-date", format.format(date));
        result.put("expiration-date", format.format(expiresDate));
        result.put("expires", finalFormat);

        return result;
    }

    public void ban(String reason, String moderator) {
        ModerationExtension e = (ModerationExtension) Util.getExtension(ExtensionName.MODERATION);
        assert e != null;

        int bans = getBans();

        long duration = e.getBanDuration(bans);
        long now = System.currentTimeMillis();
        long expires = duration+now;

        String id = String.valueOf(bans+1);
        String query = "moderation.bans." + id + ".";

        data.set("moderation.banned", true);
        data.set("moderation.expires", expires);

        data.set(query + "reason", reason);
        data.set(query + "moderator", moderator);
        data.set(query + "date", now);
        data.set(query + "expires", expires);

        save();
    }

    public void warn(String reason, CommandSender sender, long time) {
        ModerationExtension extension = (ModerationExtension) Util.getExtension(ExtensionName.MODERATION);
        assert extension != null;

        String uuid = (sender instanceof Player) ? String.valueOf(((Player) sender).getUniqueId()) : "Console";
        int warns = getWarns();

        String id = String.valueOf(warns+1);
        String query = "moderation.warns." + id + ".";

        data.set(query + "reason", reason);
        data.set(query + "moderator", uuid);
        data.set(query + "date", time);

        save();
    }

    public int getBans() {
        ConfigurationSection section = data.getConfigurationSection("moderation.bans");
        if (section == null) return 0;

        return section.getKeys(false).size();
    }

    public int getWarns() {
        ConfigurationSection section = data.getConfigurationSection("moderation.warns");
        if (section == null) return 0;

        return section.getKeys(false).size();
    }

    public void save() {
        try { data.save(dataFile);
        } catch (IOException e) { e.printStackTrace(); }
    }

}
