// 
// Decompiled by Procyon v0.5.36
// 

package freedy.freedyminigamemaker.events;

import freedy.freedyminigamemaker.FreedyMinigameMaker;
import freedy.freedyminigamemaker.MiniGames;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityExplodeEvent;

import java.util.ArrayList;
import java.util.List;

public class ExplodeBlockEvent implements Listener
{
    MiniGames miniGames;
    
    public ExplodeBlockEvent() {
        this.miniGames = FreedyMinigameMaker.miniGames;
    }
    
    @EventHandler
    public void onBlockExplode(final EntityExplodeEvent event) {
        if (this.miniGames.getSettings().getConfig().getBoolean("cancelBlockExplode")) {
            if (event.isCancelled()) {
                return;
            }
            final List<Block> blockListCopy = new ArrayList<Block>(event.blockList());
            for (final Block block : blockListCopy) {
                event.blockList().remove(block);
            }
        }
    }
}
