package freedy.freedyminigamemaker;

import java.util.ArrayList;
import java.util.List;

public class DataEditor
{
    public String gameName;
    public String gamePath;
    public FreedyMinigameMaker plugin;
    
    public DataEditor(final FreedyMinigameMaker plugin, final String gameName) {
        this.plugin = plugin;
        this.gameName = gameName;
        this.gamePath = "miniGames." + gameName + ".";
    }
    
    public void remove(final String gameName) {
        final List<String> gameList = this.plugin.getConfig().getStringList("gameList");
        gameList.remove(gameName);
        this.plugin.getConfig().set("miniGames." + gameName, null);
        this.plugin.getConfig().set("gameList", gameList);
        this.plugin.saveConfig();
    }
    
    public void create(final int maxPlayers, final int maxStartPlayers, final int waitForStartTime) {
        this.plugin.getConfig().set(this.gamePath + "maxPlayers", maxPlayers);
        this.plugin.getConfig().set(this.gamePath + "maxStartPlayers", maxStartPlayers);
        this.plugin.getConfig().set(this.gamePath + "waitForStartTime", waitForStartTime*20);
        this.plugin.getConfig().set(this.gamePath + "joinMsg", "§6{player} joined in the {game} game §7({playerAmount}/{maxPlayers})");
        this.plugin.getConfig().set(this.gamePath + "quitMsg", "§6{player} has left.");
        this.plugin.getConfig().set(this.gamePath + "startMsg", "§aThe {game} has started!");
        this.plugin.getConfig().set(this.gamePath + "morePlayerMsg", "§cI'm waiting for more players....");
        this.plugin.getConfig().set(this.gamePath + "conStartCmd", new ArrayList<>());
        this.plugin.getConfig().set(this.gamePath + "joinCmd", new ArrayList<>());
        this.plugin.getConfig().set(this.gamePath + "quitCmd", new ArrayList<>());
        this.plugin.getConfig().set(this.gamePath + "conEndCmd", new ArrayList<>());
        final List<String> gameList = this.plugin.getConfig().getStringList("gameList");
        if (!gameList.contains(this.gameName)) gameList.add(this.gameName);
        this.plugin.getConfig().set("gameList", gameList);
        this.plugin.saveConfig();
    }
    
    public void setLocation(final String locationPath, final String world, final double x, final double y, final double z, final float yaw, final float pitch) {
        this.plugin.getConfig().set(this.gamePath + locationPath + ".world", world);
        this.plugin.getConfig().set(this.gamePath + locationPath + ".x", x);
        this.plugin.getConfig().set(this.gamePath + locationPath + ".y", y);
        this.plugin.getConfig().set(this.gamePath + locationPath + ".z", z);
        this.plugin.getConfig().set(this.gamePath + locationPath + ".yaw", yaw);
        this.plugin.getConfig().set(this.gamePath + locationPath + ".pitch", pitch);
        this.plugin.saveConfig();
    }
    
    public void setLocation(final String locationPath, final String world, final double x, final double y, final double z) {
        this.plugin.getConfig().set(this.gamePath + locationPath + ".world", world);
        this.plugin.getConfig().set(this.gamePath + locationPath + ".x", x);
        this.plugin.getConfig().set(this.gamePath + locationPath + ".y", y);
        this.plugin.getConfig().set(this.gamePath + locationPath + ".z", z);
        this.plugin.saveConfig();
    }

}
