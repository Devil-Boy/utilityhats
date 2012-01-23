package pgDev.bukkit.UtilityHats;

import org.bukkit.event.block.BlockListener;

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
