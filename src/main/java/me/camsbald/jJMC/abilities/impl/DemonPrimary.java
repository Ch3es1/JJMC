package me.camsbald.jJMC.abilities.impl;

import me.camsbald.jJMC.JJMC;
import me.camsbald.jJMC.abilities.Ability;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Fireball;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import java.util.HashMap;
import java.util.Map;

public class DemonPrimary implements Ability {

    private final JJMC plugin;

    public DemonPrimary(JJMC plugin) {
        this.plugin = plugin;
    }

    @Override
    public String getName() { return "§cFireball"; }

    @Override
    public String getId() { return "demon_primary"; }

    @Override
    public void use(Player player) {
        Location eye = player.getEyeLocation();
        Vector direction = eye.getDirection().normalize(); // normalize to unit vector

        int yieldOverWorld = 5;
        int YieldNether = 15;

        // Choose duration based on dimension
        int finalYield = player.getWorld().getEnvironment() == World.Environment.NETHER
                ? yieldOverWorld
                : YieldNether;

        // Spawn a fireball
        Fireball fireball = player.getWorld().spawn(eye.add(direction.multiply(1)), Fireball.class);

        // Make it go forward in the direction player is looking
        fireball.setDirection(direction);
        fireball.setShooter(player); // optional, marks player as shooter
        fireball.setYield(finalYield); // explosion power
        fireball.setIsIncendiary(true); // set to burn blocks
    }

    public String getDescription() {
        return "Shoot a fireball";
    }
}
