package gg.bitcash.corridor.components.sideboard;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.yaml.snakeyaml.error.YAMLException;

import java.util.List;
import java.util.Map;
import java.util.Set;

public class SideboardConfiguration {

    private FileConfiguration config;

    public SideboardConfiguration(FileConfiguration config) {
        this.config = config;
    }

    public String loadTitle(String board) {
        return config.getString(keyPath(board,"title"));
    }

    public List<String> loadBody(String board) {
        List<String> body = config.getStringList(keyPath(board,"body"));
        if (body.size() > 16)
            throw new UnsupportedOperationException("Configured scoreboard cannot have more than 16 lines");
        return body;
    }

    public Map<String,Object> loadDisplayConditions(String board) {
        ConfigurationSection section = config.getConfigurationSection(keyPath(board,"displayConditions"));
        return section != null ? section.getValues(false) : null;
    }

    private String keyPath(String board, String key) {
        return String.join(".","scoreboards",board,key);
    }

    protected Set<String> loadBoardNames() {
        ConfigurationSection section = config.getConfigurationSection("scoreboards");
        return section != null ? section.getKeys(false) : null;

    }


}
