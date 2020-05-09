package freedy.freedyminigamemaker.events;

import freedy.freedyminigamemaker.FreedyMinigameMaker;
import freedy.freedyminigamemaker.MiniGame;
import freedy.freedyminigamemaker.MiniGames;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import java.util.concurrent.ThreadLocalRandom;

public class DamageEvent implements Listener {

    FreedyMinigameMaker plugin;

    MiniGames miniGames;

    public DamageEvent(FreedyMinigameMaker plugin) {
        this.plugin = plugin;
        this.miniGames = plugin.miniGames;
    }

    @EventHandler
    public void onDamage(EntityDamageByEntityEvent event) {
        Entity damagedEntity = event.getEntity();
        if (damagedEntity instanceof Player){
            Player player = (Player) damagedEntity;
            if (miniGames.isJoined(player)) {
                MiniGame miniGame = miniGames.getJoined(player);
                if (miniGame.getPlayerData(player).resistingDamageMode)
                    if (ThreadLocalRandom.current().nextInt(1, 100 + 1) <= miniGame.getResistingRate()) {
                        double damage = event.getDamage();
                        damage -= miniGame.getResistingDamage();
                        event.setDamage(damage);
                    }

            }

        }

        Entity damagerEntity = event.getDamager();
        if (damagerEntity instanceof Player) {
            Player player = (Player) damagerEntity;
            if (miniGames.isJoined(player)) {
                MiniGame miniGame = miniGames.getJoined(player);
                if (miniGame.getPlayerData(player).extraDamageMode)
                    if (ThreadLocalRandom.current().nextInt(1, 100 + 1) <= miniGame.getDamageRate()) {
                        double damage = event.getDamage();
                        damage += miniGame.getExtraDamage();
                        event.setDamage(damage);
                    }

            }
        }
    }
}
