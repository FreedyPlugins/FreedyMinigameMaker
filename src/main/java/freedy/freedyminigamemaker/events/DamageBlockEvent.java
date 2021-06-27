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
import org.bukkit.event.block.BlockDamageEvent;

public class DamageBlockEvent implements Listener
{
    MiniGames miniGames;
    
    public DamageBlockEvent() {
        this.miniGames = FreedyMinigameMaker.miniGames;
    }
    
    @EventHandler
    public void onIgnite(final BlockDamageEvent event) {
        final Player player = event.getPlayer();
        if (this.miniGames.isJoined(player)) {
            final MiniGame miniGame = this.miniGames.getJoined(player);
            final Block block = event.getBlock();
            for (final String cmd : miniGame.getMessageList("blockDamageCmd")) {
                final String output = miniGame.executeEventCommands(cmd.replace("{blockType}", block.getType().name()).replace("{blockX}", String.valueOf(block.getX())).replace("{blockY}", String.valueOf(block.getY())).replace("{blockZ}", String.valueOf(block.getZ())).replace("{blockFace}", block.getFace(block).name()), player);
                if (output.equals("false")) {
                    event.setCancelled(true);
                }
            }
        }
    }
}
