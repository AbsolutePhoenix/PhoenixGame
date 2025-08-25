package com.absolutephoenix.engine.input;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class MappingRegistryTest {
    @Test
    void defaultMappingResolvesDualSenseTrackpad() {
        Integer idx = MappingRegistry.resolve("id1", "dualsense wireless controller", MappingRegistry.Logical.PS_TRACKPAD);
        assertEquals(13, idx);
    }

    @Test
    void learnedMappingOverridesDefault() {
        String key = "id2";
        MappingRegistry.learn(key, MappingRegistry.Logical.PS_TRACKPAD, 5);
        Integer idx = MappingRegistry.resolve(key, "dualsense", MappingRegistry.Logical.PS_TRACKPAD);
        assertEquals(5, idx);
    }

    @Test
    void resolveReturnsNullForUnknownControl() {
        Integer idx = MappingRegistry.resolve("id3", "unknown controller", MappingRegistry.Logical.PS_MUTE);
        assertNull(idx);
    }
}
