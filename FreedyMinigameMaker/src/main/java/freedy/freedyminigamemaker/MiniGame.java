package freedy.freedyminigamemaker;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.WorldBorder;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitScheduler;

import java.util.*;
import java.util.stream.Stream;

import static org.bukkit.Bukkit.getServer;

public class MiniGame extends DataStore {

    FreedyMinigameMaker plugin;

    int taskId;
    BukkitScheduler scheduler = getServer().getScheduler();


    public List<Player> playerList;
    public Map<String, List<Player>> teamPlayers;

    boolean isPlaying;
    boolean isWaiting;
    int waitTime;


    public MiniGame(FreedyMinigameMaker plugin, String gameName) {
        super(plugin, gameName);
        this.plugin = plugin;
        playerList = new ArrayList<>();
        teamPlayers = new HashMap<>();

    }



    void stopChecker() {
        System.out.println("찰칵, 리피트 스케줄러 스탑 됨");
        scheduler.cancelTask(taskId);
    }

    void startChecker() {
        taskId = scheduler.scheduleSyncRepeatingTask(plugin, this::checker, 0L, 20L);
    }

    void checker() {

        if (isPlaying) { //플레이 중 멈추는 걸 체크
            int endTime = (getWaitForEndTime() * getTimePerPlayer());
            if (waitTime >= endTime) {
                waitTime = 0;
                isPlaying = false;
                stop();
            } else {
                for (Player p : playerList)
                    p.sendMessage(getMessage("endTimerMsg").replace("{time}", String.valueOf(endTime - waitTime)));
                waitTime++;
            }
        } else { //대기 중 시작 하는 걸 체크
            if (playerList.size() >= getMaxStartPlayers()) {
                isWaiting = true;
                if (waitTime >= getWaitForStartTime()) {
                    isWaiting = false;
                    isPlaying = true;
                    waitTime = 0;
                    start();
                } else {
                    for (Player p : playerList)
                        p.sendMessage(getMessage("startTimerMsg").replace("{time}", String.valueOf(getWaitForStartTime() - waitTime)));
                    waitTime++;
                }
            }

        }

    }

    public static boolean hasEmptyInventory(Player player) {
        return !(Stream.of(player.getInventory().getContents()).anyMatch(Objects::nonNull)
                || Stream.of(player.getInventory().getArmorContents()).anyMatch(Objects::nonNull));
    }

    public void add(Player player) {
        String playerName = player.getName();

        if (getGameList().contains(gameName)) {
            if (!playerList.contains(player)) {
                if (hasEmptyInventory(player)) {
                    if (playerList.size() < getMaxPlayers()) {
                        if (!isPlaying) {

                            playerList.add(player);
                            for (Player p : playerList)
                                p.sendMessage(getMessage("joinMsg").replace("{player}", playerName).replace("{game}", gameName));
                            player.teleport(getLocation("waitLocation"));
                            if (playerList.size() >= getMaxStartPlayers()) startChecker();

                        } else player.sendMessage("§c" + "게임이 이미 시작되었습니다");
                    } else player.sendMessage("§c" + "게임이 최대인원에 도달했습니다");
                } else player.sendMessage("§c" + "그 게임에 참가하려면 인벤토리를 비워야 합니다");
            } else player.sendMessage("§c" + "게임을 이미 플레이 중입니다");
        } else player.sendMessage("§c" + "그 게임이 없습니다");
    }


    public void removeAll(List<Player> playerList) {
        for (Player p : playerList)
            removeSituation(p);
    }

    public void removeSituation(Player player) {

        String playerName = player.getName();
        for (Player p : playerList)
            p.sendMessage(getMessage("quitMsg").replace("{player}", playerName));
        for (String cmd : getMessageList("quitCmd"))
            Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(), cmd.replace("{player}", playerName).replace("{game}", gameName));
        player.teleport(getLocation("endLocation"));
        player.getInventory().clear();
        player.updateInventory();
        if (getQuitGameMode()) player.setGameMode(GameMode.valueOf(getDefaultEndGameMode()));
        player.setHealthScale(20.0);

    }

    public void remove(Player player) {

        removeSituation(player);

        playerList.remove(player);
        for (List<Player> playerList : teamPlayers.values())
            playerList.remove(player);

    }



    public void disable() {


        isPlaying = false;
        isWaiting = false;
        waitTime = 0;
        playerList = new ArrayList<>();
        teamPlayers.replaceAll((n, v) -> new ArrayList<>());

        stopChecker();
    }

    public boolean isTimeToStop() {
        if ((isWaiting && !isPlaying) && playerList.size() < getMaxStartPlayers())
            return false;
        else if (getGameType().equalsIgnoreCase("zombieMode") && (teamPlayers.get("red").isEmpty() || teamPlayers.get("blue").isEmpty()))
            return true;
        else if (getTeamMode() && (teamPlayers.get("red").isEmpty() || teamPlayers.get("blue").isEmpty()))
            return true;
        else
            return playerList.isEmpty();
    }

    public void stop() {

        if (isTimeToStop()) return;

        if (isWaiting && !isPlaying) {
            isWaiting = false;
            waitTime = 0;
            for (Player p : playerList)
                p.sendMessage(getMessage("waitPlayerMsg"));
            return;
        }

        String showingEndMsg;
        switch (getGameType()) {
            case "zombieMode":
                if (isPlaying && waitTime == 0) showingEndMsg = getMessage("noWinnerEndMsg");
                else if (teamPlayers.get("red").size() == 0) showingEndMsg = getMessage("blueWinEndMsg");
                else if (teamPlayers.get("blue").size() == 0) showingEndMsg = getMessage("redWinEndMsg");
                else showingEndMsg = getMessage("noWinnerEndMsg");
                break;
            default:
                if (getTeamMode()) {
                    if (isPlaying && waitTime == 0) showingEndMsg = getMessage("noWinnerEndMsg");
                    else if (teamPlayers.get("red").size() == 0) showingEndMsg = getMessage("redWinEndMsg");
                    else if (teamPlayers.get("blue").size() == 0) showingEndMsg = getMessage("blueWinEndMsg");

                    else showingEndMsg = getMessage("noWinnerEndMsg");
                } else {
                    if (isPlaying && waitTime == 0) showingEndMsg = getMessage("noWinnerEndMsg");
                    else if (playerList.size() == 1) showingEndMsg = getMessage("endMsg").replace("{game}", gameName);
                    else
                        showingEndMsg = getMessage("noWinnerEndMsg").replace("{game}", gameName).replace("{player}", playerList.get(0).getName());

                }
                break;
        }

        for (Player p : playerList){
            p.sendMessage(showingEndMsg);
        }


        removeAll(playerList);

        for (String cmd : getMessageList("conEndCmd"))
            Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(), cmd.replace("{game}", gameName));

        disable();
    }

    public void start() {

        teamPlayers.put("red", new ArrayList<>());
        teamPlayers.put("blue", new ArrayList<>());

        for (String cmd : getMessageList("conStartCmd"))
            Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(), cmd.replace("{game}", gameName));

        for (String team : teamTypes)
            getLocationList(team + "StartLocation");
        int i = 0;
        switch (getGameType()) {
            case "hideAndSeek":
                for (Player p : playerList) {
                    p.sendMessage(getMessage("startMsg").replace("{game}", gameName));
                    for (String cmd : getMessageList("startCmd"))
                        Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(), cmd.replace("{player}", p.getName()).replace("{game}", gameName));
                    p.teleport(getLocation("defaultStartLocation", i));
                    i++;

                    p.setHealthScale(getDefaultStartMaxHeart());
                    if (getStartGameMode()) p.setGameMode(getDefaultStartGameMode());
                }
                //setBlockOnStart(playerNameList, gameName);
                break;
            case "zombieMode":
                Collections.shuffle(playerList);
                i = 0;
                for (Player p : playerList) {
                    p.sendMessage(getMessage("startMsg").replace("{game}", gameName));
                    for (String cmd : getMessageList("startCmd"))
                        Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(), cmd.replace("{player}", p.getName()).replace("{game}", gameName));
                    if (i == 0) {
                        teamPlayers.get("red").add(p);
                        p.teleport(getLocationList("red").get(i));
                        i++;
                        p.setHealthScale(getRedStartMaxHeart());
                    } else {
                        teamPlayers.get("blue").add(p);
                        p.teleport(getLocationList("blue").get(i));
                        i++;
                        p.setHealthScale(getBlueStartMaxHeart());
                    }
                    if (getStartGameMode()) p.setGameMode(getDefaultStartGameMode());
                }
                break;
            default:
                i = 0;
                int j = 0;
                if (getTeamMode()) {
                    for (Player p : playerList) {
                        p.sendMessage(getMessage("startMsg").replace("{game}", gameName));
                        for (String cmd : getMessageList("startCmd"))
                            Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(), cmd.replace("{player}", p.getName()).replace("{game}", gameName));
                        if (i % 2 == 0) {
                            teamPlayers.get("red").add(p);
                            p.teleport(getLocationList("redStartLocation").get(i));
                            i++;
                            p.setHealthScale(getRedStartMaxHeart());
                        } else {
                            teamPlayers.get("blue").add(p);
                            p.teleport(getLocationList("blueStartLocation").get(j));
                            j++;
                            p.setHealthScale(getBlueStartMaxHeart());
                        }
                    }
                } else {
                    for (Player p : playerList) {
                        p.sendMessage(getMessage("startMsg").replace("{game}", gameName));
                        for (String cmd : getMessageList("startCmd"))
                            Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(), cmd.replace("{player}", p.getName()).replace("{game}", gameName));
                        p.teleport(getLocationList("defaultStartLocation").get(i));
                        i++;
                        p.setHealthScale(getDefaultStartMaxHeart());
                    }
                }
                break;
        }
    }



}