package me.nedayazady.globalstafflogger.utils;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;

public class ColorUtils {

    // Legacy Serializer supporting ampersand (&) and hex codes via section symbol (&#RRGGBB)
    private static final LegacyComponentSerializer LEGACY_SERIALIZER = LegacyComponentSerializer.builder()
            .character('&')
            .hexColors()
            .useUnusualXRepeatedCharacterHexFormat()
            .build();

    // MiniMessage Serializer
    private static final MiniMessage MINIMESSAGE = MiniMessage.miniMessage();

    /**
     * Parses a string that may contain MiniMessage tags or legacy color codes (&a, &#ff0000).
     *
     * @param message The raw string to parse.
     * @return The parsed Adventure Component.
     */
    public static Component parse(String message) {
        if (message == null) return Component.empty();

        // 1. Convert Legacy Ampersand (&) and Hex to MiniMessage Tags so they can be processed together.
        // We do this by deserializing legacy to a Component, then serializing it to MiniMessage strings.
        Component legacyParsed = LEGACY_SERIALIZER.deserialize(message);
        String miniMessageString = MINIMESSAGE.serialize(legacyParsed);

        // 2. Deserialize the combined output using MiniMessage
        return MINIMESSAGE.deserialize(miniMessageString);
    }
}
