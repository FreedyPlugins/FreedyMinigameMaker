package freedy.freedyminigamemaker;

import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

public class MiniGames {

    FreedyMinigameMaker plugin;
    Map<String, MiniGame> miniGames;

    public MiniGames(FreedyMinigameMaker plugin) {
        this.plugin = plugin;
        miniGames = new HashMap<>();
        for (String gameName : plugin.getConfig().getStringList("gameList"))
            add(gameName);
    }

    public void add(String gameName) {
        miniGames.put(gameName, new MiniGame(plugin, gameName));
    }

    public void remove(String gameName) {
        miniGames.remove(gameName);
    }


    public boolean isJoined(Player player) {
        for (MiniGame minigame : miniGames.values())
            if (minigame.playerList.contains(player))
                return true;

        return false;
    }

    public boolean isJoined(String gameName, Player player) {
        return miniGames.get(gameName).playerList.contains(player);
    }

    public MiniGame getJoined(Player player) {
        for (MiniGame minigame : miniGames.values())
            if (minigame.playerList.contains(player)) return minigame;

        return null;
    }

    public MiniGame get(String gameName) {
        MiniGame miniGame = miniGames.get(gameName);
        if (miniGame == null) miniGame = new MiniGame(plugin, gameName);
        return miniGame;
    }

    public DataEditor getEditor(String gameName) {
        MiniGame miniGame = miniGames.get(gameName);
        if (miniGame == null) miniGame = new MiniGame(plugin, gameName);
        return miniGame;
    }


}