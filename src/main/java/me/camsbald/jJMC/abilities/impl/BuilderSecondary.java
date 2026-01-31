package me.camsbald.jJMC.abilities.impl;

import me.camsbald.jJMC.JJMC;
import me.camsbald.jJMC.abilities.Ability;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.Player;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.HashMap;
import java.util.Map;

public class BuilderSecondary implements Ability {
    @Override
    public String getName() { return "§aHarden"; }

    @Override
    public String getId() { return "builder_secondary"; }

    @Override
    public void use(Player player) {
        int duration = 10;
        int radius = 20;

        var playerLocation = player.getLocation();
        for (Player p : playerLocation.getWorld().getPlayers()) {
            if (p.getLocation().distanceSquared(playerLocation) <= radius * radius) {
                p.playSound(playerLocation, Sound.ENTITY_LIGHTNING_BOLT_THUNDER, 1f, 1f);
            }
        }

        player.addPotionEffect(new PotionEffect(
                PotionEffectType.STRENGTH,
                duration*20,
                1,
                false,
                true,
                true)
        );
        player.addPotionEffect(new PotionEffect(
                PotionEffectType.RESISTANCE,
                duration*20,
                9,
                false,
                true,
                true)
        );
        player.addPotionEffect(new PotionEffect(
                PotionEffectType.SLOWNESS,
                duration*20,
                1,
                false,
                true,
                true)
        );
        player.addPotionEffect(new PotionEffect(
                PotionEffectType.GLOWING,
                duration*20,
                0,
                false,
                true,
                true)
        );
    }

    public String getDescription() {
        return "Gives you Strength III and makes you invincible, but you glow and have slowness II";
    }
}
