package me.camsbald.jJMC.manager;

import me.camsbald.jJMC.JJMC;
import me.camsbald.jJMC.recipes.Hunting_Sword_Recipe;
import me.camsbald.jJMC.recipes.Reroll_Token_Recipe;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.plugin.java.JavaPlugin;

public class RecipeManager {

    private final JavaPlugin plugin;

    public RecipeManager(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    public void registerRecipes() {
        new Reroll_Token_Recipe((JJMC) plugin).registerRecipes();
        new Hunting_Sword_Recipe((JJMC) plugin).registerRecipes();
    }
}
