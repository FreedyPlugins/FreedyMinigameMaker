// 
// Decompiled by Procyon v0.5.36
// 

package freedy.freedyminigamemaker.events;

import org.bukkit.event.EventHandler;
import java.util.Iterator;
import org.bukkit.block.Block;
import freedy.freedyminigamemaker.MiniGame;
import org.bukkit.entity.Player;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.ChatMessageType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.Material;
import java.util.concurrent.ThreadLocalRandom;
import org.bukkit.event.block.BlockBreakEvent;
import freedy.freedyminigamemaker.FreedyMinigameMaker;
import freedy.freedyminigamemaker.MiniGames;
import org.bukkit.event.Listener;

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
                final String output = miniGame.executeEventCommands(cmd.replace("{blockType}", block.getType().name()).replace("{blockX}", String.valueOf(block.getX())).replace("{blockY}", String.valueOf(block.getY())).replace("{blockZ}", String.valueOf(block.getZ())).replace("{blockFace}", block.getFace(block).name()), player);
                if (output.equals("false")) {
                    event.setCancelled(true);
                    return;
                }
            }
            if (miniGame.getGameType().equals("build")) {
                miniGame.addBlock(block);
            }
            if (miniGame.getPlayerData(player).getCustomData("skillMode").equals("dropItemMode")) {
                final Material material = block.getType();
                if (miniGame.getDropList().contains(material.toString()) && ThreadLocalRandom.current().nextInt(1, 101) <= miniGame.getDropRate()) {
                    block.setType(Material.AIR);
                    for (int i = 0; i < miniGame.getDrops(material.toString()); ++i) {
                        block.getWorld().dropItemNaturally(block.getLocation().add(0.5, 0.5, 0.5), new ItemStack(material));
                    }
                    final String message = miniGame.getMessage("dropMsg");
                    if (message != null) {
                        player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(message));
                    }
                }
            }
        }
    }
}
