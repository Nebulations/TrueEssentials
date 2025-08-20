package me.nebu.trueEssentials.events;

import io.papermc.paper.event.player.AsyncChatEvent;
import me.clip.placeholderapi.PlaceholderAPI;
import me.nebu.trueEssentials.TrueEssentials;
import me.nebu.trueEssentials.Util;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextReplacementConfig;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class ChatHandler implements Listener {

    private final String format;
    private final MiniMessage mm = MiniMessage.miniMessage();
    private final PlainTextComponentSerializer plainText = PlainTextComponentSerializer.plainText();

    public ChatHandler(String format) {
        this.format = format;
    }

    @EventHandler
    public void onAsyncChat(AsyncChatEvent e) {
        Player player = e.getPlayer();

        // Get the raw text of the message
        String plainMessage = plainText.serialize(e.message());

        // Format the placeholders in the message owner
        String finalFormat = TrueEssentials.PAPI
                ? PlaceholderAPI.setPlaceholders(player, format)
                : Util.formatPlaceholders(player, format);

        // Get the formatted message as a component.
//        Component formatted = mm.deserialize(finalFormat.replace("%message%", ""))
//                .append(Component.text(plainMessage));

        TextReplacementConfig textReplacementConfig = TextReplacementConfig
                .builder()
                .match("%message%")
                .replacement(plainMessage)
                .build();

        Component formatted = mm.deserialize(finalFormat)
                .replaceText(textReplacementConfig);

        // Render the final message
        e.renderer((source, sourceDisplayName, message, audience) -> formatted);
    }


}
