package gg.bitcash.corridor.components.sideboard.eventlisteners;

import gg.bitcash.corridor.components.sideboard.SideboardHandler;
import gg.bitcash.corridor.components.sideboard.displaycondition.WorldCondition;
import gg.bitcash.corridor.components.sideboard.events.ScoreboardChangeEvent;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerJoinEvent;

public class ScoreboardDisplayListener implements Listener {

    private final SideboardHandler handler;

    public ScoreboardDisplayListener(SideboardHandler handler) {
        this.handler = handler;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        ScoreboardChangeEvent changeEvent = new ScoreboardChangeEvent(handler,player);
        handler.getInstance().getServer().getPluginManager().callEvent(changeEvent);
    }

    @EventHandler
    public void onWorldChange(PlayerChangedWorldEvent event) {
        Player player = event.getPlayer();
        ScoreboardChangeEvent changeEvent = new ScoreboardChangeEvent(handler,player, WorldCondition.class);
        handler.getInstance().getServer().getPluginManager().callEvent(changeEvent);
    }

}
