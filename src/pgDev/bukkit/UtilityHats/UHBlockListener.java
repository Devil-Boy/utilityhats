package pgDev.bukkit.UtilityHats;

import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.Material;
import org.bukkit.event.block.BlockCanBuildEvent;
import org.bukkit.event.block.BlockListener;
import org.bukkit.event.block.BlockPhysicsEvent;

/**
 * UtilityHats block listener
 * @author PG Dev Team (Devil Boy)
 */
public class UHBlockListener extends BlockListener {
    private final UtilityHats plugin;

    public UHBlockListener(final UtilityHats plugin) {
        this.plugin = plugin;
    }
}
