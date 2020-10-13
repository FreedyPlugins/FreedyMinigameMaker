// 
// Decompiled by Procyon v0.5.36
// 

package freedy.freedyminigamemaker.events;

import org.bukkit.event.EventHandler;
import freedy.freedyminigamemaker.MiniGame;
import org.bukkit.entity.Entity;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.ChatMessageType;
import java.util.concurrent.ThreadLocalRandom;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import freedy.freedyminigamemaker.FreedyMinigameMaker;
import freedy.freedyminigamemaker.MiniGames;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;

public class DamageEvent implements Listener
{
    MiniGames miniGames;
    
    public DamageEvent() {
        this.miniGames = FreedyMinigameMaker.miniGames;
    }
    
    @EventHandler
    public void onDamage(final EntityDamageByEntityEvent event) {



        final Entity damagedEntity = event.getEntity();
        if (damagedEntity instanceof Player) {
            final Player player = (Player)damagedEntity;
            if (this.miniGames.isJoined(player)) {
                final MiniGame miniGame = this.miniGames.getJoined(player);


                if (miniGame.getPlayerData(player).getCustomData("skillMode").equals("resistingDamageMode") && ThreadLocalRandom.current().nextInt(1, 101) <= miniGame.getResistingRate()) {
                    double damage = event.getDamage();
                    damage *= miniGame.getResistingDamage() / 100.0;
                    event.setDamage(damage);
                    player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(miniGame.getMessage("resistingDamageMsg")));
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

                String clickedEntityName;
                String clickedEntityType;
                clickedEntityName = damagedEntity.getName();
                clickedEntityType = damagedEntity.getType().name();
                for (final String cmd : miniGame2.getMessageList("interactEntityCmd")) {
                    final String output = miniGame2.executeEventCommands(cmd
                                    .replace("{entityName}", clickedEntityName)
                                    .replace("{entityType}", clickedEntityType)
                                    .replace("{itemName}", itemName)
                                    .replace("{itemDurability}", itemDurability)
                                    .replace("{itemType}", itemType)
                            , player2);
                    if (output.equals("false")) {
                        event.setCancelled(true);
                    }
                }

                if (miniGame2.getPlayerData(player2).getCustomData("skillMode").equals("extraDamageMode") && ThreadLocalRandom.current().nextInt(1, 101) <= miniGame2.getDamageRate()) {
                    double damage2 = event.getDamage();
                    damage2 *= miniGame2.getExtraDamage() / 100.0;
                    event.setDamage(damage2);
                    final String message = miniGame2.getMessage("extraDamageMsg");
                    if (message != null) {
                        player2.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(message));
                    }
                }
            }
        }
    }
}
