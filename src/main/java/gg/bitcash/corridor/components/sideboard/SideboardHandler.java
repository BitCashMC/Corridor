package gg.bitcash.corridor.components.sideboard;

import gg.bitcash.corridor.Corridor;
import gg.bitcash.corridor.ThreadService;
import gg.bitcash.corridor.components.sideboard.displaycondition.DisplayCondition;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.ScoreboardManager;

import java.util.Optional;
import java.util.function.BiConsumer;

/**
 * This class (which is only instantiated once at startup) is responsible for tracking, delivering and ceasing scoreboard displays to players. It maintains a registry (See {@link gg.bitcash.corridor.components.sideboard.SideboardRegistry}).
 */
public class SideboardHandler {

    private final SideboardRegistry sideboardRegistry;
    private final Corridor instance;
    private final SideboardMonitor monitor;
    /**
     * Constructs a new SideboardHandler instance -- this should only be done once during startup, and this single object will be reused throughout the plugins entire lifecycle.
     * @param config The {@link SideboardConfiguration} responsible for processing the configured boards'
     * @param instance central plugin instance
     */
    public SideboardHandler(SideboardConfiguration config, Corridor instance) {
        this.instance = instance;
        this.sideboardRegistry = new SideboardRegistry(config.buildAllFromConfig());
        this.monitor = new SideboardMonitor(sideboardRegistry);
    }

    public Corridor getInstance() {
        return instance;
    }

    public void openBoard(Player player, Class<? extends DisplayCondition> type) {
        final ThreadService service = instance.getThreadService();

        Runnable op = () -> {
            BiConsumer<Player,SideboardMeta> sendBoard = (plyr,brd) -> service.runBukkitTask(()->brd.forceBoardDisplay(plyr));
            Optional<SideboardMeta> validBoard = monitor.getCurrentBoard(player);

            if (validBoard.isPresent()) {
                sendBoard.accept(player,validBoard.get());
                return;
            }

            for (SideboardMeta board : sideboardRegistry.getSection(type)) {
                if (board.meetsConditions(player)) {
                    sendBoard.accept(player,board);
                }
            }
        };

        service.runAsync(op);
    }

    public void closeBoard(Player player) {
        instance.getThreadService().runBukkitTask(()->player.setScoreboard(Bukkit.getScoreboardManager().getNewScoreboard()));
    }
}
