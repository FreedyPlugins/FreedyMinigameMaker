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

import java.util.List;

public class InteractEvent implements Listener {

    FreedyMinigameMaker plugin;

    MiniGames miniGames;

    public InteractEvent(FreedyMinigameMaker plugin) {
        this.plugin = plugin;
        this.miniGames = plugin.miniGames;
    }

    @EventHandler
    public void interactEvent(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        if (miniGames.isJoined(player)) {
            MiniGame miniGame = miniGames.getJoined(player);


            ItemStack itemStack = event.getItem();
            String itemType = "none";
            String itemName = "none";
            if (itemStack != null) {
                itemType = itemStack.getType().name();
                if (itemStack.hasItemMeta() && itemStack.getItemMeta().hasDisplayName()) {
                    itemName = itemStack.getItemMeta().getDisplayName();
                }
            }

            String actionName = event.getAction().name();
            for (String cmd : miniGame.getMessageList("interactCmd")) {
                miniGame.executeCommands(cmd
                        .replace("{actionName}", actionName)
                        .replace("{action}", actionName)
                        .replace("{itemName}", itemName)
                        .replace("{itemType}", itemType),
                        player);
            }

            Block block = event.getClickedBlock();
            if (block != null) {
                if (miniGame.getGameType().equals("build"))
                    miniGame.addBlock(block);
            }
        }


    }
}
