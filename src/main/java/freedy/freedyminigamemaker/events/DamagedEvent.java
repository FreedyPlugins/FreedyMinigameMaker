// 
// Decompiled by Procyon v0.5.36
// 

package freedy.freedyminigamemaker.events;

import freedy.freedyminigamemaker.FreedyMinigameMaker;
import freedy.freedyminigamemaker.MiniGame;
import freedy.freedyminigamemaker.MiniGames;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;

public class DamagedEvent implements Listener
{
    MiniGames miniGames;
    
    public DamagedEvent() {
        this.miniGames = FreedyMinigameMaker.miniGames;
    }
    
    @EventHandler
    public void onDamaged(final EntityDamageEvent event) {
        final Entity entity = event.getEntity();
        if (entity instanceof Player) {
            final Player player = (Player)entity;
            if (this.miniGames.isJoined(player)) {
                final MiniGame miniGame = this.miniGames.getJoined(player);
                for (final String cmd : miniGame.getMessageList("damageCmd")) {
                    final String output = miniGame.executeEventCommands(cmd.replace("{cause}", event.getCause().name()).replace("{damage}", String.valueOf(event.getFinalDamage())), player);
                    if (output.equals("false")) {
                        event.setCancelled(true);
                    }
                }
            }
        }
    }
}
