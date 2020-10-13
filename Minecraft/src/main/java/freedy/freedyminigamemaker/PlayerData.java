// 
// Decompiled by Procyon v0.5.36
// 

package freedy.freedyminigamemaker;

import org.apache.commons.lang.StringUtils;
import java.util.HashMap;
import java.util.Map;
import org.bukkit.entity.Player;

public class PlayerData
{
    public Player player;
    public Map<String, String> customData;
    public Map<Integer, String> scoreBoard;
    public String scoreBoardTitle;
    
    public PlayerData(final Player player) {
        this.player = player;
        this.customData = new HashMap<String, String>();
        this.scoreBoard = new HashMap<Integer, String>();
        this.scoreBoardTitle = "";
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
        final String area = StringUtils.substringBetween(string, "{playerData(", ")}");
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
