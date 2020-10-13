// 
// Decompiled by Procyon v0.5.36
// 

package freedy.freedyminigamemaker;

import java.util.Iterator;
import freedy.freedyminigamemaker.commands.MinigameUtilities;
import org.bukkit.command.CommandExecutor;
import freedy.freedyminigamemaker.commands.MinigameCommand;
import freedy.freedyminigamemaker.events.ChatEvent;
import freedy.freedyminigamemaker.events.MoveEvent;
import freedy.freedyminigamemaker.events.ClickEvent;
import freedy.freedyminigamemaker.events.DeathEvent;
import freedy.freedyminigamemaker.events.QuitEvent;
import freedy.freedyminigamemaker.events.BreakEvent;
import freedy.freedyminigamemaker.events.PlaceEvent;
import freedy.freedyminigamemaker.events.InteractEvent;
import freedy.freedyminigamemaker.events.BucketEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.event.Listener;
import freedy.freedyminigamemaker.events.DamageEvent;
import org.bukkit.plugin.java.JavaPlugin;

public final class FreedyMinigameMaker extends JavaPlugin
{
    public static MiniGames miniGames;
    
    public void onEnable() {
        this.getConfig().options().copyDefaults();
        this.saveDefaultConfig();
        FreedyMinigameMaker.miniGames = new MiniGames(this);
        this.getServer().getPluginManager().registerEvents((Listener)new DamageEvent(), (Plugin)this);
        this.getServer().getPluginManager().registerEvents((Listener)new BucketEvent(), (Plugin)this);
        this.getServer().getPluginManager().registerEvents((Listener)new InteractEvent(), (Plugin)this);
        this.getServer().getPluginManager().registerEvents((Listener)new PlaceEvent(), (Plugin)this);
        this.getServer().getPluginManager().registerEvents((Listener)new BreakEvent(), (Plugin)this);
        this.getServer().getPluginManager().registerEvents((Listener)new QuitEvent(), (Plugin)this);
        this.getServer().getPluginManager().registerEvents((Listener)new DeathEvent(), (Plugin)this);
        this.getServer().getPluginManager().registerEvents((Listener)new ClickEvent(), (Plugin)this);
        this.getServer().getPluginManager().registerEvents((Listener)new MoveEvent(), (Plugin)this);
        this.getServer().getPluginManager().registerEvents((Listener)new ChatEvent(), (Plugin)this);
        this.getCommand("fmg").setExecutor((CommandExecutor)new MinigameCommand(this));
        this.getCommand("fut").setExecutor((CommandExecutor)new MinigameUtilities(this));
    }
    
    public void onDisable() {
        for (final MiniGame miniGame : FreedyMinigameMaker.miniGames.miniGames.values()) {
            miniGame.disable();
        }
    }
}
