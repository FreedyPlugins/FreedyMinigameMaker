// 
// Decompiled by Procyon v0.5.36
// 

package freedy.freedyminigamemaker;

import freedy.freedyminigamemaker.commands.MinigameCommand;
import freedy.freedyminigamemaker.commands.MinigameUtilities;
import freedy.freedyminigamemaker.events.*;
import org.bukkit.plugin.java.JavaPlugin;

public final class FreedyMinigameMaker extends JavaPlugin
{
    public static MiniGames miniGames;

    public void onEnable() {
        this.getConfig().options().copyDefaults();
        this.saveDefaultConfig();
        FreedyMinigameMaker.miniGames = new MiniGames(this);
        this.getServer().getPluginManager().registerEvents(new VehicleEvent(), this);
        this.getServer().getPluginManager().registerEvents(new DamagedEvent(), this);
        this.getServer().getPluginManager().registerEvents(new EntityDamagedEvent(), this);
        this.getServer().getPluginManager().registerEvents(new ProjectileEvent(), this);
        this.getServer().getPluginManager().registerEvents(new BucketEvent(), this);
        this.getServer().getPluginManager().registerEvents(new InteractEvent(), this);
        this.getServer().getPluginManager().registerEvents(new InteractEntityEvent(), this);
        this.getServer().getPluginManager().registerEvents(new PlaceEvent(), this);
        this.getServer().getPluginManager().registerEvents(new BreakEvent(), this);
        this.getServer().getPluginManager().registerEvents(new DamageBlockEvent(), this);
        this.getServer().getPluginManager().registerEvents(new ExplodeBlockEvent(), this);
        this.getServer().getPluginManager().registerEvents(new QuitEvent(), this);
        this.getServer().getPluginManager().registerEvents(new JoinEvent(), this);
        this.getServer().getPluginManager().registerEvents(new ClickEvent(), this);
        this.getServer().getPluginManager().registerEvents(new MoveEvent(), this);
        this.getServer().getPluginManager().registerEvents(new ChatEvent(), this);
        this.getServer().getPluginManager().registerEvents(new DropEvent(), this);
        this.getServer().getPluginManager().registerEvents(new PickupEvent(), this);
        this.getServer().getPluginManager().registerEvents(new WorldChangeEvent(), this);
        this.getServer().getPluginManager().registerEvents(new CommandEvent(), this);
        this.getCommand("fmg").setExecutor(new MinigameCommand(this));
        this.getCommand("fut").setExecutor(new MinigameUtilities(this));

    }
    
    public void onDisable() {
    }
}
