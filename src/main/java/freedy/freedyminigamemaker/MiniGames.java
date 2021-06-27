// 
// Decompiled by Procyon v0.5.36
// 

package freedy.freedyminigamemaker;

import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

public class MiniGames
{
    public FreedyMinigameMaker plugin;
    public Map<String, MiniGame> miniGames;
    public MiniGame noneGame;
    public static FileStore fileStore;
    public static FileStore settings;
    public static FileStore items;
    public static FileStore locations;

    public MiniGames(final FreedyMinigameMaker plugin) {
        MiniGames.fileStore = new FileStore(plugin, "data.yml");
        MiniGames.settings = new FileStore(plugin, "settings.yml");
        MiniGames.items = new FileStore(plugin, "items.yml");
        MiniGames.locations = new FileStore(plugin, "locations.yml");
        this.plugin = plugin;
        this.reset();
    }
    
    public void setupWE() {
        final String version = StringUtils.substringBetween(Bukkit.getVersion(), "(MC: ", ")");
        final String substring;
        final String finalVersion = substring = version.substring(0, StringUtils.lastIndexOfIgnoreCase(version, "."));
        switch (substring) {
        }
    }
    
    public void reset(final String gameName) {
        this.remove(gameName);
        this.add(gameName);
    }
    
    public void reset() {
        this.miniGames = new HashMap<String, MiniGame>();
        for (final String gameName : this.plugin.getConfig().getStringList("gameList")) {
            this.add(gameName);
        }
        this.noneGame = new MiniGame(this.plugin, "none");
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
        return this.getNoneGame();
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
    
    public static FileStore getFileStore() {
        return MiniGames.fileStore;
    }
    
    public static FileStore getSettings() {
        return MiniGames.settings;
    }
    
    public static FileStore getItems() {
        return MiniGames.items;
    }

    public static FileStore getLocations() {
        return MiniGames.locations;
    }
}
