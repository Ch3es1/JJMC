package me.camsbald.jJMC.items;

import me.camsbald.jJMC.abilities.Ability;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.NamespacedKey;
import org.bukkit.plugin.java.JavaPlugin;

public class CustomItem {
    private final String id;
    private final ItemStack item;
    private final Ability ability;

    public CustomItem(String id, Material material, String displayName, Ability ability, JavaPlugin plugin) {
        this.id = id.toLowerCase();
        this.ability = ability;

        this.item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            meta.setDisplayName(displayName);
            NamespacedKey key = new NamespacedKey(plugin, this.id); // must use plugin
            meta.getPersistentDataContainer().set(key, PersistentDataType.STRING, this.id);
            item.setItemMeta(meta);
        }
    }

    public String getId() { return id; }
    public ItemStack getItem() { return item; }
    public Ability getAbility() { return ability; }
}
