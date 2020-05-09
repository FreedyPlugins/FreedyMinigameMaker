package freedy.freedyminigamemaker;

import org.bukkit.entity.Player;

public class PlayerData {


    public Player player;
    //public String teamName;
    public boolean extraDamageMode;
    public boolean resistingDamageMode;
    public boolean dropItemMode;


    public PlayerData(Player player) {
        this.player = player;
    }

}
