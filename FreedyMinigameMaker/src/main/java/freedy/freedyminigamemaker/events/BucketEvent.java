package freedy.freedyminigamemaker.events;

import freedy.freedyminigamemaker.FreedyMinigameMaker;
import freedy.freedyminigamemaker.MiniGame;
import freedy.freedyminigamemaker.MiniGames;
import org.bukkit.block.BlockFace;
import org.bukkit.event.player.PlayerBucketFillEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.entity.Player;
import org.bukkit.block.Block;
import org.bukkit.event.player.PlayerBucketEmptyEvent;
import org.bukkit.event.Listener;

public class BucketEvent implements Listener
{
    MiniGames miniGames;
    
    public BucketEvent() {
        this.miniGames = FreedyMinigameMaker.miniGames;
    }
    
    @EventHandler
    public void onBucketEmpty(final PlayerBucketEmptyEvent event) {
        final Block block = this.getTargetBlock(event.getBlockFace(), event.getBlockClicked());
        final Player player = event.getPlayer();
        if (this.miniGames.isJoined(player)) {
            final MiniGame miniGame = this.miniGames.getJoined(player);
            if (miniGame.getGameType().equals("build")) {
                miniGame.addBlock(block);
            }
        }
    }
    
    @EventHandler
    public void onBucketFill(final PlayerBucketFillEvent event) {
        final Block block = this.getTargetBlock(event.getBlockFace(), event.getBlockClicked());
        final BlockFace blockFace = event.getBlockFace();
        final Player player = event.getPlayer();
        if (this.miniGames.isJoined(player)) {
            final MiniGame miniGame = this.miniGames.getJoined(player);
            if (miniGame.getGameType().equals("build")) {
                miniGame.addBlock(block);
            }
        }
    }
    
    Block getTargetBlock(final BlockFace blockFace, final Block block) {
        final String name = blockFace.name();
        switch (name) {
            case "UP": {
                return block.getLocation().add(0.0, 1.0, 0.0).getBlock();
            }
            case "DOWN": {
                return block.getLocation().add(0.0, -1.0, 0.0).getBlock();
            }
            case "SOUTH": {
                return block.getLocation().add(0.0, 0.0, 1.0).getBlock();
            }
            case "NORTH": {
                return block.getLocation().add(0.0, 0.0, -1.0).getBlock();
            }
            case "EAST": {
                return block.getLocation().add(1.0, 0.0, 0.0).getBlock();
            }
            case "WEST": {
                return block.getLocation().add(-1.0, 0.0, 0.0).getBlock();
            }
            default: {
                return block;
            }
        }
    }
}
