package freedy.freedyminigamemaker.events;

import freedy.freedyminigamemaker.FreedyMinigameMaker;
import freedy.freedyminigamemaker.MiniGame;
import freedy.freedyminigamemaker.MiniGames;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

public class BreakEvent implements Listener
{
    MiniGames miniGames;
    
    public BreakEvent() {
        this.miniGames = FreedyMinigameMaker.miniGames;
    }
    
    @EventHandler
    public void onBreak(final BlockBreakEvent event) {
        final Player player = event.getPlayer();
        if (this.miniGames.isJoined(player)) {
            final MiniGame miniGame = this.miniGames.getJoined(player);
            final Block block = event.getBlock();
            for (final String cmd : miniGame.getMessageList("blockBreakCmd")) {
                final String output = miniGame.executeEventCommands(cmd
                        .replace("{blockWorld}", block.getWorld().getName())
                        .replace("{blockType}", block.getType().name())
                        .replace("{blockX}", String.valueOf(block.getX()))
                        .replace("{blockY}", String.valueOf(block.getY()))
                        .replace("{blockZ}", String.valueOf(block.getZ()))
                        .replace("{blockFace}", block.getFace(block).name()), player);
                if (output.equals("false")) {
                    event.setCancelled(true);
                    return;
                }
            }
            if (miniGame.getGameType().equals("build")) {
                miniGame.addBlock(block);
            }

        }
    }
}
