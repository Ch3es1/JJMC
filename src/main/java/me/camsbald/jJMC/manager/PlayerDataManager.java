package me.camsbald.jJMC.manager;

import me.camsbald.jJMC.JJMC;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;
import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.UUID;

public class PlayerDataManager {

    private final JJMC plugin;
    private final File file;
    private FileConfiguration data;

    // Add these in PlayerDataManager.java

    public List<UUID> getAllPlayerUUIDs() {
        if (data.getConfigurationSection("players") == null) return List.of();
        return data.getConfigurationSection("players").getKeys(false).stream()
                .map(UUID::fromString)
                .collect(Collectors.toList());
    }

    public Map<String, Long> getAllCooldownsAsLong(UUID uuid) {
        Map<String, Long> result = new HashMap<>();
        Map<String, Object> map = getAllCooldowns(uuid);
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            if (entry.getValue() instanceof Long l) result.put(entry.getKey(), l);
            else if (entry.getValue() instanceof Integer i) result.put(entry.getKey(), i.longValue());
        }
        return result;
    }


    public PlayerDataManager(JJMC plugin) {
        this.plugin = plugin;
        this.file = new File(plugin.getDataFolder(), "players.yml");
        if (!file.exists()) {
            file.getParentFile().mkdirs();
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        this.data = YamlConfiguration.loadConfiguration(file);
    }

    // --- Classes ---

    public void setPlayerClass(UUID uuid, String classId) {
        data.set("players." + uuid.toString() + ".class", classId);
    }

    public String getPlayerClass(UUID uuid) {
        return data.getString("players." + uuid.toString() + ".class");
    }

    public boolean hasClass(UUID uuid) {
        return data.contains("players." + uuid.toString() + ".class");
    }

    // --- Cooldowns ---
    public void setCooldown(UUID uuid, String abilityId, long endTime) {
        data.set("players." + uuid + ".cooldowns." + abilityId, endTime);
    }

    public long getCooldown(UUID uuid, String abilityId) {
        return data.getLong("players." + uuid + ".cooldowns." + abilityId, 0L);
    }

    public Map<String, Object> getAllCooldowns(UUID uuid) {
        return data.getConfigurationSection("players." + uuid + ".cooldowns") != null
                ? data.getConfigurationSection("players." + uuid + ".cooldowns").getValues(false)
                : Map.of();
    }

    // --- Save & reload ---

    public void save() {
        try {
            data.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void reload() {
        data = YamlConfiguration.loadConfiguration(file);
    }

    // Periodic save
    public void startAutoSave() {
        Bukkit.getScheduler().runTaskTimer(plugin, this::save, 20 * 60 * 5L, 20 * 60 * 5L); // every 5 mins
    }
}
