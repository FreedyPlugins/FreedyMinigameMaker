// 
// Decompiled by Procyon v0.5.36
// 

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
        this.plugin.getConfig().set(this.gamePath + "gameType", "death");
        this.plugin.getConfig().set(this.gamePath + "joinMsg", "§6{player}\uc774(\uac00) {game}\uc5d0 \ucc38\uc5ec\ud588\uc2b5\ub2c8\ub2e4 §7({playerAmount}/{maxPlayers})");
        this.plugin.getConfig().set(this.gamePath + "quitMsg", "§6{player}\uc774(\uac00) \ub5a0\ub0ac\uc2b5\ub2c8\ub2e4");
        this.plugin.getConfig().set(this.gamePath + "startMsg", "§a{game}\uc774(\uac00) \uc2dc\uc791\ub418\uc5c8\uc5b4\uc694!");
        this.plugin.getConfig().set(this.gamePath + "morePlayerMsg", "§c\ud50c\ub808\uc774\uc5b4\ub97c \ub354 \uae30\ub2e4\ub9ac\uace0 \uc788\uc2b5\ub2c8\ub2e4...");
        this.plugin.getConfig().set(this.gamePath + "conStartCmd", new ArrayList<>());
        this.plugin.getConfig().set(this.gamePath + "joinCmd", new ArrayList<>());
        this.plugin.getConfig().set(this.gamePath + "quitCmd", new ArrayList<>());
        this.plugin.getConfig().set(this.gamePath + "conEndCmd", new ArrayList<>());
        final List<String> gameList = this.plugin.getConfig().getStringList("gameList");
        gameList.add(this.gameName);
        this.plugin.getConfig().set("gameList", gameList);
        this.plugin.saveConfig();
    }
    
    public void addLocation(final String locationPath, final String world, final double x, final double y, final double z, final float yaw, final float pitch) {
        final int locNum = this.plugin.getConfig().getInt(this.gamePath + "." + locationPath + "Amount") + 1;
        this.plugin.getConfig().set(this.gamePath + locationPath + "Amount", locNum);
        this.plugin.getConfig().set(this.gamePath + locationPath + "." + locNum + ".world", world);
        this.plugin.getConfig().set(this.gamePath + locationPath + "." + locNum + ".x", x);
        this.plugin.getConfig().set(this.gamePath + locationPath + "." + locNum + ".y", y);
        this.plugin.getConfig().set(this.gamePath + locationPath + "." + locNum + ".z", z);
        this.plugin.getConfig().set(this.gamePath + locationPath + "." + locNum + ".yaw", yaw);
        this.plugin.getConfig().set(this.gamePath + locationPath + "." + locNum + ".pitch", pitch);
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
    
    public void addMessage(final String messagePath, final String message) {
        final List<String> messageList = this.plugin.getConfig().getStringList(this.gamePath + messagePath);
        messageList.add(message);
        this.plugin.getConfig().set(this.gamePath + messagePath, messageList);
        this.plugin.saveConfig();
    }
}
