package freedy.freedyminigamemaker.events;

import freedy.freedyminigamemaker.FreedyMinigameMaker;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

import java.util.List;

public class MoveEvent implements Listener {

    private FreedyMinigameMaker plugin;

    public MoveEvent(FreedyMinigameMaker plugin) {
        this.plugin = plugin;
    }


    @EventHandler
    public void onMove(PlayerMoveEvent event) {
        Location to = event.getTo();
        Location from = event.getFrom();
        if (!to.getBlock().equals(from.getBlock())) {
            Block blockTo = to.getBlock();
            Block blockFrom = from.getBlock();
            Player player = event.getPlayer();
            String playerName = player.getName();
            List<String> gameList = plugin.getConfig().getStringList("gameList");
            for (String gameName : gameList) {
                if (plugin.getConfig().getStringList("miniGames." + gameName + ".players").contains(playerName)) {
                    switch (plugin.getConfig().getString("miniGames." + gameName + ".gameType")) {
                        case "hideAndSeek":
                            blockFrom.setType(Material.valueOf(plugin.getConfig().getString("miniGames." + gameName + ".playerData." + playerName + ".backup")));
                            plugin.getConfig().set("miniGames." + gameName + "playerData" + playerName + "backup", blockTo.getType().toString());
                            blockTo.setType(Material.valueOf(plugin.getConfig().getString("miniGames." + gameName + ".playerData." + playerName + ".blockData")));
                            plugin.saveConfig();
                            break;
                    }
                }
            }
        }
    }
}
