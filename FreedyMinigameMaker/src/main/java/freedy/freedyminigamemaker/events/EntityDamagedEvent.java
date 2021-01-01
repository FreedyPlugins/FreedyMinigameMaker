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

public class EntityDamagedEvent implements Listener {
    MiniGames miniGames;

    public EntityDamagedEvent() {
        this.miniGames = FreedyMinigameMaker.miniGames;
    }










    @EventHandler
    public void onDamage(final EntityDamageByEntityEvent event) {


        final Entity damagedEntity = event.getEntity();
        if (damagedEntity instanceof Player) {
            final Player player = (Player) damagedEntity;
            if (player.getHealth() <= event.getFinalDamage()) {



                if (this.miniGames.isJoined(player)) {
                    final MiniGame miniGame = this.miniGames.getJoined(player);
                    for (final String cmd : miniGame.getMessageList("preDeathCmd")) {
                        final String output = miniGame.executeEventCommands(cmd
                                        .replace("{killerType}", event.getDamager().getType().name())
                                        .replace("{killerName}", event.getDamager().getName())
                                , player);
                        if (output.equals("false")) {
                            event.setCancelled(true);
                        }
                    }
                }


            }

        }


        final Entity damagerEntity = event.getDamager();
        if (damagerEntity instanceof Player) {
            final Player player2 = (Player) damagerEntity;
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

                String clickedEntityName = damagedEntity.getName();
                String clickedEntityType = damagedEntity.getType().name();
                String clickedEntityUuid = damagedEntity.getUniqueId().toString();

                for (final String cmd : miniGame2.getMessageList("damagedCmd")) {
                    final String output = miniGame2.executeEventCommands(cmd
                                    .replace("{entityName}", clickedEntityName)
                                    .replace("{entityUuid}", clickedEntityUuid)
                                    .replace("{entityType}", clickedEntityType)
                                    .replace("{itemName}", itemName)
                                    .replace("{itemDurability}", itemDurability)
                                    .replace("{itemType}", itemType)
                            , player2);
                    if (output.equals("false")) {
                        event.setCancelled(true);
                    }
                }


            }
        } else if (damagerEntity instanceof Projectile) {
            Projectile projectile = ((Projectile) damagerEntity);
            if (projectile.getShooter() instanceof Player) {
                Player player = ((Player) projectile.getShooter());
                if (this.miniGames.isJoined(player)) {
                    final MiniGame miniGame = this.miniGames.getJoined(player);
                    for (final String cmd : miniGame.getMessageList("projectileCmd")) {
                        final String output = miniGame.executeEventCommands(cmd
                                        .replace("{damage}", String.valueOf(event.getFinalDamage()))
                                        .replace("{cause}", event.getCause().name())
                                        .replace("{projectileType}", damagerEntity.getType().name())
                                        .replace("{projectileName}", damagerEntity.getName())
                                        .replace("{projectileUuid}", damagerEntity.getUniqueId().toString())
                                        .replace("{entityType}", damagedEntity.getType().name())
                                        .replace("{entityName}", damagedEntity.getName())
                                        .replace("{entityUuid}", damagedEntity.getUniqueId().toString())
                                , player);
                        if (output.equals("false")) {
                            event.setCancelled(true);
                        }
                    }
                }
            }
        }
    }
}


