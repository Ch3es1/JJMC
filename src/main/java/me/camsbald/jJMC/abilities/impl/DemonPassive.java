package me.camsbald.jJMC.abilities.impl;

import me.camsbald.jJMC.JJMC;
import me.camsbald.jJMC.abilities.Ability;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.HashMap;
import java.util.Map;

public class DemonPassive implements Ability {

    private final JJMC plugin;

    public DemonPassive(JJMC plugin) {
        this.plugin = plugin;
    }

    @Override
    public String getName() { return "Demon Passive"; }

    @Override
    public String getId() { return "demon_passive"; }

    @Override
    public void use(Player player) {
        player.addPotionEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE, Integer.MAX_VALUE, 0, false, false, true));

        if (player.getWorld().getEnvironment() == World.Environment.NETHER) {
            player.addPotionEffect(new PotionEffect(PotionEffectType.STRENGTH, 30, 4, false, false, true));
        }
    }

    public String getDescription() {
        return "Makes you way stronger in the nether, buffs every affect in some way";
    }
}
