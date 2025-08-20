package me.nebu.trueEssentials.completers;

import me.nebu.trueEssentials.Util;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class LoreCompleter implements TabCompleter {

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args) {
        List<String> completer = new ArrayList<>();

        if (args.length == 1) {
            String param = args[0];

            Util.addIfMatches(param, "add", completer);
            Util.addIfMatches(param, "clear", completer);
            Util.addIfMatches(param, "remove", completer);
            Util.addIfMatches(param, "insert", completer);
            Util.addIfMatches(param, "set", completer);
        }

        if (args.length == 2) {
            String param = args[0];

            if (param.equals("remove") || param.equals("set")) {
                if (!(sender instanceof Player player)) {
                    return completer;
                }

                ItemStack hand = player.getInventory().getItemInMainHand();
                if (hand.getType().equals(Material.AIR)) return completer;

                List<Component> lore = hand.getItemMeta().hasLore() ? hand.lore() : new ArrayList<>();

                for (int i = 0; i < lore.size(); i++) {
                    completer.add(String.valueOf(i+1));
                }
            }
        }

        if (args.length == 3) {
            String number = args[1];

            if (args[0].equals("set")) {
                if (!(sender instanceof Player player)) {
                    return completer;
                }

                ItemStack hand = player.getInventory().getItemInMainHand();
                if (hand.getType().equals(Material.AIR)) return completer;

                List<Component> lore = hand.getItemMeta().hasLore() ? hand.lore() : new ArrayList<>();

                int num;
                try {
                    num = Integer.parseInt(number);
                    num--;
                } catch (Exception ignored) {
                    return completer;
                }

                if (lore.size() <= num || num < 0) {
                    return completer;
                }

                PlainTextComponentSerializer serializer = PlainTextComponentSerializer.plainText();

                completer.add(serializer.serialize(lore.get(num)));

                return completer;
            }
        }

        return completer;
    }
}
