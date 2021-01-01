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

    public MiniGames(final FreedyMinigameMaker plugin) {
        fileStore = new FileStore(plugin, "data.yml");
        settings = new FileStore(plugin, "settings.yml");
        items = new FileStore(plugin, "items.yml");
        this.plugin = plugin;
        reset();
        setupWE();
    }

    public void setupWE() {
        final String version = StringUtils.substringBetween(Bukkit.getVersion(), "(MC: ", ")");
        final String finalVersion = version.substring(0, StringUtils.lastIndexOfIgnoreCase(version, "."));
        switch (finalVersion) {
            case "1.12":
                break;
            case "1.16":
                break;
        }
    }

    public void reset(String gameName) {
        remove(gameName);
        add(gameName);
    }

    public void reset() {
        this.miniGames = new HashMap<>();
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
        return getNoneGame();
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

    public FileStore getSettings() {
        return MiniGames.settings;
    }

    public FileStore getItems() {
        return MiniGames.items;
    }
}
