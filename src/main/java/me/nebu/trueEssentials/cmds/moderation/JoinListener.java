package me.nebu.trueEssentials.cmds.moderation;

import me.nebu.trueEssentials.TrueEssentials;
import me.nebu.trueEssentials.Util;
import me.nebu.trueEssentials.extensions.ExtensionName;
import me.nebu.trueEssentials.extensions.ModerationExtension;
import me.nebu.trueEssentials.playerdata.PlayerData;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;

public class JoinListener implements Listener {

    @EventHandler
    public void onConnect(PlayerLoginEvent e) {
        PlayerData data = PlayerData.of(e.getPlayer());

        ModerationExtension extension = (ModerationExtension) Util.getExtension(ExtensionName.MODERATION);

        if (extension == null) {
            TrueEssentials.getInstance().getLogger().severe("An issue occurred when processing a login event. Please restart the server or join the discord server for support.");
            e.disallow(PlayerLoginEvent.Result.KICK_OTHER, Util.error("A critical error has occurred."));
            return;
        }

        if (data.isBanned()) {
            e.disallow(PlayerLoginEvent.Result.KICK_BANNED, extension.formatBanScreen(data));
        }
    }

}
