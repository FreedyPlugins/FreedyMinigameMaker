package freedy.freedyminigamemaker.events;

import freedy.freedyminigamemaker.FreedyMinigameMaker;
import freedy.freedyminigamemaker.MiniGame;
import freedy.freedyminigamemaker.MiniGames;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChatEvent;

public class ChatEvent implements Listener
{
    MiniGames miniGames;
    
    public ChatEvent() {
        this.miniGames = FreedyMinigameMaker.miniGames;
    }
    
    @EventHandler
    public void onChat(final PlayerChatEvent event) {
        final Player player = event.getPlayer();
        if (this.miniGames.isJoined(player)) {
            final MiniGame miniGame = this.miniGames.getJoined(player);
            final String format = event.getFormat().replace("%1$s", player.getName()).replace("%2$s", "");
            final String chat = event.getMessage();
            for (final String cmd : miniGame.getMessageList("chatCmd")) {
                final String output = miniGame.executeEventCommands(cmd
                        .replace("{format}", format)
                        .replace("{chat}", chat), player);
                if (output.equals("false")) {
                    event.setCancelled(true);
                }
            }
        }
    }
}
