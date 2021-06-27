// 
// Decompiled by Procyon v0.5.36
// 

package freedy.freedyminigamemaker;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public class FileStore
{
    File file;
    FileConfiguration config;
    FreedyMinigameMaker plugin;
    
    public FileStore(final FreedyMinigameMaker plugin, final String fileName) {
        this.plugin = plugin;
        this.file = new File(plugin.getDataFolder().getAbsolutePath(), fileName);
        this.load();
        if (!this.file.exists()) {
            plugin.saveResource(fileName, false);
        }
    }
    
    public void load() {
        this.config = YamlConfiguration.loadConfiguration(this.file);
    }
    
    public FileConfiguration getConfig() {
        return this.config;
    }
    
    public void saveConfig() {
        try {
            this.config.save(this.file);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
}
