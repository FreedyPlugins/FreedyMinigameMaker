// 
// Decompiled by Procyon v0.5.36
// 

package freedy.freedyminigamemaker.events;

import freedy.freedyminigamemaker.FreedyMinigameMaker;
import freedy.freedyminigamemaker.MiniGame;
import freedy.freedyminigamemaker.MiniGames;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class InteractEvent implements Listener
{
    MiniGames miniGames;
    
    public InteractEvent() {
        this.miniGames = FreedyMinigameMaker.miniGames;
    }
    
    @EventHandler
    public void interactEvent(final PlayerInteractEvent event) {
        final Player player = event.getPlayer();
        if (this.miniGames.isJoined(player)) {
            final MiniGame miniGame = this.miniGames.getJoined(player);
            final ItemStack itemStack = event.getItem();
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
            final Block block = event.getClickedBlock();
            String blockType = "none";
            if (block != null) {
                blockType = block.getType().name();
            }
            final String actionName = event.getAction().name();
            for (final String cmd : miniGame.getMessageList("interactCmd")) {
                final String output = miniGame.executeEventCommands(cmd.replace("{actionName}", actionName).replace("{action}", actionName).replace("{itemName}", itemName).replace("{itemAmount}", itemAmount).replace("{itemDurability}", itemDurability).replace("{itemType}", itemType).replace("{blockType}", blockType), player);
                if (output.equals("false")) {
                    event.setCancelled(true);
                }
            }
            if (block != null && miniGame.getGameType().equals("build")) {
                miniGame.addBlock(block);
            }
        }
    }
}
