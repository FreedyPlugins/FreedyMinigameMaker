// 
// Decompiled by Procyon v0.5.36
// 

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
        return (gameType == null) ? "none" : gameType;
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
        return MiniGames.getLocations().getConfig().isSet(this.gamePath + locationPath);
    }

    public Location getLocation(final String locationPath) {
        final String worldName = MiniGames.getLocations().getConfig().getString(this.gamePath + locationPath + ".world");
        if (worldName == null) {
            return null;
        }
        final World world = Bukkit.getWorld(worldName);
        final double x = Double.parseDouble(MiniGames.getLocations().getConfig().getString(this.gamePath + locationPath + ".x"));
        final double y = Double.parseDouble(MiniGames.getLocations().getConfig().getString(this.gamePath + locationPath + ".y"));
        final double z = Double.parseDouble(MiniGames.getLocations().getConfig().getString(this.gamePath + locationPath + ".z"));
        final float yaw = Float.parseFloat(MiniGames.getLocations().getConfig().getString(this.gamePath + locationPath + ".yaw"));
        final float pitch = Float.parseFloat(MiniGames.getLocations().getConfig().getString(this.gamePath + locationPath + ".pitch"));
        return new Location(world, x, y, z, yaw, pitch);
    }
    
    public String getInventoryTitle(final String invName) {
        return MiniGames.getItems().getConfig().getString("inventories." + invName + ".title");
    }
    
    public List<String> getInventoryCmd(final String invName, final int index) {
        return MiniGames.getItems().getConfig().getStringList("inventories." + invName + "." + index + "Cmd");
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
        return MiniGames.getItems().getConfig().getStringList("inventoryList");
    }
    
    public List<String> getInventoryTitleList() {
        final List<String> inventoryList = this.getInventoryList();
        final List<String> titleList = new ArrayList<String>();
        for (final String invName : inventoryList) {
            titleList.add(MiniGames.getItems().getConfig().getString("inventories." + invName + ".title"));
        }
        return titleList;
    }
    
    public Inventory getInventory(final String invName) {
        if (MiniGames.getItems().getConfig().getStringList("inventoryList").contains(invName)) {
            final int size = MiniGames.getItems().getConfig().getInt("inventories." + invName + ".size");
            final String title = MiniGames.getItems().getConfig().getString("inventories." + invName + ".title");
            final Inventory inventory = Bukkit.createInventory(null, size, title);
            for (int i = 0; i < size; ++i) {
                final ItemStack itemStack = MiniGames.getItems().getConfig().getItemStack("inventories." + invName + ".items." + i);
                if (itemStack != null) {
                    inventory.setItem(i, itemStack);
                }
            }
            return inventory;
        }
        return Bukkit.createInventory(null, 27, "This inventory does not exist");
    }
    
    public ItemStack getItem(final String kitName, final int index) {
        if (MiniGames.getItems().getConfig().getStringList("kitList").contains(kitName)) {
            return MiniGames.getItems().getConfig().getItemStack("kits." + kitName + ".items." + index);
        }
        return new ItemStack(Material.AIR);
    }
    
    public ItemStack getItem(final String itemName) {
        if (MiniGames.getItems().getConfig().getStringList("itemList").contains(itemName)) {
            return MiniGames.getItems().getConfig().getItemStack("items." + itemName);
        }
        return new ItemStack(Material.AIR);
    }
}
