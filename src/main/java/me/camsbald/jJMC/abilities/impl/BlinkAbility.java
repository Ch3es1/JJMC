package me.camsbald.jJMC.abilities.impl;

import me.camsbald.jJMC.abilities.Ability;
import org.bukkit.entity.Player;

// Secondary
public class BlinkAbility implements Ability {
    @Override
    public String getName() { return "Blink"; }
    @Override
    public String getId() { return "blink"; }
    @Override
    public void use(Player player) {
        var loc = player.getLocation().add(player.getLocation().getDirection().multiply(5));
        player.teleport(loc);
        player.sendMessage("§bYou blinked forward!");
    }

    public String getDescription() {
        return "Teleport Forward 5 Blocks";
    }
}
