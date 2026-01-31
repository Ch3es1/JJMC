package me.camsbald.jJMC.abilities.impl;

import me.camsbald.jJMC.JJMC;
import me.camsbald.jJMC.abilities.Ability;
import org.bukkit.entity.Player;

public class RunnerPassive implements Ability {

    private final JJMC plugin;

    public RunnerPassive(JJMC plugin) {
        this.plugin = plugin;
    }

    @Override
    public String getId() { return "speed_passive"; }

    @Override
    public String getName() { return "Speed Passive"; }

    @Override
    public void use(Player player) {
        player.setWalkSpeed(0.2f); // increase base walk speed
    }

    public String getDescription() {
        return "Makes you slightly faster";
    }
}

