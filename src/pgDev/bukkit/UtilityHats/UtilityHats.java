package pgDev.bukkit.UtilityHats;

import java.io.File;
import java.util.HashMap;
import java.util.LinkedList;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Server;
import org.bukkit.event.Event.Priority;
import org.bukkit.event.Event;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginLoader;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.PluginManager;

import com.nijiko.permissions.PermissionHandler;
import com.nijikokun.bukkit.Permissions.Permissions;

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
    
    // Permissions support
    static PermissionHandler Permissions;

    public void onEnable() {
        // Register our events
        PluginManager pm = getServer().getPluginManager();
       
        
        // Get permissions involved!
        setupPermissions();
        
        // Yo bro! We're alivez!
        PluginDescriptionFile pdfFile = this.getDescription();
        System.out.println( pdfFile.getName() + " version " + pdfFile.getVersion() + " is enabled!" );
    }
    
    public void onDisable() {
        System.out.println("UtilityHats disabled!");
    }
    
    // Permissions Methods
    private void setupPermissions() {
        Plugin permissions = this.getServer().getPluginManager().getPlugin("Permissions");

        if (Permissions == null) {
            if (permissions != null) {
                Permissions = ((Permissions)permissions).getHandler();
            } else {
            }
        }
    }
    
    public static boolean hasPermissions(Player player, String node) {
        if (Permissions != null) {
        	return Permissions.has(player, node);
        } else {
            return player.hasPermission(node);
        }
    }
    
    // Placing of hats on head
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
    	if (sender instanceof Player) {
			Player player = (Player) sender;
			ItemStack prospectiveHat = player.getItemInHand();
			if (prospectiveHat == null) {
				player.sendMessage(ChatColor.RED + "There is nothing in your hand.");
			} else {
				if (prospectiveHat.getType() == Material.BOOK) { // They want help
					if (hasPermissions(player, "UtilityHats.book")) {
						if (args.length == 0) { // Give them the list
							player.sendMessage(ChatColor.GREEN + "Use \"/hat <hat type>\" with a book in your hand, to learn about a certain hat.");
							LinkedList<String> availableTypes = new LinkedList<String>();
							if (hasPermissions(player, "UtilityHats.glowstone")) {
								availableTypes.add("glowstone");
							}
							if (hasPermissions(player, "UtilityHats.glass")) {
								availableTypes.add("glass");
							}
							if (hasPermissions(player, "UtilityHats.spawner")) {
								availableTypes.add("spawner");
							}
							if (hasPermissions(player, "UtilityHats.obsidian")) {
								availableTypes.add("obsidian");
							}
							String returnList = "";
							for (String type : availableTypes) {
								if (returnList.equals("")) {
									returnList = type;
								} else {
									returnList = returnList + ", " + type;
								}
							}
							player.sendMessage(ChatColor.GREEN + "Available types: " + returnList);
						} else {
							if (args[0].equalsIgnoreCase("glowstone")) {
								player.sendMessage(ChatColor.GREEN + "Pro: Lights up the area around you");
								player.sendMessage(ChatColor.GREEN + "Con: Attracts mobs at a greater range");
							} else if (args[0].equalsIgnoreCase("glass")) {
								player.sendMessage(ChatColor.GREEN + "Pro: Can breath underwater");
								player.sendMessage(ChatColor.GREEN + "Con: Food depletes quicker");
								player.sendMessage(ChatColor.GREEN + "Con: Squids target you, but will not drop ink");
							} else if (args[0].equalsIgnoreCase("spawner")) {
								player.sendMessage(ChatColor.GREEN + "Pro: 50% less damage from mobs that can naturally be found in spawners");
								player.sendMessage(ChatColor.GREEN + "Con: 50% more damage from mobs that aren't found in spawners");
							} else if (args[0].equalsIgnoreCase("obsidian")) {
								player.sendMessage(ChatColor.GREEN + "Pro: No damage from explosions");
								player.sendMessage(ChatColor.GREEN + "Con: Fall damage is increased by 50%");
							} else {
								player.sendMessage(ChatColor.RED + "The hat type you specified was not recognized.");
							}
						}
					} else {
						player.sendMessage(ChatColor.RED + "You do not have permission to read this book about hats.");
					}
				} else if (prospectiveHat.getType() == Material.GLOWSTONE) {
					if (hasPermissions(player, "UtilityHats.glowstone")) {
						setHandToHead(player.getInventory(), prospectiveHat);
						player.sendMessage(ChatColor.GOLD + "You now have glowstone on your head.");
					} else {
						player.sendMessage(ChatColor.RED + "You do not have permissions to place glowstone upon your head.");
					}
				} else if (prospectiveHat.getType() == Material.GLASS) {
					if (hasPermissions(player, "UtilityHats.glass")) {
						setHandToHead(player.getInventory(), prospectiveHat);
						player.sendMessage(ChatColor.GOLD + "You now have glass on your head.");
					} else {
						player.sendMessage(ChatColor.RED + "You do not have permissions to place glass upon your head.");
					}
				} else if (prospectiveHat.getType() == Material.MOB_SPAWNER) {
					if (hasPermissions(player, "UtilityHats.spawner")) {
						setHandToHead(player.getInventory(), prospectiveHat);
						player.sendMessage(ChatColor.GOLD + "You now have a spawner on your head.");
					} else {
						player.sendMessage(ChatColor.RED + "You do not have permissions to place a spawner upon your head.");
					}
				} else if (prospectiveHat.getType() == Material.OBSIDIAN) {
					if (hasPermissions(player, "UtilityHats.obsidian")) {
						setHandToHead(player.getInventory(), prospectiveHat);
						player.sendMessage(ChatColor.GOLD + "You now have obsidian on your head.");
					} else {
						player.sendMessage(ChatColor.RED + "You do not have permissions to place obsidian upon your head.");
					}
				} else {
					player.sendMessage(ChatColor.RED + "That block is not to be used as a hat.");
				}
			}
		} else {
			sender.sendMessage("Hats cannot be placed on your console as that would look funny.");
		}
    	return true;
    }
    
    // This method helps reduce repetition XD
    public void setHandToHead(PlayerInventory inv, ItemStack leHatThingie) {
		// Place their current helmet in their inventory
		ItemStack helmet = inv.getArmorContents()[3]; // getHelmet() wouldn't give me non-helmet blocks
		if (helmet != null && helmet.getType() != Material.AIR) {
			inv.addItem(helmet);
		}
		
		// Give them the new hat
		if (leHatThingie.getAmount() > 1) { // Has more than one of the block in hand
			ItemStack newHandStack = leHatThingie.clone();
			newHandStack.setAmount(newHandStack.getAmount() - 1);
			
			leHatThingie.setAmount(1);
			inv.setHelmet(leHatThingie);
			
			inv.setItemInHand(newHandStack);
		} else { // Just has one of the block in hand
			inv.setHelmet(leHatThingie);
			inv.removeItem(leHatThingie);
		}
    }
}

