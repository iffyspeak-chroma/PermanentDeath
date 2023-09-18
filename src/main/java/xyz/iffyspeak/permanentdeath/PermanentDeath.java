package xyz.iffyspeak.permanentdeath;

import dev.dejvokep.boostedyaml.YamlDocument;
import dev.dejvokep.boostedyaml.settings.dumper.DumperSettings;
import dev.dejvokep.boostedyaml.settings.general.GeneralSettings;
import dev.dejvokep.boostedyaml.settings.loader.LoaderSettings;
import dev.dejvokep.boostedyaml.settings.updater.UpdaterSettings;
import org.bukkit.Bukkit;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import xyz.iffyspeak.permanentdeath.Tools.Globals;
import xyz.iffyspeak.permanentdeath.Tools.SQL.MySQL;
import xyz.iffyspeak.permanentdeath.Tools.SQL.SQLToolkit;

import java.io.File;
import java.util.Objects;

public final class PermanentDeath extends JavaPlugin {

    @Override
    public void onEnable() {
        // Plugin startup logic
        try {
            Globals.Configuration.configuration = YamlDocument.create(new File(getDataFolder(), "configuration.yml"), Objects.requireNonNull(getResource("configuration.yml")),
                    GeneralSettings.DEFAULT,
                    LoaderSettings.DEFAULT,
                    DumperSettings.DEFAULT,
                    UpdaterSettings.DEFAULT
            );

            Globals.Configuration.loadLocalConfig();

            if (Globals.Configuration.configuration.getBoolean("database.enabled"))
            {
                Globals.Database.useDatabase = true;
                try {
                    Globals.Database.mySQL = new MySQL();
                    Globals.Database.mySQL.connect();
                } catch (Exception e)
                {
                    Bukkit.getLogger().severe(e.toString());
                    Bukkit.getLogger().severe("Unable to connect to database.\nDo you have the right credentials?");
                }
            } else
            {
                Bukkit.getLogger().info("Not using databases.");
            }

            if (Globals.Database.mySQL != null)
            {
                if (Globals.Database.mySQL.isConnected())
                {
                    Bukkit.getLogger().info("Successfully connected to database.");
                    Bukkit.getLogger().info("Checking tables");
                    SQLToolkit.createTable(Globals.Database.mySQL);
                }
            }
        } catch (Exception e)
        {
            Bukkit.getLogger().severe(e.toString());
        }

        Globals.Implementation.initializeItems();
        getServer().addRecipe(Globals.Implementation.supershard.getRecipe());
        getServer().addRecipe(Globals.Implementation.untappedHeartcore.getRecipe());
        getServer().addRecipe(Globals.Implementation.heartcore.getRecipe());

        getServer().getPluginManager().registerEvents(new EventListener(), this);

        getServer().getScheduler().scheduleSyncRepeatingTask(this, () -> {

            for (Player p : Bukkit.getOnlinePlayers())
            {
                if (p.getHealth() % 2 == 0)
                {
                    Objects.requireNonNull(p.getAttribute(Attribute.GENERIC_MAX_HEALTH)).setBaseValue(p.getHealth());
                }
            }

        }, 0, 5);

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        if (Globals.Database.mySQL != null)
        {
            Globals.Database.mySQL.disconnect();
        }
    }
}
