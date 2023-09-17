package xyz.iffyspeak.permanentdeath.Tools.SQL;

import org.bukkit.Bukkit;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class SQLToolkit {

    public static void createTable(MySQL sql)
    {
        PreparedStatement ps;
        try {
            ps = sql.getConnection().prepareStatement("CREATE TABLE IF NOT EXISTS lifelist "
                    + "(NAME VARCHAR(100),UUID VARCHAR(100),LIFECOUNT INT,PRIMARY KEY (UUID))");
            ps.executeUpdate();
        } catch (Exception e)
        {
            Bukkit.getLogger().severe(e.toString());
            //e.printStackTrace();
        }
    }

    public static void addPlayer(MySQL sql, String uuid, String name, int lives)
    {
        try {
            if (!uuidExists(sql, uuid))
            {
                PreparedStatement ps = sql.getConnection().prepareStatement("INSERT IGNORE INTO lifelist(NAME,UUID,LIFECOUNT) VALUES (?,?,?)");
                ps.setString(1, name);
                ps.setString(2, uuid);
                ps.setInt(3, lives);
                ps.executeUpdate();

                return;
            }

        } catch (Exception e)
        {
            Bukkit.getLogger().severe(e.toString());
            //e.printStackTrace();
        }
    }

    public static void setPlayerLives(MySQL sql, String uuid, int lives)
    {
        try {
            PreparedStatement ps = sql.getConnection().prepareStatement("UPDATE lifelist SET LIFECOUNT=? WHERE UUID=?");
            ps.setInt(1, lives);
            ps.setString(2, uuid);
            ps.executeUpdate();
        } catch (Exception e)
        {
            Bukkit.getLogger().severe(e.toString());
            //e.printStackTrace();
        }
    }

    public static int getPlayerLives(MySQL sql, String uuid)
    {
        try {
            PreparedStatement ps = sql.getConnection().prepareStatement("SELECT LIFECOUNT FROM lifelist WHERE UUID=?");
            ps.setString(1, uuid);
            ResultSet rs = ps.executeQuery();
            int rslives = -1;

            if (rs.next())
            {
                rslives = rs.getInt("LIFECOUNT");
                return rslives;
            }
        } catch (Exception e)
        {
            Bukkit.getLogger().severe(e.toString());
            //e.printStackTrace();
        }
        return -1;
    }

    public static boolean uuidExists(MySQL sql, String uuid)
    {
        try {
            PreparedStatement ps = sql.getConnection().prepareStatement("SELECT * FROM lifelist WHERE UUID=?");
            ps.setString(1, uuid);

            ResultSet results = ps.executeQuery();
            if (results.next())
            {
                // There is a player
                return true;
            }
            // No player
            return false;
        } catch (Exception e)
        {
            Bukkit.getLogger().severe(e.toString());
            //e.printStackTrace();
        }
        return false;
    }

}
