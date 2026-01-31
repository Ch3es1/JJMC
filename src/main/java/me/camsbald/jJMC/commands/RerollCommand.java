package me.camsbald.jJMC.commands;

import me.camsbald.jJMC.JJMC;
import me.camsbald.jJMC.classes.PlayerClass;
import me.camsbald.jJMC.manager.ClassManager;
import org.bukkit.Bukkit;
import org.bukkit.command.*;
import org.bukkit.entity.Player;

public class RerollCommand implements CommandExecutor {

    private final JJMC plugin;

    public RerollCommand(JJMC plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if (!sender.isOp()) {
            sender.sendMessage("§cNo permission.");
            return true;
        }

        Player target = null;

        if (args.length > 0) {
            target = Bukkit.getPlayer(args[0]);
        } else if (sender instanceof Player p) {
            target = p;
        }

        if (target == null) {
            sender.sendMessage("§cUsage: /reroll [player]");
            return true;
        }

        PlayerClass pc = ClassManager.rollRandomClass(target, plugin);
        ClassManager.setPlayerClass(target.getUniqueId(), pc);

        target.sendMessage("§eYour class has been rerolled to: " + pc.getDisplayName());
        sender.sendMessage("§aRerolled class for " + target.getName());

        return true;
    }
}
