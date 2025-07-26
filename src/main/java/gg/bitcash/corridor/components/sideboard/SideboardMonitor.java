package gg.bitcash.corridor.components.sideboard;

import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public class SideboardMonitor {
    private final Map<UUID,SideboardMeta> playerCurrentBoard;
    private final SideboardHandler handler;

    public SideboardMonitor(SideboardHandler handler) {
        this.playerCurrentBoard = new HashMap<>();
        this.handler = handler;
    }
    /**
     * Attempts to set the Player's current board to the passed one, possibly overwriting an existing board in the process.
     * @param player
     * @param board
     * @return <code>true</code> if player was eligible and now possesses that board as their active one, or <code>false</code> if they did not meet the needed conditions.
     */
    public boolean tryUpdatingCurrentBoard(Player player, SideboardMeta board) {
        if (board.meetsConditions(player)) {
            //Add (or replace) the players active board to the Map.
            playerCurrentBoard.put(player.getUniqueId(),board);
            return true;
        }
        return false;
    }
    /**
     * This private method will be invoked whenever the public method {@link SideboardMonitor#getCurrentBoard(Player)} is invoked. It will determine whether the players active board
     * is still valid. If it is not, then it will overwrite that board with <code>null</code>. Conversely, if there was no entry for that Player, then it will register them with null as well.
     * @param player
     * @return whether the player's current board is still valid
     */
    private boolean checkCurrentBoard(Player player) {
        UUID uuid = player.getUniqueId();
        if (playerCurrentBoard.computeIfAbsent(uuid, k->null) == null) {
            return false;
        }
        if (!playerCurrentBoard.get(uuid).meetsConditions(player)) {
            playerCurrentBoard.put(uuid,null);
            return false;
        }
        return true;
    }
    /**
     * @param player
     * @return an {@link Optional} which may or may not contain the player's active board. Why wouldn't it? Because it will only fetch the value corresponding to that UUID if it is NOT null.
     */
    public Optional<SideboardMeta> getCurrentBoard(Player player) {
        return checkCurrentBoard(player) ? Optional.of(playerCurrentBoard.get(player.getUniqueId())) : Optional.empty();
    }
}
