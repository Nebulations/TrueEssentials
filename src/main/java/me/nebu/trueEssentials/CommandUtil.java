package me.nebu.trueEssentials;

import org.bukkit.command.CommandExecutor;
import org.bukkit.command.TabCompleter;

public record CommandUtil(String name, CommandExecutor executor, TabCompleter completer, String ... permissions) {
}
