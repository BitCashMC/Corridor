package gg.bitcash.corridor.components.sideboard;

import org.bukkit.entity.Player;

import java.util.*;

public class SideboardMonitor {
    private final Map<UUID, Stack<SideboardMeta>> stackMap;
    private final SideboardRegistry registry;

    public SideboardMonitor(SideboardRegistry registry) {
        this.stackMap = new HashMap<>();
        this.registry = registry;
    }
    /**
     * This private method will be invoked whenever the public method {@link SideboardMonitor#getCurrentBoard(Player)} is invoked. It will determine whether the players active board
     * is still valid. If it is not, then it will overwrite that board with <code>null</code>. Conversely, if there was no entry for that Player, then it will register them with null as well.
     * @param player
     * @return whether the player's current board is still valid
     */
    private boolean checkCurrentBoard(Player player) {
        UUID uuid = player.getUniqueId();
        if (!stackMap.containsKey(uuid) || !stackMap.get(uuid).isEmpty()) {
            return false;
        }

        for (Stack<SideboardMeta> boards = stackMap.get(uuid); !boards.isEmpty(); boards.pop()) {
            if (boards.peek().meetsConditions(player)) {
                return true;
            }
        }
        return ;
    }
    /**
     * @param player
     * @return an {@link Optional} which may or may not contain the player's active board. Why wouldn't it? Because it will only fetch the value corresponding to that UUID if it is NOT null.
     */
    public Optional<SideboardMeta> getCurrentBoard(Player player) {
        return checkCurrentBoard(player) ? Optional.of(stackMap.get(player.getUniqueId()).peek()) : Optional.empty();
    }
}
