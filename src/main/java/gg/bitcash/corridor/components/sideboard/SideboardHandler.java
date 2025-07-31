package gg.bitcash.corridor.components.sideboard;

import gg.bitcash.corridor.Corridor;
import gg.bitcash.corridor.components.sideboard.config.SideboardConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;

/**
 * This class (which is only instantiated once at startup) is responsible for tracking, delivering and ceasing scoreboard displays to players. It maintains a registry (See {@link gg.bitcash.corridor.components.sideboard.SideboardRegistry}).
 */
public class SideboardHandler {

    private final SideboardRegistry sideboardRegistry;
    private SideboardConfiguration config;
    private final Corridor instance;
    private final SideboardMonitor monitor;
    /**
     * Constructs a new SideboardHandler instance -- this should only be done once during startup, and this single object will be reused throughout the plugins entire lifecycle.
     * @param config The {@link SideboardConfiguration} responsible for processing the configured boards'
     * @param instance central plugin instance
     */
    public SideboardHandler(SideboardConfiguration config, Corridor instance) {
        this.instance = instance;
        this.config = config;
        this.sideboardRegistry = new SideboardRegistry(config.buildAllFromConfig());
        this.monitor = new SideboardMonitor(this);
    }

    public SideboardMonitor getMonitor() {
        return monitor;
    }

    public Corridor getInstance() {
        return instance;
    }

    public SideboardRegistry getSideboardRegistry() {
        return sideboardRegistry;
    }
    /**
     * A simple method to shut the board for a player. Gets
     * @param player
     */
    public void closeActiveBoardFromPlayer(Player player) {
        player.getScoreboard().clearSlot(DisplaySlot.SIDEBAR);
    }
    /**
     * Open the active board for a player
     * @param player
     */
    public void openActiveBoardForPlayer(Player player) {
        monitor.getCurrentBoard(player).ifPresent(board->board.forceBoardDisplay(player));
    }
}
