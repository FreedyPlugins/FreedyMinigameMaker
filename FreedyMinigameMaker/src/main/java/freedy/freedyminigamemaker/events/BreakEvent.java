package freedy.freedyminigamemaker.events;

import freedy.freedyminigamemaker.FreedyMinigameMaker;
import freedy.freedyminigamemaker.MiniGame;
import freedy.freedyminigamemaker.MiniGames;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;

import java.util.concurrent.ThreadLocalRandom;

public class BreakEvent implements Listener {

    FreedyMinigameMaker plugin;

    MiniGames miniGames;

    public BreakEvent(FreedyMinigameMaker plugin) {
        this.plugin = plugin;
        this.miniGames = plugin.miniGames;
    }

    @EventHandler
    public void onBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();
        if (miniGames.isJoined(player)) {
            MiniGame miniGame = miniGames.getJoined(player);
            if (miniGame.getPlayerData(player).dropItemMode) {
                Block block = event.getBlock();
                Material material = block.getType();
                if (miniGame.getDropList().contains(material.toString())) {
                    if (ThreadLocalRandom.current().nextInt(1, 100 + 1) <= miniGame.getDropRate()) {
                        block.setType(Material.AIR);
                        for (int i = 0; i < miniGame.getDrops(material.toString()); i++) {
                            block.getWorld().dropItemNaturally(block.getLocation().add(0.5, 0.5, 0.5), new ItemStack(material));
                        }
                        String message = miniGame.getMessage("dropMsg");
                        if (message != null)
                            player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(message));
                    }
                }
            }
        }

    }
}
