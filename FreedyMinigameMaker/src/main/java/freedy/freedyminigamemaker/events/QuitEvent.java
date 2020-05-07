package freedy.freedyminigamemaker.events;

import freedy.freedyminigamemaker.FreedyMinigameMaker;
import freedy.freedyminigamemaker.MiniGame;
import freedy.freedyminigamemaker.MiniGames;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.List;

public class QuitEvent implements Listener {

    FreedyMinigameMaker plugin;

    MiniGames miniGames;

    public QuitEvent(FreedyMinigameMaker plugin) {
        this.plugin = plugin;
        miniGames = plugin.miniGames;
    }


    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        if (miniGames.isJoined(player)) {
            MiniGame miniGame = miniGames.getJoined(player);
            miniGame.remove(player);
            miniGame.stop();
        }



    }
}
