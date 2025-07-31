package gg.bitcash.corridor.components.sideboard;

import gg.bitcash.corridor.components.sideboard.displaycondition.DisplayCondition;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.*;

import java.util.*;

public class SideboardMeta {

    private final String name;
    private final Scoreboard scoreboard;
    private final Objective objective;
    private final List<DisplayCondition> displayConditions;

    public SideboardMeta(String name, TextComponent title, List<TextComponent> body, List<DisplayCondition> conditions) {
        this.name = name;
        this.displayConditions = conditions;
        this.scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
        this.objective = scoreboard.registerNewObjective("CORRIDOR_SIDEBOARD",Criteria.DUMMY,Component.empty());
        objective.setDisplaySlot(DisplaySlot.SIDEBAR);
        objective.displayName(title);

        for (int )
    }

    public String getName() {
        return name;
    }

    public List<DisplayCondition> getDisplayConditions() {
        return Collections.unmodifiableList(displayConditions);
    }

    protected void forceBoardDisplay(Player player) {
        player.setScoreboard(scoreboard);
    }

    public Objective getObjective() {
        return objective;
    }

    protected boolean meetsConditions(Player player) {
        for (DisplayCondition condition : getDisplayConditions()) {
            if (!condition.shouldDisplay(player)) return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return name;
    }
}
