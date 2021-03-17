// 
// Decompiled by Procyon v0.5.36
// 

package freedy.freedyminigamemaker.events;

import freedy.freedyminigamemaker.FreedyMinigameMaker;
import freedy.freedyminigamemaker.MiniGame;
import freedy.freedyminigamemaker.MiniGames;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;

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
            final Block preBlock = event.getBlockReplacedState().getBlock();
            for (final String cmd : miniGame.getMessageList("blockPlaceCmd")) {
                final String output = miniGame.executeEventCommands(cmd.replace("{preBlockWorld}", preBlock.getWorld().getName()).replace("{preBlockType}", preBlock.getType().name()).replace("{preBlockX}", String.valueOf(preBlock.getX())).replace("{preBlockY}", String.valueOf(preBlock.getY())).replace("{preBlockZ}", String.valueOf(preBlock.getZ())).replace("{preBlockFace}", preBlock.getFace(preBlock).name()).replace("{blockWorld}", block.getWorld().getName()).replace("{blockType}", block.getType().name()).replace("{blockX}", String.valueOf(block.getX())).replace("{blockY}", String.valueOf(block.getY())).replace("{blockZ}", String.valueOf(block.getZ())).replace("{blockFace}", block.getFace(block).name()), player);
                if (output.equals("false")) {
                    event.setCancelled(true);
                    return;
                }
            }
            if (miniGame.getGameType().equals("build")) {
                miniGame.addBlock(event.getBlockReplacedState());
            }
        }
    }
}
