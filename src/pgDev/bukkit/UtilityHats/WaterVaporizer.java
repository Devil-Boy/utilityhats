package pgDev.bukkit.UtilityHats;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import org.bukkit.Material;
import org.bukkit.block.Block;

public class WaterVaporizer implements ActionListener {
	Block block;
	
	public WaterVaporizer(Block blockI) {
		block = blockI;
	}
	
	public void actionPerformed(ActionEvent arg0) {
		if (block.getType() == Material.WATER || block.getType() == Material.STATIONARY_WATER) {
			block.setType(Material.AIR);
		}
	}

}
