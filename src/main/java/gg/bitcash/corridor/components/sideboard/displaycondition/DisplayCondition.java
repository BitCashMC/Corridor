package gg.bitcash.corridor.components.sideboard.displaycondition;

import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public interface DisplayCondition {

     class ConditionRegistry {
        private final Map<String,Supplier<DisplayCondition>> conditionsRegistry;

        public ConditionRegistry() {
            this.conditionsRegistry = new HashMap<>();
        }

        public void register(String key, Supplier<DisplayCondition> supplier) {
            this.conditionsRegistry.put(key,supplier);
        }

        public DisplayCondition get(String key) {
            if (!conditionsRegistry.containsKey(key)) {
                throw new IllegalArgumentException("No registered condition found under the name: " + key);
            }
            return conditionsRegistry.get(key).get();
        }
    }

    boolean shouldDisplay(Player player);

    void setValue(String value);
}
