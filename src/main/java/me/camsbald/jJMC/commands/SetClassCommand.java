package me.camsbald.jJMC.commands;

import me.camsbald.jJMC.classes.PlayerClass;
import me.camsbald.jJMC.manager.ClassManager;
import org.bukkit.Bukkit;
import org.bukkit.command.*;
import org.bukkit.entity.Player;

public class SetClassCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if (!sender.isOp()) {
            sender.sendMessage("§cYou do not have permission to use this command!");
            return true;
        }

        if (args.length < 2) {
            sender.sendMessage("§cUsage: /setclass <player> <class>");
            return true;
        }

        Player target = Bukkit.getPlayer(args[0]);
        if (target == null) {
            sender.sendMessage("§cPlayer not found.");
            return true;
        }

        String classId = args[1].toLowerCase();
        PlayerClass pc = ClassManager.getClass(classId);

        if (pc == null) {
            sender.sendMessage("§cUnknown class: " + args[1]);
            return true;
        }

        // Assign the full PlayerClass to the player
        ClassManager.setPlayerClass(target.getUniqueId(), pc);
        ClassManager.applyPassives(target); // apply passives immediately

        sender.sendMessage("§aSet " + target.getName() + "'s class to " + pc.getDisplayName());
        target.sendMessage("§eYour class is now: " + pc.getDisplayName());

        return true;
    }
}
