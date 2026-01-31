package me.camsbald.jJMC.commands;

import me.camsbald.jJMC.manager.ClassManager;
import org.bukkit.command.*;
import org.bukkit.entity.Player;

public class ClassesCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        sender.sendMessage("§6Available Classes:");
        ClassManager.getAllClasses().forEach(pc ->
                sender.sendMessage(" §e- " + pc.getDisplayName() + " §7(" + pc.getId() + ")")
        );
        return true;
    }
}
