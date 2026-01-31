package me.camsbald.jJMC.abilities.impl;

import me.camsbald.jJMC.JJMC;
import me.camsbald.jJMC.abilities.Ability;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.HashMap;
import java.util.Map;

public class SwordMasterPassive implements Ability {

    private final JJMC plugin;

    public SwordMasterPassive(JJMC plugin) {
        this.plugin = plugin;
    }

    @Override
    public String getName() { return "SwordMaster Passive"; }

    @Override
    public String getId() { return "swordmaster_passive"; }

    @Override
    public void use(Player player) {
        player.addPotionEffect(new PotionEffect(PotionEffectType.STRENGTH, Integer.MAX_VALUE, 0, false, false, true));
        player.addPotionEffect(new PotionEffect(PotionEffectType.RESISTANCE, Integer.MAX_VALUE, 0, false, false, true));
    }

    public String getDescription() {
        return "Gives you Strength I & Resistance I";
    }
}
