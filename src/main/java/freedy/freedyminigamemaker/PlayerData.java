// 
// Decompiled by Procyon v0.5.36
// 

package freedy.freedyminigamemaker;

import org.bukkit.Location;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.util.HashMap;
import java.util.Map;

public class PlayerData
{
    public Player player;
    public Map<String, String> customData;
    public Map<Integer, String> scoreBoard;
    public Map<String, BossBar> bossBar;
    public String scoreBoardTitle;
    public Map<String, Inventory> inventory;
    public Location respawnPoint;
    
    public PlayerData(final Player player) {
        this.respawnPoint = null;
        this.bossBar = new HashMap<String, BossBar>();
        this.inventory = new HashMap<String, Inventory>();
        this.player = player;
        this.customData = new HashMap<String, String>();
        this.scoreBoard = new HashMap<Integer, String>();
        this.scoreBoardTitle = "";
    }
    
    public void setRespawnPoint(final Location location) {
        this.respawnPoint = location;
    }
    
    public Location getRespawnPoint() {
        return this.respawnPoint;
    }
    
    public void resetInventory() {
        this.inventory = new HashMap<String, Inventory>();
    }
    
    public void setBossBar(final String barName, final BossBar bossBar) {
        this.bossBar.put(barName, bossBar);
    }
    
    public BossBar getBossBar(final String barName) {
        return this.bossBar.get(barName);
    }
    
    public void setInventory(final String invName, final Inventory inventory) {
        this.inventory.put(invName, inventory);
    }
    
    public Inventory getInventory(final String invName) {
        return this.inventory.get(invName);
    }
    
    public void setCustomData(final String key, final String value) {
        if (value.equals("none")) {
            this.removeCustomData(key);
        }
        else {
            this.customData.put(key, value);
        }
    }
    
    public void removeCustomData(final String key) {
        this.customData.remove(key);
    }
    
    public String getCustomData(final String key) {
        if (this.customData.containsKey(key)) {
            return this.customData.get(key);
        }
        return "none";
    }
    
    public String replaceData(final String string) {
        final String area = MiniGame.getSubFunc(string, "{playerData(");
        if (area == null) {
            return string;
        }
        final String result = this.getCustomData(area);
        if (result == null) {
            return string;
        }
        return string.replace("{playerData(" + area + ")}", result);
    }
}
