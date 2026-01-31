package me.camsbald.jJMC.recipes;

import me.camsbald.jJMC.JJMC;
import me.camsbald.jJMC.items.CustomItemManager;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.List;

public class Hunting_Sword_Recipe {

    private final JJMC plugin;

    public Hunting_Sword_Recipe(JJMC plugin) {
        this.plugin = plugin;
    }

    public void registerRecipes() {
        ItemStack item = new ItemStack(Material.IRON_SWORD);
        ItemMeta meta = item.getItemMeta();

// Display name & lore (optional)
        meta.setDisplayName("§cHunting §aSword");
        meta.setLore(List.of("§7Use to point towards nearest player within 100 blocks"));

// ⚡ Add PersistentDataContainer entry
        NamespacedKey key = new NamespacedKey(JJMC.getInstance(), "hunting_sword");
        meta.getPersistentDataContainer().set(key, PersistentDataType.STRING, "hunting_sword");

        item.setItemMeta(meta);

        ShapedRecipe recipe = new ShapedRecipe(key, item);
        recipe.shape(" I ",
                "ICI",
                " S ");

        recipe.setIngredient('I', Material.IRON_INGOT);
        recipe.setIngredient('C', Material.COMPASS);
        recipe.setIngredient('S', Material.STICK);

        Bukkit.addRecipe(recipe);
    }
}
