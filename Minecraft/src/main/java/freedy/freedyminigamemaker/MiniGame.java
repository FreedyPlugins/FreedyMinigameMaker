// 
// Decompiled by Procyon v0.5.36
// 

package freedy.freedyminigamemaker;

import java.text.DateFormat;
import java.util.Calendar;
import java.text.SimpleDateFormat;
import java.util.concurrent.ThreadLocalRandom;
import java.util.Collection;
import java.util.Arrays;
import javax.script.ScriptEngine;
import javax.script.ScriptException;
import javax.script.ScriptEngineManager;
import java.math.BigDecimal;
import org.apache.commons.lang.StringUtils;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.Location;
import org.bukkit.WorldBorder;
import org.bukkit.block.Biome;
import java.util.Collections;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.inventory.ItemStack;
import java.util.function.Predicate;
import java.util.Objects;
import java.util.stream.Stream;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.Plugin;
import org.bukkit.block.Block;
import java.util.Iterator;
import java.util.ArrayList;
import java.util.HashMap;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.block.BlockState;
import java.util.List;
import java.util.Map;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.scheduler.BukkitScheduler;

public class MiniGame extends DataStore
{
    int taskId;
    public BukkitScheduler scheduler;
    public ConsoleCommandSender console;
    public Map<String, String> customData;
    public List<BlockState> blockList;
    public List<Player> playerList;
    //public Map<String, List<Player>> teamPlayers;
    public List<PlayerData> playerDataList;
    int playerAmount;
    boolean isPlaying;
    boolean isWaiting;
    int waitTime;
    Map<String, Integer> repeaterTimeList;

    public MiniGame(final FreedyMinigameMaker plugin, final String gameName) {
        super(plugin, gameName);
        this.scheduler = Bukkit.getServer().getScheduler();
        this.console = Bukkit.getServer().getConsoleSender();
        super.plugin = plugin;
        this.customData = new HashMap<String, String>();
        this.blockList = new ArrayList<BlockState>();
        this.playerList = new ArrayList<Player>();
        //this.teamPlayers = new HashMap<String, List<Player>>();
        this.playerDataList = new ArrayList<PlayerData>();
        this.playerAmount = 0;
        this.waitTime = 0;
        this.repeaterTimeList = new HashMap<String, Integer>();
        this.isPlaying = false;
        this.isWaiting = false;
    }

    public String getFile(final String path) {
        return MiniGames.fileStore.getConfig().getString(path);
    }

    public void setFile(final String path, final String value) {
        MiniGames.fileStore.config.set(path, (Object)value);
    }

    public PlayerData getPlayerData(final Player player) {
        for (final PlayerData pData : this.playerDataList) {
            if (pData.player.equals(player)) {
                return pData;
            }
        }
        return new PlayerData(player);
    }

    public void addBlock(final Block block) {
        final BlockState blockState = block.getState();
        if (!this.blockList.contains(blockState)) {
            for (final BlockState bs : this.blockList) {
                if (bs.getLocation().equals((Object)blockState.getLocation())) {
                    return;
                }
            }
            this.blockList.add(blockState);
        }
    }

    public void resetBlocks() {
        for (final BlockState blockState : this.blockList) {
            final Block block = blockState.getLocation().getBlock();
            if (!block.getState().equals(blockState)) {
                block.setType(blockState.getType());
                blockState.update();
            }
        }
        this.blockList = new ArrayList<BlockState>();
    }

    public void setCustomData(final String key, final String value) {
        if (value.equals("none")) {
            this.removeCustomData(key);
        }
        else {
            this.customData.put(key, value);
        }
    }

    public void removeCustomData(final String key) {
        this.customData.remove(key);
    }

    public String getCustomData(final String key) {
        if (this.customData.containsKey(key)) {
            return this.customData.get(key);
        }
        return "none";
    }

    void stopChecker() {
        this.scheduler.cancelTask(this.taskId);
    }

    void startChecker() {
        this.taskId = this.scheduler.scheduleSyncRepeatingTask((Plugin)this.plugin, this::checker, 0L, 20L);
    }

    void checker() {
        if (this.isPlaying) {
            final int endTime = this.getWaitForEndTime() * this.getTimePerPlayer();
            if (this.waitTime >= endTime) {
                this.waitTime = 0;
                this.repeaterTimeList = new HashMap<String, Integer>();
                this.isPlaying = false;
                this.stop();
            }
            else {
                this.setScoreBoardAll(endTime - this.waitTime);
                for (final String repeatName : this.getRepeatList()) {
                    int repeaterTimer = (this.repeaterTimeList.get(repeatName) == null) ? 0 : this.repeaterTimeList.get(repeatName);
                    ++repeaterTimer;
                    this.repeaterTimeList.put(repeatName, repeaterTimer);
                    if (this.getRepeatTimes(repeatName).isEmpty()) {
                        if (repeaterTimer != this.getRepeatTime(repeatName)) {
                            continue;
                        }
                    }
                    else if (!this.getRepeatTimes(repeatName).contains(repeaterTimer)) {
                        continue;
                    }
                    for (final String cmd : this.getMessageList("repeats." + repeatName + ".cmd")) {
                        if (cmd.contains("{allPlayer}")) {
                            for (final Player p : this.playerList) {
                                Bukkit.getServer().dispatchCommand((CommandSender)this.console, this.replaceAll(cmd.replace("{leftTime}", String.valueOf(endTime - this.waitTime)), p));
                            }
                        }
                        else {
                            Bukkit.getServer().dispatchCommand((CommandSender)this.console, this.replaceAll(cmd.replace("{leftTime}", String.valueOf(this.getWaitForStartTime() - this.waitTime)), null));
                        }
                    }
                    if (this.getRepeatTimes(repeatName).isEmpty()) {
                        this.repeaterTimeList.put(repeatName, 0);
                    }
                }
                ++this.waitTime;
            }
        }
        else if (this.playerList.size() >= this.getMaxStartPlayers()) {
            this.isWaiting = true;
            if (this.waitTime >= this.getWaitForStartTime()) {
                this.isWaiting = false;
                this.isPlaying = true;
                this.waitTime = 0;
                this.repeaterTimeList = new HashMap<String, Integer>();
                this.start();
            }
            else {
                final String repeatName2 = "wait";
                int repeaterTimer2 = (this.repeaterTimeList.get(repeatName2) == null) ? 0 : this.repeaterTimeList.get(repeatName2);
                ++repeaterTimer2;
                this.repeaterTimeList.put(repeatName2, repeaterTimer2);
                Label_0786: {
                    if (this.getRepeatTimes(repeatName2).isEmpty()) {
                        if (repeaterTimer2 != this.getRepeatTime(repeatName2)) {
                            break Label_0786;
                        }
                    }
                    else if (!this.getRepeatTimes(repeatName2).contains(repeaterTimer2)) {
                        break Label_0786;
                    }
                    for (final String cmd2 : this.getMessageList("repeats." + repeatName2 + ".cmd")) {
                        if (cmd2.contains("{allPlayer}")) {
                            for (final Player p2 : this.playerList) {
                                Bukkit.getServer().dispatchCommand((CommandSender)this.console, this.replaceAll(cmd2.replace("{leftTime}", String.valueOf(this.getWaitForStartTime() - this.waitTime)), p2));
                            }
                        }
                        else {
                            Bukkit.getServer().dispatchCommand((CommandSender)this.console, this.replaceAll(cmd2.replace("{leftTime}", String.valueOf(this.getWaitForStartTime() - this.waitTime)), null));
                        }
                    }
                    if (this.getRepeatTimes(repeatName2).isEmpty()) {
                        this.repeaterTimeList.put(repeatName2, 0);
                    }
                }
                this.setScoreBoardAll(this.getWaitForStartTime() - this.waitTime);
                ++this.waitTime;
            }
        }
    }

    public boolean hasEmptyInventory(final Player player) {
        return !Stream.of(player.getInventory().getContents()).anyMatch(Objects::nonNull) && !Stream.of(player.getInventory().getArmorContents()).anyMatch(Objects::nonNull);
    }

    public void add(final Player player) {
        if (this.getGameList().contains(this.gameName)) {
            if (!this.playerList.contains(player)) {
                if (!this.getNeedClearInv() || this.hasEmptyInventory(player)) {
                    if (this.playerList.size() < this.getMaxPlayers()) {
                        if (!this.isPlaying) {
                            for (final String cmd : this.getMessageList("preJoinCmd")) {
                                final String output = this.executeEventCommands(cmd, player);
                                if (output.equals("false")) {
                                    return;
                                }
                            }
                            this.playerList.add(player);
                            this.playerDataList.add(new PlayerData(player));
                            if (this.getMessage("joinMsg") != null) {
                                for (final Player p : this.playerList) {
                                    p.sendMessage(this.replaceAll(this.getMessage("joinMsg"), player));
                                }
                            }
                            if (this.getLocationIsExist("waitLocation")) {
                                player.teleport(this.getLocation("waitLocation"));
                            }
                            this.setScoreBoardAll(0);
                            for (final String cmd : this.getMessageList("joinCmd")) {
                                Bukkit.getServer().dispatchCommand((CommandSender)this.console, this.replaceAll(cmd, player));
                            }
                            if (!this.isWaiting && this.playerList.size() == this.getMaxStartPlayers()) {
                                this.startChecker();
                                if (this.getMessage("readyToStartMsg") != null) {
                                    for (final Player p : this.playerList) {
                                        p.sendMessage(this.replaceAll(this.getMessage("readyToStartMsg"), player));
                                    }
                                }
                            }
                        }
                        else {
                            player.sendMessage("§c\uac8c\uc784\uc774 \uc774\ubbf8 \uc2dc\uc791\ub418\uc5c8\uc2b5\ub2c8\ub2e4");
                        }
                    }
                    else {
                        player.sendMessage("§c\uac8c\uc784\uc774 \ucd5c\ub300\uc778\uc6d0\uc5d0 \ub3c4\ub2ec\ud588\uc2b5\ub2c8\ub2e4");
                    }
                }
                else {
                    player.sendMessage("§c\uadf8 \uac8c\uc784\uc5d0 \ucc38\uac00\ud558\ub824\uba74 \uc778\ubca4\ud1a0\ub9ac\ub97c \ube44\uc6cc\uc57c \ud569\ub2c8\ub2e4");
                }
            }
            else {
                player.sendMessage("§c\uac8c\uc784\uc744 \uc774\ubbf8 \ud50c\ub808\uc774 \uc911\uc785\ub2c8\ub2e4");
            }
        }
        else {
            player.sendMessage("§c\uadf8 \uac8c\uc784\uc774 \uc5c6\uc2b5\ub2c8\ub2e4");
        }
    }

    public void removeAll() {
        List<Player> playerList = this.playerList;
        for (int i = 0; i < playerList.size(); i++) {
            remove(playerList.get(i));
        }
    }

    public void shutDown() {
        for (Player p : playerList) {
            this.remove(p);
        }
        disable();
    }

    public void removeSituation(final Player player) {
        this.removeScoreBoard(player);
        player.getInventory().clear();
        player.updateInventory();
        if (this.getMessage("quitMsg") != null) {
            for (final Player p : this.playerList) {
                p.sendMessage(this.replaceAll(this.getMessage("quitMsg"), player));
            }
        }
        this.executeCommands(this.getMessageList("quitCmd"), player);
        if (this.getLocationIsExist("endLocation")) {
            player.teleport(this.getLocation("endLocation"));
        }
    }

    public void remove(final Player player) {
        this.removeSituation(player);
        this.playerList.remove(player);
        this.playerDataList.remove(this.getPlayerData(player));
    }

    public void disable() {
        this.isPlaying = false;
        this.isWaiting = false;
        this.waitTime = 0;
        this.repeaterTimeList = new HashMap<String, Integer>();
        this.playerAmount = 0;
        this.customData = new HashMap<String, String>();
        this.blockList = new ArrayList<BlockState>();
        this.playerList = new ArrayList<Player>();
        this.playerDataList = new ArrayList<PlayerData>();
        //this.teamPlayers.replaceAll((n, v) -> new ArrayList());
        this.stopChecker();
    }

    public boolean wasNothingToStop() {
        return this.playerList.size() != 0 && (!this.isWaiting || this.playerList.size() >= this.getMaxStartPlayers()) && this.waitTime != 0 && this.playerList.size() != 1;
    }

    public void stop() {
        if (this.isWaiting && this.playerList.size() < this.getMaxStartPlayers()) {
            this.waitTime = 0;
            this.repeaterTimeList = new HashMap<>();
            for (final Player p : this.playerList) {
                p.sendMessage(this.getMessage("morePlayerMsg"));
                this.setScoreBoard(p, 0);
            }
            return;
        }
        if (this.wasNothingToStop()) {
            return;
        }
        this.executeCommands(this.getMessageList("preConEndCmd"), null);
        /*
        String showingEndMsg = null;
                if (this.isPlaying && this.waitTime == 0) {
                    showingEndMsg = this.getMessage("noWinnerEndMsg");

                }
                if (this.playerList.size() == 1) {
                    showingEndMsg = this.replaceAll(this.getMessage("endMsg"), null);
                    for (final String cmd2 : this.getMessageList("winnerCmd")) {
                        Bukkit.getServer().dispatchCommand((CommandSender) this.console, this.replaceAll(cmd2, null));
                    }

                }
                showingEndMsg = this.getMessage("noWinnerEndMsg");

        if (showingEndMsg != null) {
            for (final Player p2 : this.playerList) {
                p2.sendMessage(showingEndMsg.replace("{game}", this.gameName));
            }
        }*/
        this.removeAll();
        this.executeCommands(this.getMessageList("conEndCmd"), null);
        this.disable();
    }

    public void setScoreBoardAll(final int time) {
        if (this.getScoreBoardMode()) {
            for (final Player p : this.playerList) {
                this.setScoreBoard(p, time);
            }
        }
    }

    public void createScoreBoard(final Player player, final String title) {
        final Scoreboard board = Bukkit.getScoreboardManager().getNewScoreboard();
        final Objective obj = board.registerNewObjective(this.gameName, "dummy");
        obj.setDisplayName(title);
        final PlayerData playerData = this.getPlayerData(player);
        playerData.scoreBoardTitle = title;
        obj.setDisplaySlot(DisplaySlot.SIDEBAR);
        player.setScoreboard(board);
    }

    public void setScoreBoard(final Player player, final int index, final String message) {
        final Scoreboard board = player.getScoreboard();
        final Objective obj = board.getObjective(this.gameName);
        final PlayerData playerData = this.getPlayerData(player);
        final Map<Integer, String> line = playerData.scoreBoard;
        if (line.containsKey(index)) {
            board.resetScores((String)line.get(index));
        }
        line.put(index, message);
        playerData.scoreBoard = line;
        for (final int i : line.keySet()) {
            obj.getScore((String)line.get(i)).setScore(i);
        }
        player.setScoreboard(board);
    }

    public void setScoreBoard(final Player player, final int time) {
        final Scoreboard board = Bukkit.getScoreboardManager().getNewScoreboard();
        final Objective obj = board.registerNewObjective(this.getMessage("scoreBoardTitle"), "dummy");
        obj.setDisplaySlot(DisplaySlot.SIDEBAR);
        final List<String> scoreBoard = this.getMessageList("scoreBoard");
        Collections.reverse(scoreBoard);
        for (int i = 0; i < scoreBoard.size(); ++i) {
            obj.getScore(this.replaceAll(scoreBoard.get(i), player).replace("{displayTime}", this.isWaiting ? this.getMessage("startTimerMsg").replace("{time}", String.valueOf(time)) : (this.isPlaying ? this.getMessage("endTimerMsg").replace("{time}", String.valueOf(time)) : this.getMessage("morePlayerMsg")))).setScore(i);
        }
        player.setScoreboard(board);
    }

    public void removeScoreBoard(final Player player) {
        player.setScoreboard(Bukkit.getScoreboardManager().getNewScoreboard());
    }

    public void setWorldBoarder() {
        if (this.getWorldBoarderMode()) {
            final WorldBorder worldBorder = this.getWorldBoarder();
            final Location worldBoarderLocation = this.getWorldBoarderLocation();
            Label_0127: {
                if (this.getSafeWorldBoarderFinderMode()) {
                    for (int x = 0; x < 1000; ++x) {
                        for (int z = 0; z < 1000; ++z) {
                            final Biome biome = worldBoarderLocation.getWorld().getBiome(x, z);
                            if (biome != Biome.OCEAN && biome != Biome.DEEP_OCEAN && biome != Biome.FROZEN_OCEAN) {
                                worldBoarderLocation.setX((double)x);
                                worldBoarderLocation.setZ((double)z);
                                worldBorder.setCenter(worldBoarderLocation);
                                break Label_0127;
                            }
                        }
                    }
                }
                else {
                    worldBorder.setCenter(worldBoarderLocation);
                }
            }
            worldBorder.setDamageAmount(this.getWorldBoarderOutDamage());
            worldBorder.setSize((double)(this.getWorldBoarderSizePerPlayer() * this.playerAmount));
            worldBorder.setSize((double)this.getWorldBoarderMinSize(), (long)this.getWorldBoarderSpeed());
        }
    }

    public void start() {
        Collections.shuffle(this.playerList);
        this.playerAmount = this.playerList.size();
        this.setWorldBoarder();
        this.executeCommands(this.getMessageList("conStartCmd"), null);
        int i = 0;
        final String gameType = this.getGameType();
        switch (gameType) {
            case "hideAndSeek": {
                for (final Player p : this.playerList) {
                    p.sendMessage(this.getMessage("startMsg").replace("{game}", this.gameName));
                    if (this.getLocationIsExist("defaultStartLocation")) {
                        p.teleport(this.getLocation("defaultStartLocation"));
                    }
                    ++i;
                }
                break;
            }
            default: {
                i = 0;
                int j = 0;
                for (final Player p2 : this.playerList) {
                    p2.sendMessage(this.getMessage("startMsg").replace("{game}", this.gameName));
                    if (this.getLocationIsExist("defaultStartLocation")) {
                        p2.teleport((Location)this.getLocationList("defaultStartLocation").get(i));
                    }
                    ++i;
                }
                break;
            }
        }
    }

    public void openInv(final Player player, final String invName) {
        player.openInventory(this.getInventory(invName));
    }

    public void giveItem(final Player player, final String itemName) {
        player.getInventory().addItem(new ItemStack[] { this.getItem(itemName) });
        player.updateInventory();
    }

    public void applyKit(final Player player, final String kitName) {
        final PlayerInventory inventory = player.getInventory();
        for (int i = 0; i < inventory.getSize(); ++i) {
            inventory.setItem(i, this.getItem(kitName, i));
        }
        player.updateInventory();
    }

    public boolean isExistingTitle(final String title) {
        return this.getInventoryTitleList().contains(title);
    }

    public String replaceInteger(final String string) {
        final String area = StringUtils.substringBetween(string, "{integer(", ")}");
        if (area == null) {
            return string;
        }
        final String result = String.valueOf(BigDecimal.valueOf(Double.parseDouble(area)).intValue());
        return string.replace("{integer(" + area + ")}", result);
    }

    public String replaceCalc(final String string) {
        final String area = StringUtils.substringBetween(string, "{calc(", ")}");
        if (area == null) {
            return string;
        }
        final ScriptEngineManager mgr = new ScriptEngineManager();
        final ScriptEngine engine = mgr.getEngineByName("JavaScript");
        try {
            final Object subResult = engine.eval(area);
            String result;
            if (subResult instanceof Integer) {
                result = ((Integer)subResult).toString();
            }
            else {
                result = subResult.toString();
            }
            return string.replace("{calc(" + area + ")}", String.valueOf(result));
        }
        catch (ScriptException e) {
            e.printStackTrace();
            return "(\uc5d0\ub7ec,\ucf58\uc194 \ud655\uc778 \ubc14\ub78c)";
        }
    }

    public String replaceRound(final String string) {
        final String area = StringUtils.substringBetween(string, "{round(", ")}");
        if (area == null) {
            return string;
        }
        final String result = String.valueOf(Math.round(Double.parseDouble(area)));
        return string.replace("{round(" + area + ")}", result);
    }

    public String replaceRoundUp(final String string) {
        final String area = StringUtils.substringBetween(string, "{roundUp(", ")}");
        if (area == null) {
            return string;
        }
        final String result = String.valueOf(Math.ceil(Double.parseDouble(area)));
        return string.replace("{roundUp(" + area + ")}", result);
    }

    public String replaceRoundDown(final String string) {
        final String area = StringUtils.substringBetween(string, "{roundDown(", ")}");
        if (area == null) {
            return string;
        }
        final String result = String.valueOf(Math.floor(Double.parseDouble(area)));
        return string.replace("{roundDown(" + area + ")}", result);
    }

    public String replaceReplace(final String string) {
        final String area = StringUtils.substringBetween(string, "{replace(", ")}");
        if (area == null) {
            return string;
        }
        final List<String> stringList = new ArrayList<String>(Arrays.asList(area.split(", ")));
        final String oldChar = stringList.get(0);
        stringList.remove(0);
        String newChar = stringList.get(0);
        stringList.remove(0);
        if (newChar.equals("none")) {
            newChar = "";
        }
        for (int i = 0; i < stringList.size(); ++i) {
            if (stringList.get(i).equals(oldChar)) {
                stringList.set(i, newChar);
            }
        }
        final String result = stringList.toString().substring(1, stringList.toString().length() - 1);
        return string.replace("{replace(" + area + ")}", result);
    }

    public String replaceShuffle(final String string) {
        final String area = StringUtils.substringBetween(string, "{shuffle(", ")}");
        if (area == null) {
            return string;
        }
        final List<String> stringList = new ArrayList<String>(Arrays.asList(area.split(", ")));
        Collections.shuffle(stringList);
        final String result = stringList.toString().substring(1, stringList.toString().length() - 1);
        return string.replace("{shuffle(" + area + ")}", result);
    }

    public String replacePlayerList(final String string) {
        final String area = StringUtils.substringBetween(string, "{playerList(", ")}");
        if (area == null) {
            return string;
        }
        final List<String> playerNameList = new ArrayList<String>();
        for (final Player p : this.playerList) {
            playerNameList.add(p.getName());
        }
        final String result = playerNameList.toString().substring(1, playerNameList.toString().length() - 1);
        return string.replace("{playerList(" + area + ")}", result);
    }

    public String replaceRandom(final String string) {
        final String area = StringUtils.substringBetween(string, "{random(", ")}");
        if (area == null) {
            return string;
        }
        final List<String> stringList = new ArrayList<String>(Arrays.asList(area.split(", ")));
        final String result = stringList.get(ThreadLocalRandom.current().nextInt(1, stringList.size()));
        return string.replace("{random(" + area + ")}", result);
    }

    public String replaceHighList(final String string) {
        final String area = StringUtils.substringBetween(string, "{highList(", ")}");
        if (area == null) {
            return string;
        }
        final List<String> stringList = new ArrayList<String>(Arrays.asList(area.split(", ")));
        Collections.sort(stringList);
        final String result = stringList.toString().substring(1, stringList.toString().length() - 1);
        return string.replace("{highList(" + area + ")}", result);
    }

    public String replaceFlip(final String string) {
        final String area = StringUtils.substringBetween(string, "{flip(", ")}");
        if (area == null) {
            return string;
        }
        final List<String> stringList = new ArrayList<String>(Arrays.asList(area.split(", ")));
        Collections.reverse(stringList);
        final String result = stringList.toString().substring(1, stringList.toString().length() - 1);
        return string.replace("{flip(" + area + ")}", result);
    }

    public String replaceLength(final String string) {
        final String area = StringUtils.substringBetween(string, "{length(", ")}");
        if (area.isEmpty()) {
            return string;
        }
        final String result = String.valueOf(area.length());
        return string.replace("{length(" + area + ")}", result);
    }

    public String replaceIndexOf(final String string) {
        final String area = StringUtils.substringBetween(string, "{indexOf(", ")}");
        if (area.isEmpty()) {
            return string;
        }
        final List<String> stringList = new ArrayList<String>(Arrays.asList(area.split(", ")));
        final int index = Integer.parseInt(stringList.get(0));
        stringList.remove(0);
        final String result = stringList.get(index);
        return string.replace("{indexOf(" + area + ")}", result);
    }

    public String replaceValueOf(final String string) {
        final String area = StringUtils.substringBetween(string, "{valueOf(", ")}");
        if (area.isEmpty()) {
            return string;
        }
        final List<String> stringList = new ArrayList<String>(Arrays.asList(area.split(", ")));
        final String value = stringList.get(0);
        stringList.remove(0);
        final int index = stringList.indexOf(value);
        return string.replace("{valueOf(" + area + ")}", String.valueOf(index));
    }

    public String replaceSizeOf(final String string) {
        final String area = StringUtils.substringBetween(string, "{sizeOf(", ")}");
        if (area.isEmpty()) {
            return string;
        }
        final List<String> stringList = new ArrayList<String>(Arrays.asList(area.split(", ")));
        final int size = stringList.size();
        return string.replace("{sizeOf(" + area + ")}", String.valueOf(size));
    }

    public String replaceHighestNumber(final String string) {
        final String area = StringUtils.substringBetween(string, "{highestNumber(", ")}");
        if (area == null) {
            return string;
        }
        final List<String> stringList = new ArrayList<String>(Arrays.asList(area.split(", ")));
        final List<Integer> integerList = new ArrayList<Integer>();
        for (final String s : stringList) {
            integerList.add(Integer.parseInt(s));
        }
        final Integer result = Collections.max((Collection<? extends Integer>)integerList);
        return string.replace("{highestNumber(" + area + ")}", String.valueOf(result));
    }

    public String replaceLowestNumber(final String string) {
        final String area = StringUtils.substringBetween(string, "{lowestNumber(", ")}");
        if (area == null) {
            return string;
        }
        final List<String> stringList = new ArrayList<String>(Arrays.asList(area.split(", ")));
        final List<Integer> integerList = new ArrayList<Integer>();
        for (final String s : stringList) {
            integerList.add(Integer.parseInt(s));
        }
        final Integer result = Collections.min((Collection<? extends Integer>)integerList);
        return string.replace("{lowestNumber(" + area + ")}", String.valueOf(result));
    }

    public String replaceRandomNumber(final String string) {
        final String area = StringUtils.substringBetween(string, "{randomNumber(", ")}");
        if (area == null) {
            return string;
        }
        final List<String> stringList = new ArrayList<String>(Arrays.asList(area.split(", ")));
        int result;
        if (stringList.size() == 1) {
            result = ThreadLocalRandom.current().nextInt(0, Integer.parseInt(stringList.get(0) + 1));
        }
        else {
            if (stringList.size() != 2) {
                return string;
            }
            result = ThreadLocalRandom.current().nextInt(Integer.parseInt(stringList.get(0)), Integer.parseInt(stringList.get(1)) + 1);
        }
        return string.replace("{randomNumber(" + area + ")}", String.valueOf(result));
    }

    public String replaceData(final String string) {
        final String area = StringUtils.substringBetween(string, "{data(", ")}");
        if (area == null) {
            return string;
        }
        String result = this.getCustomData(area);
        if (result == null) {
            result = "none";
        }
        return string.replace("{data(" + area + ")}", result);
    }

    public String replaceRemove(final String string) {
        final String area = StringUtils.substringBetween(string, "{remove(", ")}");
        if (area.isEmpty()) {
            return string;
        }
        final List<String> stringList = new ArrayList<String>(Arrays.asList(area.split(", ")));
        final String removing = stringList.get(0);
        stringList.remove(0);
        stringList.remove(removing);
        final String result = stringList.toString().substring(1, stringList.toString().length() - 1);
        return string.replace("{remove(" + area + ")}", result);
    }

    public String replaceAdd(final String string) {
        final String area = StringUtils.substringBetween(string, "{add(", ")}");
        if (area.isEmpty()) {
            return string;
        }
        final List<String> stringList = new ArrayList<String>(Arrays.asList(area.split(", ")));
        final String adding = stringList.get(0);
        stringList.remove(0);
        if (stringList.get(0).equals("none")) {
            stringList.remove(0);
        }
        stringList.add(adding);
        final String result = stringList.toString().substring(1, stringList.toString().length() - 1);
        return string.replace("{add(" + area + ")}", result);
    }

    public String replaceFile(final String string) {
        final String area = StringUtils.substringBetween(string, "{file(", ")}");
        if (area == null) {
            return string;
        }
        String result = this.getFile(area);
        if (result == null) {
            result = "none";
        }
        return string.replace("{file(" + area + ")}", result);
    }

    public String replaceConstant(final String string) {
        final String area = StringUtils.substringBetween(string, "{constant(", ")}");
        if (area == null) {
            return string;
        }
        String result = this.getMessage(area);
        if (result == null) {
            result = "none";
        }
        return string.replace("{constant(" + area + ")}", result);
    }

    public String replaceDate(final String string) {
        final String area = StringUtils.substringBetween(string, "{date(", ")}");
        if (area == null) {
            return string;
        }
        final DateFormat dateFormat = new SimpleDateFormat(area);
        final Calendar cal = Calendar.getInstance();
        final String result = dateFormat.format(cal.getTime());
        return string.replace("{date(" + area + ")}", result);
    }

    public String replaceGameData(final String string) {
        return string.replace("{maxPlayers}", String.valueOf(this.getMaxStartPlayers())).replace("{randomPlayer}", (this.playerList.size() == 1) ? this.playerList.get(0).getName() : (this.playerList.isEmpty() ? "none" : this.playerList.get(ThreadLocalRandom.current().nextInt(0, this.playerList.size() - 1)).getName())).replace("{playerAmount}", String.valueOf(this.playerList.size())).replace("{playerSize}", String.valueOf(this.playerList.size())).replace("{isPlaying}", String.valueOf(this.isPlaying)).replace("{isWaiting}", String.valueOf(this.isWaiting)).replace("{game}", this.gameName).replace("{gameType}", (this.getGameType() == null) ? "none" : this.getGameType()).replace("{gameName}", this.gameName);
    }

    public String replaceCalcAll(String string, final Player player) {
        final String[] strings = StringUtils.substringsBetween(string, "{", "(");
        if (strings == null) {
            return string;
        }
        final List<String> areas = Arrays.asList(strings);
        Collections.reverse(areas);
        for (final String s : areas) {
            final String area = s;
            switch (s) {
                case "playerList": {
                    string = this.replacePlayerList(string);
                    continue;
                }
                case "add": {
                    string = this.replaceAdd(string);
                    continue;
                }
                case "remove": {
                    string = this.replaceRemove(string);
                    continue;
                }
                case "file": {
                    string = this.replaceFile(string);
                    continue;
                }
                case "date": {
                    string = this.replaceDate(string);
                    continue;
                }
                case "length": {
                    string = this.replaceLength(string);
                    continue;
                }
                case "indexOf": {
                    string = this.replaceIndexOf(string);
                    continue;
                }
                case "valueOf": {
                    string = this.replaceValueOf(string);
                    continue;
                }
                case "sizeOf": {
                    string = this.replaceSizeOf(string);
                    continue;
                }
                case "shuffle": {
                    string = this.replaceShuffle(string);
                    continue;
                }
                case "highList": {
                    string = this.replaceHighList(string);
                    continue;
                }
                case "flip": {
                    string = this.replaceFlip(string);
                    continue;
                }
                case "randomNumber": {
                    string = this.replaceRandomNumber(string);
                    continue;
                }
                case "random": {
                    string = this.replaceRandom(string);
                    continue;
                }
                case "constant": {
                    string = this.replaceConstant(string);
                    continue;
                }
                case "data": {
                    string = this.replaceData(string);
                    continue;
                }
                case "playerData": {
                    if (player != null) {
                        string = this.getPlayerData(player).replaceData(string);
                        continue;
                    }
                    continue;
                }
                case "replace": {
                    string = this.replaceReplace(string);
                    continue;
                }
                case "highestNumber": {
                    string = this.replaceHighestNumber(string);
                    continue;
                }
                case "lowestNumber": {
                    string = this.replaceLowestNumber(string);
                    continue;
                }
                case "integer": {
                    string = this.replaceInteger(string);
                    continue;
                }
                case "round": {
                    string = this.replaceRound(string);
                    continue;
                }
                case "roundUp": {
                    string = this.replaceRoundUp(string);
                    continue;
                }
                case "roundDown": {
                    string = this.replaceRoundDown(string);
                    continue;
                }
                case "calc": {
                    string = this.replaceCalc(string);
                    continue;
                }
            }
        }
        return string;
    }

    public String replacePlayerData(String string, final Player player) {
        if (player != null) {
            string = string.replace("{playerUuid}", player.getUniqueId().toString()).replace("{x}", String.valueOf(player.getLocation().getX())).replace("{y}", String.valueOf(player.getLocation().getY())).replace("{z}", String.valueOf(player.getLocation().getZ())).replace("{yaw}", String.valueOf(player.getLocation().getYaw())).replace("{pitch}", String.valueOf(player.getLocation().getPitch())).replace("{world}", player.getLocation().getWorld().getName()).replace("{allPlayer}", player.getName()).replace("{playerName}", player.getName()).replace("{playerItemSlot}", String.valueOf(player.getInventory().getHeldItemSlot())).replace("{playerIndex}", String.valueOf(this.playerList.indexOf(player))).replace("{player}", player.getName());
        }
        else if (this.playerList.isEmpty()) {
            string = string.replace("{allPlayer}", "none").replace("{playerName}", "none").replace("{playerIndex}", "none").replace("{player}", "none");
            if (string.contains("{playerMode}")) {
                string = string.replace("{playerMode}", "none");
            }
            if (string.contains("{realPlayerMode}")) {
                string = string.replace("{realPlayerMode}", "none");
            }
        }
        else {
            string = string.replace("{allPlayer}", this.playerList.get(0).getName()).replace("{playerName}", this.playerList.get(0).getName()).replace("{playerIndex}", "0").replace("{player}", this.playerList.get(0).getName());
        }
        string = this.replaceCalcAll(string, player);
        return string;
    }

    public List<String> replaceAllPlayer(String string, final List<Player> playerList) {
        string = this.replaceGameData(string);
        final List<String> playerCmdList = new ArrayList<String>();
        for (final Player p : playerList) {
            playerCmdList.add(this.replacePlayerData(string, p));
        }
        return playerCmdList;
    }

    public String replaceAll(final String string, final Player player) {
        if (string == null) {
            return null;
        }
        return this.replacePlayerData(this.replaceGameData(string), player);
    }

    public void executeCommands(final List<String> commands, final Player player) {
        for (final String cmd : commands) {
            if (cmd.contains("{allPlayer}")) {
                for (final String playerCmd : this.replaceAllPlayer(cmd, this.playerList)) {
                    Bukkit.getServer().dispatchCommand((CommandSender)this.console, playerCmd);
                }
            }
            else {
                Bukkit.getServer().dispatchCommand((CommandSender)this.console, this.replaceAll(cmd, player));
            }
        }
    }

    public String executeEventCommands(final String command, final Player player) {
        final freedy.freedyminigamemaker.commands.CommandSender sender = new freedy.freedyminigamemaker.commands.CommandSender();
        if (command.contains("{allPlayer}")) {
            for (final String playerCmd : this.replaceAllPlayer(command, this.playerList)) {
                Bukkit.getServer().dispatchCommand((CommandSender)sender, playerCmd);
            }
        }
        else {
            Bukkit.getServer().dispatchCommand((CommandSender)sender, this.replaceAll(command, player));
        }
        return sender.output;
    }
}
