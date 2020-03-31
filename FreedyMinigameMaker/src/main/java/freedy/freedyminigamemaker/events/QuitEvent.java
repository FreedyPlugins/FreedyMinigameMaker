package freedy.freedyminigamemaker.events;

import freedy.freedyminigamemaker.FreedyMinigameMaker;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.List;

public class QuitEvent implements Listener {

    FreedyMinigameMaker plugin;

    public QuitEvent(FreedyMinigameMaker plugin) {
        this.plugin = plugin;
    }


    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        String playerName = player.getName();
        List<String> gameList = plugin.getConfig().getStringList("gameList");
        for (String gameName : gameList) {
            List<String> playerNameList = plugin.getConfig().getStringList("miniGames." + gameName + ".players");
            String gameType = plugin.getConfig().getString("miniGames." + gameName + ".gameType");
            if (playerNameList.contains(playerName)) {
                plugin.removePlayer(playerName, gameName);
                if (plugin.checkWillStopGame(gameName)) plugin.stopGame(gameName);
            }
        }
    }
}
