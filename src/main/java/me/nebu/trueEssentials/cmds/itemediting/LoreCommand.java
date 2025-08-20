package me.nebu.trueEssentials.cmds.itemediting;

import me.nebu.trueEssentials.Util;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class LoreCommand implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args) {
        if (!sender.hasPermission("trueessentials.lore")) {
            sender.sendMessage(Util.error("You do not have permission to run this command."));
            return true;
        }

        if (!(sender instanceof Player player)) {
            sender.sendMessage(Util.error("This command can only be run by players."));
            return true;
        }

        if (args.length == 0) {
            player.sendMessage(Util.error("No arguments provided."));
            return true;
        }

        String param = args[0];
        ItemStack tool = player.getInventory().getItemInMainHand();

        if (tool.getType().equals(Material.AIR)) {
            player.sendMessage(Util.error("You cannot edit this item."));
            return true;
        }

        ItemMeta meta = tool.getItemMeta();

        if (param.equalsIgnoreCase("clear")) {
            meta.lore(null);
            tool.setItemMeta(meta);
            return true;
        }

        if (args.length == 1) {
            player.sendMessage(Util.error("This command requires 2 arguments."));
            return true;
        }

        StringBuilder sb = new StringBuilder();

        for (String s : args) { sb.append(s).append(" "); }

        if (param.equalsIgnoreCase("add")) {
            String loreText = sb.substring(4, sb.length() - 1);

            List<Component> lore = meta.hasLore() ? meta.lore() : new ArrayList<>();

            lore.add(Util.colorize(loreText));

            meta.lore(lore);
        }


        if (tool.getItemMeta() != meta) {
            tool.setItemMeta(meta);
            return true;
        }


        return true;
    }
}
