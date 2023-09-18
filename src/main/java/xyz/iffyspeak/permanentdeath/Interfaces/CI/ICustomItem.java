package xyz.iffyspeak.permanentdeath.Interfaces.CI;

import org.bukkit.NamespacedKey;
import org.bukkit.inventory.CraftingRecipe;
import org.bukkit.inventory.ItemStack;

public interface ICustomItem {

    NamespacedKey getKey();
    ItemStack getItem();
    CraftingRecipe getRecipe();

}
