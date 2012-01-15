package pgDev.bukkit.UtilityHats;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.LinkedList;

import net.minecraft.server.EnumSkyBlock;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.*;
import org.bukkit.event.player.*;

import org.bukkit.craftbukkit.CraftWorld;

/**
 * Handle events for all Player related events
 * @author PG Dev Team (Devil Boy)
 */
public class UHPlayerListener extends PlayerListener {
    private final UtilityHats plugin;
    
    LinkedList<String> lightHeads = new LinkedList<String>();
    LinkedList<String> tntHeads = new LinkedList<String>();

    public UHPlayerListener(UtilityHats instance) {
        plugin = instance;
    }
    
    public void onPlayerMove(PlayerMoveEvent event) {
    	if (!event.getFrom().getBlock().getLocation().equals(event.getTo().getBlock().getLocation())) {
    		// Glow stuff
    		if (event.getPlayer().getInventory().getArmorContents()[3].getType() == Material.GLOWSTONE) { // light the way!
        		if (plugin.debug) {
        			System.out.println(event.getPlayer().getName() + " is running around with glowstone on his head!");
        		}
        		
        		// first remove any old light area
        		removeOldLight(event.getFrom());
        		
        		// then add a new one
        		makeNewLight(event.getTo());
        		
        		// add them to the database of peeps who made light
        		if (!lightHeads.contains(event.getPlayer().getName())) {
        			lightHeads.add(event.getPlayer().getName());
        		}
    		} else if (lightHeads.contains(event.getPlayer().getName())) { // Light area check
    			// first remove any old light area
        		removeOldLight(event.getFrom());
        		
        		// remove from database
        		lightHeads.remove(event.getPlayer().getName());
    		}
    			
    		// TNT Stuff	
    		if (event.getPlayer().getInventory().getArmorContents()[3].getType() == Material.TNT) {
    			if (!tntHeads.contains(event.getPlayer().getName())) {
        			tntHeads.add(event.getPlayer().getName());
        		}
        	} else if (tntHeads.contains(event.getPlayer().getName())) {
    			// take his food!
    			event.getPlayer().setFoodLevel(0);
        		
        		// remove from database
        		lightHeads.remove(event.getPlayer().getName());
        	}
    		
    		// Movement checks
    		if (event.getPlayer().getInventory().getArmorContents()[3].getType() == Material.OBSIDIAN && event.getPlayer().isSprinting()) { // no sprinting fatty
    			event.getPlayer().setSprinting(false);
    		} else if (event.getPlayer().getInventory().getArmorContents()[3].getType() == Material.ICE && event.getPlayer().isSneaking()) { // you shall slide~
    			event.getPlayer().setSneaking(false);
    		}
    	}
    }
    
    public void onPlayerJoin(PlayerJoinEvent event) {
    	if (event.getPlayer().getInventory().getArmorContents()[3].getType() == Material.GLOWSTONE) { // light the way!
    		makeNewLight(event.getPlayer().getLocation());
    		
    		// add them to the database of peeps who made light
    		if (!lightHeads.contains(event.getPlayer().getName())) {
    			lightHeads.add(event.getPlayer().getName());
    		}
    	}
    }
    
    public void onPlayerQuit(PlayerQuitEvent event) {
    	if (lightHeads.contains(event.getPlayer().getName())) {
			// first remove any old light area
    		removeOldLight(event.getPlayer().getLocation());
    		
    		// remove from database
    		lightHeads.remove(event.getPlayer().getName());
		}
    }
    
    // Stop sprinting
    /* Does not work with stopping sprinting
    public void onPlayerToggleSprint(PlayerToggleSprintEvent event) {
    	if (event.getPlayer().getInventory().getArmorContents()[3].getType() == Material.OBSIDIAN && event.isSprinting()) { // no sprinting fatty
    		if (plugin.debug) {
    			System.out.println(event.getPlayer().getName() + " tried to sprint with obsidian on his head!");
    		}
    		
    		event.getPlayer().setSprinting(false);
    		event.setCancelled(true);
    	}
    }*/
    
    // Glow functions
    public void removeOldLight(Location oldL) {
    	Location oldLightOrigin = new Location (((CraftWorld) oldL.getWorld()), oldL.getBlockX(), oldL.getBlockY() + 2, oldL.getBlockZ());
		oldLightOrigin.getBlock().setType(oldLightOrigin.getBlock().getType());
    }
    
    public void makeNewLight(Location newL) {
    	CraftWorld hackyWorld = (CraftWorld) newL.getWorld();
		hackyWorld.getHandle().a(EnumSkyBlock.BLOCK, newL.getBlockX(), newL.getBlockY() + 2, newL.getBlockZ(), plugin.pluginSettings.glowPower);
		Location newLightOrigin = new Location (hackyWorld, newL.getBlockX(), newL.getBlockY() + 1, newL.getBlockZ());
		newLightOrigin.getBlock().setType(newLightOrigin.getBlock().getType());
    }
    
    // Mob attack
    ActionListener mobAlert = new ActionListener() {
    	public void actionPerformed(ActionEvent evt) {
    		for (String glowHead : lightHeads) {
    			Player leHead = plugin.getServer().getPlayer(glowHead);
    			if (leHead == null) {
    				lightHeads.remove(glowHead);
    			} else {
    				for (Entity localMobs : leHead.getNearbyEntities(32, 32, 32)) {
    	    			if ((localMobs instanceof Monster) && !(localMobs instanceof Enderman) && !(localMobs instanceof PigZombie)) {
    	    				Monster brute = (Monster) localMobs;
    	    				if (brute.getTarget() == null) {
    	    					brute.setTarget(leHead);
    	    				}
    	    			}
    	    		}
    			}
    		}
    	}
    };
}

