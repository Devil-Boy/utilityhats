package pgDev.bukkit.UtilityHats;

import javax.swing.Timer;

import org.bukkit.Effect;
import org.bukkit.Material;
import org.bukkit.block.Biome;
import org.bukkit.block.Block;
import org.bukkit.entity.*;
import org.bukkit.event.*;
import org.bukkit.event.entity.*;
import org.bukkit.inventory.ItemStack;

public class UHEntityListener implements Listener {
	private final UtilityHats plugin;

    public UHEntityListener(UtilityHats instance) {
        plugin = instance;
    }
    
    // For when they should or do take damage
    @EventHandler
    public void onEntityDamage(EntityDamageEvent event) {
    	// Piston attack?
		if (event instanceof EntityDamageByEntityEvent) {
			EntityDamageByEntityEvent eT = (EntityDamageByEntityEvent) event;
			Entity attacker = eT.getDamager();
			if (attacker instanceof Player) { // Attacked by player!
				Player pistonHead = (Player) attacker;
				if (pistonHead.getInventory().getArmorContents()[3].getType() == Material.PISTON_BASE && pistonHead.getItemInHand().getType() == Material.REDSTONE_TORCH_ON) {
					event.setCancelled(true);
					event.getEntity().setVelocity(event.getEntity().getVelocity().setY(plugin.pluginSettings.pistonPunchSpeed));
				}
			}
		}
    	
		// Any player damages
    	if (event.getEntity() instanceof Player) { // filter out the tons of mobs first
			Player diver = (Player) event.getEntity();
			if (diver.getInventory().getArmorContents()[3].getType() == Material.TNT) { // Blow him up!!!!
				// Clear inv
				diver.getInventory().clear();
				diver.getInventory().setArmorContents(new ItemStack[4]);
				
				if (plugin.playerListener.tntHeads.contains(diver.getName())) {
					plugin.playerListener.tntHeads.remove(diver.getName());
				}
				diver.getWorld().createExplosion(diver.getLocation(), plugin.pluginSettings.tntRadius);
			} else if (diver.getInventory().getArmorContents()[3].getType() == Material.ICE) {
				Block positionBlock = diver.getLocation().getBlock();
    			// turn his spot into water
				positionBlock.setType(Material.WATER);
				
				// start a tiny timer
				Timer vapors = new Timer(1000, new WaterVaporizer(positionBlock));
				vapors.setRepeats(false);
				vapors.start();
				
				// play break sound
				diver.getWorld().playEffect(diver.getLocation(), Effect.POTION_BREAK, 0);
				
				// remove hat from head
				diver.getInventory().setHelmet(null);
			} else if (diver.getInventory().getArmorContents()[3].getType() == Material.PISTON_BASE) { // overheated?
				if (diver.getHealth() - event.getDamage() < 10) {
					diver.setFireTicks(plugin.pluginSettings.pistonOverHeat);
				}
			} else {
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
		    			diver.getWorld().playEffect(diver.getLocation(), Effect.POTION_BREAK, 0);
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
		    		} else if (diver.getInventory().getArmorContents()[3].getType() == Material.SNOW_BLOCK) { // soft fall :)
		    			event.setCancelled(true);
		    			diver.getWorld().playEffect(diver.getLocation(), Effect.ENDER_SIGNAL, 0);
		    			diver.getInventory().setHelmet(null);
		    		}
		    	}
			}
    	}
    }
    
    // Don't let squids drop ink when near a diver
    @EventHandler
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
    @EventHandler
    public void onFoodLevelChange(FoodLevelChangeEvent event) {
    	if (event.getEntity() instanceof Player) {
    		Player player = (Player) event.getEntity();
    		if (player.getInventory().getArmorContents()[3].getType() == Material.GLASS && player.getFoodLevel() > event.getFoodLevel()) {
    			event.setFoodLevel(event.getFoodLevel() - 3);
    		} else if (player.getInventory().getArmorContents()[3].getType() == Material.ICE && player.getFoodLevel() > event.getFoodLevel() && player.getLocation().getBlock().getBiome() == Biome.OCEAN) {
    			event.setFoodLevel(event.getFoodLevel() - 3);
    		}
    	}
    }
}
