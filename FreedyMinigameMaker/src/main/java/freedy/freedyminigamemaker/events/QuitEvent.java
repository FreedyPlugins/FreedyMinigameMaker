// 
// Decompiled by Procyon v0.5.36
// 

package freedy.freedyminigamemaker.events;

import freedy.freedyminigamemaker.commands.FreedyCommandSender;
import org.bukkit.event.EventHandler;
import freedy.freedyminigamemaker.MiniGame;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerQuitEvent;
import freedy.freedyminigamemaker.FreedyMinigameMaker;
import freedy.freedyminigamemaker.MiniGames;
import org.bukkit.event.Listener;

import java.util.List;

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
            MiniGame miniGame = this.miniGames.getJoined(player);
            if (miniGames.getSettings().getConfig().getBoolean("hideJoinLeaveMessage")) event.setQuitMessage(null);
            List<String> cmds = miniGames.getSettings().getConfig().getStringList("serverLeaveCmd");
            if (cmds != null) miniGame.executeCommands(new FreedyCommandSender(), cmds, player);
            miniGame = this.miniGames.getJoined(player);
            miniGame.kick(player);
            miniGame.stop();
        }
    }
}
