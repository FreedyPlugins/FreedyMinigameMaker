package freedy.freedyminigamemaker.events;

import freedy.freedyminigamemaker.FreedyMinigameMaker;
import freedy.freedyminigamemaker.MiniGame;
import freedy.freedyminigamemaker.MiniGames;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

import java.util.List;

public class ClickEvent implements Listener {

    FreedyMinigameMaker plugin;

    MiniGames miniGames;

    public ClickEvent(FreedyMinigameMaker plugin) {
        this.plugin = plugin;
        this.miniGames = plugin.miniGames;
    }

    @EventHandler
    public void onClick(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        if (miniGames.isJoined(player)) {
            MiniGame miniGame = miniGames.getJoined(player);
            String title = event.getInventory().getTitle();
            int index = event.getSlot();
            if (miniGame.isExistingTitle(title)) {
                event.setCancelled(true);
                List<String> cmdList = miniGame.getCmdByTitle(title, index);
                for (String cmd : cmdList) {
                    if (cmd != null)
                        Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(), cmd
                                .replace("{player}", player.getName())
                                .replace("{playerMode}", miniGame.getMode(player)));
                }
            }
        }
    }
}
