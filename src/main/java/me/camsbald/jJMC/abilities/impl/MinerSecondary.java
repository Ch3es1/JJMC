package me.camsbald.jJMC.abilities.impl;

import me.camsbald.jJMC.JJMC;
import me.camsbald.jJMC.abilities.Ability;
import me.camsbald.jJMC.manager.GojoDomainManager;
import org.bukkit.*;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.HashMap;
import java.util.Map;

import static me.camsbald.jJMC.manager.ClassManager.applyPassives;

public class MinerSecondary implements Ability {

    private final JJMC plugin;

    public MinerSecondary(JJMC plugin) {
        this.plugin = plugin;
    }

    @Override
    public String getName() { return "§bEnchant"; }

    @Override
    public String getId() { return "miner_secondary"; }

    @Override
    public void use(Player player) {
        int durationSeconds = 10;
        new BukkitRunnable() {
            int ticks = 0;

            @Override
            public void run() {
                ticks++;

                AttributeInstance SCALE = player.getAttribute(Attribute.SCALE);
                if (SCALE != null) {
                    SCALE.setBaseValue(0.5);
                }

                if (ticks >= durationSeconds * 20) {
                    applyPassives(player);
                    cancel();
                }


            }
        }.runTaskTimer(plugin, 0L,1L);
    }

    public String getDescription() {
        return "Makes you a block tall...";
    }
}
