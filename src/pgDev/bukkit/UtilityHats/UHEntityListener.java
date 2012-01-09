package pgDev.bukkit.UtilityHats;

import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Squid;
import org.bukkit.event.entity.*;

public class UHEntityListener extends EntityListener {
	private final UtilityHats plugin;

    public UHEntityListener(UtilityHats instance) {
        plugin = instance;
    }
    
    // Stop drowning in suit and attract squids
    public void onEntityDamage(EntityDamageEvent event) {
    	if (plugin.debug) {
    		System.out.println("Something is drowning!");
    	}
    	if (event.getCause() == EntityDamageEvent.DamageCause.DROWNING) {
    		if (event.getEntity() instanceof Player) {
    			Player diver = (Player) event.getEntity();
    			if (plugin.debug) {
    				System.out.println("It was " + diver.getName());
    			}
    			if (diver.getInventory().getArmorContents()[3].getType() == Material.GLASS) { // Save him! :o
    				if (plugin.debug) {
        				System.out.println("But he has glass on his head!");
        			}
    				
    				// Go squids!
    				for (Entity passerBy : diver.getNearbyEntities(16, 16, 16)) {
    					if (passerBy instanceof Squid) {
    						passerBy.setVelocity(diver.getLocation().toVector().subtract(passerBy.getLocation().toVector()).multiply(0.06));
    						
    						/* Squids do not follow their target
    						((Squid) passerBy).setTarget(diver);
    						if (plugin.debug) {
    	        				System.out.println("Targetted by a squid!");
    	        			}*/
    					}
    				}
    				
    				// Negate drowning
    				event.setCancelled(true);
    				//diver.setRemainingAir(diver.getMaximumAir());
    			}
    		}
    	}
    }
    
    // Don't let squids drop ink when near a diver
    public void onEntityDeath(EntityDeathEvent event) {
    	if (event.getEntity() instanceof Squid) {
    		if (((Squid) event.getEntity()).getKiller().getInventory().getArmorContents()[3].getType() == Material.GLASS) {
    			event.getDrops().clear();
				event.setDroppedExp(0);
    		}
    	}
    }
}
