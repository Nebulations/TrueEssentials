package me.nebu.trueEssentials.cmds.discord;

import me.nebu.trueEssentials.TrueEssentials;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import org.bukkit.configuration.file.FileConfiguration;

public class DiscordBot {

    private static JDA jda;
    public static String CHANNEL_ID;

    public static void start(String token) {
        jda = JDABuilder.createLight(token)
                .build();

        FileConfiguration config = TrueEssentials.getInstance().getConfig();

        CHANNEL_ID = config.getString("settings.extensions.discord-server.id");
    }

}
