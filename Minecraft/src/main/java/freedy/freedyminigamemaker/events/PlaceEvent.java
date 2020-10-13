// 
// Decompiled by Procyon v0.5.36
// 

package freedy.freedyminigamemaker.events;

import org.bukkit.event.EventHandler;
import java.util.Iterator;
import org.bukkit.block.Block;
import freedy.freedyminigamemaker.MiniGame;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockPlaceEvent;
import freedy.freedyminigamemaker.FreedyMinigameMaker;
import freedy.freedyminigamemaker.MiniGames;
import org.bukkit.event.Listener;

public class PlaceEvent implements Listener
{
    MiniGames miniGames;
    
    public PlaceEvent() {
        this.miniGames = FreedyMinigameMaker.miniGames;
    }
    
    @EventHandler
    public void onPlaceBlock(final BlockPlaceEvent event) {
        final Player player = event.getPlayer();
        if (this.miniGames.isJoined(player)) {
            final MiniGame miniGame = this.miniGames.getJoined(player);
            final Block block = event.getBlockPlaced();
            for (final String cmd : miniGame.getMessageList("blockPlaceCmd")) {
                final String output = miniGame.executeEventCommands(cmd.replace("{blockType}", block.getType().name()).replace("{blockX}", String.valueOf(block.getX())).replace("{blockY}", String.valueOf(block.getY())).replace("{blockZ}", String.valueOf(block.getZ())).replace("{blockFace}", block.getFace(block).name()), player);
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
