package freedy.freedyminigamemaker;

import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.WorldBorder;
import org.bukkit.block.Biome;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.command.ConsoleCommandSender;
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

    int taskId;
    BukkitScheduler scheduler = getServer().getScheduler();
    ConsoleCommandSender console = getServer().getConsoleSender();


    public Map<String, String> customData;
    public List<BlockState> blockList;

    public List<Player> playerList;
    public Map<String, List<Player>> teamPlayers;
    public List<PlayerData> playerDataList;

    int playerAmount; //미니 게임 시작한 순간 참여 해있는 플레이어 수

    boolean isPlaying;
    boolean isWaiting;
    int waitTime; //타이머
    int repeaterTimer;


    public MiniGame(FreedyMinigameMaker plugin, String gameName) {
        super(plugin, gameName);
        super.plugin = plugin;
        customData = new HashMap<>();
        blockList = new ArrayList<>();
        playerList = new ArrayList<>();
        teamPlayers = new HashMap<>();
        playerDataList = new ArrayList<>();
        playerAmount = 0;
        waitTime = 0;
        repeaterTimer = 0;
        isPlaying = false;
        isWaiting = false;
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

    public String getRealMode(Player player) {
        if (getPlayerData(player).extraDamageMode) return "extraDamage";
        else if (getPlayerData(player).dropItemMode) return "dropItem";
        else if (getPlayerData(player).resistingDamageMode) return "resistingDamage";
        else return "none";
    }

    public void addBlock(Block block) {
        BlockState blockState = block.getState();
        if (!blockList.contains(blockState)) {
            for (BlockState bs : blockList) {
                if (bs.getLocation().equals(blockState.getLocation())) {
                    return;
                }
            }
            blockList.add(blockState);
        }
    }

    public void resetBlocks() {
        for (BlockState blockState : blockList) {
            Block block = blockState.getLocation().getBlock();
            if (!block.getState().equals(blockState)) {
                block.setType(blockState.getType());
                blockState.update();
            }
        }
        blockList = new ArrayList<>();
    }

    public void setCustomData(String key, String value) {
        if (value.equals("none")) removeCustomData(key);
        else this.customData.put(key, value);
    }

    public void removeCustomData(String key) {
        this.customData.remove(key);
    }

    public String getCustomData(String key) {
        if (customData.containsKey(key)) return this.customData.get(key);
        else return "none";
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
                if (getRepeatTime() > 0 ? repeaterTimer == getRepeatTime() : getRepeatTimes().contains(repeaterTimer)) {
                    for (String cmd : getMessageList("repeatCmd")) {
                        if (cmd.contains("{allPlayer}")) {
                            for (Player p : playerList) {
                                Bukkit.getServer().dispatchCommand(console,
                                        replaceAll(cmd.replace("{leftTime}", String.valueOf(endTime - waitTime)), p));
                            }
                        } else Bukkit.getServer().dispatchCommand(console, replaceAll(cmd, null));
                    }
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
        if (getGameList().contains(gameName)) {
            if (!playerList.contains(player)) {
                if (!getNeedClearInv() || hasEmptyInventory(player)) {
                    if (playerList.size() < getMaxPlayers()) {
                        if (!isPlaying) {

                            playerList.add(player);
                            playerDataList.add(new PlayerData(player));
                            if (getMessage("joinMsg") != null)
                                for (Player p : playerList)
                                    p.sendMessage(replaceAll(getMessage("joinMsg"), player));
                            if (getLocationIsExist("waitLocation")) player.teleport(getLocation("waitLocation"));
                            setScoreBoardAll(0);
                            for (String cmd : getMessageList("joinCmd")) {
                                Bukkit.getServer().dispatchCommand(console, replaceAll(cmd, player));
                            }
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

        removeScoreBoard(player);
        if (getMessage("quitMsg") != null)
            for (Player p : playerList)
                p.sendMessage(replaceAll(getMessage("quitMsg"), player));
        for (String cmd : getMessageList("quitCmd"))
            Bukkit.getServer().dispatchCommand(console, replaceAll(cmd, player));
        if (getLocationIsExist("endLocation")) player.teleport(getLocation("endLocation"));
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
        playerAmount = 0;
        customData = new HashMap<>();
        blockList = new ArrayList<>();
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
        if (playerList.size() == 0)
            return false;
        else if (isWaiting && playerList.size() < getMaxStartPlayers())
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



        /*
        if (isWaiting && !isPlaying) {
            isWaiting = false;
            waitTime = 0;
            repeaterTimer = 0;
            for (Player p : playerList)
                p.sendMessage(getMessage("waitPlayerMsg"));
            return;
        }*/

        String showingEndMsg;
        switch (getGameType()) {
            case "zombieMode":
                if (isPlaying && waitTime == 0) showingEndMsg = getMessage("noWinnerEndMsg").replace("{game}", gameName);
                else if (teamPlayers.get("red").size() == 0) {
                    showingEndMsg = getMessage("blueWinEndMsg").replace("{game}", gameName);

                    for (Player player : teamPlayers.get("blue"))
                        for (String cmd : getMessageList("winnerCmd"))
                            Bukkit.getServer().dispatchCommand(console, replaceAll(cmd, player));


                }
                else if (teamPlayers.get("blue").size() == 0) {
                    showingEndMsg = getMessage("redWinEndMsg").replace("{game}", gameName);
                    for (Player player : teamPlayers.get("red"))
                        for (String cmd : getMessageList("winnerCmd"))
                            Bukkit.getServer().dispatchCommand(console, replaceAll(cmd, player));
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
                                Bukkit.getServer().dispatchCommand(console, replaceAll(cmd, player));
                    }
                    else if (teamPlayers.get("red").size() == 0) {
                        showingEndMsg = getMessage("blueWinEndMsg").replace("{game}", gameName);
                        for (Player player : teamPlayers.get("blue"))
                            for (String cmd : getMessageList("winnerCmd"))
                                Bukkit.getServer().dispatchCommand(console, replaceAll(cmd, player));
                    }

                    else showingEndMsg = getMessage("noWinnerEndMsg").replace("{game}", gameName);
                } else {
                    if (isPlaying && waitTime == 0) showingEndMsg = getMessage("noWinnerEndMsg").replace("{game}", gameName);
                    else if (playerList.size() == 1) {
                        showingEndMsg = replaceAll(getMessage("endMsg"), null);
                            for (String cmd : getMessageList("winnerCmd"))
                                Bukkit.getServer().dispatchCommand(console, replaceAll(cmd, null));
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
            if (cmd.contains("{allPlayer}")) {
                for (Player p : playerList)
                    Bukkit.getServer().dispatchCommand(console, replaceAll(cmd, p));
            } else Bukkit.getServer().dispatchCommand(console, replaceAll(cmd, null));
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
            obj.getScore(replaceAll(scoreBoard.get(i), player)
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


            if (getSafeWorldBoarderFinderMode()) {
                main : for (int x = 0; x < 1000; x++) {
                    for (int z = 0; z < 1000; z++) {
                        Biome biome = worldBoarderLocation.getWorld().getBiome(x, z);
                        if (!(biome == Biome.OCEAN || biome == Biome.DEEP_OCEAN || biome == Biome.FROZEN_OCEAN)) {
                            worldBoarderLocation.setX(x);
                            worldBoarderLocation.setZ(z);
                            worldBorder.setCenter(worldBoarderLocation);
                            break main;
                        }
                    }
                }

            } else worldBorder.setCenter(worldBoarderLocation);
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
            if (cmd.contains("{allPlayer}")) {
                for (String playerCmd : replaceAllPlayer(cmd, playerList))
                    Bukkit.getServer().dispatchCommand(console, playerCmd);
            } else Bukkit.getServer().dispatchCommand(console, replaceAll(cmd, null));
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
                    if (getLocationIsExist("defaultStartLocation")) p.teleport(getLocation("defaultStartLocation"));
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
                        if (getLocationIsExist("redStartLocation")) p.teleport(getLocation("redStartLocation"));
                        i++;
                        p.setHealthScale(getRedStartMaxHeart());
                    } else {
                        teamPlayers.get("blue").add(p);
                        if (getLocationIsExist("blueStartLocation")) p.teleport(getLocation("blueStartLocation"));
                        i++;
                        p.setHealthScale(getBlueStartMaxHeart());
                    }
                    if (getStartGameMode()) p.setGameMode(getDefaultStartGameMode());
                }
                break;
            default:
                Collections.shuffle(playerList);
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
                            if (getLocationIsExist("redStartLocation")) p.teleport(getLocationList("redStartLocation").get(i));
                            i++;
                            p.setHealthScale(getRedStartMaxHeart());
                        } else {
                            teamPlayers.get("blue").add(p);
                            if (getLocationIsExist("blueStartLocation")) p.teleport(getLocationList("blueStartLocation").get(j));
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
                        if (getLocationIsExist("defaultStartLocation")) p.teleport(getLocationList("defaultStartLocation").get(i));
                        i++;
                        p.setHealthScale(getDefaultStartMaxHeart());
                    }
                }
                break;
        }


    }

    public void openInv(Player player, String invName) {
        player.openInventory(getInventory(invName));
    }

    public boolean isExistingTitle(String title) {
        return getInventoryTitleList().contains(title);
    }

    public String calc(String string) {


        String area = StringUtils.substringBetween(string, "{calc(", ")}");
        //System.out.println(area);

        if (area == null) return string;

        ScriptEngineManager mgr = new ScriptEngineManager();
        ScriptEngine engine = mgr.getEngineByName("JavaScript");
        try {
            int result = (int) engine.eval(area);

            //System.out.println(string + "의 결과는:" + result);

            return string.replace("{calc("+ area + ")}", String.valueOf(result));
        } catch (ScriptException e) {
            e.printStackTrace();
        }



        return "(에러,콘솔 확인 바람)";

    }

    public String randomChoose(String string) {

        String area = StringUtils.substringBetween(string, "{random(", ")}");
        if (area == null) return string;
        List<String> stringList = new ArrayList<>(Arrays.asList(area.split(", ")));
        String result = stringList.get(ThreadLocalRandom.current().nextInt(1, stringList.size()));
        return string.replace("{random("+ area + ")}", result);
    }

    public String highestNumber(String string) {

        String area = StringUtils.substringBetween(string, "{highestNumber(", ")}");
        if (area == null) return string;
        List<String> stringList = new ArrayList<>(Arrays.asList(area.split(", ")));
        List<Integer> integerList = new ArrayList<>();
        for (String s : stringList) {
            integerList.add(Integer.parseInt(s));
        }
        Integer result = Collections.max(integerList);
        return string.replace("{highestNumber("+ area + ")}", String.valueOf(result));
    }

    public String lowestNumber(String string) {

        String area = StringUtils.substringBetween(string, "{lowestNumber(", ")}");
        if (area == null) return string;
        List<String> stringList = new ArrayList<>(Arrays.asList(area.split(", ")));
        List<Integer> integerList = new ArrayList<>();
        for (String s : stringList) {
            integerList.add(Integer.parseInt(s));
        }
        Integer result = Collections.min(integerList);
        return string.replace("{lowestNumber("+ area + ")}", String.valueOf(result));
    }

    public String randomNumber(String string) {


        String area = StringUtils.substringBetween(string, "{randomNumber(", ")}");
        //System.out.println(area);

        if (area == null) return string;

        List<String> stringList = new ArrayList<>(Arrays.asList(area.split(", ")));

        int result;

        if (stringList.size() == 1) {
            result = ThreadLocalRandom.current().nextInt(0, Integer.parseInt(stringList.get(0) + 1));

        } else if (stringList.size() == 2) {
            result = ThreadLocalRandom.current().nextInt(Integer.parseInt(stringList.get(0)), Integer.parseInt(stringList.get(1) + 1));

        } else return string;

        return string.replace("{randomNumber("+ area + ")}", String.valueOf(result));


    }

    public String getData(String string) {


        String area = StringUtils.substringBetween(string, "{data(", ")}");

        if (area == null) return string;

        String result = getCustomData(area);

        if (result == null) return string;

        return string.replace("{data("+ area + ")}", result);


    }

    public String replaceGameData(String string) {
        return string
                .replace("{maxPlayers}", String.valueOf(getMaxStartPlayers()))
                .replace("{playerAmount}", String.valueOf(playerList.size()))
                .replace("{playerSize}", String.valueOf(playerList.size()))
                .replace("{isPlaying}", String.valueOf(isPlaying))
                .replace("{isWaiting}", String.valueOf(isWaiting))
                .replace("{game}", gameName)
                .replace("{gameName}", gameName);
    }

    public String replaceCalc(String string, Player player) {
        string = randomNumber(string);
        string = randomChoose(string);
        string = getData(string);
        if (player != null)
            string = getPlayerData(player).getData(string);
        string = highestNumber(string);
        string = lowestNumber(string);
        string = calc(string);
        return string;
    }

    public String replacePlayerData(String string, Player player) {
        if (player != null) {
            string = string
                    .replace("{playerName}", player.getName())
                    .replace("{player}", player.getName());
            if (string.contains("{playerMode}")) string = string.replace("{playerMode}", getMode(player));
            if (string.contains("{realPlayerMode}")) string = string.replace("{realPlayerMode}", getRealMode(player));
        } else {
            string = string
                    .replace("{playerName}", playerList.get(0).getName())
                    .replace("{player}", playerList.get(0).getName());
            if (string.contains("{playerMode}")) string = string.replace("{playerMode}", getMode(playerList.get(0)));
            if (string.contains("{realPlayerMode}")) string = string.replace("{realPlayerMode}", getRealMode(playerList.get(0)));
        }
        string = replaceCalc(string, player);

        return string;
    }

    public List<String> replaceAllPlayer(String string, List<Player> playerList) {
        string = replaceGameData(string);
        List<String> playerCmdList = new ArrayList<>();
        for (Player p : playerList) {
            playerCmdList.add(replacePlayerData(string, p));
        }
        return playerCmdList;
    }

    public String replaceAll(String string, Player player) {
        return replaceCalc(replacePlayerData(replaceGameData(string), player), player);
    }

}