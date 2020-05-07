package freedy.freedyminigamemaker;

import java.util.ArrayList;
import java.util.List;

public class DataEditor {

    public final List<String> teamTypes = new ArrayList<String>() { {
        add("blue");
        add("red");
    } };

    public final String superPath = "miniGames.";

    public String gameName;

    public String gamePath;

    FreedyMinigameMaker plugin;

    public DataEditor(FreedyMinigameMaker plugin, String gameName) {
        this.plugin = plugin;
        this.gameName = gameName;
        this.gamePath = superPath + gameName + ".";
    }

    public void remove(String gameName) {
        List<String> gameList = plugin.getConfig().getStringList("gameList");
        gameList.remove(gameName);
        plugin.getConfig().set("miniGames." + gameName, null);
        plugin.getConfig().set("gameList", gameList);
        plugin.saveConfig();
    }

    public void create(int maxPlayers, int maxStartPlayers, int waitForStartTime, int waitForEndTime) {
        //<미니게임이름> <미니게임최대인원> <미니게임시작인원> <시작대기시간초> <게임종료시간초>
        plugin.getConfig().set(gamePath + "maxPlayers", maxPlayers);
        plugin.getConfig().set(gamePath + "maxStartPlayers", maxStartPlayers);
        plugin.getConfig().set(gamePath + "waitForStartTime", waitForStartTime);
        plugin.getConfig().set(gamePath + "waitForEndTime", waitForEndTime);
        plugin.getConfig().set(gamePath + "gameType", "death");
        plugin.getConfig().set(gamePath + "defaultStartGameMode", "ADVENTURE");
        plugin.getConfig().set(gamePath + "defaultEndGameMode", "ADVENTURE");
        plugin.getConfig().set(gamePath + "defaultStartMaxHeart", 20.0);
        plugin.getConfig().set(gamePath + "redTeamStartMaxHeart", 20.0);
        plugin.getConfig().set(gamePath + "blueTeamStartMaxHeart", 20.0);
        plugin.getConfig().set(gamePath + "timePerPlayer", 1);
        plugin.getConfig().set(gamePath + "joinMsg", "§6{player}이(가) {game}에 참여했습니다");
        plugin.getConfig().set(gamePath + "quitMsg", "§6{player}이(가) 떠났습니다");
        plugin.getConfig().set(gamePath + "startMsg", "§a{game}이(가) 시작되었어요!");
        plugin.getConfig().set(gamePath + "noWinnerEndMsg", "§a{game}이(가) 종료되었어요, 무승부입니다!");
        plugin.getConfig().set(gamePath + "redWinEndMsg", "§a{game}이(가) 종료되었어요, 레드팀이 승리하였습니다!");
        plugin.getConfig().set(gamePath + "blueWinEndMsg", "§a{game}이(가) 종료되었어요, 블루팀이 승리하였습니다!");
        plugin.getConfig().set(gamePath + "endMsg", "§a{game}이(가) 종료되었어요! 승자는 {player}입니다!");
        plugin.getConfig().set(gamePath + "startTimerMsg", "§7{time}초 후에 시작...");
        plugin.getConfig().set(gamePath + "endTimerMsg", "§7{time}초 후에 종료...");
        plugin.getConfig().set(gamePath + "morePlayerMsg", "§c플레이어를 더 기다리고 있습니다...");
        plugin.getConfig().set(gamePath + "beZombieMsg", "§c{player}이(가) {killer}에 의해 좀비가 되었습니다!");
        plugin.getConfig().set(gamePath + "startCmd", new ArrayList<>());
        plugin.getConfig().set(gamePath + "ConStartCmd", new ArrayList<>());
        plugin.getConfig().set(gamePath + "quitCmd", new ArrayList<>());
        plugin.getConfig().set(gamePath + "conEndCmd", new ArrayList<>());
        List<String> gameList = plugin.getConfig().getStringList("gameList");
        gameList.add(gameName);
        plugin.getConfig().set("gameList", gameList);
        plugin.saveConfig();
    }

    public void addLocation(String locationPath, String world, double x, double y, double z, float yaw, float pitch) {
        int locNum = plugin.getConfig().getInt(gamePath + "." + locationPath + "Amount") + 1;
        plugin.getConfig().set(gamePath + locationPath + "Amount", locNum);
        plugin.getConfig().set(gamePath + locationPath + "." + locNum + ".world", world);
        plugin.getConfig().set(gamePath + locationPath + "." + locNum + ".x", x);
        plugin.getConfig().set(gamePath + locationPath + "." + locNum + ".y", y);
        plugin.getConfig().set(gamePath + locationPath + "." + locNum + ".z", z);
        plugin.getConfig().set(gamePath + locationPath + "." + locNum + ".yaw", yaw);
        plugin.getConfig().set(gamePath + locationPath + "." + locNum + ".pitch", pitch);
        plugin.saveConfig();
    }

    public void setLocation(String locationPath, String world, double x, double y, double z, float yaw, float pitch) {
        plugin.getConfig().set(gamePath + locationPath + ".world", world);
        plugin.getConfig().set(gamePath + locationPath + ".x", x);
        plugin.getConfig().set(gamePath + locationPath + ".y", y);
        plugin.getConfig().set(gamePath + locationPath + ".z", z);
        plugin.getConfig().set(gamePath + locationPath + ".yaw", yaw);
        plugin.getConfig().set(gamePath + locationPath + ".pitch", pitch);
        plugin.saveConfig();
    }


    public void set(String path, Object object) {
        plugin.getConfig().set(path, object);
    }

    public void save() {
        plugin.saveConfig();
    }

    public void reload() {
        plugin.reloadConfig();
    }
}
