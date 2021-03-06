// 
// Decompiled by Procyon v0.5.36
// 

package freedy.freedyminigamemaker.events;

import freedy.freedyminigamemaker.FreedyMinigameMaker;
import freedy.freedyminigamemaker.MiniGame;
import freedy.freedyminigamemaker.MiniGames;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.vehicle.VehicleDamageEvent;
import org.bukkit.event.vehicle.VehicleEntityCollisionEvent;
import org.bukkit.event.vehicle.VehicleExitEvent;

public class VehicleEvent implements Listener
{
    MiniGames miniGames;
    
    public VehicleEvent() {
        this.miniGames = FreedyMinigameMaker.miniGames;
    }
    
    @EventHandler
    public void onDamageVehicle(final VehicleDamageEvent event) {
        if (event.getAttacker() instanceof Player) {
            final Player player = (Player)event.getAttacker();
            if (this.miniGames.isJoined(player)) {
                final MiniGame miniGame = this.miniGames.getJoined(player);
                for (final String cmd : miniGame.getMessageList("vehicleDamageCmd")) {
                    final String output = miniGame.executeEventCommands(cmd.replace("{vehicleName}", event.getVehicle().getCustomName()).replace("{vehicleType}", event.getVehicle().getType().name()), player);
                    if (output.equals("false")) {
                        event.setCancelled(true);
                    }
                }
            }
        }
    }
    
    @EventHandler
    public void onExitVehicle(final VehicleExitEvent event) {
        if (event.getExited() instanceof Player) {
            final Player player = (Player)event.getExited();
            if (this.miniGames.isJoined(player)) {
                final MiniGame miniGame = this.miniGames.getJoined(player);
                for (final String cmd : miniGame.getMessageList("vehicleExitCmd")) {
                    final String output = miniGame.executeEventCommands(cmd.replace("{vehicleName}", event.getVehicle().getCustomName()).replace("{vehicleType}", event.getVehicle().getType().name()), player);
                    if (output.equals("false")) {
                        event.setCancelled(true);
                    }
                }
            }
        }
    }
    
    @EventHandler
    public void onMoveVehicle(final VehicleEntityCollisionEvent event) {
        if (event.getVehicle().getPassenger() instanceof Player) {
            final Player player = (Player)event.getVehicle().getPassenger();
            if (this.miniGames.isJoined(player)) {
                final MiniGame miniGame = this.miniGames.getJoined(player);
                for (final String cmd : miniGame.getMessageList("vehicleCollisionCmd")) {
                    miniGame.executeEventCommands(cmd.replace("{vehicleName}", event.getVehicle().getCustomName()).replace("{vehicleType}", event.getVehicle().getType().name()), player);
                }
            }
        }
    }
}
