// 
// Decompiled by Procyon v0.5.36
// 

package freedy.freedyminigamemaker.events;

import freedy.freedyminigamemaker.FreedyMinigameMaker;
import freedy.freedyminigamemaker.MiniGame;
import freedy.freedyminigamemaker.MiniGames;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

public class CommandEvent implements Listener
{
    MiniGames miniGames;
    
    public CommandEvent() {
        this.miniGames = FreedyMinigameMaker.miniGames;
    }
    
    @EventHandler
    public void onExecuteCommand(final PlayerCommandPreprocessEvent event) {
        final Player player = event.getPlayer();
        if (this.miniGames.isJoined(player)) {
            final MiniGame miniGame = this.miniGames.getJoined(player);
            for (final String cmd : this.miniGames.getSettings().getConfig().getStringList("commandCmd")) {
                final String output = miniGame.executeEventCommands(cmd.replace("{command}", event.getMessage()).replace("{args}", event.getMessage().replace(" ", ", ")), player);
                if (output.equals("false")) {
                    event.setCancelled(true);
                    return;
                }
            }
            for (final String cmd : miniGame.getMessageList("commandCmd")) {
                final String output = miniGame.executeEventCommands(cmd.replace("{command}", event.getMessage()).replace("{args}", event.getMessage().replace(" ", ", ")), player);
                if (output.equals("false")) {
                    event.setCancelled(true);
                }
            }
        }
    }
}
