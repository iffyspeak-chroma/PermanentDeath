package xyz.iffyspeak.permanentdeath.Tools.SQL;

import org.bukkit.Bukkit;
import xyz.iffyspeak.permanentdeath.Tools.Globals;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MySQL {

    private Connection connection;

    public boolean isConnected()
    {
        return (connection == null ? false : true);
    }

    public void connect() throws ClassNotFoundException, SQLException
    {
        if (!isConnected())
        {
            try {
                Class.forName("com.mysql.cj.jdbc.Driver");
                connection = DriverManager.getConnection("jdbc:mysql://" +
                                Globals.Database.dbHost +
                                ":" + Globals.Database.dbPort +
                                "/" + Globals.Database.dbDatabase +
                                "?useSSL=" + Globals.Database.useSsl(),
                        Globals.Database.dbUsername, Globals.Database.dbPassword
                );
            }
            catch (Exception e) {
                // TODO Auto-generated catch block
                //e.printStackTrace();
            }
        }
    }

    public void disconnect()
    {
        if (isConnected())
        {
            try {
                connection.close();
            } catch (Exception e)
            {
                Bukkit.getLogger().severe(e.toString());
                //e.printStackTrace();
            }
        }
    }

    public Connection getConnection()
    {
        return connection;
    }
}