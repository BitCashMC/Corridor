package gg.bitcash.corridor.components.sideboard;

import gg.bitcash.corridor.components.sideboard.displaycondition.DisplayCondition;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.scoreboard.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class SideboardMeta {

    private final String name;
    private final Scoreboard scoreboard;
    private final Objective objective;
    private final List<DisplayCondition> displayConditions;
    private final SideboardHandler handler;

    public SideboardMeta(SideboardHandler handler, String name) {
        this.name = name;
        this.handler = handler;
        this.displayConditions = new ArrayList<>();
        this.scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
        this.objective = scoreboard.registerNewObjective("CORRIDOR_SIDEBOARD",Criteria.DUMMY,Component.empty());
        objective.setDisplaySlot(DisplaySlot.SIDEBAR);
        setTitle();
        setBody();
        setDisplayConditions();
    }

    public String getName() {
        return name;
    }

    public List<DisplayCondition> getDisplayConditions() {
        return Collections.unmodifiableList(displayConditions);
    }

    public Scoreboard getScoreboard() {
        return scoreboard;
    }

    private void setTitle() {
        objective.displayName(Component.text(handler.getConfiguration().loadTitle(name)));
    }

    private void setBody() {
        List<String> body = handler.getConfiguration().loadBody(name);

        int j = 0;
        for (int i = body.size()-1; i>=0; i--) {
            Score s = objective.getScore("Line #"+i);
            s.customName(Component.text(body.get(i)));
            s.setScore(j++);
        }
    }

    private void setDisplayConditions() {
        Map<String,Object> conditions = handler.getConfiguration().loadDisplayConditions(name);
        for (Map.Entry<String,Object> entry : conditions.entrySet()) {
            DisplayCondition condition = handler.getConditionRegistry().get(entry.getKey());
            condition.setValue((String) entry.getValue());
            displayConditions.add(condition);
        }
    }
}
