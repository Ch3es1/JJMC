package me.camsbald.jJMC.abilities.impl;

import me.camsbald.jJMC.JJMC;
import me.camsbald.jJMC.abilities.Ability;
import me.camsbald.jJMC.classes.PlayerClass;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.Map;

import static me.camsbald.jJMC.manager.ClassManager.getPlayerClass;

public class SukunaUltimate implements Ability, Listener {

    private final JJMC plugin;
    private Player caster;
    private Map<Location, Material> originalBarrierBlocks = new HashMap<>();
    private Location center;
    private BukkitRunnable task;
    private final int attackRadius = 40;       // Outer bedrock sphere
    private final int innerRadius = attackRadius - 1; // Inner destruction radius
    private final int durationSeconds = 60;

    public SukunaUltimate(JJMC plugin) {
        this.plugin = plugin;
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    @Override
    public String getId() { return "sukuna_ultimate"; }

    @Override
    public String getName() { return "Sukuna Ultimate"; }

    @Override
    public void use(Player caster) {
        this.caster = caster;
        this.center = caster.getLocation().clone();

        caster.sendMessage("§cSukuna Ultimate activated! Domain Expansion!");

        // --- Gradual Domain Task ---
        task = new BukkitRunnable() {
            int ticksElapsed = 0;
            final int totalTicks = durationSeconds * 20;

            @Override
            public void run() {

                // Stop if caster dies
                if (!caster.isValid() || caster.isDead()) {
                    cleanupDomain();
                    cancel();
                    return;
                }

                if (ticksElapsed >= totalTicks) {
                    cleanupDomain();
                    cancel();
                    return;
                }

                // --- Gradually destroy the inner sphere (all layers) ---
                destroyInnerSphereLayer(ticksElapsed % innerRadius);

                // --- Gradually place bedrock barrier (outer sphere) ---
                placeBarrierLayer(ticksElapsed % attackRadius);

                // --- Damage & effects every second ---
                if (ticksElapsed % 20 == 0) {

                    for (LivingEntity entity : center.getWorld().getNearbyEntities(center, attackRadius, attackRadius, attackRadius)
                            .stream().filter(e -> e instanceof LivingEntity).map(e -> (LivingEntity) e).toList()) {
                        if (entity.equals(caster)) continue;

                        // Damage
                        entity.damage(5.0, caster);

                        // Slashing particle effect
                        entity.getWorld().spawnParticle(Particle.SWEEP_ATTACK, entity.getLocation().add(0,1,0), 10, 0.5, 1, 0.5, 0.1);

                        // Title and sound
                        if ((entity instanceof Player p) && (ticksElapsed == 1)) {
                            p.sendTitle("§4§lDomain Expansion!", "§cSukuna’s curse descends...", 5, 20, 5);
                            p.playSound(p.getLocation(), Sound.ENTITY_WITHER_SPAWN, 1.0f, 0.8f);
                        }
                    }
                }

                ticksElapsed++;
            }

            private void destroyInnerSphereLayer(int layer) {
                for (int x = -innerRadius; x <= innerRadius; x++) {
                    for (int y = -innerRadius; y <= innerRadius; y++) {
                        for (int z = -innerRadius; z <= innerRadius; z++) {
                            Location loc = center.clone().add(x, y, z);
                            double distance = loc.distance(center);
                            if ((int) distance == layer && distance < innerRadius) {
                                Block block = loc.getBlock();
                                if (block.getType() != Material.AIR && block.getType() != Material.BEDROCK) {
                                    block.setType(Material.AIR);
                                }
                            }
                        }
                    }
                }
            }

            private void placeBarrierLayer(int layer) {
                for (int x = -attackRadius; x <= attackRadius; x++) {
                    for (int y = -attackRadius; y <= attackRadius; y++) {
                        for (int z = -attackRadius; z <= attackRadius; z++) {
                            Location loc = center.clone().add(x, y, z);
                            double distance = loc.distance(center);
                            if ((int) distance == layer && distance >= innerRadius && distance <= attackRadius) {
                                Block block = loc.getBlock();
                                if (block.getType() != Material.BEDROCK) {
                                    originalBarrierBlocks.put(loc, block.getType());
                                    block.setType(Material.BEDROCK);
                                }
                            }
                        }
                    }
                }
            }

            private void cleanupDomain() {
                for (Map.Entry<Location, Material> entry : originalBarrierBlocks.entrySet()) {
                    entry.getKey().getBlock().setType(entry.getValue());
                }
                originalBarrierBlocks.clear();
                if (caster != null && caster.isValid()) {
                    caster.sendMessage("§cSukuna Ultimate ended!");
                }
            }

        };

        task.runTaskTimer(plugin, 0L, 1L); // run every tick
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        if (event.getPlayer().equals(caster)) {
            if (task != null) task.cancel();
            cleanupDomain();
        }
    }

    @EventHandler
    public void onPlayerTeleport(PlayerTeleportEvent event) {
        if (event.getPlayer().equals(caster) && !event.getFrom().getWorld().equals(event.getTo().getWorld())) {
            if (task != null) task.cancel();
            cleanupDomain();
        }
    }

    private void cleanupDomain() {
        if (originalBarrierBlocks == null || originalBarrierBlocks.isEmpty()) return;
        for (Map.Entry<Location, Material> entry : originalBarrierBlocks.entrySet()) {
            entry.getKey().getBlock().setType(entry.getValue());
        }
        if (caster != null && caster.isValid()) {
            caster.sendMessage("§cSukuna Ultimate ended!");
        }
        originalBarrierBlocks.clear();
        task = null;
    }

    public String getDescription() {
        return "The best domain expansion(arguably)";
    }
}
