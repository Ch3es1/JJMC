package me.camsbald.jJMC.items;

import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.NamespacedKey;
import me.camsbald.jJMC.JJMC;

import java.util.HashMap;
import java.util.Map;
import java.util.Collection;

public class CustomItemManager {

    private static final Map<String, CustomItem> items = new HashMap<>();

    public static void register(CustomItem item) {
        items.put(item.getId(), item);
    }

    public static CustomItem getById(String id) {
        return items.get(id.toLowerCase());
    }

    public static Collection<CustomItem> getAllItems() {
        return items.values();
    }

    public static CustomItem getByItemStack(ItemStack item) {
        if (item == null || !item.hasItemMeta()) return null;
        return items.values().stream().filter(ci -> {
            var meta = item.getItemMeta();
            if (meta == null) return false;
            NamespacedKey key = new NamespacedKey(JJMC.getInstance(), ci.getId());
            return meta.getPersistentDataContainer().has(key, PersistentDataType.STRING);
        }).findFirst().orElse(null);
    }
}
