package me.nebu.trueEssentials.events;

import me.nebu.trueEssentials.TrueEssentials;
import me.nebu.trueEssentials.Util;
import me.nebu.trueEssentials.util.JoinUtil;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.title.Title;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.time.Duration;

public class JoinAndQuitEvent implements Listener {

    private final String join;
    private final String quit;
    private final String newJoin;

    public JoinAndQuitEvent(String join, String newJoin, String quit) {
        this.join = join;
        this.quit = quit;
        this.newJoin = newJoin;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        Player player = e.getPlayer();

        String message = player.hasPlayedBefore() ? join : newJoin;

        e.joinMessage(Util.colorize(Util.formatPlaceholders(player, message)));

        if (Util.JOINUTIL == null) return;

        JoinUtil util = Util.JOINUTIL;
        Component formattedTitle = Util.colorize(Util.formatPlaceholders(player, util.title()));
        Component formattedSubtitle = Util.colorize(Util.formatPlaceholders(player, util.subtitle()));

        player.showTitle(Title.title(
                formattedTitle, formattedSubtitle,
                Title.Times.times(
                        Duration.ofMillis((long) util.fadein()*1000),
                        Duration.ofMillis((long) util.displayTime()*1000),
                        Duration.ofMillis((long) util.fadeout()*1000))
                )
        );

        if (util.playSound()) {
            Sound sound = Sound.ENTITY_ENDER_EYE_DEATH;

            try {
                sound = Sound.valueOf(util.sound().toUpperCase());
            } catch (Exception ex) {
                TrueEssentials.getInstance().getLogger().warning("Failed to play sound! Make sure you have properly configured sounds in the spawn extension.");
            }

            float pitch = util.pitch();
            float volume = util.volume();

            Sound finalSound = sound;

            player.playSound(player, finalSound, volume, pitch);
        }

    }


    @EventHandler
    public void onQuit(PlayerQuitEvent e) {
        e.quitMessage(Util.colorize(Util.formatPlaceholders(e.getPlayer(), quit)));
    }

}
