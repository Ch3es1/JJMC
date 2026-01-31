package me.camsbald.jJMC.items.abilities;

import me.camsbald.jJMC.JJMC;
import me.camsbald.jJMC.abilities.Ability;
import org.bukkit.*;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

// Ability for Hollow Purple item
public class HollowPurpleItem implements Ability {

    @Override
    public String getId() {
        return "hollow_purple";
    }

    @Override
    public String getName() {
        return "Hollow Purple";
    }

    @Override
    public void use(Player player) {
        World world = player.getWorld();
        Vector direction = player.getEyeLocation().getDirection().normalize();
        Location start = player.getEyeLocation();
        double range = 30; // beam range
        double step = 0.5;

        Particle.DustOptions purpleDust = new Particle.DustOptions(Color.fromRGB(128, 0, 255), 1f); // purple, size 1
        player.addPotionEffect(new PotionEffect(PotionEffectType.RESISTANCE, 10, 9, false, false, true));

        for (double d = 0; d < range; d += step) {
            Location point = start.clone().add(direction.clone().multiply(d));

            breakBlock(point.getWorld(), player, point);

            // Spawn purple particles
            world.spawnParticle(Particle.ENCHANT, point, 2, 0.2, 0.2, 0.2, 0.01);
            world.spawnParticle(Particle.DUST, point, 2, 0.2, 0.2, 0.2, 0.01, purpleDust);

            // Damage entities
            for (Entity e : world.getNearbyEntities(point, 0.5, 0.5, 0.5)) {
                if (e instanceof LivingEntity target && !target.equals(player)) {
                    target.damage(20, player); // 4 damage per tick
                    target.setVelocity(new Vector(0, 0.2, 0)); // slight knock up
                }
            }
        }

        // Play a deep hollow sound
        world.playSound(player.getLocation(), Sound.ENTITY_WITHER_SHOOT, 1f, 0.8f);

        // Optional: give the player a brief speed boost to feel empowered
        player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 20 * 3, 1, false, false, true));
    }

    private void breakBlock(org.bukkit.World world, Player player, org.bukkit.Location loc) {
        if (loc.isGenerated()) {
            loc.createExplosion(10);
        }
    }
}
