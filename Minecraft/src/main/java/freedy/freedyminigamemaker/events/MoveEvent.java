// 
// Decompiled by Procyon v0.5.36
// 

package freedy.freedyminigamemaker.events;

import org.bukkit.event.EventHandler;
import java.util.Iterator;
import freedy.freedyminigamemaker.MiniGame;
import org.bukkit.entity.Player;
import org.bukkit.block.Block;
import org.bukkit.event.player.PlayerMoveEvent;
import freedy.freedyminigamemaker.FreedyMinigameMaker;
import freedy.freedyminigamemaker.MiniGames;
import org.bukkit.event.Listener;

public class MoveEvent implements Listener
{
    MiniGames miniGames;
    
    public MoveEvent() {
        this.miniGames = FreedyMinigameMaker.miniGames;
    }
    
    @EventHandler
    public void onMove(final PlayerMoveEvent event) {
        final Block from = event.getFrom().getBlock();
        final Block to = event.getTo().getBlock();
        if (from.getLocation().getBlockX() != to.getLocation().getBlockX() || from.getLocation().getBlockY() != to.getLocation().getBlockY() || from.getLocation().getBlockZ() != to.getLocation().getBlockZ()) {
            final Player player = event.getPlayer();
            if (this.miniGames.isJoined(player)) {
                final MiniGame miniGame = this.miniGames.getJoined(player);
                for (final String cmd : miniGame.getMessageList("moveCmd")) {
                    final String output = miniGame.executeEventCommands(cmd.replace("{fromBlockType}", from.getType().name()).replace("{fromBlockX}", String.valueOf(from.getX())).replace("{fromBlockY}", String.valueOf(from.getY())).replace("{fromBlockZ}", String.valueOf(from.getZ())).replace("{fromBlockFace}", from.getFace(from).name()).replace("{fromBlockWorld}", from.getWorld().getName()).replace("{toBlockType}", to.getType().name()).replace("{toBlockX}", String.valueOf(to.getX())).replace("{toBlockY}", String.valueOf(to.getY())).replace("{toBlockZ}", String.valueOf(to.getZ())).replace("{toBlockFace}", to.getFace(to).name()).replace("{toBlockWorld}", to.getWorld().getName()), player);
                    if (output.equals("false")) {
                        event.setCancelled(true);
                    }
                }
            }
        }
    }
}
