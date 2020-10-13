// 
// Decompiled by Procyon v0.5.36
// 

package freedy.freedyminigamemaker.events;

import org.bukkit.event.EventHandler;
import freedy.freedyminigamemaker.MiniGame;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerQuitEvent;
import freedy.freedyminigamemaker.FreedyMinigameMaker;
import freedy.freedyminigamemaker.MiniGames;
import org.bukkit.event.Listener;

public class QuitEvent implements Listener
{
    MiniGames miniGames;
    
    public QuitEvent() {
        this.miniGames = FreedyMinigameMaker.miniGames;
    }
    
    @EventHandler
    public void onQuit(final PlayerQuitEvent event) {
        final Player player = event.getPlayer();
        if (this.miniGames.isJoined(player)) {
            final MiniGame miniGame = this.miniGames.getJoined(player);
            miniGame.remove(player);
            miniGame.stop();
        }
    }
}
