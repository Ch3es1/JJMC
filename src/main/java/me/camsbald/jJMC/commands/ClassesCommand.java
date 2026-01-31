package me.camsbald.jJMC.commands;

import me.camsbald.jJMC.JJMC;
import me.camsbald.jJMC.classes.PlayerClass;
import me.camsbald.jJMC.manager.ClassManager;
import org.bukkit.command.*;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;

import static me.camsbald.jJMC.manager.ClassManager.classRegistry;

public class ClassesCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        JJMC plugin = JJMC.getInstance();
        List<String> allowedIds = plugin.getConfig().getStringList("class-pools.default");
        List<PlayerClass> allowed = new ArrayList<>();
        for (String id : allowedIds) {
            PlayerClass pc = classRegistry.get(id.toLowerCase());
            if (pc != null) {
                allowed.add(pc);
            }
        }

        sender.sendMessage("§6Available Classes:");
        allowed.forEach(pc ->
                sender.sendMessage(" §e- " + pc.getDisplayName() + " §7(" + pc.getId() + ")")
        );
        return true;
    }
}
