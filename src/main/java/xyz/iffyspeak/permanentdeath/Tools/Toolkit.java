package xyz.iffyspeak.permanentdeath.Tools;

import org.bukkit.entity.Player;
import xyz.iffyspeak.permanentdeath.Tools.SQL.SQLToolkit;

public class Toolkit {

    public static class StringManipulation
    {
        public static String parsePdArgs(String translatable, Player player)
        {
            String modify = translatable;

            if (Globals.Database.useDatabase)
            {
                int lives = SQLToolkit.getPlayerLives(Globals.Database.mySQL, player.getUniqueId().toString());
                modify = modify.replace("%lives%", String.valueOf(lives));
            } else
            {
                modify = modify.replace("%lives%", Globals.Configuration.configuration.getInt("playerinfo." + player.getUniqueId() + ".lives").toString());
            }

            modify = modify.replace("%player%", player.getName());
            modify = modify.replace("%coords%",
                    "x: " + player.getLocation().getBlockX() +
                    ", y: " + player.getLocation().getBlockY() +
                    ", z: " + player.getLocation().getBlockZ());

            modify = modify.replace("%px%", String.valueOf(player.getLocation().getBlockX()));
            modify = modify.replace("%py%", String.valueOf(player.getLocation().getBlockY()));
            modify = modify.replace("%pz%", String.valueOf(player.getLocation().getBlockZ()));

            return modify;
        }
    }

}
