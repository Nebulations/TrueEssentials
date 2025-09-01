package me.nebu.trueEssentials.events;

import me.nebu.trueEssentials.TrueEssentials;
import me.nebu.trueEssentials.Util;
import me.nebu.trueEssentials.extensions.ExtensionName;
import me.nebu.trueEssentials.extensions.JoinExtension;
import me.nebu.trueEssentials.extensions.SplashScreenExtension;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.title.Title;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.time.Duration;

public class JoinAndQuitEvent implements Listener {

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        Player player = e.getPlayer();

        JoinExtension extension = (JoinExtension) Util.getExtension(ExtensionName.JOIN_QUIT_MESSAGES);
        assert extension != null;

        if (extension.enabled()) {
            String message = player.hasPlayedBefore() ? extension.joinMessage : extension.newJoinMessage;
            e.joinMessage(Util.colorize(Util.formatPlaceholders(player, message)));
        }

        SplashScreenExtension splashExtension = (SplashScreenExtension) Util.getExtension(ExtensionName.SPLASHSCREEN);
        assert splashExtension != null;

        if (!splashExtension.enabled()) {
            return;
        }


        Component formattedTitle = Util.colorize(Util.formatPlaceholders(player, splashExtension.title));
        Component formattedSubtitle = Util.colorize(Util.formatPlaceholders(player, splashExtension.subtitle));

        player.showTitle(Title.title(
                formattedTitle, formattedSubtitle,
                Title.Times.times(
                        Duration.ofMillis((long) splashExtension.fadeInTime*1000),
                        Duration.ofMillis((long) splashExtension.displayTime*1000),
                        Duration.ofMillis((long) splashExtension.fadeOutTime*1000))
                )
        );

        if (splashExtension.playSound) {
            Sound sound = Sound.ENTITY_ENDER_EYE_DEATH;

            try {
                sound = Sound.valueOf(splashExtension.soundId.toUpperCase());
            } catch (Exception ex) {
                TrueEssentials.getInstance().getLogger().warning("Failed to play sound! Make sure you have properly configured sounds in the splash screen extension.");
            }

            float pitch = splashExtension.pitch;
            float volume = splashExtension.volume;

            Sound finalSound = sound;

            player.playSound(player, finalSound, volume, pitch);
        }

        Util.updateVanished();
    }


    @EventHandler
    public void onQuit(PlayerQuitEvent e) {
        JoinExtension extension = (JoinExtension) Util.getExtension(ExtensionName.JOIN_QUIT_MESSAGES);
        if (extension == null || !extension.enabled()) return;

        e.quitMessage(Util.colorize(Util.formatPlaceholders(e.getPlayer(), extension.quitMessage)));
    }

}
