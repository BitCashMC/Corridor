package gg.bitcash.corridor.components.datamanager.players.eventlisteners;

import gg.bitcash.corridor.Corridor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

public class PlayerUsernameChangeListener implements Listener {

    private final Corridor instance;

    public PlayerUsernameChangeListener(Corridor instance) {
        this.instance = instance;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        UUID uuid = player.getUniqueId();

        Bukkit.getScheduler().runTaskAsynchronously(instance,()->{
            try {
                Optional<String> usernameOptional = instance.getDataSource().getPlayerDAO().fetchUsername(uuid).get();
                String username;
                if (usernameOptional.isPresent()) {
                    username = usernameOptional.get();
                    if (username.equals(player.getName())) {
                        return;
                    }
                    instance.getDataSource().getPlayerDAO().putPlayer(uuid, player.getName());
                }
            } catch (InterruptedException | ExecutionException e) {
                throw new RuntimeException(e);
            }
        });
    }
}
