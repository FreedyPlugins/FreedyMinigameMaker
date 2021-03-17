// 
// Decompiled by Procyon v0.5.36
// 

package freedy.freedyminigamemaker.events;

import freedy.freedyminigamemaker.FreedyMinigameMaker;
import freedy.freedyminigamemaker.MiniGame;
import freedy.freedyminigamemaker.MiniGames;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityTameEvent;
import org.bukkit.event.player.*;
import org.bukkit.inventory.ItemStack;

public class PlayerDoingEvent implements Listener
{
    MiniGames miniGames;
    
    public PlayerDoingEvent() {
        this.miniGames = FreedyMinigameMaker.miniGames;
    }
    
    @EventHandler
    public void onSneak(final PlayerToggleSneakEvent event) {
        final Player player = event.getPlayer();
        if (this.miniGames.isJoined(player)) {
            final MiniGame miniGame = this.miniGames.getJoined(player);
            for (final String cmd : miniGame.getMessageList("sneakCmd")) {
                final String output = miniGame.executeEventCommands(cmd, player);
                if (output.equals("false")) {
                    event.setCancelled(true);
                }
            }
        }
    }
    
    @EventHandler
    public void onSwapHandItems(final PlayerSwapHandItemsEvent event) {
        final Player player = event.getPlayer();
        if (this.miniGames.isJoined(player)) {
            final MiniGame miniGame = this.miniGames.getJoined(player);
            final ItemStack itemStack = event.getMainHandItem();
            String itemType = "none";
            String itemName = "none";
            String itemAmount = "none";
            String itemDurability = "none";
            if (itemStack != null) {
                itemType = itemStack.getType().name();
                itemDurability = String.valueOf(itemStack.getDurability());
                itemAmount = String.valueOf(itemStack.getAmount());
                if (itemStack.hasItemMeta() && itemStack.getItemMeta().hasDisplayName()) {
                    itemName = itemStack.getItemMeta().getDisplayName();
                }
            }
            final ItemStack itemStack2 = event.getMainHandItem();
            String itemType2 = "none";
            String itemName2 = "none";
            String itemAmount2 = "none";
            String itemDurability2 = "none";
            if (itemStack2 != null) {
                itemType2 = itemStack2.getType().name();
                itemDurability2 = String.valueOf(itemStack2.getDurability());
                itemAmount2 = String.valueOf(itemStack2.getAmount());
                if (itemStack2.hasItemMeta() && itemStack2.getItemMeta().hasDisplayName()) {
                    itemName2 = itemStack2.getItemMeta().getDisplayName();
                }
            }
            for (final String cmd : miniGame.getMessageList("swapHandCmd")) {
                final String output = miniGame.executeEventCommands(cmd.replace("{itemName}", itemName).replace("{itemAmount}", itemAmount).replace("{itemDurability}", itemDurability).replace("{itemType}", itemType).replace("{offItemName}", itemName2).replace("{offItemAmount}", itemAmount2).replace("{offItemDurability}", itemDurability2).replace("{offItemType}", itemType2), player);
                if (output.equals("false")) {
                    event.setCancelled(true);
                }
            }
        }
    }
    
    @EventHandler
    public void onItemHeld(final PlayerItemHeldEvent event) {
        final Player player = event.getPlayer();
        if (this.miniGames.isJoined(player)) {
            final MiniGame miniGame = this.miniGames.getJoined(player);
            for (final String cmd : miniGame.getMessageList("itemHeldCmd")) {
                final String output = miniGame.executeEventCommands(cmd.replace("{newSlot}", String.valueOf(event.getNewSlot())).replace("{previousSlot}", String.valueOf(event.getPreviousSlot())), player);
                if (output.equals("false")) {
                    event.setCancelled(true);
                }
            }
        }
    }
    
    @EventHandler
    public void onFish(final PlayerFishEvent event) {
        final Player player = event.getPlayer();
        if (this.miniGames.isJoined(player)) {
            final MiniGame miniGame = this.miniGames.getJoined(player);
            final Entity entity = event.getCaught();
            final String entityName = entity.getName();
            final String entityUuid = entity.getUniqueId().toString();
            final String entityType = entity.getType().name();
            final Location loc = event.getHook().getLocation();
            final String locString = loc.getWorld().getName() + ", " + loc.getX() + ", " + loc.getY() + ", " + loc.getZ();
            String cmdName = null;
            switch (event.getState()) {
                case FISHING: {
                    cmdName = "fishingThrowCmd";
                    break;
                }
                case REEL_IN: {
                    cmdName = "fishingBackCmd";
                    break;
                }
                default: {
                    return;
                }
            }
            for (final String cmd : miniGame.getMessageList(cmdName)) {
                final String output = miniGame.executeEventCommands(cmd.replace("{entityUuid}", entityUuid).replace("{entityName}", entityName).replace("{entityType}", entityType).replace("{hookLoc}", locString), player);
                if (output.equals("false")) {
                    event.setCancelled(true);
                }
            }
        }
    }
    
    @EventHandler
    public void onTame(final EntityTameEvent event) {
        event.getOwner().getName();
        final Player player = Bukkit.getPlayer(event.getOwner().getUniqueId());
        if (player == null) {
            return;
        }
        if (this.miniGames.isJoined(player)) {
            final MiniGame miniGame = this.miniGames.getJoined(player);
            final Entity entity = event.getEntity();
            final String entityName = entity.getName();
            final String entityUuid = entity.getUniqueId().toString();
            final String entityType = entity.getType().name();
            for (final String cmd : miniGame.getMessageList("tameCmd")) {
                final String output = miniGame.executeEventCommands(cmd.replace("{entityUuid}", entityUuid).replace("{entityName}", entityName).replace("{entityType}", entityType), player);
                if (output.equals("false")) {
                    event.setCancelled(true);
                }
            }
        }
    }
    
    @EventHandler
    public void onRespawn(final PlayerRespawnEvent event) {
        final Player player = event.getPlayer();
        if (this.miniGames.isJoined(player)) {
            final MiniGame miniGame = this.miniGames.getJoined(player);
            for (final String cmd : miniGame.getMessageList("respawnCmd")) {
                final String output = miniGame.executeEventCommands(cmd, player);
                if (output.equals("false")) {}
            }
            final Location respawnPoint = miniGame.getPlayerData(player).getRespawnPoint();
            if (respawnPoint != null) {
                event.setRespawnLocation(respawnPoint);
            }
        }
    }
}
