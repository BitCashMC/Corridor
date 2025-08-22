package gg.bitcash.corridor.components.sideboard;

import gg.bitcash.corridor.components.sideboard.displaycondition.DisplayCondition;

import java.util.*;

public class SideboardRegistry {
    private final Map<Class<? extends DisplayCondition>, List<SideboardMeta>> conditionsMap;
    private final List<SideboardMeta> boardList;

    public SideboardRegistry(List<SideboardMeta> boardList) {
        this.conditionsMap = new HashMap<>();
        this.boardList = boardList;

        for (SideboardMeta board : boardList) {
            register(board);
        }
    }

    public List<SideboardMeta> asList() {
        return boardList;
    }

    private void register(SideboardMeta board) {
        for (DisplayCondition condition : board.getDisplayConditions()) {
            conditionsMap.computeIfAbsent(condition.getClass(),k->new ArrayList<>()).add(board);
        }
    }

    public List<SideboardMeta> getSection(Class<? extends DisplayCondition> key) {
        List<SideboardMeta> section = conditionsMap.getOrDefault(key, null);
        if (section == null)
            throw new IllegalArgumentException("No such condition exists: " + key.getName());
        return section;
    }
}
