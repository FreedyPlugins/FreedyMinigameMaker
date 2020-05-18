package freedy.freedyminigamemaker;

import org.apache.commons.lang.StringUtils;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

public class PlayerData {


    public Player player;
    //public String teamName;
    public boolean extraDamageMode;
    public boolean resistingDamageMode;
    public boolean dropItemMode;
    public Map<String, String> customData;

    public PlayerData(Player player) {
        this.player = player;
        customData = new HashMap<>();
    }

    public void setCustomData(String key, String value) {
        if (value.equals("none")) removeCustomData(key);
        else this.customData.put(key, value);
    }


    public void removeCustomData(String key) {
        this.customData.remove(key);
    }


    public String getCustomData(String key) {
        if (customData.containsKey(key)) return this.customData.get(key);
        else return "none";
    }

    public String getData(String string) {


        String area = StringUtils.substringBetween(string, "{playerData(", ")}");

        if (area == null) return string;

        String result = getCustomData(area);

        if (result == null) return string;

        return string.replace("{playerData("+ area + ")}", result);


    }
}
