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
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.inventory.ItemStack;

public class PlayerDoingEvent implements Listener {

    MiniGames miniGames;

    public PlayerDoingEvent() {
        this.miniGames = FreedyMinigameMaker.miniGames;
    }

    @EventHandler
    public void onSneak(PlayerToggleSneakEvent event) {
        final Player player = event.getPlayer();
        if (this.miniGames.isJoined(player)) {
            final MiniGame miniGame = this.miniGames.getJoined(player);
            for (final String cmd : miniGame.getMessageList("sneakCmd")) {
                final String output = miniGame.executeEventCommands(cmd
                        , player);
                if (output.equals("false")) {
                    event.setCancelled(true);
                }
            }
        }
    }



    @EventHandler
    public void onSwapHandItems(PlayerSwapHandItemsEvent event) {
        final Player player = event.getPlayer();
        if (this.miniGames.isJoined(player)) {
            final MiniGame miniGame = this.miniGames.getJoined(player);
            ItemStack itemStack = event.getMainHandItem();
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


            ItemStack itemStack2 = event.getMainHandItem();
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
                final String output = miniGame.executeEventCommands(cmd
                                .replace("{itemName}", itemName)
                                .replace("{itemAmount}", itemAmount)
                                .replace("{itemDurability}", itemDurability)
                                .replace("{itemType}", itemType)
                                .replace("{offItemName}", itemName2)
                                .replace("{offItemAmount}", itemAmount2)
                                .replace("{offItemDurability}", itemDurability2)
                                .replace("{offItemType}", itemType2)
                        , player);
                if (output.equals("false")) {
                    event.setCancelled(true);
                }
            }
        }
    }


    @EventHandler
    public void onItemHeld(PlayerItemHeldEvent event) {
        final Player player = event.getPlayer();
        if (this.miniGames.isJoined(player)) {
            final MiniGame miniGame = this.miniGames.getJoined(player);
            for (final String cmd : miniGame.getMessageList("itemHeldCmd")) {
                final String output = miniGame.executeEventCommands(cmd
                        .replace("{newSlot}", String.valueOf(event.getNewSlot()))
                        .replace("{previousSlot}", String.valueOf(event.getPreviousSlot()))
                        , player);
                if (output.equals("false")) {
                    event.setCancelled(true);
                }
            }
        }
    }

    @EventHandler
    public void onFish(PlayerFishEvent event) {
        final Player player = event.getPlayer();
        if (this.miniGames.isJoined(player)) {
            final MiniGame miniGame = this.miniGames.getJoined(player);


                Entity entity = event.getCaught();
                String entityName = entity.getName();
                String entityUuid = entity.getUniqueId().toString();
                String entityType = entity.getType().name();

                Location loc = event.getHook().getLocation();
                String locString = loc.getWorld().getName() + ", " + loc.getX() + ", " + loc.getY() + ", " + loc.getZ();
                String cmdName;
                switch (event.getState()) {
                    case FISHING:
                        cmdName = "fishingThrowCmd";
                        break;
                    case REEL_IN:
                        cmdName = "fishingBackCmd";
                        break;
                    default:
                        return;
                }

            for (final String cmd : miniGame.getMessageList(cmdName)) {
                final String output = miniGame.executeEventCommands(cmd
                                .replace("{entityUuid}", entityUuid)
                                .replace("{entityName}", entityName)
                                .replace("{entityType}", entityType)
                                .replace("{hookLoc}", locString)
                        , player);
                if (output.equals("false")) {
                    event.setCancelled(true);
                }
            }
        }
    }

    @EventHandler
    public void onTame(EntityTameEvent event) {
        event.getOwner().getName();
        final Player player = Bukkit.getPlayer(event.getOwner().getUniqueId());
        if (player == null) return;
        if (this.miniGames.isJoined(player)) {
            final MiniGame miniGame = this.miniGames.getJoined(player);



            Entity entity = event.getEntity();
            String entityName = entity.getName();
            String entityUuid = entity.getUniqueId().toString();
            String entityType = entity.getType().name();


            for (final String cmd : miniGame.getMessageList("tameCmd")) {
                final String output = miniGame.executeEventCommands(cmd
                                .replace("{entityUuid}", entityUuid)
                                .replace("{entityName}", entityName)
                                .replace("{entityType}", entityType)
                        , player);
                if (output.equals("false")) {
                    event.setCancelled(true);
                }
            }
        }
    }



}
