package me.camsbald.jJMC.abilities.impl;

import me.camsbald.jJMC.JJMC;
import me.camsbald.jJMC.abilities.Ability;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.HashMap;
import java.util.Map;

public class DemonUltimate implements Ability {

    private final JJMC plugin;

    public DemonUltimate(JJMC plugin) {
        this.plugin = plugin;
    }

    @Override
    public String getName() { return "§c§lImplode"; }

    @Override
    public String getId() { return "demon_ultimate"; }

    @Override
    public void use(Player player) {
        Location loc = player.getLocation();
        player.addPotionEffect(new PotionEffect(PotionEffectType.RESISTANCE, 10*20, 9, false, false, true));

        if (player.getWorld().getEnvironment() == World.Environment.NETHER) {
            player.getWorld().createExplosion(loc, 50);
        } else {
            player.getWorld().createExplosion(loc, 15);
        }

    }

    public String getDescription() {
        return "Blows up a huge area(in the nether), & makes you invincible for 10 seconds";
    }
}
