package me.camsbald.jJMC.manager;

import me.camsbald.jJMC.JJMC;
import org.bukkit.Bukkit;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

public class CooldownManager {

    private static final Map<UUID, Map<String, Long>> cooldowns = new HashMap<>();
    private static final Map<String, Integer> abilityCooldowns = new HashMap<>(); // abilityId -> seconds
    private static JJMC plugin;

    public static void init(JJMC pluginInstance) {
        plugin = pluginInstance;

        // Load ability cooldowns from config
        if (plugin.getConfig().contains("abilities")) {
            for (String key : plugin.getConfig().getConfigurationSection("abilities").getKeys(false)) {
                int seconds = plugin.getConfig().getInt("abilities." + key);
                abilityCooldowns.put(key.toLowerCase(), seconds);
            }
        } else {
            Bukkit.getLogger().warning("[JJMC] No ability cooldowns found in config.yml!");
        }
    }

    public static boolean isOnCooldown(UUID playerId, String abilityId) {
        if (!cooldowns.containsKey(playerId)) return false;
        Map<String, Long> playerMap = cooldowns.get(playerId);
        return playerMap.containsKey(abilityId) && System.currentTimeMillis() < playerMap.get(abilityId);
    }

    public static void setCooldown(UUID playerId, String abilityId, String abilityName) {
        int seconds = abilityCooldowns.getOrDefault(abilityId.toLowerCase(), 10); // default 10s
        long expireTime = System.currentTimeMillis() + (seconds * 1000L);
        cooldowns.computeIfAbsent(playerId, k -> new HashMap<>()).put(abilityId.toLowerCase(), expireTime);

        // Schedule a task to notify player when cooldown ends
        Bukkit.getScheduler().runTaskLater(plugin, () -> {
            if (Bukkit.getPlayer(playerId) != null) {
                Bukkit.getPlayer(playerId).sendMessage("§aAbility §b" + abilityName + " §ais now off cooldown!");
                ClassManager.applyPassives(Objects.requireNonNull(Bukkit.getPlayer(playerId)));
            }
        }, seconds * 20L); // convert seconds to ticks
    }

    public static long getRemaining(UUID playerId, String abilityId) {
        if (!cooldowns.containsKey(playerId)) return 0;
        long expire = cooldowns.get(playerId).getOrDefault(abilityId.toLowerCase(), 0L);
        long remaining = expire - System.currentTimeMillis();
        return remaining > 0 ? remaining / 1000 : 0;
    }
}
