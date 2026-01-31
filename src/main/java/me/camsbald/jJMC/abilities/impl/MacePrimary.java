package me.camsbald.jJMC.abilities.impl;

import me.camsbald.jJMC.JJMC;
import me.camsbald.jJMC.abilities.Ability;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.HashMap;
import java.util.Map;

public class MacePrimary implements Ability {

    private final JJMC plugin;

    public MacePrimary(JJMC plugin) {
        this.plugin = plugin;
    }

    @Override
    public String getName() { return "§bLaunch"; }

    @Override
    public String getId() { return "mace_primary"; }

    @Override
    public void use(Player player) {
        player.setVelocity(new Vector(0, 1.5, 0));
    }

    public String getDescription() {
        return "Jump up in the air";
    }
}
