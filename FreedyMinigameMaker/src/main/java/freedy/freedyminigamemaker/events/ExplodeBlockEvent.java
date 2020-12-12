package freedy.freedyminigamemaker.events;

import freedy.freedyminigamemaker.FreedyMinigameMaker;
import freedy.freedyminigamemaker.MiniGames;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockExplodeEvent;
import org.bukkit.event.entity.EntityExplodeEvent;

import java.util.ArrayList;
import java.util.List;

public class ExplodeBlockEvent implements Listener {

    MiniGames miniGames;

    public ExplodeBlockEvent() {
        this.miniGames = FreedyMinigameMaker.miniGames;
    }


    @EventHandler
    public void onBlockExplode(EntityExplodeEvent event) {
        if (miniGames.getSettings().getConfig().getBoolean("cancelBlockExplode")) {
            if (event.isCancelled()) return;

            List<Block> blockListCopy = new ArrayList<>(event.blockList());
            for (Block block : blockListCopy) {
                event.blockList().remove(block);
            }

        }

    }
}