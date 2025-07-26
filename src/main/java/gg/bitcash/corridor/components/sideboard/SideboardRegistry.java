package gg.bitcash.corridor.components.sideboard;

import gg.bitcash.corridor.components.sideboard.displaycondition.DisplayCondition;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SideboardRegistry {
    private final Map<Class<? extends DisplayCondition>, List<SideboardMeta>> conditionsMap;
    private List<SideboardMeta> asList;
    public SideboardRegistry() {
        this.conditionsMap = new HashMap<>();
    }

    /**
     * Temporary function for now:
     */
    public List<SideboardMeta> asList() {
        return asList;
    }
    public void putAsList(List<SideboardMeta> list) {
        this.asList = list;
    }

    public void register(SideboardMeta board) {
        for (DisplayCondition condition : board.getDisplayConditions()) {
            conditionsMap.computeIfAbsent(condition.getClass(),k->new ArrayList<>()).add(board);
        }
    }

    public List<SideboardMeta> getSection(Class<? extends DisplayCondition> key) {
        List<SideboardMeta> section = conditionsMap.getOrDefault(key, null);
        if (section == null)
            throw new IllegalStateException("No such condition has been registered @ " + key.getName());
        return section;
    }
}
