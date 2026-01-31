package me.camsbald.jJMC.abilities.impl;

import me.camsbald.jJMC.JJMC;
import me.camsbald.jJMC.abilities.Ability;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

public class SwordMasterSecondary implements Ability {

    private final JJMC plugin;

    public SwordMasterSecondary(JJMC plugin) {
        this.plugin = plugin;
    }

    @Override
    public String getName() { return "§aLeap"; }

    @Override
    public String getId() { return "swordmaster_secondary"; }

    @Override
    public void use(Player player) {
        player.setVelocity(player.getLocation().getDirection().multiply(2.75));
    }

    public String getDescription() {
        return "Dash forward";
    }
}
