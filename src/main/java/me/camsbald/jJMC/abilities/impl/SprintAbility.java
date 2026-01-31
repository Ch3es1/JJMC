package me.camsbald.jJMC.abilities.impl;

import me.camsbald.jJMC.abilities.Ability;
import me.camsbald.jJMC.JJMC;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class SprintAbility implements Ability {

    private final JJMC plugin;

    public SprintAbility(JJMC plugin) { // ✅ pass plugin instead of new
        this.plugin = plugin;
    }

    @Override
    public String getName() { return "Sprint"; }

    @Override
    public String getId() { return "sprint"; }

    @Override
    public void use(Player player) {
        player.setWalkSpeed(0.5f);
        Bukkit.getScheduler().runTaskLater(plugin, () -> player.setWalkSpeed(0.2f), 100L);
        player.sendMessage("§eYou sprint at incredible speed!");
    }

    public String getDescription() {
        return "Makes you super fast for 5 seconds";
    }
}
