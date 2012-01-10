package pgDev.bukkit.UtilityHats;

import org.bukkit.Effect;
import org.bukkit.Material;
import org.bukkit.entity.*;
import org.bukkit.event.entity.*;

public class UHEntityListener extends EntityListener {
	private final UtilityHats plugin;

    public UHEntityListener(UtilityHats instance) {
        plugin = instance;
    }
    
    // For when they should or do take damage
    public void onEntityDamage(EntityDamageEvent event) {
    	if (plugin.debug) {
    		System.out.println("Something is drowning!");
    	}
    	
    	if (event.getEntity() instanceof Player) { // filter out the tons of mobs first
			Player diver = (Player) event.getEntity();
	    	if (event.getCause() == EntityDamageEvent.DamageCause.DROWNING) { // Stop drowning in suit and attract squids
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
							passerBy.setVelocity(diver.getLocation().toVector().subtract(passerBy.getLocation().toVector()).multiply(0.1));
							
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
	    	} else if (event.getCause() == EntityDamageEvent.DamageCause.CONTACT || event.getCause() == EntityDamageEvent.DamageCause.ENTITY_ATTACK || event.getCause() == EntityDamageEvent.DamageCause.ENTITY_EXPLOSION || event.getCause() == EntityDamageEvent.DamageCause.PROJECTILE) { // Likely taken from a monster
	    		if (diver.getInventory().getArmorContents()[3].getType() == Material.GLASS) { // break his glasses!
	    			diver.setRemainingAir(diver.getMaximumAir());
	    			diver.playEffect(diver.getLocation(), Effect.POTION_BREAK, 0);
	    			for (Entity localPeeps : diver.getNearbyEntities(32, 32, 32)) {
	    				if (localPeeps instanceof Player) {
	    					((Player) localPeeps).playEffect(diver.getLocation(), Effect.POTION_BREAK, 0);
	    				}
	    			}
	    			diver.getInventory().setHelmet(null);
	    		} else if (diver.getInventory().getArmorContents()[3].getType() == Material.MOB_SPAWNER) { // toughen up? or beat down?
	    			if (event instanceof EntityDamageByEntityEvent) { // He was attackedQ!
	    				EntityDamageByEntityEvent eT = (EntityDamageByEntityEvent) event;
	    				Entity attacker = eT.getDamager();
	    				if ((attacker instanceof Zombie) || (attacker instanceof Skeleton) || (attacker instanceof Spider) || (attacker instanceof CaveSpider) || (attacker instanceof Silverfish) || (attacker instanceof Blaze)) { // It was a cage match!
	    					event.setDamage(event.getDamage() / 2);
	    				} else if (attacker instanceof Monster) {
	    					event.setDamage(event.getDamage() * 2);
	    				}
	    			} else if (event.getCause() == EntityDamageEvent.DamageCause.ENTITY_EXPLOSION) { // he was xploded
	    				event.setDamage(event.getDamage() * 2);
	    			} else if (event.getCause() == EntityDamageEvent.DamageCause.PROJECTILE) { // dude got shot
	    				event.setDamage(event.getDamage() / 2);
	    			}
	    		} else if (diver.getInventory().getArmorContents()[3].getType() == Material.OBSIDIAN) { // creeper-proof!
	    			if (event.getCause() == EntityDamageEvent.DamageCause.ENTITY_EXPLOSION) { // he was xploded
	    				event.setCancelled(true);
	    			}
	    		}
	    	} else if (event.getCause() == EntityDamageEvent.DamageCause.FALL) {
	    		if (diver.getInventory().getArmorContents()[3].getType() == Material.OBSIDIAN) { // heavy fall :O
	    			event.setDamage(event.getDamage() * 2);
	    		}
	    	}
    	}
    }
    
    // Don't let squids drop ink when near a diver
    public void onEntityDeath(EntityDeathEvent event) {
    	if (event.getEntity() instanceof Squid) {
    		Player hunter = ((Squid) event.getEntity()).getKiller();
    		if (hunter != null) {
    			if (hunter.getInventory().getArmorContents()[3].getType() == Material.GLASS) {
        			event.getDrops().clear();
    				event.setDroppedExp(0);
        		}
    		}
    	}
    }
    
    // Increase hunger speed thing for the fish-bowl heads
    public void onFoodLevelChange(FoodLevelChangeEvent event) {
    	if (event.getEntity() instanceof Player) {
    		Player player = (Player) event.getEntity();
    		if (player.getInventory().getArmorContents()[3].getType() == Material.GLASS && player.getFoodLevel() > event.getFoodLevel()) {
    			event.setFoodLevel(event.getFoodLevel() - 3);
    		}
    	}
    }
}
