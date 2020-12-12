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
            String itemAmount = "none";
            String itemDurability = "none";
            if (itemStack != null) {
                itemType = itemStack.getType().name();
                itemDurability = String.valueOf(itemStack.getDurability());
                itemAmount = String.valueOf(itemStack.getAmount());
                if (itemStack.hasItemMeta() && itemStack.getItemMeta().hasDisplayName()) {
                    itemName = itemStack.getItemMeta().getDisplayName();
                }
            }


            String clickedEntityName;
            String clickedEntityType;
            String clickedEntityUuid;
            Entity entity = event.getRightClicked();
            clickedEntityName = entity.getName();
            clickedEntityUuid = entity.getUniqueId().toString();
            clickedEntityType = entity.getType().name();
            for (final String cmd : miniGame.getMessageList("interactEntityCmd")) {
                final String output = miniGame.executeEventCommands(cmd
                                .replace("{entityUuid}", clickedEntityUuid)
                                .replace("{entityName}", clickedEntityName)
                                .replace("{entityType}", clickedEntityType)
                                .replace("{itemName}", itemName)
                                .replace("{itemAmount}", itemAmount)
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
