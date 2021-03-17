// 
// Decompiled by Procyon v0.5.36
// 

package freedy.freedyminigamemaker.events;

import freedy.freedyminigamemaker.FreedyMinigameMaker;
import freedy.freedyminigamemaker.MiniGame;
import freedy.freedyminigamemaker.MiniGames;
import org.bukkit.Bukkit;
import org.bukkit.block.Block;
import org.bukkit.entity.FallingBlock;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityChangeBlockEvent;
import org.bukkit.event.entity.ProjectileHitEvent;

import java.util.UUID;

public class ProjectileEvent implements Listener
{
    MiniGames miniGames;
    
    public ProjectileEvent() {
        this.miniGames = FreedyMinigameMaker.miniGames;
    }
    
    @EventHandler
    public void onHitProjectile(final ProjectileHitEvent event) {
        final Projectile damagerEntity = event.getEntity();
        if (damagerEntity.getShooter() instanceof Player) {
            final Player player = (Player)damagerEntity.getShooter();
            final Block block = event.getHitBlock();
            String blockWorld = "none";
            String blockType = "none";
            String blockX = "none";
            String blockY = "none";
            String blockZ = "none";
            if (block != null) {
                blockWorld = block.getWorld().getName();
                blockType = block.getType().name();
                blockX = String.valueOf(block.getX());
                blockY = String.valueOf(block.getY());
                blockZ = String.valueOf(block.getZ());
            }
            String entityName = "none";
            String entityType = "none";
            if (event.getHitEntity() != null) {
                entityType = event.getHitEntity().getType().name();
                entityName = event.getHitEntity().getName();
            }
            if (this.miniGames.isJoined(player)) {
                final MiniGame miniGame = this.miniGames.getJoined(player);
                for (final String cmd : miniGame.getMessageList("projectileHitCmd")) {
                    miniGame.executeEventCommands(cmd.replace("{projectileType}", damagerEntity.getType().name()).replace("{projectileName}", damagerEntity.getName()).replace("{projectileUuid}", damagerEntity.getUniqueId().toString()).replace("{blockWorld}", blockWorld).replace("{blockType}", blockType).replace("{blockX}", blockX).replace("{blockY}", blockY).replace("{blockZ}", blockZ).replace("{entityType}", entityType).replace("{entityName}", entityName), player);
                }
            }
        }
    }
    
    @EventHandler
    public void onBlockChange(final EntityChangeBlockEvent event) {
        if (!(event.getEntity() instanceof FallingBlock)) {
            return;
        }
        final FallingBlock fallingBlock = (FallingBlock)event.getEntity();
        if (fallingBlock == null) {
            return;
        }
        if (event.getEntity().getCustomName() == null) {
            return;
        }
        final Player player = Bukkit.getPlayer(UUID.fromString(event.getEntity().getCustomName()));
        if (player != null && this.miniGames.isJoined(player)) {
            final MiniGame miniGame = this.miniGames.getJoined(player);
            for (final String cmd : miniGame.getMessageList("fallingBlockCmd")) {
                final String output = miniGame.executeEventCommands(cmd.replace("{fallingBlockUuid}", fallingBlock.getUniqueId().toString()).replace("{fallingBlockType}", String.valueOf(fallingBlock.getBlockData().getMaterial())).replace("{fallingBlockData}", String.valueOf(fallingBlock.getBlockData())), player);
                if (output.equals("false")) {
                    event.setCancelled(true);
                }
            }
        }
    }
}
