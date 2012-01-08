package pgDev.bukkit.UtilityHats;

import java.io.File;
import java.util.HashMap;
import org.bukkit.entity.Player;
import org.bukkit.Server;
import org.bukkit.event.Event.Priority;
import org.bukkit.event.Event;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginLoader;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.PluginManager;

/**
 * UtilityHats for Bukkit
 * @author PG Dev Team (Devil Boy)
 */
public class UtilityHats extends JavaPlugin {
	// File Locations
    static String pluginMainDir = "./plugins/UtilityHats";
    static String pluginConfigLocation = pluginMainDir + "/UtilityHats.cfg";
    
	// Listeners
    private final UtilityHatsPlayerListener playerListener = new UtilityHatsPlayerListener(this);
    private final UtilityHatsBlockListener blockListener = new UtilityHatsBlockListener(this);

    public void onEnable() {
        // Register our events
        PluginManager pm = getServer().getPluginManager();
       

        // Yo bro! We're alivez!
        PluginDescriptionFile pdfFile = this.getDescription();
        System.out.println( pdfFile.getName() + " version " + pdfFile.getVersion() + " is enabled!" );
    }
    
    public void onDisable() {
        System.out.println("UtilityHats disabled!");
    }
    
    public boolean onCommand() {
    	// TODO: Code that places hat in hand, on head
    	return true;
    }
}

