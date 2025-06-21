package gg.bitcash.corridor.components.datamanager.players.eventlisteners;

import gg.bitcash.corridor.Corridor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.Optional;
import java.util.UUID;

public class PlayerUsernameChangeListener implements Listener {

    private final Corridor instance;

    public PlayerUsernameChangeListener(Corridor instance) {
        this.instance = instance;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        UUID uuid = player.getUniqueId();
        String username;

        Optional<String> usernameOptional = instance.getPlayerDataService().getUsername(uuid);
        if (usernameOptional.isPresent()) {
            username = usernameOptional.get();
            if (username.equals(player.getName())) {
                return;
            }
        }
        instance.getPlayerDataService().putPlayer(uuid,player.getName());
    }
}
