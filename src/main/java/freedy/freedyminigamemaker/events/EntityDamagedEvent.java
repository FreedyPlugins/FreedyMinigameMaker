// 
// Decompiled by Procyon v0.5.36
// 

package freedy.freedyminigamemaker.events;

import freedy.freedyminigamemaker.FreedyMinigameMaker;
import freedy.freedyminigamemaker.MiniGame;
import freedy.freedyminigamemaker.MiniGames;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;

public class EntityDamagedEvent implements Listener
{
    MiniGames miniGames;
    
    public EntityDamagedEvent() {
        this.miniGames = FreedyMinigameMaker.miniGames;
    }
    
    @EventHandler
    public void onDamage(final EntityDamageByEntityEvent event) {
        final Entity damagedEntity = event.getEntity();
        if (damagedEntity instanceof Player) {
            final Player player = (Player)damagedEntity;
            if (player.getHealth() <= event.getFinalDamage() && this.miniGames.isJoined(player)) {
                final MiniGame miniGame = this.miniGames.getJoined(player);
                for (final String cmd : miniGame.getMessageList("preDeathCmd")) {
                    final String output = miniGame.executeEventCommands(cmd.replace("{killerType}", event.getDamager().getType().name()).replace("{killerName}", event.getDamager().getName()), player);
                    if (output.equals("false")) {
                        event.setCancelled(true);
                    }
                }
            }
        }
        final Entity damagerEntity = event.getDamager();
        if (damagerEntity instanceof Player) {
            final Player player2 = (Player)damagerEntity;
            if (this.miniGames.isJoined(player2)) {
                final MiniGame miniGame2 = this.miniGames.getJoined(player2);
                final ItemStack itemStack = player2.getItemOnCursor();
                String itemType = "none";
                String itemName = "none";
                String itemDurability = "none";
                if (itemStack != null) {
                    itemType = itemStack.getType().name();
                    itemDurability = String.valueOf(itemStack.getDurability());
                    if (itemStack.hasItemMeta() && itemStack.getItemMeta().hasDisplayName()) {
                        itemName = itemStack.getItemMeta().getDisplayName();
                    }
                }
                final String clickedEntityName = damagedEntity.getName();
                final String clickedEntityType = damagedEntity.getType().name();
                final String clickedEntityUuid = damagedEntity.getUniqueId().toString();
                for (final String cmd2 : miniGame2.getMessageList("damagedCmd")) {
                    final String output2 = miniGame2.executeEventCommands(cmd2.replace("{entityName}", clickedEntityName).replace("{entityUuid}", clickedEntityUuid).replace("{entityType}", clickedEntityType).replace("{itemName}", itemName).replace("{itemDurability}", itemDurability).replace("{itemType}", itemType), player2);
                    if (output2.equals("false")) {
                        event.setCancelled(true);
                    }
                }
            }
        }
        else if (damagerEntity instanceof Projectile) {
            final Projectile projectile = (Projectile)damagerEntity;
            if (projectile.getShooter() instanceof Player) {
                final Player player3 = (Player)projectile.getShooter();
                if (this.miniGames.isJoined(player3)) {
                    final MiniGame miniGame3 = this.miniGames.getJoined(player3);
                    for (final String cmd3 : miniGame3.getMessageList("projectileCmd")) {
                        final String output3 = miniGame3.executeEventCommands(cmd3.replace("{damage}", String.valueOf(event.getFinalDamage())).replace("{cause}", event.getCause().name()).replace("{projectileType}", damagerEntity.getType().name()).replace("{projectileName}", damagerEntity.getName()).replace("{projectileUuid}", damagerEntity.getUniqueId().toString()).replace("{entityType}", damagedEntity.getType().name()).replace("{entityName}", damagedEntity.getName()).replace("{entityUuid}", damagedEntity.getUniqueId().toString()), player3);
                        if (output3.equals("false")) {
                            event.setCancelled(true);
                        }
                    }
                }
            }
        }
    }
}
