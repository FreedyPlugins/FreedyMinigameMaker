package freedy.freedyminigamemaker.events;

import freedy.freedyminigamemaker.FreedyMinigameMaker;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import java.util.List;

public class DeathEvent implements Listener {

    FreedyMinigameMaker plugin;

    public DeathEvent(FreedyMinigameMaker plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onDamage(EntityDamageByEntityEvent event) {

        Entity entity = event.getEntity();
        Entity damager = event.getDamager();
        if (entity instanceof Player) {
            Player player = ((Player) entity).getPlayer();
            if (player.getHealth() <= event.getDamage()) {
                if (damager instanceof Projectile) {
                    Entity shooter = (Entity) ((Projectile) damager).getShooter();

                    if (shooter instanceof Player) {
                        Player playerShooter = (Player) shooter;
                        if (this.onDeath(playerShooter.getName(), playerShooter.getKiller().getName())) event.setCancelled(true);
                    }


                } else if (damager instanceof Player) {
                    if(this.onDeath(player.getName(), player.getKiller().getName())) event.setCancelled(true);
                }
            }
        }
    }

    private boolean onDeath(String playerName, String killerName) {
        List<String> gameList = plugin.getConfig().getStringList("gameList");
        boolean willCancel = false;
        for (String gameName : gameList) {
            List<String> playerNameList = plugin.getConfig().getStringList("miniGames." + gameName + ".players");
            if (playerNameList.contains(playerName)) {
                willCancel = true;
                switch (plugin.getConfig().getString("miniGames." + gameName + ".gameType")) {
                    case "zombieMode":
                        List<String> redTeamPlayerNameList = plugin.getConfig().getStringList("miniGames." + gameName + ".redTeamPlayerList");
                        List<String> blueTeamPlayerNameList = plugin.getConfig().getStringList("miniGames." + gameName + ".blueTeamPlayerList");
                        if (blueTeamPlayerNameList.contains(playerName)) {
                            blueTeamPlayerNameList.remove(playerName);
                            redTeamPlayerNameList.add(playerName);
                            for (String s : playerNameList) {
                                Bukkit.getPlayer(s).sendMessage("§6플레이어 " + playerName + "이(가) §c좀비 &6" + killerName + "가 되었습니다" );
                            }
                        } else if (redTeamPlayerNameList.contains(playerName)) {
                            redTeamPlayerNameList.remove(playerName);
                            Player player = Bukkit.getPlayer(playerName);
                            for (String s : playerNameList) {
                                Bukkit.getPlayer(s).sendMessage("§c좀비 " + playerName + "§6이(가) 플레이어" + killerName + "에게 죽었습니다" );
                            }
                            player.setGameMode(GameMode.SPECTATOR);
                            player.sendMessage("§6당신은 이제 관전 모드입니다");
                        }
                        plugin.getConfig().set("miniGames." + gameName + ".blueTeamPlayerList", blueTeamPlayerNameList);
                        plugin.getConfig().set("miniGames." + gameName + ".redTeamPlayerList", redTeamPlayerNameList);
                        plugin.saveConfig();
                        if (plugin.checkWillStopGame(gameName)) plugin.stopGame(gameName);
                        break;
                }
            }
        }
        return willCancel;
    }
}
