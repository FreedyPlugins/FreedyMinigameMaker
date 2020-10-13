// 
// Decompiled by Procyon v0.5.36
// 

package freedy.freedyminigamemaker.events;

import org.bukkit.event.EventHandler;
import java.util.Iterator;
import freedy.freedyminigamemaker.MiniGame;
import org.bukkit.entity.Player;
import java.util.List;
import org.bukkit.event.entity.PlayerDeathEvent;
import freedy.freedyminigamemaker.FreedyMinigameMaker;
import freedy.freedyminigamemaker.MiniGames;
import org.bukkit.event.Listener;

public class DeathEvent implements Listener
{
    MiniGames miniGames;
    
    public DeathEvent() {
        this.miniGames = FreedyMinigameMaker.miniGames;
    }
    
    @EventHandler
    public void onDamage(final PlayerDeathEvent event) {
        final Player player = event.getEntity().getPlayer();
        final String playerName = player.getName();
        final Player killer = player.getKiller();
        String killerName;
        if (killer != null) {
            killerName = killer.getName();
        }
        else {
            killerName = "";
        }
        if (this.miniGames.isJoined(player)) {
            final MiniGame miniGame = this.miniGames.getJoined(player);
            miniGame.remove(player);
            miniGame.stop();
        }
    }
}
