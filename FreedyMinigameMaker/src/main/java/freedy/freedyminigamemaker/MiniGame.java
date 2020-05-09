package freedy.freedyminigamemaker;

import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.WorldBorder;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Stream;

import static org.bukkit.Bukkit.getServer;

public class MiniGame extends DataStore {

    FreedyMinigameMaker plugin;

    int taskId;
    BukkitScheduler scheduler = getServer().getScheduler();


    public List<Player> playerList;
    public Map<String, List<Player>> teamPlayers;
    public List<PlayerData> playerDataList;

    int playerAmount; //미니 게임 시작한 순간 참여 해있는 플레이어 수 \

    boolean isPlaying;
    boolean isWaiting;
    int waitTime; //타이머
    int repeaterTimer;


    public MiniGame(FreedyMinigameMaker plugin, String gameName) {
        super(plugin, gameName);
        this.plugin = plugin;
        playerList = new ArrayList<>();
        teamPlayers = new HashMap<>();
        playerDataList = new ArrayList<>();
        waitTime = 0;
        repeaterTimer = 0;
    }

    public PlayerData getPlayerData(Player player) {
        for (PlayerData pData : playerDataList) {
            if (pData.player.equals(player)) {
                return pData;
            }
        }
        return new PlayerData(player);
    }

    public String getMode(Player player) {
        if (getPlayerData(player).extraDamageMode) return getMessage("extraDamageMode");
        else if (getPlayerData(player).dropItemMode) return getMessage("dropItemMode");
        else if (getPlayerData(player).resistingDamageMode) return getMessage("resistingDamageMode");
        else return getMessage("none");
    }



    void stopChecker() {
        //System.out.println("찰칵, 리피트 스케줄러 스탑 됨");
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
                repeaterTimer = 0;
                isPlaying = false;
                stop();
            } else {
                //for (Player p : playerList) p.sendMessage(getMessage("endTimerMsg").replace("{time}", String.valueOf(endTime - waitTime)));
                setScoreBoardAll(endTime - waitTime);

                repeaterTimer++;
                if (repeaterTimer == getRepeatTime()) {
                    for (String cmd : getMessageList("repeatCmd"))
                        Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(), cmd
                                .replace("{game}", gameName)
                                .replace("{player}", playerList.get(0).getName())
                                .replace("{playerAmount}", String.valueOf(playerAmount))
                                .replace("{leftTime}", String.valueOf(endTime - waitTime))
                                .replace("{playerSize}", String.valueOf(playerList.size())));
                    repeaterTimer = 0;

                }
                waitTime++;

            }
        } else { //대기 중 시작 하는 걸 체크
            if (playerList.size() >= getMaxStartPlayers()) {
                isWaiting = true;
                if (waitTime >= getWaitForStartTime()) {
                    isWaiting = false;
                    isPlaying = true;
                    waitTime = 0;
                    repeaterTimer = 0;
                    start();
                } else {
                    //for (Player p : playerList) p.sendMessage(getMessage("startTimerMsg").replace("{time}", String.valueOf(getWaitForStartTime() - waitTime)));
                    setScoreBoardAll(getWaitForStartTime() - waitTime);
                    waitTime++;
                }
            }

        }

    }

    public boolean hasEmptyInventory(Player player) {
        return !(Stream.of(player.getInventory().getContents()).anyMatch(Objects::nonNull)
                || Stream.of(player.getInventory().getArmorContents()).anyMatch(Objects::nonNull));
    }

    public void add(Player player) {
        String playerName = player.getName();

        if (getGameList().contains(gameName)) {
            if (!playerList.contains(player)) {
                if (!getNeedClearInv() || hasEmptyInventory(player)) {
                    if (playerList.size() < getMaxPlayers()) {
                        if (!isPlaying) {

                            playerList.add(player);
                            playerDataList.add(new PlayerData(player));
                            for (Player p : playerList)
                                p.sendMessage(getMessage("joinMsg")
                                        .replace("{player}", playerName)
                                        .replace("{game}", gameName)
                                        .replace("{maxPlayers}", String.valueOf(getMaxStartPlayers()))
                                        .replace("{playerAmount}", String.valueOf(playerList.size())));
                            for (String cmd : getMessageList("joinCmd")) {
                                Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(), cmd
                                        .replace("{player}", playerName)
                                        .replace("{game}", gameName));
                            }
                            if (getLocationIsExist("waitLocation")) player.teleport(getLocation("waitLocation"));
                            setScoreBoardAll(0);
                            if (!isWaiting && playerList.size() >= getMaxStartPlayers()) startChecker();

                        } else player.sendMessage("§c" + "게임이 이미 시작되었습니다");
                    } else player.sendMessage("§c" + "게임이 최대인원에 도달했습니다");
                } else player.sendMessage("§c" + "그 게임에 참가하려면 인벤토리를 비워야 합니다");
            } else player.sendMessage("§c" + "게임을 이미 플레이 중입니다");
        } else player.sendMessage("§c" + "그 게임이 없습니다");
    }


    public void removeAll(List<Player> playerList) {
        for (Player p : playerList)
            removeSituation(p);
        this.playerList = new ArrayList<>();
        this.playerDataList = new ArrayList<>();
        this.teamPlayers.replaceAll((n, v) -> new ArrayList<>());
    }

    public void removeSituation(Player player) {

        String playerName = player.getName();
        removeScoreBoard(player);
        for (Player p : playerList)
            p.sendMessage(getMessage("quitMsg").replace("{player}", playerName));
        for (String cmd : getMessageList("quitCmd"))
            Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(), cmd
                    .replace("{player}", playerName)
                    .replace("{game}", gameName));
        player.teleport(getLocation("endLocation"));
        player.getInventory().clear();
        player.updateInventory();
        if (getQuitGameMode()) player.setGameMode(GameMode.valueOf(getDefaultEndGameMode()));
        player.setHealthScale(20.0);

    }

    public void remove(Player player) {

        removeSituation(player);

        playerList.remove(player);
        playerDataList.remove(getPlayerData(player));

        for (String teamName : this.teamPlayers.keySet()) {
            List<Player> teamPlayers = this.teamPlayers.get(teamName);
            teamPlayers.remove(player);
            this.teamPlayers.put(teamName, teamPlayers);
        }

    }



    public void disable() {


        isPlaying = false;
        isWaiting = false;
        waitTime = 0;
        repeaterTimer = 0;
        playerList = new ArrayList<>();
        playerDataList = new ArrayList<>();
        teamPlayers.replaceAll((n, v) -> new ArrayList<>());

        stopChecker();
    }

    public boolean wasNothingToStop() {
        //미니게임을 끝낼지 말지 고민 하는 메소드
        /*

        끝나야 하는 경우
        2. 대기중인데 플레이어가 나갈 때
        3. 플레이중에 나가서 한쪽 팀이 탕진될 때
        4. 플레이 중에 나가서 한 명밖에 안 남았을 때

        끝나지 말아야 하는 경우

         */
        if (isWaiting && playerList.size() < getMaxStartPlayers())
            return false;
        else if (getGameType().equalsIgnoreCase("zombieMode") && (teamPlayers.get("red").isEmpty() || teamPlayers.get("blue").isEmpty()))
            return false;
        else if (getTeamMode() && (teamPlayers.get("red").isEmpty() || teamPlayers.get("blue").isEmpty()))
            return false;
        else if (waitTime == 0)
            return false;
        else
            return playerList.size() != 1;
    }

    public void stop() {

        if (isWaiting && playerList.size() < getMaxStartPlayers()) {
            waitTime = 0;
            repeaterTimer = 0;
            for (Player p : playerList){
                p.sendMessage(getMessage("morePlayerMsg"));
                setScoreBoard(p, 0);
            }

            return;

        }

        if (wasNothingToStop()) return;



        if (isWaiting && !isPlaying) {
            isWaiting = false;
            waitTime = 0;
            repeaterTimer = 0;
            for (Player p : playerList)
                p.sendMessage(getMessage("waitPlayerMsg"));
            return;
        }

        String showingEndMsg;
        switch (getGameType()) {
            case "zombieMode":
                if (isPlaying && waitTime == 0) showingEndMsg = getMessage("noWinnerEndMsg").replace("{game}", gameName);
                else if (teamPlayers.get("red").size() == 0) {
                    showingEndMsg = getMessage("blueWinEndMsg").replace("{game}", gameName);

                    for (Player player : teamPlayers.get("blue"))
                        for (String cmd : getMessageList("winnerCmd"))
                            Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(), calc(cmd
                                    .replace("{player}", player.getName())
                                    .replace("{playerAmount}", String.valueOf(playerAmount))
                                    .replace("{game}", gameName)));


                }
                else if (teamPlayers.get("blue").size() == 0) {
                    showingEndMsg = getMessage("redWinEndMsg").replace("{game}", gameName);
                    for (Player player : teamPlayers.get("red"))
                        for (String cmd : getMessageList("winnerCmd"))
                            Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(), calc(cmd
                                    .replace("{player}", player.getName())
                                    .replace("{playerAmount}", String.valueOf(playerAmount))
                                    .replace("{game}", gameName)));
                }
                else showingEndMsg = getMessage("noWinnerEndMsg").replace("{game}", gameName);
                break;
            default:
                if (getTeamMode()) {
                    if (isPlaying && waitTime == 0) showingEndMsg = getMessage("noWinnerEndMsg").replace("{game}", gameName);
                    else if (teamPlayers.get("blue").size() == 0) {
                        showingEndMsg = getMessage("redWinEndMsg").replace("{game}", gameName);
                        for (Player player : teamPlayers.get("red"))
                            for (String cmd : getMessageList("winnerCmd"))
                                Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(), calc(cmd
                                        .replace("{player}", player.getName())
                                        .replace("{playerAmount}", String.valueOf(playerAmount))
                                        .replace("{game}", gameName)));
                    }
                    else if (teamPlayers.get("red").size() == 0) {
                        showingEndMsg = getMessage("blueWinEndMsg").replace("{game}", gameName);
                        for (Player player : teamPlayers.get("blue"))
                            for (String cmd : getMessageList("winnerCmd"))
                                Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(), calc(cmd
                                        .replace("{player}", player.getName())
                                        .replace("{playerAmount}", String.valueOf(playerAmount))
                                        .replace("{game}", gameName)));
                    }

                    else showingEndMsg = getMessage("noWinnerEndMsg").replace("{game}", gameName);
                } else {
                    if (isPlaying && waitTime == 0) showingEndMsg = getMessage("noWinnerEndMsg").replace("{game}", gameName);
                    else if (playerList.size() == 1) {
                        showingEndMsg = getMessage("endMsg")
                                .replace("{game}", gameName)
                                .replace("{player}", playerList.get(0).getName())
                                .replace("{playerAmount}", String.valueOf(playerAmount));
                            for (String cmd : getMessageList("winnerCmd"))
                                Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(), calc(cmd
                                        .replace("{player}", playerList.get(0).getName())
                                        .replace("{playerAmount}", String.valueOf(playerAmount))
                                        .replace("{game}", gameName)));
                    }
                    else showingEndMsg = getMessage("noWinnerEndMsg").replace("{game}", gameName);

                }
                break;
        }

        for (Player p : playerList){
            p.sendMessage(showingEndMsg);
        }


        removeAll(playerList);

        for (String cmd : getMessageList("conEndCmd")) {
            cmd = cmd
                    .replace("{player}", playerList.get(0).getName())
                    .replace("{game}", gameName);
            cmd = randomChoose(cmd);
            if (cmd.contains("{allPlayer}")) {
                for (Player p : playerList)
                    Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(), cmd
                            .replace("{allPlayer}", p.getName())
                            .replace("{playerMode}", getMode(p)));
            } else Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(), cmd);
        }
        disable();
    }

    public void setScoreBoardAll(int time) {
        if (getScoreBoardMode())
            for (Player p : playerList)
                setScoreBoard(p, time);
    }

    public void setScoreBoard(Player player, int time) {
        Scoreboard board = Bukkit.getScoreboardManager().getNewScoreboard();
        Objective obj = board.registerNewObjective(getMessage("scoreBoardTitle"), "dummy");
        obj.setDisplaySlot(DisplaySlot.SIDEBAR);
        List<String> scoreBoard = getMessageList("scoreBoard");
        Collections.reverse(scoreBoard);
        for (int i = 0; i < scoreBoard.size(); i++) {
            obj.getScore(scoreBoard.get(i)
                    .replace("{player}", player.getName())
                    .replace("{playerAmount}", String.valueOf(playerAmount))
                    .replace("{playerSize}", String.valueOf(playerList.size()))
                    .replace("{displayTime}",
                            isWaiting ?
                                    getMessage("startTimerMsg").replace("{time}", String.valueOf(time))
                                    : isPlaying ? getMessage("endTimerMsg").replace("{time}", String.valueOf(time))
                                    : getMessage("morePlayerMsg"))

            ).setScore(i);
        }
        player.setScoreboard(board);
    }

    public void removeScoreBoard(Player player) {
        player.setScoreboard(Bukkit.getScoreboardManager().getNewScoreboard());
    }

    public void setWorldBoarder() {
        if (getWorldBoarderMode()) {

            WorldBorder worldBorder = getWorldBoarder();
            Location worldBoarderLocation = getWorldBoarderLocation();

            worldBorder.setCenter(worldBoarderLocation);
            worldBorder.setDamageAmount(getWorldBoarderOutDamage());
            worldBorder.setSize(getWorldBoarderSizePerPlayer() * playerAmount);
            worldBorder.setSize(getWorldBoarderMinSize(), getWorldBoarderSpeed());

        }
    }

    public void start() {

        playerAmount = playerList.size();
        setWorldBoarder();

        teamPlayers.put("red", new ArrayList<>());
        teamPlayers.put("blue", new ArrayList<>());

        for (String cmd : getMessageList("conStartCmd")) {
            cmd = cmd
                    .replace("{player}", playerList.get(0).getName())
                    .replace("{game}", gameName);
            cmd = randomChoose(cmd);
            if (cmd.contains("{allPlayer}")) {
                for (Player p : playerList)
                    Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(), cmd
                            .replace("{allPlayer}", p.getName())
                    .replace("{playerMode}", getMode(p)));
            } else Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(), cmd);
        }

        int i = 0;
        switch (getGameType()) {
            case "hideAndSeek":
                for (Player p : playerList) {
                    p.sendMessage(getMessage("startMsg").replace("{game}", gameName));
                    /*for (String cmd : getMessageList("startCmd"))
                        Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(), cmd
                                .replace("{player}", p.getName())
                                .replace("{game}", gameName));
                     */
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
                    /*for (String cmd : getMessageList("startCmd"))
                        Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(), cmd
                                .replace("{player}", p.getName())
                                .replace("{game}", gameName));
                     */
                    if (i == 0) {
                        teamPlayers.get("red").add(p);
                        p.teleport(getLocationList("redStartLocation").get(i));
                        i++;
                        p.setHealthScale(getRedStartMaxHeart());
                    } else {
                        teamPlayers.get("blue").add(p);
                        p.teleport(getLocationList("blueStartLocation").get(i));
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
                        /*for (String cmd : getMessageList("startCmd"))
                            Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(), cmd
                                    .replace("{player}", p.getName())
                                    .replace("{game}", gameName));

                         */
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
                        /*for (String cmd : getMessageList("startCmd"))
                            Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(), cmd
                                    .replace("{player}", p.getName())
                                    .replace("{game}", gameName));
                         */
                        p.teleport(getLocationList("defaultStartLocation").get(i));
                        i++;
                        p.setHealthScale(getDefaultStartMaxHeart());
                    }
                }
                break;
        }


    }

    public String calc(String string) {


        String area = StringUtils.substringBetween(string, "{calc(", ")}");
        //System.out.println(area);

        if (area == null) return string;

        ScriptEngineManager mgr = new ScriptEngineManager();
        ScriptEngine engine = mgr.getEngineByName("JavaScript");
        try {
            int result = (int) engine.eval(area);

            System.out.println(string + "의 결과는:" + result);

            return string.replace("{calc("+ area + ")}", String.valueOf(result));
        } catch (ScriptException e) {
            e.printStackTrace();
        }



        return "(에러,콘솔 확인 바람)";

    }

    public String randomChoose(String string) { //{random(extraDamageMode:resistingDamageMode:dropItemMode)}

        String area = StringUtils.substringBetween(string, "{random(", ")}");
        if (area == null) return string;
        List<String> stringList = new ArrayList<String>(Arrays.asList(area.split("-")));
        String result = stringList.get(ThreadLocalRandom.current().nextInt(1, stringList.size() + 1));
        return string.replace("{random("+ area + ")}", result);
    }

}