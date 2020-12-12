// 
// Decompiled by Procyon v0.5.36
// 

package freedy.freedyminigamemaker;

import java.io.IOException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.configuration.file.FileConfiguration;
import java.io.File;

public class FileStore
{
    File file;
    FileConfiguration config;
    FreedyMinigameMaker plugin;
    
    public FileStore(final FreedyMinigameMaker plugin, String fileName) {
        this.plugin = plugin;
        this.file = new File(plugin.getDataFolder().getAbsolutePath(), fileName);
        load();
        if (!this.file.exists()) {
            plugin.saveResource(fileName, false);
        }
    }

    public void load() {
        this.config = (FileConfiguration)YamlConfiguration.loadConfiguration(this.file);
    }

    public FileConfiguration getConfig() {
        return this.config;
    }
    
    public void save() {
        try {
            this.config.save(this.file);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
}
