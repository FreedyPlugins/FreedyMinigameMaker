package freedy.freedyminigamemaker.events;

import freedy.freedyminigamemaker.FreedyMinigameMaker;
import freedy.freedyminigamemaker.MiniGame;
import freedy.freedyminigamemaker.MiniGames;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class JoinEvent implements Listener {

    MiniGames miniGames;

    public JoinEvent() {
        this.miniGames = FreedyMinigameMaker.miniGames;
    }

    @EventHandler
    public void onQuit(final PlayerJoinEvent event) {
        final Player player = event.getPlayer();
        if (miniGames.getSettings().getConfig().getBoolean("hideJoinLeaveMessage")) event.setJoinMessage(null);
        final String defaultJoinGame = miniGames.getNoneGame().getSettings("defaultJoinGame");
        if (defaultJoinGame!= null && !(defaultJoinGame.equals("none"))) {
            FreedyMinigameMaker.miniGames.get(defaultJoinGame).add(player);
        }
    }

}