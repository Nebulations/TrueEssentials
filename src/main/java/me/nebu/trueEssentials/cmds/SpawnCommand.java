package me.nebu.trueEssentials.cmds;

import com.google.common.util.concurrent.AtomicDouble;
import me.nebu.trueEssentials.TrueEssentials;
import me.nebu.trueEssentials.Util;
import me.nebu.trueEssentials.util.SpawnUtil;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.atomic.AtomicInteger;

public class SpawnCommand implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args) {
        if (!sender.hasPermission("trueessentials.spawn")) {
            sender.sendMessage(Util.error("You do not have permission to run this command."));
            return true;
        }

        if (!(sender instanceof Player player)) {
            sender.sendMessage(Util.error("This command can only be run by players."));
            return true;
        }

        Location teleportLocation = TrueEssentials.getInstance().getConfig().getLocation("storage.spawn-location");

        if (teleportLocation == null) {
            player.sendMessage(Util.error("The spawn location is not set."));
            return true;
        }

        SpawnUtil spawnUtil = Util.SPAWNUTIL;

        int wait = (spawnUtil.teleportTime() == 0 ? 0 : spawnUtil.teleportTime());

        Sound sound = Sound.ENTITY_ENDER_EYE_DEATH;

        try {
            sound = Sound.valueOf(spawnUtil.sound().toUpperCase());
        } catch (Exception e) {
            TrueEssentials.getInstance().getLogger().warning("Failed to play sound! Make sure you have properly configured sounds in the spawn extension.");
        }

        float pitch = spawnUtil.pitch();
        float volume = spawnUtil.volume();

        if (spawnUtil.teleportTime() != 0) {
            player.sendMessage(Util.message("Teleporting in " + spawnUtil.teleportTime() + " seconds."));
        }

        Sound finalSound = sound;

        final AtomicInteger timer = new AtomicInteger(wait);
        final AtomicDouble playerHealth = new AtomicDouble(player.getHealth());

        Location initialTp = player.getLocation();

        new BukkitRunnable() {
            @Override
            public void run() {
                Location loc = player.getLocation();
                if (initialTp.getX() != loc.getX()
                        || initialTp.getY() != loc.getY()
                        || initialTp.getZ() != loc.getZ()
                        || !initialTp.getWorld().equals(loc.getWorld())
                ) {
                    if (spawnUtil.cancelOnMove()) {
                        player.sendMessage(Util.error("Teleportation canceled: You moved."));
                        cancel();
                        return;
                    }
                }

                if (player.getHealth() > playerHealth.get()) {
                    playerHealth.set(player.getHealth());
                }

                if (player.getHealth() < playerHealth.get() && spawnUtil.cancelOnDamage()) {
                    player.sendMessage(Util.error("Teleportation canceled: You took damage."));
                    cancel();
                    return;
                }

                final double value = timer.getAndAdd(-1);

                if (value <= 0) {
                    player.teleport(teleportLocation);

                    player.sendMessage(Util.message("Teleported to spawn."));

                    if (spawnUtil.playSound()) {
                        player.playSound(player, finalSound, volume, pitch);
                    }

                    cancel();
                }
            }
        }.runTaskTimer(TrueEssentials.getInstance(), 0L, 20L);

        return true;
    }
}
