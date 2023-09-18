package xyz.iffyspeak.permanentdeath;

import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import xyz.iffyspeak.permanentdeath.Tools.Globals;
import xyz.iffyspeak.permanentdeath.Tools.SQL.MySQL;
import xyz.iffyspeak.permanentdeath.Tools.SQL.SQLToolkit;
import xyz.iffyspeak.permanentdeath.Tools.Toolkit;

import java.util.Objects;

public class EventListener implements Listener {
    MySQL sql;

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e)
    {
        Player localplayer = e.getPlayer();

        localplayer.undiscoverRecipe(Globals.Implementation.supershard.getKey());
        localplayer.undiscoverRecipe(Globals.Implementation.untappedHeartcore.getKey());
        localplayer.undiscoverRecipe(Globals.Implementation.heartcore.getKey());

        localplayer.discoverRecipe(Globals.Implementation.supershard.getKey());
        localplayer.discoverRecipe(Globals.Implementation.untappedHeartcore.getKey());
        localplayer.discoverRecipe(Globals.Implementation.heartcore.getKey());

        // Let's first check and see if they are new.
        // If they are: Assign their lives value and return out of the void
        if (Globals.Database.mySQL != null)
        {
            sql = Globals.Database.mySQL;
            if (SQLToolkit.uuidExists(Globals.Database.mySQL, localplayer.getUniqueId().toString()))
            {
                SQLToolkit.addPlayer(sql, localplayer.getUniqueId().toString(), localplayer.getName(), 3);
                return;
            }
        }

        // Now let's check and see if they are already fully dead
        // If they are: We'll put their gamemode into spectator
        if (Globals.Database.mySQL != null)
        {
            sql = Globals.Database.mySQL;
            if (SQLToolkit.getPlayerLives(sql, localplayer.getUniqueId().toString()) <= 0)
            {
                localplayer.setGameMode(GameMode.SPECTATOR);
                localplayer.sendMessage(MiniMessage.miniMessage().deserialize(Toolkit.StringManipulation.parsePdArgs(Globals.Language.PlayerOnly.FinalDeath, localplayer)));
            }
        }
    }

    @EventHandler
    public void onPlayerRespawn(PlayerRespawnEvent e)
    {
        Player localplayer = e.getPlayer();

        // Let's check and see if the player is fully dead
        // If they are: We'll put their gamemode into spectator
        if (Globals.Database.mySQL != null)
        {
            sql = Globals.Database.mySQL;
            if (SQLToolkit.getPlayerLives(sql, localplayer.getUniqueId().toString()) <= 0)
            {
                localplayer.setGameMode(GameMode.SPECTATOR);
                localplayer.sendMessage(MiniMessage.miniMessage().deserialize(Toolkit.StringManipulation.parsePdArgs(Globals.Language.PlayerOnly.FinalDeath, localplayer)));
            }
        }
        Objects.requireNonNull(localplayer.getAttribute(Attribute.GENERIC_MAX_HEALTH)).setBaseValue(20d);
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent e)
    {
        Player localplayer = e.getPlayer();

        if (Globals.Database.mySQL != null)
        {
            sql = Globals.Database.mySQL;
            int livesrem = SQLToolkit.getPlayerLives(sql, localplayer.getUniqueId().toString());
            livesrem--;

            // First, lets remove 1 life from the player.
            SQLToolkit.setPlayerLives(sql, localplayer.getUniqueId().toString(), livesrem);

            // Now we should check if they've hit zero
            // If they have: Let's send out the alert
            if (livesrem <= 0)
            {
                for (Player p : Bukkit.getOnlinePlayers())
                {
                    p.sendMessage(MiniMessage.miniMessage().deserialize(Toolkit.StringManipulation.parsePdArgs(Globals.Language.AllOnline.AnnounceFinalPlayerDeath, localplayer)));
                }

                return;
            }

            // Now that we've done the most important check
            // Let's send out the announcement for lives
            for (Player p : Bukkit.getOnlinePlayers())
            {
                p.sendMessage(MiniMessage.miniMessage().deserialize(Toolkit.StringManipulation.parsePdArgs(Globals.Language.AllOnline.AnnouncePlayerLives, localplayer)));
            }

            // Let's now check if they are on their last life
            // If they are we send the final life to them
            if (livesrem == 1)
            {
                localplayer.sendMessage(MiniMessage.miniMessage().deserialize(Toolkit.StringManipulation.parsePdArgs(Globals.Language.PlayerOnly.FinalLifeMessage, localplayer)));
            } else
            {
                localplayer.sendMessage(MiniMessage.miniMessage().deserialize(Toolkit.StringManipulation.parsePdArgs(Globals.Language.PlayerOnly.LivesLeft, localplayer)));
            }
        }
    }

    /*
    @EventHandler
    public void onEntityTakeDamage(EntityDamageEvent _e)
    {
        if (Settings.General.Lifeforce.Enabled)
        {
            // Load up the affected entity and get a spot ready for the player
            Entity affectedEntity = _e.getEntity();
            Player localPlayer;

            // Make sure it's a player taking damage before going further in this event
            if (affectedEntity.getType() != EntityType.PLAYER)
            {
                return;
            }

            // Setup that spot because now we know it's the player
            localPlayer = (Player) _e.getEntity();
            double old_max = Objects.requireNonNull(localPlayer.getAttribute(Attribute.GENERIC_MAX_HEALTH)).getBaseValue();

            // This stuff should only apply if they've taken a heart or more of damage.
            if (Tools.CalculateHeartsUsed(_e.getFinalDamage()) >= 1)
            {
                Objects.requireNonNull(localPlayer.getAttribute(Attribute.GENERIC_MAX_HEALTH)).setBaseValue(old_max - (Tools.CalculateHeartsUsed(_e.getFinalDamage()) * 2));
                localPlayer.sendHealthUpdate();
            }
        }
    }
     */ // Stripped straight from old version of the plugin. Will help with implementation of the new version

    @EventHandler // For the heart healing bullshit
    public void onPlayerInteract(PlayerInteractEvent e)
    {
        Player localplayer = e.getPlayer();
        //Bukkit.getLogger().info("interact event detected");
        if (e.getHand() == EquipmentSlot.HAND && e.getAction().isRightClick())
        {
            //Bukkit.getLogger().info("is right hand");
            if (e.getItem() != null && e.getItem().getItemMeta().getPersistentDataContainer().has(new NamespacedKey("this_plugin", "sucks_ass"), PersistentDataType.BOOLEAN))
            {

                // If their max health is already above that max overheal amount
                // (This is checking for 15 hearts)
                if (!(localplayer.getHealth() < Objects.requireNonNull(localplayer.getAttribute(Attribute.GENERIC_MAX_HEALTH)).getValue()))
                {
                    if (Objects.requireNonNull(localplayer.getAttribute(Attribute.GENERIC_MAX_HEALTH)).getValue() >= 30)
                    {
                        localplayer.sendMessage(MiniMessage.miniMessage().deserialize(Toolkit.StringManipulation.parsePdArgs(Globals.Language.Item.Heartcore.UseFailFull, localplayer)));
                        //Bukkit.getLogger().info("too many hearts, ignoring.");
                    } else
                    {
                        localplayer.sendMessage(MiniMessage.miniMessage().deserialize(Toolkit.StringManipulation.parsePdArgs(Globals.Language.Item.Heartcore.Use, localplayer)));
                        Objects.requireNonNull(localplayer.getAttribute(Attribute.GENERIC_MAX_HEALTH)).setBaseValue(Objects.requireNonNull(localplayer.getAttribute(Attribute.GENERIC_MAX_HEALTH)).getValue() + 2);
                        localplayer.setHealth(Objects.requireNonNull(localplayer.getAttribute(Attribute.GENERIC_MAX_HEALTH)).getValue());
                        localplayer.sendHealthUpdate();

                        Objects.requireNonNull(e.getItem()).setAmount(e.getItem().getAmount() - 1);
                        localplayer.getInventory().addItem(new ItemStack(Material.GUNPOWDER));
                        //localplayer.updateInventory();
                        // ^ Paper claims it is not necessary which I'm inclined to believe
                        //Bukkit.getLogger().info("update health");
                    }
                } else
                {
                    localplayer.sendMessage(MiniMessage.miniMessage().deserialize(Globals.Language.Item.Heartcore.UseFailHealth));
                }
            }
        }
    }


}
