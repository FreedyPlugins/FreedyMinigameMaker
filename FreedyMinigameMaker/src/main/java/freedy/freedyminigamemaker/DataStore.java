package freedy.freedyminigamemaker;

import org.bukkit.*;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class DataStore extends DataEditor {



    public DataStore(FreedyMinigameMaker plugin, String gameName) {
        super(plugin, gameName);
    }


    public int getMaxPlayers() {
        return plugin.getConfig().getInt(gamePath + "maxPlayers");
    }

    public int getMaxStartPlayers() {
        return plugin.getConfig().getInt(gamePath + "maxStartPlayers");
    }

    public int getWaitForStartTime() {
        return plugin.getConfig().getInt(gamePath + "waitForStartTime");
    }

    public int getWaitForEndTime() {
        return plugin.getConfig().getInt(gamePath + "waitForEndTime");
    }

    public int getTimePerPlayer() {
        return plugin.getConfig().getInt(gamePath + "timePerPlayer");
    }

    public String getGameType() {
        return plugin.getConfig().getString(gamePath + "gameType");
    }

    public GameMode getDefaultStartGameMode() {
        return GameMode.valueOf(plugin.getConfig().getString(gamePath + "defaultStartGameMode"));
    }

    public String getDefaultEndGameMode() {
        return plugin.getConfig().getString(gamePath + "defaultEndGameMode");
    }

    public double getDefaultStartMaxHeart() {
        return plugin.getConfig().getDouble(gamePath + "defaultStartMaxHeart");
    }

    public double getRedStartMaxHeart() {
        return plugin.getConfig().getDouble(gamePath + "redTeamStartMaxHeart");
    }

    public double getBlueStartMaxHeart() {
        return plugin.getConfig().getDouble(gamePath + "blueTeamStartMaxHeart");
    }

    public List<String> getGameList() {
        return plugin.getConfig().getStringList("gameList");
    }

    public String getMessage(String messagePath) {
        return plugin.getConfig().getString(gamePath + messagePath);
    }

    public List<String> getMessageList(String messagePath) {
        return plugin.getConfig().getStringList(gamePath + messagePath);
    }

    public boolean getTeamMode() {
        return plugin.getConfig().getBoolean(gamePath + "teamMode");
    }

    public boolean getStartGameMode() {
        return plugin.getConfig().getBoolean(gamePath + "startGameMode");
    }

    public boolean getQuitGameMode() {
        return plugin.getConfig().getBoolean(gamePath + "quitGameMode");
    }

    public List<String> getDropList() {
        return plugin.getConfig().getStringList(gamePath + "dropItems.dropList");
    }

    public int getDrops(String material) {
        return plugin.getConfig().getInt(gamePath + "dropItems.drop." + material);
    }

    public int getDropRate() {
        return plugin.getConfig().getInt(gamePath + "dropRate");
    }

    public boolean getNeedClearInv() {
        return plugin.getConfig().getBoolean(gamePath + "needClearInv");
    }

    public int getRepeatTime() {
        return plugin.getConfig().getInt(gamePath + "repeatTime");
    }

    public double getExtraDamage() {
        return plugin.getConfig().getDouble(gamePath + "extraDamage");
    }

    public int getDamageRate() {
        return plugin.getConfig().getInt(gamePath + "damageRate");
    }

    public double getResistingDamage() {
        return plugin.getConfig().getDouble(gamePath + "resistingDamage");
    }

    public int getResistingRate() {
        return plugin.getConfig().getInt(gamePath + "resistingRate");
    }


    public boolean getLocationIsExist(String locationPath) {
        return plugin.getConfig().getBoolean(gamePath + locationPath + "isExist");
    }

    public boolean getScoreBoardMode() {
        return plugin.getConfig().getBoolean(gamePath + "scoreBoardEnable");
    }

    public boolean getWorldBoarderMode() {
        return plugin.getConfig().getBoolean(gamePath + "worldBoarder.enable");
    }

    public Location getWorldBoarderLocation() {
        World world = Bukkit.getWorld(plugin.getConfig().getString(gamePath + "worldBoarder.location.world"));
        int x = plugin.getConfig().getInt(gamePath + "worldBoarder.location.x");
        int y = plugin.getConfig().getInt(gamePath + "worldBoarder.location.y");
        int z = plugin.getConfig().getInt(gamePath + "worldBoarder.location.z");
        return new Location(world, x, y, z);
    }

    public WorldBorder getWorldBoarder() {
        return Bukkit.getWorld(plugin.getConfig().getString(gamePath + "worldBoarder.location.world")).getWorldBorder();
    }

    public int getWorldBoarderSizePerPlayer() {
        return plugin.getConfig().getInt(gamePath + "worldBoarder.sizePerPlayer");
    }

    public double getWorldBoarderOutDamage() {
        return plugin.getConfig().getDouble(gamePath + "worldBoarder.outDamage");
    }

    public int getWorldBoarderMinSize() {
        return plugin.getConfig().getInt(gamePath + "worldBoarder.minSize");
    }

    public int getWorldBoarderSpeed() {
        return plugin.getConfig().getInt(gamePath + "worldBoarder.speed");
    }

    public Location getLocation(String locationPath) {
        World world = Bukkit.getWorld(plugin.getConfig().getString(gamePath + "." + locationPath + ".world"));
        double x = Double.parseDouble(plugin.getConfig().getString(gamePath + "." + locationPath + ".x"));
        double y = Double.parseDouble(plugin.getConfig().getString(gamePath + "." + locationPath + ".y"));
        double z = Double.parseDouble(plugin.getConfig().getString(gamePath + "." + locationPath + ".z"));
        float yaw = Float.parseFloat(plugin.getConfig().getString(gamePath + "." + locationPath + ".yaw"));
        float pitch = Float.parseFloat(plugin.getConfig().getString(gamePath + "." + locationPath + ".pitch"));
        return new Location(world, x, y, z, yaw, pitch);
    }

    public Location getLocation(String locationPath, int locationNumber) {
        World world = Bukkit.getWorld(plugin.getConfig().getString(gamePath + locationPath + "." + locationNumber + ".world"));
        double x = Double.parseDouble(plugin.getConfig().getString(gamePath + locationPath + "." + locationNumber + ".x"));
        double y = Double.parseDouble(plugin.getConfig().getString(gamePath + locationPath + "." + locationNumber + ".y"));
        double z = Double.parseDouble(plugin.getConfig().getString(gamePath + locationPath + "." + locationNumber + ".z"));
        float yaw = Float.parseFloat(plugin.getConfig().getString(gamePath + locationPath + "." + locationNumber + ".yaw"));
        float pitch = Float.parseFloat(plugin.getConfig().getString(gamePath + locationPath + "." + locationNumber + ".pitch"));
        return new Location(world, x, y, z, yaw, pitch);
    }

    public int getLocationAmount(String locationPath) {
        return plugin.getConfig().getInt(gamePath + locationPath + "Amount");
    }

    public List<Location> getLocationList(String locationPath) {
        List<Location> locationList = new ArrayList<>();
        for (int i = 1; i < getLocationAmount(locationPath) + 1; i++) {
            locationList.add(getLocation(locationPath, i));
        }
        return locationList;
    }

    public String getInventoryTitle(String invName) {
        return plugin.getConfig().getString(gamePath + "inventories." + invName + ".title");
    }

    public List<String> getInventoryCmd(String invName, int index) {
        return plugin.getConfig().getStringList(gamePath + "inventories." + invName + "." + index + "Cmd");
    }

    public List<String> getCmdByTitle(String title, int index) {
        for (String invName : getInventoryList()) {
            if (getInventoryTitle(invName).equals(title))
                return getInventoryCmd(invName, index);
        }
        return null;
    }

    public List<String> getInventoryList() {
        return plugin.getConfig().getStringList(gamePath + "inventoryList");
    }

    public List<String> getInventoryTitleList() {
        List<String> inventoryList = getInventoryList();
        List<String> titleList = new ArrayList<>();
        for (String invName : inventoryList)
            titleList.add(plugin.getConfig().getString(gamePath + "inventories." + invName + ".title"));
        return titleList;
    }


    public Inventory getInventory(String invName) {
        if (plugin.getConfig().getStringList(gamePath + "inventoryList").contains(invName)) {
            int size = plugin.getConfig().getInt(gamePath + "inventories." + invName + ".size");
            String title = plugin.getConfig().getString(gamePath + "inventories." + invName + ".title");
            Inventory inventory = Bukkit.createInventory(null, size, title);
            for (int i = 0; i < size; i++) {
                ItemStack itemStack = plugin.getConfig().getItemStack(gamePath + "inventories." + invName + ".items." + i);
                if (itemStack != null) inventory.setItem(i, itemStack);
            }
            return inventory;
        } else return Bukkit.createInventory(null, 27, "이 인벤토리는 존재하지 않음");
    }



}
