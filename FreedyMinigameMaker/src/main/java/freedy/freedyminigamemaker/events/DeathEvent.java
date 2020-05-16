package freedy.freedyminigamemaker.events;

import freedy.freedyminigamemaker.FreedyMinigameMaker;
import freedy.freedyminigamemaker.MiniGame;
import freedy.freedyminigamemaker.MiniGames;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

public class DeathEvent implements Listener {

    MiniGames miniGames;

    FreedyMinigameMaker plugin;

    public DeathEvent(FreedyMinigameMaker plugin) {
        this.plugin = plugin;
        this.miniGames = plugin.miniGames;
    }

    @EventHandler
    public void onDamage(PlayerDeathEvent event) {
        Player player = event.getEntity().getPlayer();
        String playerName = player.getName();
        Player killer = player.getKiller();

        String killerName;

        if (killer != null) killerName = killer.getName();
        else killerName = "";

        if (miniGames.isJoined(player)) {
            MiniGame miniGame = miniGames.getJoined(player);
            switch (miniGame.getGameType()) {
                case "zombieMode":
                    if (miniGame.teamPlayers.get("blue").contains(player)) {
                        for (Player p : miniGame.playerList)
                            p.sendMessage(miniGame.getMessage("beZombieMsg").replace("{player}", playerName).replace("{killer}", killerName));

                        miniGame.teamPlayers.get("blue").remove(player);
                        miniGame.teamPlayers.get("red").add(player);

                    } else if (miniGame.teamPlayers.get("red").contains(player)) {
                        miniGame.remove(player);
                    }
                    break;
                default:
                    miniGame.remove(player);
                    miniGame.stop();
            }

        }

    }
}