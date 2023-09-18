package xyz.iffyspeak.permanentdeath.Tools;

import dev.dejvokep.boostedyaml.YamlDocument;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.*;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import xyz.iffyspeak.permanentdeath.Interfaces.CI.ICustomItem;
import xyz.iffyspeak.permanentdeath.PermanentDeath;
import xyz.iffyspeak.permanentdeath.Tools.SQL.MySQL;

import java.util.List;

public class Globals {
    public static class Language {
        public static class PlayerOnly {
            public static String LivesLeft = "<red>You died!<br>You have <yellow>%lives%</yellow> lives remaining.</red>";
            public static String FinalLifeMessage = "<red><b><u>You died and are on your final life</u></b><br>If you die again, you will no longer be able to play.</red>";
            public static String FinalDeath = "<red>You died and can no longer continue to play.</red>";
        }

        public static class AllOnline {
            public static String AnnouncePlayerLives = "<yellow>%player%</yellow> <red>has died and has <dark_red>%lives%</dark_red> live(s) remaining.<br>Be aware that you are not permanent.</red>";
            public static String AnnounceFinalPlayerDeath = "<red><yellow>%player%</yellow> lost their final life<br>Visit their grave site at <yellow>%coords%</yellow></red>";
        }

        public static class Item {
            public static class Heartcore {
                public static String Use = "<green>You feel a searing pain that suddenly vanishes</green><br><color:#505050>The item you once held turns to nothing but ash and sulphur in your palm</color>";
                public static String UseFailFull = "<red>You await that familiar searing pain but...</red>\n<color:#872d2d>nothing.</color> <color:#2b2b2b>(You dangerous lunatic)</color>";
                public static String UseFailHealth = "<red>You cannot use this unless you are fully healed.</red>";

            }
        }
    }

    public static class Database {
        public static boolean useDatabase = false;
        public static String dbHost = "localhost";
        public static String dbPort = "3306";
        public static String dbDatabase = "iffyspeak";
        public static String dbUsername = "plugin";
        public static String dbPassword = "examplepassword";
        public static boolean dbUseSSL = false;
        public static String useSsl()
        {
            return (dbUseSSL ? "true" : "false");
        }
        public static MySQL mySQL = null;
    }

    public static class Configuration {
        public static YamlDocument configuration;

        /*
        public static void saveLocalConfig()
        {
            if (configuration != null)
            {
                try {
                    configuration.save();
                } catch (Exception e)
                {
                    Bukkit.getLogger().severe(e.toString());
                }
            }
        }
         */

        public static void loadLocalConfig()
        {
            Bukkit.getLogger().info("Loading configuration... Please wait.");
            Database.dbHost = Globals.Configuration.configuration.getString("database.host");
            Database.dbPort = Globals.Configuration.configuration.getString("database.port");
            Database.dbDatabase = Globals.Configuration.configuration.getString("database.database");
            Database.dbUsername = Globals.Configuration.configuration.getString("database.username");
            Database.dbPassword = Globals.Configuration.configuration.getString("database.password");
            Database.dbUseSSL = Globals.Configuration.configuration.getBoolean("database.useSSL");

        }
    }

    public static class Item {
        public static class Supershard implements ICustomItem {
            @Override
            public NamespacedKey getKey() {
                return new NamespacedKey(PermanentDeath.getPlugin(PermanentDeath.class), "supershard_key");
            }

            @Override
            public ItemStack getItem() {
                ItemStack supershard = new ItemStack(Material.HONEYCOMB);
                ItemMeta ssmeta = supershard.getItemMeta();
                ssmeta.displayName(MiniMessage.miniMessage().deserialize("<yellow><b>Supershard</b></yellow>"));
                ssmeta.lore(List.of(Component.text(""),
                        MiniMessage.miniMessage().deserialize("<obf><gradient:#404040:#ffffff:#404040>lol if u read this ur gay</gradient></obf>"),
                        Component.text(""),
                        MiniMessage.miniMessage().deserialize("<aqua>It's trying to tell me something</aqua>"),
                        MiniMessage.miniMessage().deserialize("<aqua>but I cannot decipher it.</aqua>"),
                        Component.text(""),
                        MiniMessage.miniMessage().deserialize("<red>Curse of Foreign Languages</red>")
                ));

                supershard.setItemMeta(ssmeta);

                return supershard;
            }

            @Override
            public CraftingRecipe getRecipe() {
                ShapelessRecipe recipe = new ShapelessRecipe(getKey(), getItem());
                recipe.addIngredient(new RecipeChoice.MaterialChoice(Material.AMETHYST_SHARD));
                recipe.addIngredient(new RecipeChoice.MaterialChoice(Material.END_CRYSTAL));
                recipe.addIngredient(new RecipeChoice.MaterialChoice(Material.ECHO_SHARD));

                return recipe;
            }
        }

        public static class UntappedHeartcore implements ICustomItem {
            @Override
            public NamespacedKey getKey() {
                return new NamespacedKey(PermanentDeath.getPlugin(PermanentDeath.class), "uhc_key");
            }

            @Override
            public ItemStack getItem() {
                ItemStack uh = new ItemStack(Material.HEART_OF_THE_SEA);
                ItemMeta uhmeta = uh.getItemMeta();
                uhmeta.displayName(MiniMessage.miniMessage().deserialize("<aqua><b>Untapped Heart Core</b></aqua>"));
                uhmeta.lore(List.of(Component.text(""),
                        MiniMessage.miniMessage().deserialize("<aqua>It feels powerful but I can't</aqua>"),
                        MiniMessage.miniMessage().deserialize("<aqua>quite use it just yet.</aqua>"),
                        Component.text(""),
                        MiniMessage.miniMessage().deserialize("<red>Curse of Usability</red>")
                ));

                uh.setItemMeta(uhmeta);

                return uh;
            }

            @Override
            public CraftingRecipe getRecipe() {
                ShapedRecipe recipe = new ShapedRecipe(getKey(), getItem());
                recipe.shape(" s ","sus"," s ");
                recipe.setIngredient('s', new RecipeChoice.ExactChoice(Implementation.supershard.getItem()));
                recipe.setIngredient('u', new RecipeChoice.MaterialChoice(Material.HEART_OF_THE_SEA));

                return recipe;
            }
        }

        public static class Heartcore implements ICustomItem {
            @Override
            public NamespacedKey getKey() {
                return new NamespacedKey(PermanentDeath.getPlugin(PermanentDeath.class), "heartcore_key");
            }

            @Override
            public ItemStack getItem() {
                ItemStack hc = new ItemStack(Material.HEART_OF_THE_SEA);
                ItemMeta hcmeta = hc.getItemMeta();
                hcmeta.displayName(MiniMessage.miniMessage().deserialize("<green><b>Heart Core</b></green>"));
                hcmeta.lore(List.of(Component.text(""),
                        MiniMessage.miniMessage().deserialize("<green><i>Knowing you are another step away</i></green>"),
                        MiniMessage.miniMessage().deserialize("<green><i>from death makes you feel more at ease.</i></green>"),
                        Component.text(""),
                        MiniMessage.miniMessage().deserialize("<red>(Press <key:key.use> to regain hearts)</red>"),
                        Component.text(""),
                        MiniMessage.miniMessage().deserialize("<aqua>Curse of calming</aqua>")
                ));

                // We're going try with persistent data containers
                // Since the right click function is not working anymore
                PersistentDataContainer pdc = hcmeta.getPersistentDataContainer();
                pdc.set(new NamespacedKey("this_plugin", "sucks_ass"), PersistentDataType.BOOLEAN, true);

                hc.setItemMeta(hcmeta);

                return hc;
            }

            @Override
            public CraftingRecipe getRecipe() {
                ShapelessRecipe recipe = new ShapelessRecipe(getKey(), getItem());
                recipe.addIngredient(new RecipeChoice.MaterialChoice(Material.GOLDEN_APPLE));
                recipe.addIngredient(new RecipeChoice.ExactChoice(Implementation.untappedHeartcore.getItem()));

                return recipe;
            }
        }

    }
    public static class Implementation {
        public static Item.Supershard supershard;
        public static Item.UntappedHeartcore untappedHeartcore;
        public static Item.Heartcore heartcore;

        public static void initializeItems()
        {
            supershard = new Item.Supershard();
            untappedHeartcore = new Item.UntappedHeartcore();
            heartcore = new Item.Heartcore();
        }
    }
}
