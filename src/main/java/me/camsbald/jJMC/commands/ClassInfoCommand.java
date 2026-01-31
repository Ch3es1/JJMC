package me.camsbald.jJMC.commands;

import me.camsbald.jJMC.classes.PlayerClass;
import me.camsbald.jJMC.manager.ClassManager;
import org.bukkit.command.*;
import org.bukkit.entity.Player;

public class ClassInfoCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if (!(sender instanceof Player player)) {
            sender.sendMessage("Only players can use this command.");
            return true;
        }

        PlayerClass pc = ClassManager.getPlayerClass(player.getUniqueId());

        if (pc == null) {
            player.sendMessage("§cYou don't have a class yet.");
            return true;
        }

        if (!pc.getAbilities().isEmpty()) {
            player.sendMessage("§7Current Class: " + pc.getDisplayName());
            player.sendMessage("§7Abilities:");
            pc.getAbilities().forEach(ability -> {
                player.sendMessage(" §e• §f" + ability.getName());

                String desc = ability.getDescription();
                if (desc != null && !desc.isBlank()) {
                    player.sendMessage("   §7↳ " + desc);
                }
            });
        }

        if (pc.getPassives() != null && !pc.getPassives().isEmpty()) {
            player.sendMessage("§7Passives:");
            pc.getPassives().forEach(passive -> {
                player.sendMessage(" §b• §f" + passive.getName());

                String desc = passive.getDescription();
                if (desc != null && !desc.isBlank()) {
                    player.sendMessage("   §7↳ " + desc);
                }
            });
        }
        return true;
    }
}
