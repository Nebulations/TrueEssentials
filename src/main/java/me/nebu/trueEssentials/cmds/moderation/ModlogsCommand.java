package me.nebu.trueEssentials.cmds.moderation;

import me.nebu.trueEssentials.Util;
import me.nebu.trueEssentials.completers.BasicCompleters;
import me.nebu.trueEssentials.playerdata.PlayerData;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class ModlogsCommand extends Command {

    public ModlogsCommand() {
        super("modlogs");
    }

    @Override
    public boolean execute(@NotNull CommandSender sender, @NotNull String label, @NotNull String[] args) {
        if (!sender.hasPermission("trueessentials.modlogs")) {
            sender.sendMessage(Util.error("You do not have permission to run this command."));
            return true;
        }

        if (!(sender instanceof Player player)) {
            sender.sendMessage(Util.error("This command can only be run by players."));
            return true;
        }

        if (args.length == 0) {
            player.sendMessage(Util.error("No player provided."));
            return true;
        }

        OfflinePlayer target = Bukkit.getOfflinePlayer(args[0]);

        if (!target.hasPlayedBefore()) {
            player.sendMessage(Util.error("This player does not exist or has never played before."));
            return true;
        }

        PlayerData data = PlayerData.of(target);

        Inventory inv = Bukkit.createInventory(player, 45, Component.text("History of " + target.getName()));

        ItemStack pane = new ItemStack(Material.BLACK_STAINED_GLASS_PANE);
        ItemMeta meta = pane.getItemMeta();
        meta.displayName(Component.text(" "));
        pane.setItemMeta(meta);

        for (int i = 0; i < 9; i++) inv.setItem(i, pane);
        inv.setItem(9, pane);
        inv.setItem(17, pane);
        inv.setItem(18, pane);
        inv.setItem(26, pane);
        inv.setItem(27, pane);

        for (int i = 35; i < 45; i++) inv.setItem(i, pane);

        ItemStack playerHead = new ItemStack(Material.PLAYER_HEAD);
        SkullMeta playerMeta = (SkullMeta) playerHead.getItemMeta();
        playerMeta.displayName(Component.text("History").color(NamedTextColor.RED).decoration(TextDecoration.ITALIC, false));
        playerMeta.setPlayerProfile(target.getPlayerProfile());

        List<Component> playerLore = new ArrayList<>();

        // ✔❌
        String banned = data.isBanned() ? "✔" : "❌";

        playerLore.add(dc("Banned").append(Component.text(banned).color(data.isBanned() ? NamedTextColor.GREEN : NamedTextColor.RED)));
        playerLore.add(dc("Bans").append(Component.text(data.getBans()).color(NamedTextColor.RED)));
        playerLore.add(dc("Warnings").append(Component.text(data.getWarns()).color(NamedTextColor.YELLOW)));

        if (data.isBanned()) {
            playerLore.add(Component.text(""));

            HashMap<String, String> banData = data.getLatestBan();

            String mod = banData.get("moderator").equals("Console") ? "Console" : Bukkit.getOfflinePlayer(UUID.fromString(banData.get("moderator"))).getName();
            assert mod != null;

            playerLore.add(dc("Reason").append(Component.text(banData.get("reason")).color(NamedTextColor.RED)));
            playerLore.add(dc("Moderator").append(Component.text(mod).color(NamedTextColor.YELLOW)));
            playerLore.add(dc("Expires on").append(Component.text(banData.get("expiration-date")).color(NamedTextColor.GREEN)));
            playerLore.add(dc("Time left").append(Component.text(banData.get("expires")).color(NamedTextColor.GOLD)));
        }

        playerMeta.lore(playerLore);
        playerHead.setItemMeta(playerMeta);

        inv.setItem(22, playerHead);

        player.openInventory(inv);

        return true;
    }

    private Component dc(String value) {
        return Component.text(value + ": ").color(NamedTextColor.GRAY).decoration(TextDecoration.ITALIC, false);
    }

    @Override
    public @NotNull List<String> tabComplete(@NotNull CommandSender sender, @NotNull String alias, @NotNull String[] args) throws IllegalArgumentException {
        return BasicCompleters.playerCompleter(args);
    }
}
