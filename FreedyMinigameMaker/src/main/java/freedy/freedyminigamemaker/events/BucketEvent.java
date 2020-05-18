package freedy.freedyminigamemaker.events;

import freedy.freedyminigamemaker.FreedyMinigameMaker;
import freedy.freedyminigamemaker.MiniGame;
import freedy.freedyminigamemaker.MiniGames;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerBucketEmptyEvent;
import org.bukkit.event.player.PlayerBucketFillEvent;

public class BucketEvent implements Listener {

    FreedyMinigameMaker plugin;

    MiniGames miniGames;

    public BucketEvent(FreedyMinigameMaker plugin) {
        this.plugin = plugin;
        this.miniGames = plugin.miniGames;
    }

    @EventHandler
    public void onBucketEmpty(PlayerBucketEmptyEvent event) {
        Block block = getTargetBlock(event.getBlockFace(), event.getBlockClicked());
        Player player = event.getPlayer();
        if (miniGames.isJoined(player)) {
            MiniGame miniGame = miniGames.getJoined(player);
            if (miniGame.getGameType().equals("build"))
                miniGame.addBlock(block);
        }
    }

    @EventHandler
    public void onBucketFill(PlayerBucketFillEvent event) {
        Block block = getTargetBlock(event.getBlockFace(), event.getBlockClicked());
        BlockFace blockFace = event.getBlockFace();
        Player player = event.getPlayer();
        player.sendMessage(blockFace.name());
        if (miniGames.isJoined(player)) {
            MiniGame miniGame = miniGames.getJoined(player);
            if (miniGame.getGameType().equals("build"))
                miniGame.addBlock(block);
        }
    }


    Block getTargetBlock(BlockFace blockFace, Block block) {


        switch (blockFace.name()) {
            case "UP":
                return block.getLocation().add(0, 1, 0).getBlock();
            case "DOWN":
                return block.getLocation().add(0, -1, 0).getBlock();
            case "SOUTH":
                return block.getLocation().add(0, 0, 1).getBlock();
            case "NORTH":
                return block.getLocation().add(0, 0, -1).getBlock();
            case "EAST":
                return block.getLocation().add(1, 0, 0).getBlock();
            case "WEST":
                return block.getLocation().add(-1, 0, 0).getBlock();
            default:
                return block;
        }


    }

}
