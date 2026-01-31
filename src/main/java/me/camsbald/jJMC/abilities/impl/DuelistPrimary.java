package me.camsbald.jJMC.abilities.impl;

import me.camsbald.jJMC.abilities.Ability;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

public class DuelistPrimary implements Ability {

    @Override
    public String getId() {
        return "duelist_primary";
    }

    @Override
    public String getName() {
        return "Lunge";
    }

    @Override
    public String getDescription() {
        return "Dash forward and empower your next hit.";
    }

    @Override
    public void use(Player p) {
        Vector dir = p.getLocation().getDirection().normalize();
        p.setVelocity(dir.multiply(1.4));

        p.getWorld().spawnParticle(Particle.CLOUD, p.getLocation(), 20, 0.2, 0.2, 0.2, 0);
        p.playSound(p.getLocation(), Sound.ENTITY_PLAYER_ATTACK_SWEEP, 1f, 1.2f);
    }
}
