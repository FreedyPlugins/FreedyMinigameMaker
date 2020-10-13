// 
// Decompiled by Procyon v0.5.36
// 

package freedy.freedyminigamemaker.events;

import org.bukkit.event.EventHandler;
import java.util.Iterator;
import java.util.List;
import freedy.freedyminigamemaker.MiniGame;
import org.bukkit.command.CommandSender;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import freedy.freedyminigamemaker.FreedyMinigameMaker;
import freedy.freedyminigamemaker.MiniGames;
import org.bukkit.event.Listener;

public class ClickEvent implements Listener
{
    MiniGames miniGames;
    
    public ClickEvent() {
        this.miniGames = FreedyMinigameMaker.miniGames;
    }
    
    @EventHandler
    public void onClick(final InventoryClickEvent event) {
        final Player player = (Player)event.getWhoClicked();
        final MiniGame miniGame = this.miniGames.getNoneGame();
        final String title = event.getInventory().getTitle();
        final int index = event.getSlot();
        if (miniGame.isExistingTitle(title)) {
            event.setCancelled(true);
            final List<String> cmdList = miniGame.getCmdByTitle(title, index);
            for (final String cmd : cmdList) {
                if (cmd != null) {
                    Bukkit.getServer().dispatchCommand((CommandSender)Bukkit.getServer().getConsoleSender(), cmd.replace("{player}", player.getName()));
                }
            }
        }
    }
}
