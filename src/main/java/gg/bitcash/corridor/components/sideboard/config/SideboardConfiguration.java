package gg.bitcash.corridor.components.sideboard.config;

import gg.bitcash.corridor.components.sideboard.SideboardHandler;
import gg.bitcash.corridor.components.sideboard.SideboardMeta;
import gg.bitcash.corridor.components.sideboard.displaycondition.DisplayCondition;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.yaml.snakeyaml.error.YAMLException;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

public class SideboardConfiguration {

    private final FileConfiguration config;

    public SideboardConfiguration(FileConfiguration config) {
        this.config = config;
    }

    public TextComponent loadTitle(String board) {
        String title = config.getString(keyPath(board,"title"));
        if (title == null) {
            throw new YAMLException("key \"title\" is missing for board \""+board+"\"");
        }
        return Component.text(title);
    }

    public List<TextComponent> loadBody(String board) {
        List<String> body = config.getStringList(keyPath(board,"body"));

        if (body.isEmpty()) {
            throw new YAMLException("key \"body\" is missing for board \""+board+"\"");
        }
        if (body.size() > 16)
            throw new UnsupportedOperationException("Configured scoreboard cannot have more than 16 lines");

        List<TextComponent> parsedBody = new ArrayList<>(body.stream().map(Component::text).toList());
        Collections.reverse(parsedBody);
        return parsedBody;
    }

    protected List<DisplayCondition> loadDisplayConditions(String board) {
        ConfigurationSection section = config.getConfigurationSection(keyPath(board, "displayConditions"));
        if (section == null) return List.of();

        List<DisplayCondition> displayConditions = new ArrayList<>();

        for (String key : section.getKeys(false)) {
            String className = section.getString(key + ".class");
            String value = section.getString(key + ".value");

            try {
                Class<?> loaded = Class.forName(className);
                if (!DisplayCondition.class.isAssignableFrom(loaded)) continue;

                Constructor<?> constructor = loaded.getConstructor(String.class);
                DisplayCondition condition = (DisplayCondition) constructor.newInstance(value);
                displayConditions.add(condition);
            } catch (Exception e) {
                // log failure
            }
        }
        return displayConditions;
    }

    public List<SideboardMeta> buildAllFromConfig() {
        List<SideboardMeta> boards = new ArrayList<>();
        for (String key : getAllBoardKeys()) {
            SideboardMeta board = new SideboardMeta(key,loadTitle(key),loadBody(key),loadDisplayConditions(key));
            boards.add(board);
        }
        return boards;
    }

    private Set<String> getAllBoardKeys() {
        ConfigurationSection section = config.getConfigurationSection("scoreboards");
        return section != null ? section.getKeys(false) : Set.of();
    }

    private String keyPath(String board, String key) {
        return String.join(".","scoreboards",board,key);
    }

}
