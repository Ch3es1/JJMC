package me.camsbald.jJMC.commands;

import me.camsbald.jJMC.items.CustomItem;
import me.camsbald.jJMC.items.CustomItemManager;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class GiveCustomItemCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (!(sender instanceof Player player)) {
            sender.sendMessage("§cOnly Players Can Use This Command!");
            return true;
        }

        if (!sender.isOp()) {
            player.sendMessage("§cInsufficient Permissions");
            return true;
        }

        if (args.length < 2) {
            sender.sendMessage("§cUsage: /giveitem <player> <itemId>");
            return true;
        }

        Player target = Bukkit.getPlayer(args[0]);
        if (target == null) {
            sender.sendMessage("§cPlayer not found!");
            return true;
        }

        String id = args[1];
        CustomItem item = CustomItemManager.getById(id);
        if (item == null) {
            sender.sendMessage("§cItem ID not found!");
            return true;
        }

        target.getInventory().addItem(item.getItem());
        sender.sendMessage("§aGave " + target.getName() + " the item " + item.getId());

        return true;
    }
}
