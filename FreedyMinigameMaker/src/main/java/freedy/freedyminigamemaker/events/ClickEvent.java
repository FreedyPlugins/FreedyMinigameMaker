package freedy.freedyminigamemaker.events;

import freedy.freedyminigamemaker.FreedyMinigameMaker;
import freedy.freedyminigamemaker.MiniGame;
import freedy.freedyminigamemaker.MiniGames;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

import java.util.List;

public class ClickEvent implements Listener
{
    MiniGames miniGames;
    
    public ClickEvent() {
        this.miniGames = FreedyMinigameMaker.miniGames;
    }
    
    @EventHandler
    public void onClick(final InventoryClickEvent event) {
        final Player player = (Player) event.getWhoClicked();
        if (this.miniGames.isJoined(player)) {
            final MiniGame miniGame = this.miniGames.getJoined(player);
            if (event.getClickedInventory() != null && event.getClickedInventory().getHolder() instanceof Player) {
                for (final String cmd : miniGame.getMessageList("PlayerInventoryClickCmd")) {
                    if (cmd != null) {
                        String output = miniGame.executeEventCommands(cmd
                                .replace("{slot}", String.valueOf(event.getSlot()))
                                .replace("{invType}", String.valueOf(event.getClickedInventory().getType().name()))
                                .replace("{clickType}", String.valueOf(event.getClick().name()))
                                , player);
                        if (output.equals("false")) {
                            event.setCancelled(true);
                        }
                    }
                }
            }
            final String title = player.getOpenInventory().getTitle();

            final int index = event.getSlot();
            if (miniGame.isExistingTitle(title)) {
                for (final String cmd : miniGame.getMessageList(miniGame.getInvName(title) + "ClickCmd")) {
                    if (cmd != null) {
                        String output = miniGame.executeEventCommands(cmd
                                        .replace("{slot}", String.valueOf(index))
                                        .replace("{invType}", String.valueOf(event.getClickedInventory().getType().name()))
                                        .replace("{clickType}", String.valueOf(event.getClick().name()))
                                , player);
                        if (output.equals("false")) {
                            event.setCancelled(true);
                        }

                    }
                }
                final List<String> cmdList = miniGame.getCmdByTitle(title, index);
                for (final String cmd : cmdList) {
                    if (cmd != null) {
                        String output = miniGame.executeEventCommands(cmd, player);
                        if (output.equals("false")) {
                            event.setCancelled(true);
                        }
                    }
                }
            }
        }
    }
}
