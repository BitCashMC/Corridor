package gg.bitcash.corridor.components.datamanager.players;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public class PlayerDataService {

    private final PlayerDAO playerDAO;
    private final Map<UUID,String> playersMap;

    public PlayerDataService(PlayerDAO playerDAO) {
        this.playerDAO = playerDAO;
        this.playersMap = loadFromDatabase();
    }

    private Map<UUID,String> loadFromDatabase() {
        return this.playerDAO.fetchAll();
    }

    public boolean saveToDatabase() {
        return this.playerDAO.putAll(this.playersMap);
    }

    public void putPlayer(UUID uuid, String username) {
        this.playersMap.put(uuid,username);
    }

    public Optional<String> getUsername(UUID uuid) {
        return this.playerExists(uuid) ? Optional.of(this.playersMap.get(uuid)) : Optional.empty();
    }

    public Optional<UUID> getUUID(String username) {
        return this.playersMap.entrySet().stream()
                .filter(entry->entry.getValue().equals(username))
                .map(Map.Entry::getKey)
                .findFirst();
    }

    public boolean playerExists(UUID uuid) {
        return this.playersMap.containsKey(uuid);
    }

}
