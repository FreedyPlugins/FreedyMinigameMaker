// 
// Decompiled by Procyon v0.5.36
// 

package freedy.freedyminigamemaker;

import org.bukkit.entity.Player;
import java.util.Iterator;
import java.util.HashMap;
import java.util.Map;

public class MiniGames
{
    FreedyMinigameMaker plugin;
    Map<String, MiniGame> miniGames;
    MiniGame noneGame;
    public static FileStore fileStore;
    
    public MiniGames(final FreedyMinigameMaker plugin) {
        MiniGames.fileStore = new FileStore(plugin);
        this.plugin = plugin;
        this.miniGames = new HashMap<String, MiniGame>();
        for (final String gameName : plugin.getConfig().getStringList("gameList")) {
            this.add(gameName);
        }
        this.noneGame = new MiniGame(plugin, "none");
    }
    
    public void add(final String gameName) {
        this.miniGames.put(gameName, new MiniGame(this.plugin, gameName));
    }
    
    public void remove(final String gameName) {
        this.miniGames.remove(gameName);
    }
    
    public boolean isJoined(final Player player) {
        for (final MiniGame minigame : this.miniGames.values()) {
            if (minigame.playerList.contains(player)) {
                return true;
            }
        }
        return false;
    }
    
    public MiniGame getJoined(final Player player) {
        for (final MiniGame minigame : this.miniGames.values()) {
            if (minigame.playerList.contains(player)) {
                return minigame;
            }
        }
        return null;
    }
    
    public MiniGame get(final String gameName) {
        MiniGame miniGame = this.miniGames.get(gameName);
        if (miniGame == null) {
            miniGame = new MiniGame(this.plugin, gameName);
        }
        return miniGame;
    }
    
    public DataEditor getEditor(final String gameName) {
        MiniGame miniGame = this.miniGames.get(gameName);
        if (miniGame == null) {
            miniGame = new MiniGame(this.plugin, gameName);
        }
        return miniGame;
    }
    
    public MiniGame getNoneGame() {
        return this.noneGame;
    }
    
    public FileStore getFileStore() {
        return MiniGames.fileStore;
    }
}
