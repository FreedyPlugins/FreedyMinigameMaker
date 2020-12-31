package freedy.freedyminigamemaker.events;

import freedy.freedyminigamemaker.FreedyMinigameMaker;
import freedy.freedyminigamemaker.MiniGame;
import freedy.freedyminigamemaker.MiniGames;
import freedy.freedyminigamemaker.commands.FreedyCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import java.util.List;

public class CommandEvent implements Listener {

    MiniGames miniGames;

    public CommandEvent() {
        this.miniGames = FreedyMinigameMaker.miniGames;
    }


    @EventHandler
    public void onExecuteCommand(PlayerCommandPreprocessEvent event) {
        Player player = event.getPlayer();
        if (this.miniGames.isJoined(player)) {
            final MiniGame miniGame = this.miniGames.getJoined(player);
            for (final String cmd : miniGames.getSettings().getConfig().getStringList("commandCmd")) {
                final String output = miniGame.executeEventCommands(cmd
                                .replace("{command}", event.getMessage())
                                .replace("{args}", event.getMessage().replace(" ", ", "))
                        , player);
                if (output.equals("false")) {
                    event.setCancelled(true);
                    return;
                }
            }
            for (final String cmd : miniGame.getMessageList("commandCmd")) {
                final String output = miniGame.executeEventCommands(cmd
                        .replace("{command}", event.getMessage())
                        .replace("{args}", event.getMessage().replace(" ", ", "))
                        , player);
                if (output.equals("false")) {
                    event.setCancelled(true);
                }
            }
        }
    }
}