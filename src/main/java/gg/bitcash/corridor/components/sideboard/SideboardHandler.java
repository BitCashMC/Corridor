package gg.bitcash.corridor.components.sideboard;

import gg.bitcash.corridor.components.sideboard.displaycondition.DisplayCondition;
import gg.bitcash.corridor.components.sideboard.displaycondition.PermissionCondition;
import gg.bitcash.corridor.components.sideboard.displaycondition.WorldCondition;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Scoreboard;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

public class SideboardHandler {

    private DisplayCondition.ConditionRegistry conditionRegistry;
    private SideboardConfiguration config;
    private Set<SideboardMeta> sideboards;

    public SideboardHandler(SideboardConfiguration config) {
        this.config = config;
        this.conditionRegistry = new DisplayCondition.ConditionRegistry();
        registerDefaultConditions();
        this.sideboards = config.loadBoardNames().stream().map(name->new SideboardMeta(this,name)).collect(Collectors.toUnmodifiableSet());
    }

    private void registerDefaultConditions() {
        conditionRegistry.register("world",()->new WorldCondition(null));
        conditionRegistry.register("permission",()->new PermissionCondition(null));
    }

    public SideboardConfiguration getConfiguration() {
        return config;
    }

    public DisplayCondition.ConditionRegistry getConditionRegistry() {
        return conditionRegistry;
    }

    public Set<SideboardMeta> getSideboards() {
        return sideboards;
    }

    protected boolean meetsConditions(Player player, SideboardMeta board) {
        for (DisplayCondition condition : board.getDisplayConditions()) {
            if (!condition.shouldDisplay(player)) return false;
        }
        return true;
    }

    public Optional<SideboardMeta> findEligibleBoard(Player player, String boardName) {
        return sideboards.stream()
                .filter(board->board.getName().equalsIgnoreCase(boardName) && meetsConditions(player,board))
                .findFirst();
    }

    public void closeBoardFromPlayer(Player player) {
        player.getScoreboard().clearSlot(DisplaySlot.SIDEBAR);
    }



}
