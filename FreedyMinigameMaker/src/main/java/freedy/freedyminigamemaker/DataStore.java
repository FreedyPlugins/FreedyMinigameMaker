package freedy.freedyminigamemaker;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.World;

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
        return plugin.getConfig().getDouble(gamePath + "redStartMaxHeart");
    }

    public double getBlueStartMaxHeart() {
        return plugin.getConfig().getDouble(gamePath + "blueStartMaxHeart");
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





}
