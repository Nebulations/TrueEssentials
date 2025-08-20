package me.nebu.trueEssentials.util;

public record SpawnUtil(boolean playSound, String sound, float pitch, float volume, int teleportTime, boolean cancelOnDamage, boolean cancelOnMove) {
}
