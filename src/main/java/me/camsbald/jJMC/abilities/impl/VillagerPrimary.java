package me.camsbald.jJMC.abilities.impl;

import me.camsbald.jJMC.JJMC;
import me.camsbald.jJMC.abilities.Ability;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.HashMap;
import java.util.Map;

public class VillagerPrimary implements Ability {

    private final JJMC plugin;

    public VillagerPrimary(JJMC plugin) {
        this.plugin = plugin;
    }

    @Override
    public String getName() { return "§dTrade Expert"; }

    @Override
    public String getId() { return "villager_primary"; }

    @Override
    public void use(Player player) {
        player.addPotionEffect(new PotionEffect(PotionEffectType.HERO_OF_THE_VILLAGE, 60*20, 255, false, false, true));
    }

    public String getDescription() {
        return "Gives you Hero of The Villager 255 for 1 minute";
    }
}
