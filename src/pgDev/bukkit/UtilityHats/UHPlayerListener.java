package pgDev.bukkit.UtilityHats;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerChatEvent;
import org.bukkit.event.player.PlayerEvent;
import org.bukkit.event.player.PlayerListener;
import org.bukkit.event.player.PlayerMoveEvent;

/**
 * Handle events for all Player related events
 * @author PG Dev Team (Devil Boy)
 */
public class UHPlayerListener extends PlayerListener {
    private final UtilityHats plugin;

    public UHPlayerListener(UtilityHats instance) {
        plugin = instance;
    }
}

