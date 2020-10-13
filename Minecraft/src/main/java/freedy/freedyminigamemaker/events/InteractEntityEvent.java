package freedy.freedyminigamemaker.events;

import freedy.freedyminigamemaker.FreedyMinigameMaker;
import freedy.freedyminigamemaker.MiniGame;
import freedy.freedyminigamemaker.MiniGames;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.ItemStack;

public class InteractEntityEvent implements Listener {

    MiniGames miniGames;

    public InteractEntityEvent() {
        this.miniGames = FreedyMinigameMaker.miniGames;
    }


    @EventHandler
    public void onEntityclick(PlayerInteractEntityEvent event) {

        final Player player = event.getPlayer();
        if (this.miniGames.isJoined(player)) {
            final MiniGame miniGame = this.miniGames.getJoined(player);

            final ItemStack itemStack = player.getItemOnCursor();
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
            Entity entity = event.getRightClicked();
            clickedEntityName = entity.getName();
            clickedEntityType = entity.getType().name();
            for (final String cmd : miniGame.getMessageList("interactEntityCmd")) {
                final String output = miniGame.executeEventCommands(cmd
                                .replace("{entityName}", clickedEntityName)
                                .replace("{entityType}", clickedEntityType)
                                .replace("{itemName}", itemName)
                                .replace("{itemDurability}", itemDurability)
                                .replace("{itemType}", itemType)
                        , player);
                if (output.equals("false")) {
                    event.setCancelled(true);
                }
            }
        }
    }
}
