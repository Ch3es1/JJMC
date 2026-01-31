package me.camsbald.jJMC.abilities.impl;

import me.camsbald.jJMC.JJMC;
import me.camsbald.jJMC.abilities.Ability;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.HashMap;
import java.util.Map;

public class DragonBornPassive implements Ability {

    private final JJMC plugin;

    public DragonBornPassive(JJMC plugin) {
        this.plugin = plugin;
    }

    @Override
    public String getName() { return "DragonBorn Passive"; }

    @Override
    public String getId() { return "dragonborn_passive"; }

    @Override
    public void use(Player player) {
        player.addPotionEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE, Integer.MAX_VALUE, 0, false, false, true));
        player.addPotionEffect(new PotionEffect(PotionEffectType.STRENGTH, Integer.MAX_VALUE, 1, false, false, true));
        player.addPotionEffect(new PotionEffect(PotionEffectType.RESISTANCE, Integer.MAX_VALUE, 0, false, false, true));
        player.addPotionEffect(new PotionEffect(PotionEffectType.HEALTH_BOOST, Integer.MAX_VALUE, 4, false, false, true));
    }

    public String getDescription() {
        return "Gives you permanent Strength II, Fire Resistance, & Resistance + an Extra Row of Hearts";
    }
}
