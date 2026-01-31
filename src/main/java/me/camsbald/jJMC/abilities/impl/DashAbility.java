package me.camsbald.jJMC.abilities.impl;

import me.camsbald.jJMC.abilities.Ability;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

public class DashAbility implements Ability {

    @Override
    public String getName() {
        return "Dash";
    }

    @Override
    public String getId() {
        return "dash";
    }

    @Override
    public void use(Player player) {
        player.setVelocity(player.getLocation().getDirection().multiply(2.5));
        player.sendMessage("§aYou dashed forward!");
    }

    public String getDescription() {
        return "Dash forward";
    }
}