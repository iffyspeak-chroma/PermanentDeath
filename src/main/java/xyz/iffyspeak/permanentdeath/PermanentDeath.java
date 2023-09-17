package xyz.iffyspeak.permanentdeath;

import dev.dejvokep.boostedyaml.YamlDocument;
import dev.dejvokep.boostedyaml.settings.dumper.DumperSettings;
import dev.dejvokep.boostedyaml.settings.general.GeneralSettings;
import dev.dejvokep.boostedyaml.settings.loader.LoaderSettings;
import dev.dejvokep.boostedyaml.settings.updater.UpdaterSettings;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import xyz.iffyspeak.permanentdeath.Tools.Globals;

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
        } catch (Exception e)
        {
            Bukkit.getLogger().severe(e.toString());
        }

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
