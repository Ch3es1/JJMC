package me.camsbald.jJMC.commands;

import me.camsbald.jJMC.abilities.Ability;
import me.camsbald.jJMC.JJMC;
import me.camsbald.jJMC.classes.PlayerClass;
import me.camsbald.jJMC.manager.AbilityManager;
import me.camsbald.jJMC.manager.ClassManager;
import me.camsbald.jJMC.manager.CooldownManager;
import org.bukkit.command.*;
import org.bukkit.entity.Player;

public class AbilityCommand implements CommandExecutor {

    private final JJMC plugin;

    public AbilityCommand(JJMC plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player player)) return true;

        if (args.length == 0) {
            player.sendMessage("§cUsage: /ability <primary|secondary|ultimate>");
            return true;
        }

        PlayerClass pc = ClassManager.getPlayerClass(player.getUniqueId());
        if (pc == null) {
            player.sendMessage("§cYou don't have a class!");
            return true;
        }

        Ability ability;
        switch (args[0].toLowerCase()) {
            case "primary" -> ability = pc.getPrimary();
            case "secondary" -> ability = pc.getSecondary();
            case "ultimate" -> ability = pc.getUltimate();
            default -> {
                player.sendMessage("§cUnknown ability type! Use primary, secondary, or ultimate.");
                return true;
            }
        }

        String id = ability.getId();
        String name = ability.getName();

        if (CooldownManager.isOnCooldown(player.getUniqueId(), id)) {
            long remaining = CooldownManager.getRemaining(player.getUniqueId(), id);
            player.sendMessage("§cCooldown: " + remaining + "s remaining.");
            return true;
        }

        player.sendMessage("Using: " + ability.getName());
        ability.use(player);

        // Use unique cooldown from config
        int seconds = plugin.getConfig().getInt("cooldowns." + id, 5);
        CooldownManager.setCooldown(player.getUniqueId(), id, name);

        return true;
    }

}
