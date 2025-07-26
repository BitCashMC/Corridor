package gg.bitcash.corridor.components.sideboard.events;

import gg.bitcash.corridor.components.sideboard.SideboardHandler;
import gg.bitcash.corridor.components.sideboard.SideboardMeta;
import gg.bitcash.corridor.components.sideboard.displaycondition.DisplayCondition;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class ScoreboardChangeEvent extends Event implements Cancellable {

    private static final HandlerList HANDLERS = new HandlerList();
    private final Player player;
    private boolean isCancelled;
    private final Class<? extends DisplayCondition> type;
    private final SideboardHandler handler;

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }

    public ScoreboardChangeEvent(SideboardHandler handler, Player player, Class<? extends DisplayCondition> type) {
        this.player = player;
        this.isCancelled = false;
        this.type = type;
        this.handler = handler;

        handler.getMonitor().getCurrentBoard(player).ifPresentOrElse(b->this.setCancelled(true),() -> {
            handler.closeActiveBoardFromPlayer(player);
            for (SideboardMeta board : handler.getSideboardRegistry().getSection(type)) {
                if (handler.getMonitor().tryUpdatingCurrentBoard(player,board)) {
                    handler.openActiveBoardForPlayer(player);
                    return;
                }
            }
        });
    }

    public ScoreboardChangeEvent(SideboardHandler handler, Player player) {
        this.player = player;
        this.isCancelled = false;
        this.type = null;
        this.handler = handler;
        handler.getMonitor().getCurrentBoard(player).ifPresent(b->player.setScoreboard(b.getScoreboard()));
    }

    @Override
    public boolean isCancelled() {
        return this.isCancelled;
    }

    @Override
    public void setCancelled(boolean isCancelled) {
        this.isCancelled = isCancelled;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLERS;
    }

    public Player getPlayer() {
        return player;
    }
}
