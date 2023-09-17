package xyz.iffyspeak.permanentdeath.Tools;

import dev.dejvokep.boostedyaml.YamlDocument;
import org.bukkit.Bukkit;
import xyz.iffyspeak.permanentdeath.Tools.SQL.MySQL;

public class Globals {
    public static class Language {
        public static class PlayerOnly {
            public static String LivesLeftBegin = "<red>You died!<br>You have <yellow>%lives%</yellow> lives remaining.</red>";
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
                public static String UseFailPrimary = "<red>You await that familiar searing pain but...</red>\n<color:#872d2d>nothing.</color> <color:#2b2b2b>(You dangerous lunatic)</color>";
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
}
