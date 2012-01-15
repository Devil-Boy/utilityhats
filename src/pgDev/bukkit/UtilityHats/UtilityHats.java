package pgDev.bukkit.UtilityHats;

import java.io.File;
import java.io.FileInputStream;
import java.util.LinkedList;
import java.util.Properties;

import javax.swing.Timer;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.event.Event.Priority;
import org.bukkit.event.Event;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.PluginManager;

import com.nijiko.permissions.PermissionHandler;
import com.nijikokun.bukkit.Permissions.Permissions;

/**
 * UtilityHats for Bukkit
 * @author PG Dev Team (Devil Boy)
 */
public class UtilityHats extends JavaPlugin {
	// Debugh mode
	public boolean debug = false;
	
	// Plugin Configuration
	UHConfig pluginSettings;
	
	// File Locations
    static String pluginMainDir = "./plugins/UtilityHats";
    static String pluginConfigLocation = pluginMainDir + "/UtilityHats.cfg";
    
	// Listeners
    final UHPlayerListener playerListener = new UHPlayerListener(this);
    final UHBlockListener blockListener = new UHBlockListener(this);
    final UHEntityListener entityListener = new UHEntityListener(this);
    
    // Permissions support
    static PermissionHandler Permissions;

    public void onEnable() {
    	// Check for the plugin directory (create if it does not exist)
    	File pluginDir = new File(pluginMainDir);
		if(!pluginDir.exists()) {
			boolean dirCreation = pluginDir.mkdirs();
			if (dirCreation) {
				System.out.println("New UtilityHats directory created!");
			}
		}
		
		// Load the Configuration
    	try {
        	Properties preSettings = new Properties();
        	if ((new File(pluginConfigLocation)).exists()) {
        		preSettings.load(new FileInputStream(new File(pluginConfigLocation)));
        		pluginSettings = new UHConfig(preSettings, this);
        		if (!pluginSettings.upToDate) {
        			pluginSettings.createConfig();
        			System.out.println("UtilityHats Configuration updated!");
        		}
        	} else {
        		pluginSettings = new UHConfig(preSettings, this);
        		pluginSettings.createConfig();
        		System.out.println("UtilityHats Configuration created!");
        	}
        } catch (Exception e) {
        	System.out.println("Could not load UtilityHats configuration! " + e);
        }
		
        // Register our events
        PluginManager pm = getServer().getPluginManager();
        pm.registerEvent(Event.Type.ENTITY_DAMAGE, entityListener, Priority.Normal, this); // Anti-drown, attract squid
        pm.registerEvent(Event.Type.ENTITY_DEATH, entityListener, Priority.Normal, this); // Squid no-drop
        pm.registerEvent(Event.Type.FOOD_LEVEL_CHANGE, entityListener, Priority.Normal, this); // Squid no-drop
        
        pm.registerEvent(Event.Type.PLAYER_MOVE, playerListener, Priority.Normal, this); // Glowstone light
        pm.registerEvent(Event.Type.PLAYER_JOIN, playerListener, Priority.Normal, this); // Glow on join
        pm.registerEvent(Event.Type.PLAYER_QUIT, playerListener, Priority.Normal, this); // Unglow on leave
        //pm.registerEvent(Event.Type.PLAYER_TOGGLE_SPRINT, playerListener, Priority.Normal, this); // Anti-sprint (does not work)
        
        // Get permissions involved!
        setupPermissions();
        
        // Start our timer
        new Timer(pluginSettings.glowHeadAlert * 1000, playerListener.mobAlert); // Mob alerter
        
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
							if (hasPermissions(player, "UtilityHats.tnt")) {
								availableTypes.add("tnt");
							}
							if (hasPermissions(player, "UtilityHats.ice")) {
								availableTypes.add("ice");
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
								player.sendMessage(ChatColor.GREEN + "Con: Attracts mobs at a greater range and through blocks");
							} else if (args[0].equalsIgnoreCase("glass")) {
								player.sendMessage(ChatColor.GREEN + "Pro: Can breath underwater");
								player.sendMessage(ChatColor.GREEN + "Con: Food depletes quicker");
								player.sendMessage(ChatColor.GREEN + "Con: Squids impede you, but will not drop ink or exp");
								player.sendMessage(ChatColor.GREEN + "Con: Your hat breaks upon taking damage");
							} else if (args[0].equalsIgnoreCase("spawner")) {
								player.sendMessage(ChatColor.GREEN + "Pro: 2x less damage from mobs that can naturally be found in spawners");
								player.sendMessage(ChatColor.GREEN + "Con: 2x more damage from mobs that aren't found in spawners");
							} else if (args[0].equalsIgnoreCase("obsidian")) {
								player.sendMessage(ChatColor.GREEN + "Pro: No damage from explosions");
								player.sendMessage(ChatColor.GREEN + "Con: Fall damage is increased by 2x");
								player.sendMessage(ChatColor.GREEN + "Con: Cannot sprint");
							} else if (args[0].equalsIgnoreCase("tnt")) {
								player.sendMessage(ChatColor.GREEN + "Pro: Explode upon taking any damage");
								player.sendMessage(ChatColor.GREEN + "Pro: Explosion is as powerful as that of a charged creeper");
								player.sendMessage(ChatColor.GREEN + "Con: Removing hat from head will deplete all food");
							} else if (args[0].equalsIgnoreCase("ice")) {
								player.sendMessage(ChatColor.GREEN + "Pro: Direct exposure to fire or lava melts ice");
								player.sendMessage(ChatColor.GREEN + "Con: Sneaking will not prevent falling off of blocks");
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
				} else if (prospectiveHat.getType() == Material.TNT) {
					if (hasPermissions(player, "UtilityHats.tnt")) {
						setHandToHead(player.getInventory(), prospectiveHat);
						playerListener.tntHeads.add(player.getName());
						player.sendMessage(ChatColor.GOLD + "You now have tnt on your head.");
					} else {
						player.sendMessage(ChatColor.RED + "You do not have permissions to place tnt upon your head.");
					}
				} else if (prospectiveHat.getType() == Material.ICE) {
					if (hasPermissions(player, "UtilityHats.ice")) {
						setHandToHead(player.getInventory(), prospectiveHat);
						player.sendMessage(ChatColor.GOLD + "You now have ice on your head.");
					} else {
						player.sendMessage(ChatColor.RED + "You do not have permissions to place ice upon your head.");
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

