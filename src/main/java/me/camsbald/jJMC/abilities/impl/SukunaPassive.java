package me.camsbald.jJMC.abilities.impl;

import me.camsbald.jJMC.JJMC;
import me.camsbald.jJMC.abilities.Ability;
import me.camsbald.jJMC.manager.ClassManager;
import org.bukkit.*;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class SukunaPassive implements Ability, Listener {
    private final JJMC plugin;

    public SukunaPassive(JJMC plugin) {
        this.plugin = plugin;
    }

    @Override
    public String getId() { return "sukuna_passive"; }

    @Override
    public String getName() { return "Sukuna Passive"; }

    @Override
    public void use(Player player) {

        AttributeInstance SCALE = player.getAttribute(Attribute.SCALE);
        if (SCALE != null) {
            SCALE.setBaseValue(1.5);
        }

        AttributeInstance BLOCK_INTERACTION_RANGE = player.getAttribute(Attribute.BLOCK_INTERACTION_RANGE);
        if (BLOCK_INTERACTION_RANGE != null) {
            BLOCK_INTERACTION_RANGE.setBaseValue(7);
        }

        AttributeInstance ENTITY_INTERACTION_RANGE = player.getAttribute(Attribute.ENTITY_INTERACTION_RANGE);
        if (ENTITY_INTERACTION_RANGE != null) {
            ENTITY_INTERACTION_RANGE.setBaseValue(6);
        }

        AttributeInstance JUMP_STRENGTH = player.getAttribute(Attribute.JUMP_STRENGTH);
        if (JUMP_STRENGTH != null) {
            JUMP_STRENGTH.setBaseValue(0.465);
        }

        AttributeInstance STEP_HEIGHT = player.getAttribute(Attribute.STEP_HEIGHT);
        if (STEP_HEIGHT != null) {
            STEP_HEIGHT.setBaseValue(1);
        }

        player.setWalkSpeed(0.235f);
        player.addPotionEffect(new PotionEffect(PotionEffectType.STRENGTH, Integer.MAX_VALUE, 2, false, false, true));
        player.addPotionEffect(new PotionEffect(PotionEffectType.HASTE, Integer.MAX_VALUE, 1, false, false, true));
        player.addPotionEffect(new PotionEffect(PotionEffectType.SATURATION, Integer.MAX_VALUE, 0, false, false, true));
        player.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, Integer.MAX_VALUE, 4, false, false, true));
        player.addPotionEffect(new PotionEffect(PotionEffectType.RESISTANCE, Integer.MAX_VALUE, 1, false, false, true));
        player.addPotionEffect(new PotionEffect(PotionEffectType.HEALTH_BOOST, Integer.MAX_VALUE, 9, false, false, true));
        player.addPotionEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE, Integer.MAX_VALUE, 0, false, false, true));

        player.addPotionEffect(new PotionEffect(PotionEffectType.UNLUCK, Integer.MAX_VALUE, 255, false, false, true));
    }

    @EventHandler
    public void onPlayerDamaged(EntityDamageByEntityEvent event) {

        if (!(event.getEntity() instanceof Player victim)) return;

        var pc = plugin.getClassManager().getPlayerClass(victim.getUniqueId());
        if (pc == null) return;

        if (!plugin.getClassManager().getPlayerClass(victim.getUniqueId())
                .getPassives().stream()
                .anyMatch(p -> p.getId().equals(this.getId())))
            return;

        if (!(event.getDamager() instanceof Player attacker)) return;

        Material item = attacker.getInventory().getItemInMainHand().getType();
        if (item == Material.WOODEN_SWORD ||
                item == Material.STONE_SWORD ||
                item == Material.IRON_SWORD ||
                item == Material.GOLDEN_SWORD ||
                item == Material.DIAMOND_SWORD ||
                item == Material.NETHERITE_SWORD) {

            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlayerDamaged(EntityDamageEvent event) {
        if (!(event.getEntity() instanceof Player victim)) return;

        // Only apply for players with Sukuna passive
        if (!ClassManager.getPlayerClass(victim.getUniqueId())
                .getPassives().stream()
                .anyMatch(p -> p.getId().equals("sukuna_passive"))) return;

        double damage = event.getDamage();

        if (event.getFinalDamage() >= victim.getHealth()) {
            event.setCancelled(true);
            victim.heal(event.getFinalDamage());
        }
        if (victim.getLocation().getWorld().getMinHeight() > victim.getLocation().getY()) {
            Location respawn = victim.getRespawnLocation();

            if (respawn == null) {
                // Fallback to world spawn if no bed/anchor set
                respawn = victim.getWorld().getSpawnLocation();
                victim.sendMessage("§eYou don't have a bed set — sending you to world spawn.");
            } else {
                victim.sendMessage("§aReturning you to your respawn point...");
            }

            victim.teleport(respawn);
            victim.playSound(respawn, Sound.ENTITY_ENDERMAN_TELEPORT, 1f, 1f);
        }
    }

    @EventHandler
    public void onPlayerRightClick(PlayerInteractEvent event) {
        Player player = event.getPlayer();

        // Only Sukuna passive players
        if (!ClassManager.getPlayerClass(player.getUniqueId())
                .getPassives().stream()
                .anyMatch(p -> p.getId().equals("sukuna_passive"))) return;

        if (event.getHand() != EquipmentSlot.HAND) return;

        // Only right-click actions
        if (event.getAction() != Action.RIGHT_CLICK_AIR && event.getAction() != Action.RIGHT_CLICK_BLOCK) return;

        // Only if holding a sword
        Material mat = player.getInventory().getItemInMainHand().getType();
        if (!(mat == Material.WOODEN_SWORD || mat == Material.STONE_SWORD ||
                mat == Material.IRON_SWORD || mat == Material.GOLDEN_SWORD ||
                mat == Material.DIAMOND_SWORD || mat == Material.NETHERITE_SWORD)) return;

        World world = player.getWorld();
        Location origin = player.getEyeLocation();
        Vector forward = origin.getDirection().normalize();

// Determine proper up reference
        Vector upRef = Math.abs(forward.getY()) > 0.98 ? new Vector(1, 0, 0) : new Vector(0, 1, 0);

// Base right vector
        Vector right = forward.clone().crossProduct(upRef).normalize();

// Play sound for nearby players (squared distance check)
        double radiusSq = 20 * 20;
        Location playerLoc = player.getLocation();
        for (Player p : playerLoc.getWorld().getPlayers()) {
            if (p.getLocation().distanceSquared(playerLoc) <= radiusSq) {
                p.playSound(playerLoc, Sound.ENTITY_WARDEN_ATTACK_IMPACT, 0.5f, 2f);
            }
        }

// Slash configuration
        double range = 100;
        double halfLength = 3.5;
        double step = 0.5;
        double rotationDegrees = 25;

// Prepare two rotated slashes (precomputed)
        Vector[] slashRotations = new Vector[]{
                rotateAroundAxis(right, forward, Math.toRadians(rotationDegrees)),
                rotateAroundAxis(right, forward, Math.toRadians(-rotationDegrees))
        };

// Precompute all offsets for each slash (reused)
        List<Vector[]> slashOffsetsList = new ArrayList<>();
        for (Vector baseSlash : slashRotations) {
            int pointsCount = (int) Math.round(halfLength * 2 / 0.2) + 1; // make sure pointsCount is correct
            Vector[] offsets = new Vector[pointsCount];
            int idx = 0;
            for (int stepIdx = 0; stepIdx < pointsCount; stepIdx++) {
                double offset = -halfLength + stepIdx * 0.2;
                Vector point = baseSlash.clone().multiply(offset);

                // Apply depth-wise curve toward forward
                double curve = -Math.pow(offset / halfLength, 2) + 1;
                point.add(forward.clone().multiply(curve * 4));

                offsets[idx++] = point; // guaranteed to fill every slot
            }
            slashOffsetsList.add(offsets);
        }

// Run slash over time
        for (Vector[] offsets : slashOffsetsList) {
            // Track traveled distance for each point
            double[] traveled = new double[offsets.length];

            new BukkitRunnable() {
                @Override
                public void run() {
                    boolean anyActive = false;

                    for (int i = 0; i < offsets.length; i++) {
                        if (traveled[i] >= range) continue;

                        // Move point forward
                        offsets[i].add(forward.clone().multiply(step));
                        traveled[i] += step;
                        anyActive = true;

                        Location loc = origin.clone().add(offsets[i]);
                        world.spawnParticle(Particle.SWEEP_ATTACK, loc, 1, 0, 0, 0, 0);

                        if (cut(world, player, loc, forward) || traveled[i] >= range) {
                            traveled[i] = range; // mark as finished so this point stops moving
                        }
                    }

                    if (!anyActive) cancel(); // All points finished
                }
            }.runTaskTimer(plugin, 0L, 1L);
        }
    }

    // Helper: rotate a vector around an axis
    private Vector rotateAroundAxis(Vector v, Vector axis, double angleRad) {
        return v.clone().rotateAroundAxis(axis, angleRad);
    }


    private boolean cut(World world, Player player, Location loc, Vector knockDir) {
        boolean removedSomething = false;

        if (!loc.isGenerated()) return false;

        Material blockType = loc.getBlock().getType();
        if (blockType != Material.AIR && blockType != Material.BEDROCK) {
            loc.getBlock().setType(Material.AIR, false);
            removedSomething = true;
        }

        for (Entity entity : world.getNearbyEntities(loc, 0.7, 0.7, 0.7)) {
            if (entity instanceof LivingEntity target && !target.equals(player)) {
                target.damage(800, player);
                target.setVelocity(knockDir.clone().multiply(3));
                removedSomething = true;
            }
        }

        return removedSomething;
    }
    @EventHandler
    public void onMove(PlayerMoveEvent e) {
        Player player = e.getPlayer();

        // Only Sukuna passive players
        if (!ClassManager.getPlayerClass(player.getUniqueId())
                .getPassives().stream()
                .anyMatch(p -> p.getId().equals("sukuna_passive"))) return;


        Material m = player.getLocation().getBlock().getType();
        if (m == Material.WATER || m == Material.LAVA) {
            Vector direction = player.getLocation().getDirection().normalize();
            Vector velocity = player.getVelocity().add(direction.multiply(0.1));
            player.setVelocity(velocity);
        }
    }

    public String getDescription() {
        return "Invincible or sum idk";
    }
}
