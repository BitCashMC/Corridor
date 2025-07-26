package gg.bitcash.corridor.components.sideboard.config;

import gg.bitcash.corridor.components.sideboard.SideboardHandler;
import gg.bitcash.corridor.components.sideboard.SideboardMeta;
import gg.bitcash.corridor.components.sideboard.displaycondition.DisplayCondition;
import net.kyori.adventure.text.Component;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.yaml.snakeyaml.error.YAMLException;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class SideboardConfiguration {

    private final FileConfiguration config;

    public SideboardConfiguration(FileConfiguration config) {
        this.config = config;
    }

    public Objective loadTitle(Objective objective, String board) {
        String s = config.getString(keyPath(board,"title"));
        if (s == null) {
            throw new YAMLException("key \"title\" is missing for board \""+board+"\"");
        }
        objective.displayName(Component.text(s));
        return objective;
    }

    public Objective loadBody(Objective objective, String board) {
        List<String> body = config.getStringList(keyPath(board,"body"));

        if (body.isEmpty()) {
            throw new YAMLException("key \"body\" is missing for board \""+board+"\"");
        }
        if (body.size() > 16)
            throw new UnsupportedOperationException("Configured scoreboard cannot have more than 16 lines");

        int i = body.size()-1;
        for (String line : body) {
            Score score = objective.getScore("line #"+i);
            score.setScore(i--);
            score.customName(Component.text(line));
        }
        return objective;
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

    public List<SideboardMeta> buildAllFromConfig(SideboardHandler handler) {
        List<SideboardMeta> boards = new ArrayList<>();
        for (String key : getAllBoardKeys()) {
            List<DisplayCondition> conditions = loadDisplayConditions(key);
            SideboardMeta board = new SideboardMeta(handler,key,conditions);
            Objective obj = board.getObjective();
            loadTitle(obj,key);
            loadBody(obj,key);

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
