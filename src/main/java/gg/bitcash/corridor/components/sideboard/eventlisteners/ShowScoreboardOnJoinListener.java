package gg.bitcash.corridor.components.sideboard.eventlisteners;

import gg.bitcash.corridor.Corridor;
import gg.bitcash.corridor.components.sideboard.SideboardHandler;
import gg.bitcash.corridor.components.sideboard.SideboardMeta;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.Optional;

public class ShowScoreboardOnJoinListener implements Listener {

    private final SideboardHandler handler;

    public ShowScoreboardOnJoinListener(SideboardHandler handler) {
        this.handler = handler;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        Optional<SideboardMeta> boardOpt = handler.findEligibleBoard(player,"example");
        boardOpt.ifPresent(p->player.setScoreboard(p.getScoreboard()));
    }
}
