package com.absolutephoenix.engine.input;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class MappingRegistry {
    static final class Logical {
        static final String PS_TRACKPAD = "ps.trackpad";
        static final String PS_MUTE     = "ps.mute";
    }

    private record NameRule(String substr, Map<String,Integer> map) {}

    // Learned mappings (persist them yourself if you want across runs)
    private static final Map<String, Map<String, Integer>> LEARNED = new ConcurrentHashMap<>();

    // Defaults keyed by name substrings. Adjust if other OS/driver combos expose different indices.
    private static final List<NameRule> DEFAULTS = List.of(
            // More specific first
            new NameRule("dualsense edge", Map.of(Logical.PS_TRACKPAD, 13, Logical.PS_MUTE, 14)),
            new NameRule("dualsense", Map.of(Logical.PS_TRACKPAD, 13, Logical.PS_MUTE, 14)),
            new NameRule("dualshock", Map.of(Logical.PS_TRACKPAD, 13, Logical.PS_MUTE, -1))


    );

    static Integer resolve(String identityKey, String normalizedName, String logicalControl) {
        Map<String, Integer> learned = LEARNED.get(identityKey);
        if (learned != null && learned.containsKey(logicalControl)) return learned.get(logicalControl);

        for (NameRule r : DEFAULTS) {
            if (normalizedName.contains(r.substr)) {
                Integer idx = r.map.get(logicalControl);
                if (idx != null) return idx;
            }
        }
        return null;
    }

    static void learn(String identityKey, String logicalControl, int rawIndex) {
        LEARNED.computeIfAbsent(identityKey, k -> new ConcurrentHashMap<>())
                .put(logicalControl, rawIndex);
    }
}
