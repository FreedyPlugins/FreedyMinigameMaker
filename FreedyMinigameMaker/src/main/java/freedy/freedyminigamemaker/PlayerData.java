// 
// Decompiled by Procyon v0.5.36
// 

package freedy.freedyminigamemaker;

import org.apache.commons.lang.StringUtils;
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
    
    public PlayerData(final Player player) {
        this.bossBar = new HashMap<>();
        this.inventory = new HashMap<>();
        this.player = player;
        this.customData = new HashMap<>();
        this.scoreBoard = new HashMap<>();
        this.scoreBoardTitle = "";
    }

    public void resetInventory() {
        this.inventory = new HashMap<>();
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
