// 
// Decompiled by Procyon v0.5.36
// 

package freedy.freedyminigamemaker;

import java.util.ArrayList;
import java.util.List;

public class DataEditor
{
    public final String superPath = "miniGames.";
    public String gameName;
    public String gamePath;
    public FreedyMinigameMaker plugin;
    
    public DataEditor(final FreedyMinigameMaker plugin, final String gameName) {
        this.plugin = plugin;
        this.gameName = gameName;
        this.gamePath = "miniGames." + gameName + ".";
    }
    
    public void remove(final String gameName) {
        final List<String> gameList = (List<String>)this.plugin.getConfig().getStringList("gameList");
        gameList.remove(gameName);
        this.plugin.getConfig().set("miniGames." + gameName, (Object)null);
        this.plugin.getConfig().set("gameList", (Object)gameList);
        this.plugin.saveConfig();
    }
    
    public void create(final int maxPlayers, final int maxStartPlayers, final int waitForStartTime, final int waitForEndTime) {
        this.plugin.getConfig().set(this.gamePath + "maxPlayers", (Object)maxPlayers);
        this.plugin.getConfig().set(this.gamePath + "maxStartPlayers", (Object)maxStartPlayers);
        this.plugin.getConfig().set(this.gamePath + "waitForStartTime", (Object)waitForStartTime);
        this.plugin.getConfig().set(this.gamePath + "waitForEndTime", (Object)waitForEndTime);
        this.plugin.getConfig().set(this.gamePath + "gameType", (Object)"death");
        this.plugin.getConfig().set(this.gamePath + "timePerPlayer", (Object)1);
        this.plugin.getConfig().set(this.gamePath + "joinMsg", (Object)"§6{player}\uc774(\uac00) {game}\uc5d0 \ucc38\uc5ec\ud588\uc2b5\ub2c8\ub2e4 §7({playerAmount}/{maxPlayers})");
        this.plugin.getConfig().set(this.gamePath + "quitMsg", (Object)"§6{player}\uc774(\uac00) \ub5a0\ub0ac\uc2b5\ub2c8\ub2e4");
        this.plugin.getConfig().set(this.gamePath + "startMsg", (Object)"§a{game}\uc774(\uac00) \uc2dc\uc791\ub418\uc5c8\uc5b4\uc694!");
        this.plugin.getConfig().set(this.gamePath + "noWinnerEndMsg", (Object)"§a{game}\uc774(\uac00) \uc885\ub8cc\ub418\uc5c8\uc5b4\uc694, \ubb34\uc2b9\ubd80\uc785\ub2c8\ub2e4!");
        this.plugin.getConfig().set(this.gamePath + "redWinEndMsg", (Object)"§a{game}\uc774(\uac00) \uc885\ub8cc\ub418\uc5c8\uc5b4\uc694, \ub808\ub4dc\ud300\uc774 \uc2b9\ub9ac\ud558\uc600\uc2b5\ub2c8\ub2e4!");
        this.plugin.getConfig().set(this.gamePath + "blueWinEndMsg", (Object)"§a{game}\uc774(\uac00) \uc885\ub8cc\ub418\uc5c8\uc5b4\uc694, \ube14\ub8e8\ud300\uc774 \uc2b9\ub9ac\ud558\uc600\uc2b5\ub2c8\ub2e4!");
        this.plugin.getConfig().set(this.gamePath + "endMsg", (Object)"§a{game}\uc774(\uac00) \uc885\ub8cc\ub418\uc5c8\uc5b4\uc694! \uc2b9\uc790\ub294 {player}\uc785\ub2c8\ub2e4!");
        this.plugin.getConfig().set(this.gamePath + "startTimerMsg", (Object)"§7{time}\ucd08 \ud6c4\uc5d0 \uc2dc\uc791...");
        this.plugin.getConfig().set(this.gamePath + "endTimerMsg", (Object)"§7{time}\ucd08 \ud6c4\uc5d0 \uc885\ub8cc...");
        this.plugin.getConfig().set(this.gamePath + "morePlayerMsg", (Object)"§c\ud50c\ub808\uc774\uc5b4\ub97c \ub354 \uae30\ub2e4\ub9ac\uace0 \uc788\uc2b5\ub2c8\ub2e4...");
        this.plugin.getConfig().set(this.gamePath + "beZombieMsg", (Object)"§c{player}\uc774(\uac00) {killer}\uc5d0 \uc758\ud574 \uc880\ube44\uac00 \ub418\uc5c8\uc2b5\ub2c8\ub2e4!");
        this.plugin.getConfig().set(this.gamePath + "conStartCmd", (Object)new ArrayList());
        this.plugin.getConfig().set(this.gamePath + "joinCmd", (Object)new ArrayList());
        this.plugin.getConfig().set(this.gamePath + "quitCmd", (Object)new ArrayList());
        this.plugin.getConfig().set(this.gamePath + "conEndCmd", (Object)new ArrayList());
        this.plugin.getConfig().set(this.gamePath + "winnerCmd", (Object)new ArrayList());
        final List<String> gameList = (List<String>)this.plugin.getConfig().getStringList("gameList");
        gameList.add(this.gameName);
        this.plugin.getConfig().set("gameList", (Object)gameList);
        this.plugin.saveConfig();
    }
    
    public void addLocation(final String locationPath, final String world, final double x, final double y, final double z, final float yaw, final float pitch) {
        final int locNum = this.plugin.getConfig().getInt(this.gamePath + "." + locationPath + "Amount") + 1;
        this.plugin.getConfig().set(this.gamePath + locationPath + "Amount", (Object)locNum);
        this.plugin.getConfig().set(this.gamePath + locationPath + "." + locNum + ".world", (Object)world);
        this.plugin.getConfig().set(this.gamePath + locationPath + "." + locNum + ".x", (Object)x);
        this.plugin.getConfig().set(this.gamePath + locationPath + "." + locNum + ".y", (Object)y);
        this.plugin.getConfig().set(this.gamePath + locationPath + "." + locNum + ".z", (Object)z);
        this.plugin.getConfig().set(this.gamePath + locationPath + "." + locNum + ".yaw", (Object)yaw);
        this.plugin.getConfig().set(this.gamePath + locationPath + "." + locNum + ".pitch", (Object)pitch);
        this.plugin.saveConfig();
    }
    
    public void setLocation(final String locationPath, final String world, final double x, final double y, final double z, final float yaw, final float pitch) {
        this.plugin.getConfig().set(this.gamePath + locationPath + ".world", (Object)world);
        this.plugin.getConfig().set(this.gamePath + locationPath + ".x", (Object)x);
        this.plugin.getConfig().set(this.gamePath + locationPath + ".y", (Object)y);
        this.plugin.getConfig().set(this.gamePath + locationPath + ".z", (Object)z);
        this.plugin.getConfig().set(this.gamePath + locationPath + ".yaw", (Object)yaw);
        this.plugin.getConfig().set(this.gamePath + locationPath + ".pitch", (Object)pitch);
        this.plugin.saveConfig();
    }
    
    public void setLocation(final String locationPath, final String world, final double x, final double y, final double z) {
        this.plugin.getConfig().set(this.gamePath + locationPath + ".world", (Object)world);
        this.plugin.getConfig().set(this.gamePath + locationPath + ".x", (Object)x);
        this.plugin.getConfig().set(this.gamePath + locationPath + ".y", (Object)y);
        this.plugin.getConfig().set(this.gamePath + locationPath + ".z", (Object)z);
        this.plugin.saveConfig();
    }
    
    public void addMessage(final String messagePath, final String message) {
        final List<String> messageList = (List<String>)this.plugin.getConfig().getStringList(this.gamePath + messagePath);
        messageList.add(message);
        this.plugin.getConfig().set(this.gamePath + messagePath, (Object)messageList);
        this.plugin.saveConfig();
    }
}
