// 
// Decompiled by Procyon v0.5.36
// 

package freedy.freedyminigamemaker.events;

import freedy.freedyminigamemaker.FreedyMinigameMaker;
import freedy.freedyminigamemaker.MiniGame;
import freedy.freedyminigamemaker.MiniGames;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.inventory.ItemStack;

public class PickupEvent implements Listener
{
    MiniGames miniGames;
    
    public PickupEvent() {
        this.miniGames = FreedyMinigameMaker.miniGames;
    }
    
    @EventHandler
    public void onPickup(final PlayerPickupItemEvent event) {
        final Player player = event.getPlayer();
        if (this.miniGames.isJoined(player)) {
            final MiniGame miniGame = this.miniGames.getJoined(player);
            for (final String cmd : miniGame.getMessageList("pickupCmd")) {
                final ItemStack itemStack = event.getItem().getItemStack();
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
                final String output = miniGame.executeEventCommands(cmd.replace("{itemName}", itemName).replace("{itemAmount}", itemAmount).replace("{itemDurability}", itemDurability).replace("{itemType}", itemType), player);
                if (output.equals("false")) {
                    event.setCancelled(true);
                }
            }
        }
    }
}
