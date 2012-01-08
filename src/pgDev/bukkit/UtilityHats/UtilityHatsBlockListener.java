package pgDev.bukkit.UtilityHats;

import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.Material;
import org.bukkit.event.block.BlockCanBuildEvent;
import org.bukkit.event.block.BlockListener;
import org.bukkit.event.block.BlockPhysicsEvent;

/**
 * UtilityHats block listener
 * @author pgDev
 */
public class UtilityHatsBlockListener extends BlockListener {
    private final UtilityHats plugin;

    public UtilityHatsBlockListener(final UtilityHats plugin) {
        this.plugin = plugin;
    }
}
