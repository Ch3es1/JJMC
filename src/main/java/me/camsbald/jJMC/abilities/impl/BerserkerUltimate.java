package me.camsbald.jJMC.abilities.impl;

import me.camsbald.jJMC.abilities.Ability;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.potion.*;

public class BerserkerUltimate implements Ability {

    @Override
    public String getName() { return "§4Unchained Rage"; }

    @Override
    public String getId() { return "berserker_ultimate"; }

    @Override
    public void use(Player player) {
        Location loc = player.getLocation();
        for (Player p : loc.getWorld().getPlayers()) {
            if (p.getLocation().distanceSquared(loc) <= 100) {
                p.playSound(loc, Sound.BLOCK_ENCHANTMENT_TABLE_USE, 1f, 2f);
            }
        }

        player.addPotionEffect(new PotionEffect(PotionEffectType.STRENGTH, 200, 3));
        player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 200, 2));
        player.addPotionEffect(new PotionEffect(PotionEffectType.RESISTANCE, 200, 2));
        player.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 200, 2));
    }

    public String getDescription() {
        return "Go into a rage, gaining massive strength and speed for 10 seconds.";
    }
}
