package me.nebu.trueEssentials.events;

import io.papermc.paper.event.player.AsyncChatEvent;
import me.nebu.trueEssentials.Util;
import me.nebu.trueEssentials.extensions.ChatFormatExtension;
import me.nebu.trueEssentials.extensions.ExtensionName;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextReplacementConfig;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class ChatHandler implements Listener {

    PlainTextComponentSerializer serializer = PlainTextComponentSerializer.plainText();

    @EventHandler
    public void onAsyncChat(AsyncChatEvent e) {
        Player player = e.getPlayer();

        ChatFormatExtension extension = (ChatFormatExtension) Util.getExtension(ExtensionName.CHAT_FORMAT);
        assert extension != null;

        if (!extension.enabled()) return;

        String message = serializer.serialize(e.message());

        String messageStartFormat = Util.formatPlaceholders(player, extension.format);

        TextReplacementConfig textReplacementConfig = TextReplacementConfig.builder()
                .match("%message%")
                .replacement(message)
                .build();

        Component formatted = Util.colorize(messageStartFormat)
                .replaceText(textReplacementConfig);

        e.renderer((s, sd, c, a) -> formatted);
    }


}
