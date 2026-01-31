package me.camsbald.jJMC.abilities.impl;

import me.camsbald.jJMC.JJMC;
import me.camsbald.jJMC.abilities.Ability;
import org.bukkit.*;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.HashMap;
import java.util.Map;

public class MinerPassive implements Ability {

    private final JJMC plugin;

    public MinerPassive(JJMC plugin) {
        this.plugin = plugin;
    }

    @Override
    public String getName() { return "§bEnchant"; }

    @Override
    public String getId() { return "miner_passive"; }

    @Override
    public void use(Player player) {
        AttributeInstance SCALE = player.getAttribute(Attribute.SCALE);
        if (SCALE != null) {
            SCALE.setBaseValue(0.95);
        }

        player.addPotionEffect(new PotionEffect(PotionEffectType.HASTE, Integer.MAX_VALUE, 2, false, false, true));
    }

    public String getDescription() {
        return "Always have Haste III";
    }
}
