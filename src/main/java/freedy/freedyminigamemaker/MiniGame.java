// 
// Decompiled by Procyon v0.5.36
// 

package freedy.freedyminigamemaker;

import freedy.freedyminigamemaker.commands.FreedyCommandSender;
import net.jafama.FastMath;
import org.apache.commons.lang.StringUtils;
import org.bukkit.*;
import org.bukkit.block.Biome;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.command.CommandException;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;
import org.bukkit.util.Vector;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Stream;

public class MiniGame extends DataStore
{
    int taskId;
    public List<Integer> taskIdList;
    public BukkitScheduler scheduler;
    public FreedyCommandSender freedyCommandSender;
    public Map<String, String> customData;
    public List<BlockState> blockList;
    public Map<String, List<BlockState>> customBlockList;
    public List<Player> playerList;
    public List<PlayerData> playerDataList;
    public Map<String, List<ItemStack>> inventoryList;
    int playerAmount;
    boolean isPlaying;
    boolean isWaiting;
    int waitTime;
    Map<String, Integer> repeaterTimeList;
    
    public MiniGame(final FreedyMinigameMaker plugin, final String gameName) {
        super(plugin, gameName);
        this.taskIdList = new ArrayList<Integer>();
        this.scheduler = Bukkit.getServer().getScheduler();
        this.freedyCommandSender = new FreedyCommandSender();
        super.plugin = plugin;
        this.customData = new HashMap<String, String>();
        this.blockList = new ArrayList<BlockState>();
        this.customBlockList = new HashMap<String, List<BlockState>>();
        this.playerList = new ArrayList<Player>();
        this.playerDataList = new ArrayList<PlayerData>();
        this.inventoryList = new HashMap<String, List<ItemStack>>();
        this.playerAmount = 0;
        this.waitTime = 0;
        this.repeaterTimeList = new HashMap<String, Integer>();
        this.isPlaying = false;
        this.isWaiting = false;
    }
    
    public String getFile(final String path) {
        return MiniGames.fileStore.getConfig().getString(path);
    }
    
    public String getSettings(final String path) {
        return MiniGames.settings.getConfig().getString(path);
    }
    
    public void setFile(final String path, final String value) {
        MiniGames.fileStore.config.set(path, value);
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
                if (bs.getLocation().equals(blockState.getLocation())) {
                    return;
                }
            }
            this.blockList.add(blockState);
        }
    }
    
    public void addBlock(final BlockState blockState) {
        if (!this.blockList.contains(blockState)) {
            for (final BlockState bs : this.blockList) {
                if (bs.getLocation().equals(blockState.getLocation())) {
                    return;
                }
            }
            this.blockList.add(blockState);
        }
    }
    
    public void addBlock(final String blockName, final BlockState blockState) {
        List<BlockState> blockList = this.customBlockList.get(blockName);
        if (blockList == null) {
            blockList = new ArrayList<BlockState>();
        }
        if (!blockList.contains(blockState)) {
            for (final BlockState bs : blockList) {
                if (bs.getLocation().equals(blockState.getLocation())) {
                    return;
                }
            }
            blockList.add(blockState);
        }
        this.customBlockList.put(blockName, blockList);
    }
    
    public void resetBlocks() {
        this.resetBlocks(this.blockList);
        this.blockList = new ArrayList<BlockState>();
        for (final String blockName : this.customBlockList.keySet()) {
            this.resetBlocks(this.customBlockList.get(blockName));
        }
        this.customBlockList = new HashMap<String, List<BlockState>>();
    }
    
    public void resetBlocks(final String blockName) {
        final List<BlockState> blockList = this.customBlockList.get(blockName);
        if (blockList == null) {
            return;
        }
        this.resetBlocks(blockList);
        this.customBlockList.remove(blockName);
    }
    
    public void resetBlocks(final List<BlockState> blockList) {
        for (final BlockState blockState : blockList) {
            final Block block = blockState.getLocation().getBlock();
            if (!block.getState().equals(blockState)) {
                block.setType(blockState.getType());
                blockState.update();
            }
        }
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
        for (final int taskId : this.taskIdList) {
            this.scheduler.cancelTask(taskId);
        }
        this.taskIdList = new ArrayList<Integer>();
        this.scheduler.cancelTask(this.taskId);
    }
    
    void startChecker() {
        this.taskId = this.scheduler.scheduleSyncRepeatingTask(this.plugin, this::checker, 1L, 1L);
    }
    
    public void cancelAllTask() {
        for (final int taskId : this.taskIdList) {
            this.scheduler.cancelTask(taskId);
        }
        this.taskIdList = new ArrayList<Integer>();
    }
    
    void checker() {
        if (this.playerList == null || this.playerList.isEmpty()) {
            return;
        }
        if (this.isPlaying) {
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
                    if (this.executeEventCommands(cmd, this.playerList.get(0)).equals("false")) {
                        break;
                    }
                }
                if (this.getRepeatTimes(repeatName).isEmpty()) {
                    this.repeaterTimeList.put(repeatName, 0);
                }
            }
            ++this.waitTime;
        }
        else if (this.getMaxStartPlayers() > 0 || this.playerList.size() >= this.getMaxStartPlayers()) {
            this.isWaiting = true;
            if (this.waitTime >= this.getWaitForStartTime() && 0 <= this.getWaitForStartTime()) {
                this.isWaiting = false;
                this.isPlaying = true;
                this.waitTime = 0;
                this.repeaterTimeList = new HashMap<String, Integer>();
                this.start();
            }
            else {
                final String repeatName2 = "wait";
                int repeaterTimer2 = (this.repeaterTimeList.get("wait") == null) ? 0 : this.repeaterTimeList.get("wait");
                ++repeaterTimer2;
                this.repeaterTimeList.put("wait", repeaterTimer2);
                Label_0562: {
                    if (this.getRepeatTimes("wait").isEmpty()) {
                        if (repeaterTimer2 != this.getRepeatTime("wait")) {
                            break Label_0562;
                        }
                    }
                    else if (!this.getRepeatTimes("wait").contains(repeaterTimer2)) {
                        break Label_0562;
                    }
                    for (final String cmd2 : this.getMessageList("repeats.wait.cmd")) {
                        this.executeCommand(this.freedyCommandSender, cmd2, this.playerList.get(0));
                    }
                    if (this.getRepeatTimes("wait").isEmpty()) {
                        this.repeaterTimeList.put("wait", 0);
                    }
                }
                ++this.waitTime;
            }
        }
    }
    
    public boolean hasEmptyInventory(final Player player) {
        return Stream.of(player.getInventory().getContents()).noneMatch(Objects::nonNull) && Stream.of(player.getInventory().getArmorContents()).noneMatch(Objects::nonNull);
    }
    
    public boolean checkMove(final Player player) {
        if (this.getGameList().contains(this.gameName)) {
            if (!this.playerList.contains(player)) {
                if (!this.getNeedClearInv() || this.hasEmptyInventory(player)) {
                    if (this.getMaxPlayers() < 1 || this.playerList.size() < this.getMaxPlayers()) {
                        if (!this.isPlaying) {
                            return true;
                        }
                        player.sendMessage(this.getSettings("gameAlreadyStarted"));
                    }
                    else {
                        player.sendMessage(this.getSettings("maxPlayerToJoin"));
                    }
                }
                else {
                    player.sendMessage(this.getSettings("needClearInv"));
                }
            }
            else {
                player.sendMessage(this.getSettings("alreadyPlaying"));
            }
        }
        return false;
    }
    
    public boolean checkAdd(final Player player) {
        if (!FreedyMinigameMaker.miniGames.isJoined(player)) {
            if (this.getGameList().contains(this.gameName)) {
                if (!this.playerList.contains(player)) {
                    if (!this.getNeedClearInv() || this.hasEmptyInventory(player)) {
                        if (this.getMaxPlayers() < 1 || this.playerList.size() < this.getMaxPlayers()) {
                            if (!this.isPlaying) {
                                return true;
                            }
                            player.sendMessage(this.getSettings("gameAlreadyStarted"));
                        }
                        else {
                            player.sendMessage(this.getSettings("maxPlayerToJoin"));
                        }
                    }
                    else {
                        player.sendMessage(this.getSettings("needClearInv"));
                    }
                }
                else {
                    player.sendMessage(this.getSettings("alreadyPlaying"));
                }
            }
            else {
                player.sendMessage(this.getSettings("noGameExist"));
            }
        }
        return false;
    }
    
    public void add(final Player player) {
        if (!this.checkAdd(player)) {
            return;
        }
        for (final String cmd : this.getMessageList("preJoinCmd")) {
            if (this.executeEventCommands(cmd, player).equals("false")) {
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
        for (final String cmd : this.getMessageList("joinCmd")) {
            Bukkit.getServer().dispatchCommand(this.freedyCommandSender, this.replaceAll(cmd, player));
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
    
    public void removeAll() {
        if (this.playerList.size() != 0) {
            while (this.playerList.size() != 0) {
                this.kick(this.playerList.get(0));
            }
        }
    }
    
    public void removeSituation(final Player player, final boolean isKicked) {
        if (!isKicked && this.getMessage("quitMsg") != null) {
            for (final Player p : this.playerList) {
                p.sendMessage(this.replaceAll(this.getMessage("quitMsg"), player));
            }
        }
        this.executeCommands(this.freedyCommandSender, this.getMessageList("quitCmd"), player);
        if (this.getLocationIsExist("endLocation")) {
            player.teleport(this.getLocation("endLocation"));
        }
    }
    
    public void remove(final Player player) {
        this.executeCommands(this.freedyCommandSender, this.getMessageList("preQuitCmd"), player);
        this.removeSituation(player, false);
        this.playerList.remove(player);
        this.playerDataList.remove(this.getPlayerData(player));
        final String defaultQuitGame = this.getSettings("defaultQuitGame");
        if (defaultQuitGame != null && !defaultQuitGame.equals("none")) {
            FreedyMinigameMaker.miniGames.get(defaultQuitGame).add(player);
        }
    }
    
    public void kick(final Player player) {
        this.executeCommands(this.freedyCommandSender, this.getMessageList("preQuitCmd"), player);
        this.removeSituation(player, true);
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
        this.customBlockList = new HashMap<String, List<BlockState>>();
        this.playerList = new ArrayList<Player>();
        this.playerDataList = new ArrayList<PlayerData>();
        this.inventoryList = new HashMap<String, List<ItemStack>>();
        this.stopChecker();
        FreedyMinigameMaker.miniGames.reset(this.gameName);
    }
    
    public boolean wasNothingToStop() {
        if (this.isWaiting && this.playerList.size() < this.getMaxStartPlayers()) {
            this.waitTime = 0;
            this.repeaterTimeList = new HashMap<String, Integer>();
            for (final Player p : this.playerList) {
                if (this.getMessage("morePlayerMsg") != null) {
                    p.sendMessage(this.getMessage("morePlayerMsg"));
                }
            }
            return true;
        }
        return this.playerList.size() != 0;
    }
    
    public void stop() {
        final boolean cancelStop = this.wasNothingToStop();
        if (cancelStop) {
            return;
        }
        this.executeCommands(this.freedyCommandSender, this.getMessageList("preConEndCmd"), null);
        this.removeAll();
        this.executeCommands(this.freedyCommandSender, this.getMessageList("conEndCmd"), null);
        this.disable();
    }
    
    public void setBoard(final Player player, final String title) {
        final Scoreboard board = player.getScoreboard();
        final Objective obj = board.registerNewObjective(title, "dummy");
        obj.setDisplaySlot(DisplaySlot.SIDEBAR);
        player.setScoreboard(board);
    }
    
    public void updateBoard(final Player player, final String title, final String lineName, final String line, final String message) {
        final Scoreboard board = player.getScoreboard();
        Objective obj = board.getObjective(title);
        if (obj == null) {
            obj = board.registerNewObjective(title, "dummy");
            obj.setDisplaySlot(DisplaySlot.SIDEBAR);
            player.setScoreboard(board);
        }
        Team team = board.getTeam(lineName);
        if (team == null || obj.getScore(lineName) == null) {
            obj.getScore(lineName).setScore(Integer.parseInt(line));
            team = board.registerNewTeam(lineName);
            team.addEntry(lineName);
            player.setScoreboard(board);
        }
        if (message.equals("none")) {
            team.removeEntry(lineName);
            team.unregister();
            player.setScoreboard(board);
        }
        else {
            team.setPrefix(message);
        }
    }
    

    
    public void start() {
        Collections.shuffle(this.playerList);
        this.playerAmount = this.playerList.size();
        this.executeCommands(this.freedyCommandSender, this.getMessageList("conStartCmd"), null);
        for (final Player p2 : this.playerList) {
            if (this.getMessage("startMsg") != null) {
                p2.sendMessage(this.getMessage("startMsg").replace("{game}", this.gameName));
            }
            if (this.getLocationIsExist("startLocation")) {
                p2.teleport(this.getLocation("startLocation"));
            }
        }
    }
    
    public void setBossBar(final Player player, final String barName, final BarColor barColor, final double progress, final String message) {
        for (final PlayerData pData : this.playerDataList) {
            if (pData.player.equals(player)) {
                BossBar bossBar = pData.getBossBar(barName);
                if (message.equals("none")) {
                    if (bossBar != null) {
                        bossBar.removePlayer(player);
                    }
                    return;
                }
                if (bossBar == null) {
                    bossBar = Bukkit.createBossBar(message, barColor, BarStyle.SOLID);
                }
                if (!bossBar.getColor().equals(barColor)) {
                    bossBar.setColor(barColor);
                }
                if (!bossBar.getTitle().equals(message)) {
                    bossBar.setTitle(message);
                }
                bossBar.setProgress(progress);
                bossBar.setVisible(true);
                pData.setBossBar(barName, bossBar);
                bossBar.addPlayer(player);
            }
        }
    }
    
    public void setGuiItemName(final Player player, final String invName, final int index, final String itemName) {
        for (final PlayerData pData : this.playerDataList) {
            if (pData.player.equals(player)) {
                Inventory inventory = pData.getInventory(invName);
                if (inventory == null) {
                    inventory = this.getInventory(invName);
                }
                final ItemStack item = inventory.getItem(index);
                final ItemMeta itemMeta = item.getItemMeta();
                itemMeta.setDisplayName(itemName);
                item.setItemMeta(itemMeta);
                inventory.setItem(index, item);
                pData.setInventory(invName, inventory);
            }
        }
    }
    
    public void setGuiItemLore(final Player player, final String invName, final int index, final int line, final String itemLore) {
        for (final PlayerData pData : this.playerDataList) {
            if (pData.player.equals(player)) {
                Inventory inventory = pData.getInventory(invName);
                if (inventory == null) {
                    inventory = this.getInventory(invName);
                }
                final ItemStack item = inventory.getItem(index);
                List<String> lore = item.getItemMeta().getLore();
                if (lore == null) {
                    lore = new ArrayList<String>();
                }
                if (itemLore.equals("none")) {
                    lore.remove(itemLore);
                }
                else if (lore.size() > line && lore.size() != 0) {
                    lore.set(line, itemLore);
                }
                else {
                    lore.add(itemLore);
                }
                final ItemMeta itemMeta = item.getItemMeta();
                itemMeta.setLore(lore);
                item.setItemMeta(itemMeta);
                inventory.setItem(index, item);
                pData.setInventory(invName, inventory);
            }
        }
    }
    
    public void setGui(final Player player, final String invName, final int index, final String itemName) {
        for (final PlayerData pData : this.playerDataList) {
            if (pData.player.equals(player)) {
                Inventory inventory = pData.getInventory(invName);
                if (inventory == null) {
                    inventory = this.getInventory(invName);
                }
                inventory.setItem(index, this.getItem(itemName));
                pData.setInventory(invName, inventory);
            }
        }
    }
    
    public void resetInventory(final Player player) {
        for (final PlayerData pData : this.playerDataList) {
            if (pData.player.equals(player)) {
                pData.resetInventory();
            }
        }
    }
    
    public void openGui(final Player player, final String invName) {
        for (final PlayerData pData : this.playerDataList) {
            if (pData.player.equals(player)) {
                Inventory inventory = pData.getInventory(invName);
                if (inventory == null) {
                    inventory = this.getInventory(invName);
                }
                player.openInventory(inventory);
            }
        }
    }
    
    public void openInv(final Player player, final String invName) {
        player.openInventory(this.getInventory(invName));
    }
    
    public void giveItem(final Player player, final String itemName) {
        player.getInventory().addItem(this.getItem(itemName));
        player.updateInventory();
    }
    
    public void giveItem(final Player player, final String itemName, final int amount) {
        final ItemStack itemStack = this.getItem(itemName);
        itemStack.setAmount(amount);
        player.getInventory().addItem(itemStack);
        player.updateInventory();
    }
    
    public void giveItemHand(final Player player, final String itemName) {
        player.getInventory().setItemInMainHand(this.getItem(itemName));
        player.updateInventory();
    }
    
    public void giveItemHand(final Player player, final String itemName, final int amount) {
        final ItemStack itemStack = this.getItem(itemName);
        itemStack.setAmount(amount);
        player.getInventory().setItemInMainHand(itemStack);
        player.updateInventory();
    }
    
    public void giveItemCursor(final Player player, final String itemName, final int cursor) {
        player.getInventory().setItem(cursor, this.getItem(itemName));
        player.updateInventory();
    }
    
    public void giveItemCursor(final Player player, final String itemName, final int cursor, final int amount) {
        final ItemStack itemStack = this.getItem(itemName);
        itemStack.setAmount(amount);
        player.getInventory().setItem(cursor, itemStack);
        player.updateInventory();
    }
    
    public void setHelmet(final Player player, final String itemName) {
        player.getInventory().setHelmet(this.getItem(itemName));
        player.updateInventory();
    }
    
    public void setChestplate(final Player player, final String itemName) {
        player.getInventory().setChestplate(this.getItem(itemName));
        player.updateInventory();
    }
    
    public void setLeggings(final Player player, final String itemName) {
        player.getInventory().setLeggings(this.getItem(itemName));
        player.updateInventory();
    }
    
    public void setBoots(final Player player, final String itemName) {
        player.getInventory().setBoots(this.getItem(itemName));
        player.updateInventory();
    }
    
    public void applyKit(final Player player, final String kitName) {
        final PlayerInventory inventory = player.getInventory();
        for (int i = 0; i < inventory.getSize(); ++i) {
            inventory.setItem(i, this.getItem(kitName, i));
        }
        player.updateInventory();
    }
    
    public void saveInv(final String invName, final List<ItemStack> itemStacks) {
        this.inventoryList.put(invName, itemStacks);
    }
    
    public void loadInv(final Player player, final String invName) {
        player.updateInventory();
        final List<ItemStack> itemStacks = this.inventoryList.get(invName);
        if (itemStacks == null || itemStacks.size() == 0) {
            return;
        }
        final PlayerInventory inventory = player.getInventory();
        for (int i = 0; i < inventory.getSize(); ++i) {
            inventory.setItem(i, itemStacks.get(i));
        }
        player.updateInventory();
    }
    
    public boolean isExistingTitle(final String title) {
        return this.getInventoryTitleList().contains(title);
    }
    
    public String getInvName(final String title) {
        for (final String invName : this.getInventoryList()) {
            if (this.getInventoryTitle(invName).equals(title)) {
                return invName;
            }
        }
        return null;
    }
    
    public String replaceMath(final String string) {
        final String area = getSubFunc(string, "{math(");
        if (area == null) {
            return string;
        }
        final List<String> stringList = new ArrayList<String>(Arrays.asList(area.split(", ")));
        final String data = stringList.get(0);
        String result = "none";
        if (stringList.size() == 3) {
            final double big1 = Double.parseDouble(stringList.get(1));
            final double big2 = Double.parseDouble(stringList.get(2));
            final String s = data;
            switch (s) {
                case "add": {
                    result = String.valueOf(big1 + big2);
                    break;
                }
                case "subtract": {
                    result = String.valueOf(big1 - big2);
                    break;
                }
                case "multiply": {
                    result = String.valueOf(big1 * big2);
                    break;
                }
                case "divide": {
                    result = String.valueOf(big1 / big2);
                    break;
                }
                case "remainder": {
                    result = String.valueOf(big1 % big2);
                    break;
                }
            }
        }
        return string.replace("{math(" + area + ")}", result);
    }
    
    public String replaceMiniGameData(final String string) {
        final String area = getSubFunc(string, "{miniGameData(");
        if (area == null) {
            return string;
        }
        final List<String> stringList = new ArrayList<String>(Arrays.asList(area.split(", ")));
        final String data = stringList.get(0);
        final MiniGame miniGame = FreedyMinigameMaker.miniGames.get(data);
        String result = "none";
        final String s = stringList.get(1);
        switch (s) {
            case "isPlaying": {
                result = String.valueOf(miniGame.isPlaying);
                return string.replace("{miniGameData(" + area + ")}", result);
            }
            case "playerList": {
                final List<String> pList = new ArrayList<String>();
                if (miniGame.playerList.size() != 0) {
                    for (int i = 0; i < miniGame.playerList.size(); ++i) {
                        pList.add(miniGame.playerList.get(i).getName());
                    }
                    result = pList.toString().substring(1, pList.toString().length() - 1);
                    return string.replace("{miniGameData(" + area + ")}", result);
                }
                break;
            }
        }
        result = miniGame.getCustomData(stringList.get(1));
        return string.replace("{miniGameData(" + area + ")}", result);
    }
    
    public String replaceHasPerm(final String string, final Player player) {
        final String area = getSubFunc(string, "{hasPerm(");
        if (area == null) {
            return string;
        }
        final String result = String.valueOf(player.hasPermission(area));
        return string.replace("{hasPerm(" + area + ")}", result);
    }
    
    public String replaceTargetBlock(final String string, final Player player) {
        final String area = getSubFunc(string, "{playerTargetBlock(");
        if (area == null) {
            return string;
        }
        final List<String> stringList = new ArrayList<String>(Arrays.asList(area.split(", ")));
        final int data = Integer.parseInt(stringList.get(0));
        final Block block = player.getTargetBlock(null, data);
        String result;
        if (block != null) {
            final Location loc = block.getLocation();
            result = loc.getWorld().getName() + ", " + loc.getX() + ", " + loc.getY() + ", " + loc.getZ();
        }
        else {
            result = "none";
        }
        return string.replace("{playerTargetBlock(" + area + ")}", result);
    }
    
    public String replaceTargetEntity(final String string, final Player player) {
        final String area = getSubFunc(string, "{playerTargetEntity(");
        if (area == null) {
            return string;
        }
        final Location eye = player.getEyeLocation();
        final List<String> stringList = new ArrayList<String>(Arrays.asList(area.split(", ")));
        final int data = Integer.parseInt(stringList.get(0));
        for (final Entity entity : player.getNearbyEntities(data, data, data)) {
            if (entity instanceof LivingEntity) {
                final LivingEntity livingEntity = (LivingEntity)entity;
                final Vector toEntity = livingEntity.getEyeLocation().toVector().subtract(eye.toVector());
                final double dot = toEntity.normalize().dot(eye.getDirection());
                if (dot > 0.99) {
                    return string.replace("{playerTargetEntity(" + area + ")}", livingEntity.getUniqueId().toString());
                }
                continue;
            }
        }
        return "none";
    }
    
    public String replaceItemType(final String string, final Player player) {
        final String area = getSubFunc(string, "{itemType(");
        if (area == null) {
            return string;
        }
        final List<String> stringList = new ArrayList<String>(Arrays.asList(area.split(", ")));
        final int data = Integer.parseInt(stringList.get(0));
        final ItemStack itemStack = player.getInventory().getItem(data);
        String result;
        if (itemStack != null) {
            result = itemStack.getType().name();
        }
        else {
            result = "none";
        }
        return string.replace("{itemType(" + area + ")}", result);
    }
    
    public String replaceItemAmount(final String string, final Player player) {
        final String area = getSubFunc(string, "{itemAmount(");
        if (area == null) {
            return string;
        }
        final List<String> stringList = new ArrayList<String>(Arrays.asList(area.split(", ")));
        final int data = Integer.parseInt(stringList.get(0));
        final ItemStack itemStack = player.getInventory().getItem(data);
        String result;
        if (itemStack != null) {
            result = String.valueOf(itemStack.getAmount());
        }
        else {
            result = "none";
        }
        return string.replace("{itemAmount(" + area + ")}", result);
    }
    
    public String replaceItemName(final String string, final Player player) {
        final String area = getSubFunc(string, "{itemName(");
        if (area == null) {
            return string;
        }
        final List<String> stringList = new ArrayList<String>(Arrays.asList(area.split(", ")));
        final int data = Integer.parseInt(stringList.get(0));
        final ItemStack itemStack = player.getInventory().getItem(data);
        String result;
        if (itemStack != null && itemStack.getItemMeta() != null && itemStack.getItemMeta().hasDisplayName()) {
            result = itemStack.getItemMeta().getDisplayName();
        }
        else {
            result = "none";
        }
        return string.replace("{itemName(" + area + ")}", result);
    }
    
    public String replaceHasItem(final String string, final Player player) {
        final String area = getSubFunc(string, "{hasItem(");
        if (area == null) {
            return string;
        }
        final List<String> stringList = new ArrayList<String>(Arrays.asList(area.split(", ")));
        final String data = stringList.get(0);
        final String result = String.valueOf(player.getInventory().first(Material.valueOf(data)));
        return string.replace("{hasItem(" + area + ")}", result);
    }
    
    public String replaceColor(final String string) {
        final String area = getSubFunc(string, "{color(");
        if (area == null) {
            return string;
        }
        final String result = ChatColor.translateAlternateColorCodes('&', area);
        return string.replace("{color(" + area + ")}", result);
    }
    
    public String replaceNumeric(final String string) {
        final String area = getSubFunc(string, "{numeric(");
        if (area == null) {
            return string;
        }
        String result;
        try {
            Double.parseDouble(area);
            result = "true";
        }
        catch (NumberFormatException e) {
            result = "false";
        }
        return string.replace("{numeric(" + area + ")}", result);
    }
    
    public String replaceContain(final String string) {
        final String area = getSubFunc(string, "{contain(");
        if (area == null) {
            return string;
        }
        final List<String> stringList = new ArrayList<String>(Arrays.asList(area.split(", ")));
        final String data = stringList.get(0);
        stringList.remove(0);
        if (stringList.get(0).equals("none")) {
            stringList.remove(0);
        }
        String result = "false";
        if (stringList.contains(data)) {
            result = "true";
        }
        return string.replace("{contain(" + area + ")}", result);
    }
    
    public String replaceSoftContain(final String string) {
        final String area = getSubFunc(string, "{softContain(");
        if (area == null) {
            return string;
        }
        final List<String> stringList = new ArrayList<String>(Arrays.asList(area.split(", ")));
        final String data = stringList.get(0);
        stringList.remove(0);
        if (stringList.get(0).equals("none")) {
            stringList.remove(0);
        }
        final String message2 = String.join(", ", stringList);
        String result = "false";
        if (message2.contains(data)) {
            result = "true";
        }
        return string.replace("{softContain(" + area + ")}", result);
    }
    
    public String replaceSub(final String string) {
        final String area = getSubFunc(string, "{sub(");
        if (area == null) {
            return string;
        }
        final List<String> stringList = new ArrayList<String>(Arrays.asList(area.split(", ")));
        final int open = Integer.parseInt(stringList.get(0));
        final int close = Integer.parseInt(stringList.get(1));
        stringList.remove(0);
        stringList.remove(0);
        final String result = stringList.toString().substring(1, stringList.toString().length() - 1);
        return string.replace("{sub(" + area + ")}", result.substring(open, close));
    }
    
    public String replaceTopLoc(final String string) {
        final String area = getSubFunc(string, "{topLoc(");
        if (area == null) {
            return string;
        }
        final List<String> stringList = new ArrayList<String>(Arrays.asList(area.split(", ")));
        final World world = Bukkit.getWorld(stringList.get(0));
        final int x = Integer.parseInt(stringList.get(1));
        final int z = Integer.parseInt(stringList.get(3));
        final int y = world.getHighestBlockYAt(x, z);
        final String result = world.getName() + ", " + x + ", " + y + ", " + z;
        return string.replace("{topLoc(" + area + ")}", result);
    }
    
    public String replaceEntityLoc(final String string) {
        final String area = getSubFunc(string, "{entityLoc(");
        if (area == null) {
            return string;
        }
        final List<String> stringList = new ArrayList<String>(Arrays.asList(area.split(", ")));
        final Entity entity = Bukkit.getEntity(UUID.fromString(stringList.get(0)));
        final World world = entity.getWorld();
        final double x = entity.getLocation().getX();
        final double y = entity.getLocation().getY();
        final double z = entity.getLocation().getZ();
        final String result = world.getName() + ", " + x + ", " + y + ", " + z;
        return string.replace("{entityLoc(" + area + ")}", result);
    }
    
    public String replaceEntityName(final String string) {
        final String area = getSubFunc(string, "{entityName(");
        if (area == null) {
            return string;
        }
        final List<String> stringList = new ArrayList<String>(Arrays.asList(area.split(", ")));
        final Entity entity = Bukkit.getEntity(UUID.fromString(stringList.get(0)));
        final String result = entity.getName();
        return string.replace("{entityName(" + area + ")}", result);
    }
    
    public String replaceEntityType(final String string) {
        final String area = getSubFunc(string, "{entityType(");
        if (area == null) {
            return string;
        }
        final List<String> stringList = new ArrayList<String>(Arrays.asList(area.split(", ")));
        final Entity entity = Bukkit.getEntity(UUID.fromString(stringList.get(0)));
        final String result = entity.getType().name();
        return string.replace("{entityType(" + area + ")}", result);
    }
    
    public String replaceBlockName(final String string) {
        final String area = getSubFunc(string, "{blockName(");
        if (area == null) {
            return string;
        }
        final List<String> stringList = new ArrayList<String>(Arrays.asList(area.split(", ")));
        final World world = Bukkit.getWorld(stringList.get(0));
        final double x = Double.parseDouble(stringList.get(1));
        final double y = Double.parseDouble(stringList.get(2));
        final double z = Double.parseDouble(stringList.get(3));
        final Location location = new Location(world, x, y, z);
        final BlockState blockState = location.getBlock().getState();
        blockState.getData();
        final String result = blockState.getType().name();
        return string.replace("{blockName(" + area + ")}", result);
    }
    
    public String replaceInteger(final String string) {
        final String area = getSubFunc(string, "{integer(");
        if (area == null) {
            return string;
        }
        final String result = String.valueOf(BigDecimal.valueOf(Double.parseDouble(area)).intValue());
        return string.replace("{integer(" + area + ")}", result);
    }
    
    public String replaceCalc(final String string) {
        System.out.println("removed syntax, calc");
        return string;
    }
    
    public String replaceRound(final String string) {
        final String area = getSubFunc(string, "{round(");
        if (area == null) {
            return string;
        }
        final String result = String.valueOf(FastMath.round(Double.parseDouble(area)));
        return string.replace("{round(" + area + ")}", result);
    }
    
    public String replaceAbs(final String string) {
        final String area = getSubFunc(string, "{abs(");
        if (area == null) {
            return string;
        }
        final String result = String.valueOf(FastMath.abs(Double.parseDouble(area)));
        return string.replace("{abs(" + area + ")}", result);
    }
    
    public String replaceCos(final String string) {
        final String area = getSubFunc(string, "{cos(");
        if (area == null) {
            return string;
        }
        final String result = String.valueOf(FastMath.cos(Double.parseDouble(area)));
        return string.replace("{cos(" + area + ")}", result);
    }
    
    public String replaceSin(final String string) {
        final String area = getSubFunc(string, "{sin(");
        if (area == null) {
            return string;
        }
        final String result = String.valueOf(FastMath.sin(Double.parseDouble(area)));
        return string.replace("{sin(" + area + ")}", result);
    }
    
    public String replaceTan(final String string) {
        final String area = getSubFunc(string, "{tan(");
        if (area == null) {
            return string;
        }
        final String result = String.valueOf(FastMath.sin(Double.parseDouble(area)));
        return string.replace("{tan(" + area + ")}", result);
    }
    
    public String replaceRoundUp(final String string) {
        final String area = getSubFunc(string, "{roundUp(");
        if (area == null) {
            return string;
        }
        final String result = String.valueOf(FastMath.ceil(Double.parseDouble(area)));
        return string.replace("{roundUp(" + area + ")}", result);
    }
    
    public String replaceRoundDown(final String string) {
        final String area = getSubFunc(string, "{roundDown(");
        if (area == null) {
            return string;
        }
        final String result = String.valueOf(FastMath.floor(Double.parseDouble(area)));
        return string.replace("{roundDown(" + area + ")}", result);
    }
    
    public String replaceReplace(final String string) {
        final String area = getSubFunc(string, "{replace(");
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
    
    public String replaceSoftReplace(final String string) {
        final String area = getSubFunc(string, "{softReplace(");
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
        String result = stringList.toString().substring(1, stringList.toString().length() - 1);
        result = result.replace(oldChar, newChar);
        return string.replace("{softReplace(" + area + ")}", result);
    }
    
    public String replaceShuffle(final String string) {
        final String area = getSubFunc(string, "{shuffle(");
        if (area == null) {
            return string;
        }
        final List<String> stringList = new ArrayList<String>(Arrays.asList(area.split(", ")));
        Collections.shuffle(stringList);
        final String result = stringList.toString().substring(1, stringList.toString().length() - 1);
        return string.replace("{shuffle(" + area + ")}", result);
    }
    
    public String replacePlayerList(final String string) {
        final String area = getSubFunc(string, "{playerList(");
        if (area == null) {
            return string;
        }
        final List<String> playerNameList = new ArrayList<String>();
        final String s = area;
        switch (s) {
            case "default": {
                for (final Player p : this.playerList) {
                    playerNameList.add(p.getName());
                }
                break;
            }
            case "all": {
                for (final Player p : Bukkit.getOnlinePlayers()) {
                    playerNameList.add(p.getName());
                }
                break;
            }
            default: {
                for (final Player p : this.playerList) {
                    if (this.getPlayerData(p).getCustomData(area).equals("true")) {
                        playerNameList.add(p.getName());
                    }
                }
                break;
            }
        }
        final String result = playerNameList.toString().substring(1, playerNameList.toString().length() - 1);
        return string.replace("{playerList(" + area + ")}", result);
    }
    
    public String replaceRandom(final String string) {
        final String area = getSubFunc(string, "{random(");
        if (area == null) {
            return string;
        }
        final List<String> stringList = new ArrayList<String>(Arrays.asList(area.split(", ")));
        final String result = stringList.get(ThreadLocalRandom.current().nextInt(0, stringList.size()));
        return string.replace("{random(" + area + ")}", result);
    }
    
    public String replaceHighList(final String string) {
        final String area = getSubFunc(string, "{highList(");
        if (area == null) {
            return string;
        }
        final List<String> stringList = new ArrayList<String>(Arrays.asList(area.split(", ")));
        Collections.sort(stringList);
        final String result = stringList.toString().substring(1, stringList.toString().length() - 1);
        return string.replace("{highList(" + area + ")}", result);
    }
    
    public String replaceFlip(final String string) {
        final String area = getSubFunc(string, "{flip(");
        if (area == null) {
            return string;
        }
        final List<String> stringList = new ArrayList<String>(Arrays.asList(area.split(", ")));
        Collections.reverse(stringList);
        final String result = stringList.toString().substring(1, stringList.toString().length() - 1);
        return string.replace("{flip(" + area + ")}", result);
    }
    
    public String replaceLength(final String string) {
        final String area = getSubFunc(string, "{length(");
        if (area == null) {
            return string;
        }
        final String result = String.valueOf(area.length());
        return string.replace("{length(" + area + ")}", result);
    }
    
    public String replaceIndexOf(final String string) {
        final String area = getSubFunc(string, "{indexOf(");
        if (area == null) {
            return string;
        }
        final List<String> stringList = new ArrayList<String>(Arrays.asList(area.split(", ")));
        final int index = Integer.parseInt(stringList.get(0));
        stringList.remove(0);
        final String result = stringList.get(index);
        return string.replace("{indexOf(" + area + ")}", result);
    }
    
    public String replaceValueOf(final String string) {
        final String area = getSubFunc(string, "{valueOf(");
        if (area == null) {
            return string;
        }
        final List<String> stringList = new ArrayList<String>(Arrays.asList(area.split(", ")));
        final String value = stringList.get(0);
        stringList.remove(0);
        final int index = stringList.indexOf(value);
        return string.replace("{valueOf(" + area + ")}", String.valueOf(index));
    }
    
    public String replaceSizeOf(final String string) {
        final String area = getSubFunc(string, "{sizeOf(");
        if (area == null) {
            return string;
        }
        final List<String> stringList = new ArrayList<String>(Arrays.asList(area.split(", ")));
        final int size = stringList.size();
        return string.replace("{sizeOf(" + area + ")}", String.valueOf(size));
    }
    
    public String replaceHighestNumber(final String string) {
        final String area = getSubFunc(string, "{highestNumber(");
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
        final String area = getSubFunc(string, "{lowestNumber(");
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
        final String area = getSubFunc(string, "{randomNumber(");
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
        final String area = getSubFunc(string, "{data(");
        String result = this.getCustomData(area);
        if (result == null) {
            result = "none";
        }
        return string.replace("{data(" + area + ")}", result);
    }
    
    public String replaceRemove(final String string) {
        final String area = getSubFunc(string, "{remove(");
        if (area == null) {
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
        final String area = getSubFunc(string, "{add(");
        if (area == null) {
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
        final String area = getSubFunc(string, "{file(");
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
        final String area = getSubFunc(string, "{constant(");
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
        final String area = getSubFunc(string, "{date(");
        if (area == null) {
            return string;
        }
        final DateFormat dateFormat = new SimpleDateFormat(area);
        final Calendar cal = Calendar.getInstance();
        final String result = dateFormat.format(cal.getTime());
        return string.replace("{date(" + area + ")}", result);
    }
    
    public String replaceGameData(final String string) {
        return string.replace("{maxPlayers}", String.valueOf(this.getMaxStartPlayers())).replace("{randomPlayer}", string.contains("{randomPlayer}") ? ((this.playerList.size() == 1) ? this.playerList.get(0).getName() : (this.playerList.isEmpty() ? "none" : this.playerList.get(ThreadLocalRandom.current().nextInt(0, this.playerList.size() - 1)).getName())) : "none").replace("{playerAmount}", String.valueOf(this.playerList.size())).replace("{playerSize}", String.valueOf(this.playerList.size())).replace("{isPlaying}", String.valueOf(this.isPlaying)).replace("{isWaiting}", String.valueOf(this.isWaiting)).replace("{game}", this.gameName).replace("{gameType}", (this.getGameType() == null) ? "none" : this.getGameType()).replace("{randomNumber}", string.contains("{randomNumber}") ? String.valueOf(FastMath.random()) : "none").replace("{gameName}", this.gameName);
    }
    
    public static String getSubFunc(final String string, final String func) {
        final int index = StringUtils.lastIndexOfIgnoreCase(string, func);
        if (index == -1) {
            return null;
        }
        final String sum = string.substring(index);
        final int lastIndex = sum.indexOf(")}");
        if (lastIndex == -1) {
            return null;
        }
        return sum.substring(func.length(), lastIndex);
    }
    
    public String replaceCalcAll(String string, final Player player) {
        final String[] strings = StringUtils.substringsBetween(string, "{", "(");
        if (strings == null) {
            return string;
        }
        final List<String> areas = Arrays.asList(strings);
        Collections.reverse(areas);
        for (final String s2 : areas) {
            final String s = s2;
            switch (s2) {
                case "math": {
                    string = this.replaceMath(string);
                    continue;
                }
                case "miniGameData": {
                    string = this.replaceMiniGameData(string);
                    continue;
                }
                case "color": {
                    string = this.replaceColor(string);
                    continue;
                }
                case "numeric": {
                    string = this.replaceNumeric(string);
                    continue;
                }
                case "entityLoc": {
                    string = this.replaceEntityLoc(string);
                    continue;
                }
                case "entityName": {
                    string = this.replaceEntityName(string);
                    continue;
                }
                case "entityType": {
                    string = this.replaceEntityType(string);
                    continue;
                }
                case "topLoc": {
                    string = this.replaceTopLoc(string);
                    continue;
                }
                case "softContain": {
                    string = this.replaceSoftContain(string);
                    continue;
                }
                case "contain": {
                    string = this.replaceContain(string);
                    continue;
                }
                case "sub": {
                    string = this.replaceSub(string);
                    continue;
                }
                case "blockName": {
                    string = this.replaceBlockName(string);
                    continue;
                }
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
                case "hasPerm": {
                    if (player != null) {
                        string = this.replaceHasPerm(string, player);
                        continue;
                    }
                    continue;
                }
                case "playerData": {
                    if (player != null) {
                        string = this.getPlayerData(player).replaceData(string);
                        continue;
                    }
                    continue;
                }
                case "playerTargetBlock": {
                    if (player != null) {
                        string = this.replaceTargetBlock(string, player);
                        continue;
                    }
                    continue;
                }
                case "playerTargetEntity": {
                    if (player != null) {
                        string = this.replaceTargetEntity(string, player);
                        continue;
                    }
                    continue;
                }
                case "itemType": {
                    if (player != null) {
                        string = this.replaceItemType(string, player);
                        continue;
                    }
                    continue;
                }
                case "itemAmount": {
                    if (player != null) {
                        string = this.replaceItemAmount(string, player);
                        continue;
                    }
                    continue;
                }
                case "itemName": {
                    if (player != null) {
                        string = this.replaceItemName(string, player);
                        continue;
                    }
                    continue;
                }
                case "hasItem": {
                    if (player != null) {
                        string = this.replaceHasItem(string, player);
                        continue;
                    }
                    continue;
                }
                case "replace": {
                    string = this.replaceReplace(string);
                    continue;
                }
                case "softReplace": {
                    string = this.replaceSoftReplace(string);
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
                case "abs": {
                    string = this.replaceAbs(string);
                    continue;
                }
                case "cos": {
                    string = this.replaceCos(string);
                    continue;
                }
                case "sin": {
                    string = this.replaceSin(string);
                    continue;
                }
                case "tan": {
                    string = this.replaceTan(string);
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
            string = string.replace("{playerIsOp}", String.valueOf(player.isOp())).replace("{playerUuid}", player.getUniqueId().toString()).replace("{x}", String.valueOf(player.getLocation().getX())).replace("{y}", String.valueOf(player.getLocation().getY())).replace("{z}", String.valueOf(player.getLocation().getZ())).replace("{footX}", String.valueOf(player.getLocation().getX())).replace("{footY}", String.valueOf(player.getLocation().getY())).replace("{footZ}", String.valueOf(player.getLocation().getZ())).replace("{eyeX}", String.valueOf(player.getEyeLocation().getX())).replace("{eyeY}", String.valueOf(player.getEyeLocation().getY())).replace("{eyeZ}", String.valueOf(player.getEyeLocation().getZ())).replace("{yaw}", String.valueOf(player.getLocation().getYaw())).replace("{pitch}", String.valueOf(player.getLocation().getPitch())).replace("{world}", player.getLocation().getWorld().getName()).replace("{allPlayer}", player.getName()).replace("{onlinePlayer}", player.getName()).replace("{playerName}", player.getName()).replace("{playerCursor}", String.valueOf(player.getInventory().getHeldItemSlot())).replace("{playerIndex}", String.valueOf(this.playerList.indexOf(player))).replace("{playerHealth}", String.valueOf(player.getHealth())).replace("{playerGameMode}", player.getGameMode().name()).replace("{playerIsBlocking}", string.contains("{playerIsBlocking}") ? String.valueOf(player.isHandRaised()) : "none").replace("{playerIsLeashed}", string.contains("{playerIsLeashed}") ? String.valueOf(player.isLeashed()) : "none").replace("{playerIsSneaking}", string.contains("{playerIsSneaking}") ? String.valueOf(player.isSneaking()) : "none").replace("{playerIsGliding}", string.contains("{playerIsGliding}") ? String.valueOf(player.isGliding()) : "none").replace("{playerIsSwimming}", string.contains("{playerIsSwimming}") ? String.valueOf(player.isSwimming()) : "none").replace("{playerIsSleeping}", string.contains("{playerIsSleeping}") ? String.valueOf(player.isSleeping()) : "none").replace("{playerFood}", string.contains("{playerFood}") ? String.valueOf(player.getFoodLevel()) : "none").replace("{player}", player.getName()).replace("{playerExp}", string.contains("{playerExp}") ? String.valueOf(player.getTotalExperience()) : "none");
        }
        else if (this.playerList.isEmpty()) {
            string = string.replace("{allPlayer}", "none").replace("{playerName}", "none").replace("{playerIndex}", "none").replace("{player}", "none");
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
    
    public void executeCommands(final FreedyCommandSender sender, final List<String> commands, final Player player) {
        String issueCmd = "none";
        try {
            for (final String cmd : commands) {
                if (cmd.contains("{allPlayer}")) {
                    final Iterator<String> iterator2 = this.replaceAllPlayer(cmd, this.playerList).iterator();
                    while (iterator2.hasNext()) {
                        final String playerCmd = issueCmd = iterator2.next();
                        Bukkit.getServer().dispatchCommand(sender, playerCmd);
                    }
                }
                else if (cmd.contains("{onlinePlayer}")) {
                    final Iterator<String> iterator3 = this.replaceAllPlayer(cmd, new ArrayList<Player>(Bukkit.getOnlinePlayers())).iterator();
                    while (iterator3.hasNext()) {
                        final String playerCmd = issueCmd = iterator3.next();
                        Bukkit.getServer().dispatchCommand(sender, playerCmd);
                    }
                }
                else {
                    issueCmd = cmd;
                    Bukkit.getServer().dispatchCommand(sender, this.replaceAll(cmd, player));
                }
            }
        }
        catch (CommandException e) {
            System.out.println("§6issue cause: ");
            e.printStackTrace();
            System.out.println("§6issue game: " + this.gameName);
            System.out.println("§6issue line: " + issueCmd);
        }
    }
    
    public String executeCommand(final FreedyCommandSender sender, final String command, final Player player) {
        String cmd = "none";
        try {
            if (command.contains("{allPlayer}")) {
                final Iterator<String> iterator = this.replaceAllPlayer(command, this.playerList).iterator();
                while (iterator.hasNext()) {
                    final String playerCmd = cmd = iterator.next();
                    Bukkit.getServer().dispatchCommand(sender, playerCmd);
                }
            }
            else if (command.contains("{onlinePlayer}")) {
                final Iterator<String> iterator2 = this.replaceAllPlayer(command, new ArrayList<Player>(Bukkit.getOnlinePlayers())).iterator();
                while (iterator2.hasNext()) {
                    final String playerCmd = cmd = iterator2.next();
                    Bukkit.getServer().dispatchCommand(sender, playerCmd);
                }
            }
            else {
                cmd = command;
                Bukkit.getServer().dispatchCommand(sender, this.replaceAll(command, player));
            }
            return sender.output;
        }
        catch (CommandException e) {
            System.out.println("§6issue cause: ");
            e.printStackTrace();
            System.out.println("§6issue game: " + this.gameName);
            System.out.println("§6issue line: " + cmd);
            return "false";
        }
    }
    
    public String executeEventCommands(final String command, final Player player) {
        String cmd = "none";
        final FreedyCommandSender sender = new FreedyCommandSender();
        try {
            if (command.contains("{allPlayer}")) {
                final Iterator<String> iterator = this.replaceAllPlayer(command, this.playerList).iterator();
                while (iterator.hasNext()) {
                    final String playerCmd = cmd = iterator.next();
                    Bukkit.getServer().dispatchCommand(sender, playerCmd);
                }
            }
            else if (command.contains("{onlinePlayer}")) {
                final Iterator<String> iterator2 = this.replaceAllPlayer(command, new ArrayList<Player>(Bukkit.getOnlinePlayers())).iterator();
                while (iterator2.hasNext()) {
                    final String playerCmd = cmd = iterator2.next();
                    Bukkit.getServer().dispatchCommand(sender, playerCmd);
                }
            }
            else {
                cmd = command;
                Bukkit.getServer().dispatchCommand(sender, this.replaceAll(command, player));
            }
            return sender.output;
        }
        catch (CommandException e) {
            System.out.println("§6issue cause: ");
            e.printStackTrace();
            System.out.println("§6issue game: " + this.gameName);
            System.out.println("§6issue line: " + cmd);
            return "false";
        }
    }
}
