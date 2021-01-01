package freedy.freedyminigamemaker;

import org.bukkit.*;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class DataStore extends DataEditor
{
    public DataStore(final FreedyMinigameMaker plugin, final String gameName) {
        super(plugin, gameName);
    }
    
    public int getMaxPlayers() {
        return this.plugin.getConfig().getInt(this.gamePath + "maxPlayers");
    }
    
    public int getMaxStartPlayers() {
        return this.plugin.getConfig().getInt(this.gamePath + "maxStartPlayers");
    }
    
    public int getWaitForStartTime() {
        return this.plugin.getConfig().getInt(this.gamePath + "waitForStartTime");
    }

    public String getGameType() {
        final String gameType = this.plugin.getConfig().getString(this.gamePath + "gameType");
        return gameType == null ? "none" : gameType;
    }
    
    public List<String> getGameList() {
        return this.plugin.getConfig().getStringList("gameList");
    }
    
    public String getMessage(final String messagePath) {
        return this.plugin.getConfig().getString(this.gamePath + messagePath);
    }
    
    public List<String> getMessageList(final String messagePath) {
        return this.plugin.getConfig().getStringList(this.gamePath + messagePath);
    }

    public boolean getNeedClearInv() {
        return this.plugin.getConfig().getBoolean(this.gamePath + "needClearInv");
    }
    
    public List<String> getRepeatList() {
        return this.plugin.getConfig().getStringList(this.gamePath + "repeatList");
    }
    
    public int getRepeatTime(final String repeatName) {
        return this.plugin.getConfig().getInt(this.gamePath + "repeats." + repeatName + ".time");
    }
    
    public List<Integer> getRepeatTimes(final String repeatName) {
        return this.plugin.getConfig().getIntegerList(this.gamePath + "repeats." + repeatName + ".times");
    }
    
    public boolean getLocationIsExist(final String locationPath) {
        return this.plugin.getConfig().isSet(this.gamePath + locationPath);
    }
    
    public boolean getSafeWorldBoarderFinderMode() {
        return this.plugin.getConfig().getBoolean(this.gamePath + "safeWorldBoarderFinderMode");
    }
    
    public boolean getScoreBoardMode() {
        return this.plugin.getConfig().getBoolean(this.gamePath + "scoreBoardEnable");
    }
    
    public boolean getWorldBoarderMode() {
        return this.plugin.getConfig().getBoolean(this.gamePath + "worldBoarder.enable");
    }
    
    public Location getWorldBoarderLocation() {
        final World world = Bukkit.getWorld(this.plugin.getConfig().getString(this.gamePath + "worldBoarder.location.world"));
        final int x = this.plugin.getConfig().getInt(this.gamePath + "worldBoarder.location.x");
        final int y = this.plugin.getConfig().getInt(this.gamePath + "worldBoarder.location.y");
        final int z = this.plugin.getConfig().getInt(this.gamePath + "worldBoarder.location.z");
        return new Location(world, x, y, z);
    }
    
    public WorldBorder getWorldBoarder() {
        return Bukkit.getWorld(this.plugin.getConfig().getString(this.gamePath + "worldBoarder.location.world")).getWorldBorder();
    }
    
    public int getWorldBoarderSizePerPlayer() {
        return this.plugin.getConfig().getInt(this.gamePath + "worldBoarder.sizePerPlayer");
    }
    
    public double getWorldBoarderOutDamage() {
        return this.plugin.getConfig().getDouble(this.gamePath + "worldBoarder.outDamage");
    }
    
    public int getWorldBoarderMinSize() {
        return this.plugin.getConfig().getInt(this.gamePath + "worldBoarder.minSize");
    }
    
    public int getWorldBoarderSpeed() {
        return this.plugin.getConfig().getInt(this.gamePath + "worldBoarder.speed");
    }
    
    public Location getLocation(final String locationPath) {
        final String worldName = this.plugin.getConfig().getString(this.gamePath + locationPath + ".world");
        if (worldName == null) {
            return null;
        }
        final World world = Bukkit.getWorld(worldName);
        final double x = Double.parseDouble(this.plugin.getConfig().getString(this.gamePath + locationPath + ".x"));
        final double y = Double.parseDouble(this.plugin.getConfig().getString(this.gamePath + locationPath + ".y"));
        final double z = Double.parseDouble(this.plugin.getConfig().getString(this.gamePath + locationPath + ".z"));
        final float yaw = Float.parseFloat(this.plugin.getConfig().getString(this.gamePath + locationPath + ".yaw"));
        final float pitch = Float.parseFloat(this.plugin.getConfig().getString(this.gamePath + locationPath + ".pitch"));
        return new Location(world, x, y, z, yaw, pitch);
    }

    public String getInventoryTitle(final String invName) {
        return FreedyMinigameMaker.miniGames.getItems().getConfig().getString("inventories." + invName + ".title");
    }
    
    public List<String> getInventoryCmd(final String invName, final int index) {
        return FreedyMinigameMaker.miniGames.getItems().getConfig().getStringList("inventories." + invName + "." + index + "Cmd");
    }
    
    public List<String> getCmdByTitle(final String title, final int index) {
        for (final String invName : this.getInventoryList()) {
            if (this.getInventoryTitle(invName).equals(title)) {
                return this.getInventoryCmd(invName, index);
            }
        }
        return null;
    }
    
    public List<String> getInventoryList() {
        return FreedyMinigameMaker.miniGames.getItems().getConfig().getStringList("inventoryList");
    }
    
    public List<String> getInventoryTitleList() {
        final List<String> inventoryList = this.getInventoryList();
        final List<String> titleList = new ArrayList<>();
        for (final String invName : inventoryList) {
            titleList.add(FreedyMinigameMaker.miniGames.getItems().getConfig().getString("inventories." + invName + ".title"));
        }
        return titleList;
    }
    
    public Inventory getInventory(final String invName) {
        if (FreedyMinigameMaker.miniGames.getItems().getConfig().getStringList("inventoryList").contains(invName)) {
            final int size = FreedyMinigameMaker.miniGames.getItems().getConfig().getInt("inventories." + invName + ".size");
            final String title = FreedyMinigameMaker.miniGames.getItems().getConfig().getString("inventories." + invName + ".title");
            final Inventory inventory = Bukkit.createInventory(null, size, title);
            for (int i = 0; i < size; ++i) {
                final ItemStack itemStack = FreedyMinigameMaker.miniGames.getItems().getConfig().getItemStack("inventories." + invName + ".items." + i);
                if (itemStack != null) {
                    inventory.setItem(i, itemStack);
                }
            }
            return inventory;
        }
        return Bukkit.createInventory(null, 27, "This inventory does not exist");
    }
    
    public ItemStack getItem(final String kitName, final int index) {
        if (FreedyMinigameMaker.miniGames.getItems().getConfig().getStringList("kitList").contains(kitName)) {
            return FreedyMinigameMaker.miniGames.getItems().getConfig().getItemStack("kits." + kitName + ".items." + index);
        }
        return new ItemStack(Material.BEDROCK);
    }
    
    public ItemStack getItem(final String itemName) {
        if (FreedyMinigameMaker.miniGames.getItems().getConfig().getStringList("itemList").contains(itemName)) {
            return FreedyMinigameMaker.miniGames.getItems().getConfig().getItemStack("items." + itemName);
        }
        return new ItemStack(Material.BEDROCK);
    }
}
