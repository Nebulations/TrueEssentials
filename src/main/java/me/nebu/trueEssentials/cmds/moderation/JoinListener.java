package me.nebu.trueEssentials.cmds.moderation;

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

        if (extension == null || !extension.enabled()) return;

        if (data.isBanned()) {
            e.disallow(PlayerLoginEvent.Result.KICK_BANNED, extension.formatBanScreen(data));
        }
    }

}
