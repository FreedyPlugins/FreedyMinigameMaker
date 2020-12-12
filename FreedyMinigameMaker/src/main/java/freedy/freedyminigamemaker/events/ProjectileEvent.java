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

import java.util.List;
import java.util.UUID;

public class ProjectileEvent implements Listener {


    MiniGames miniGames;

    public ProjectileEvent() {
        this.miniGames = FreedyMinigameMaker.miniGames;
    }


    @EventHandler
    public void onHitProjectile(ProjectileHitEvent event) {
        Projectile damagerEntity = event.getEntity();
        if (damagerEntity.getShooter() instanceof Player && event.getHitBlock() != null) {
            Player player = (Player) damagerEntity.getShooter();
            Block block = event.getHitBlock();
            if (this.miniGames.isJoined(player)) {
                final MiniGame miniGame = this.miniGames.getJoined(player);
                for (final String cmd : miniGame.getMessageList("projectileHitBlockCmd")) {
                    miniGame.executeEventCommands(cmd
                                    .replace("{projectileType}", damagerEntity.getType().name())
                                    .replace("{projectileName}", damagerEntity.getName())
                                    .replace("{projectileUuid}", damagerEntity.getUniqueId().toString())
                            .replace("{blockWorld}", block.getWorld().getName())
                            .replace("{blockType}", block.getType().name())
                            .replace("{blockX}", String.valueOf(block.getX()))
                            .replace("{blockY}", String.valueOf(block.getY()))
                            .replace("{blockZ}", String.valueOf(block.getZ()))
                            .replace("{blockFace}", block.getFace(block).name())
                            , player);
                }
            }

        }


    }



    @EventHandler
    public void onBlockChange(EntityChangeBlockEvent event){

        if(!(event.getEntity() instanceof FallingBlock)) return;

        FallingBlock fallingBlock = (FallingBlock) event.getEntity();
        if (fallingBlock == null) return;
        if (event.getEntity().getCustomName() == null) return;
        Player player = Bukkit.getPlayer(UUID.fromString(event.getEntity().getCustomName()));
        if (player != null) {
            if (this.miniGames.isJoined(player)) {
                final MiniGame miniGame = this.miniGames.getJoined(player);
                for (final String cmd : miniGame.getMessageList("fallingBlockCmd")) {
                    final String output = miniGame.executeEventCommands(cmd
                            .replace("{fallingBlockUuid}", fallingBlock.getUniqueId().toString())
                            .replace("{fallingBlockId}", String.valueOf(fallingBlock.getBlockId()))
                            .replace("{fallingBlockData}", String.valueOf(fallingBlock.getBlockData()))
                            , player);
                    if (output.equals("false")) {
                        event.setCancelled(true);
                    }
                }
            }
        }


    }
}