package freedy.freedyminigamemaker;

import freedy.freedyminigamemaker.commands.*;
import freedy.freedyminigamemaker.events.*;
import org.bukkit.plugin.java.JavaPlugin;

public final class FreedyMinigameMaker extends JavaPlugin {

    public MiniGames miniGames;

    @Override
    public void onEnable() {
        getConfig().options().copyDefaults();
        saveDefaultConfig();
        miniGames = new MiniGames(this);
        getServer().getPluginManager().registerEvents(new DamageEvent(this), this);
        getServer().getPluginManager().registerEvents(new BucketEvent(this), this);
        getServer().getPluginManager().registerEvents(new InteractEvent(this), this);
        getServer().getPluginManager().registerEvents(new PlaceEvent(this), this);
        getServer().getPluginManager().registerEvents(new BreakEvent(this), this);
        getServer().getPluginManager().registerEvents(new QuitEvent(this), this);
        getServer().getPluginManager().registerEvents(new DeathEvent(this), this);
        getServer().getPluginManager().registerEvents(new ClickEvent(this), this);
        getCommand("fmg").setExecutor(new MinigameCommand(this));
        getCommand("fut").setExecutor(new MinigameUtilities(this));

    }

    @Override
    public void onDisable() {
        for (MiniGame miniGame : miniGames.miniGames.values())
            miniGame.disable();
    }

}