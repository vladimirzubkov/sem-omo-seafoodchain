package cz.cvut.omo.sem.scm.seafood.util;

import java.util.Arrays;
import java.util.Optional;

/**
 * Utility class to handle unified string normalization for YAML configuration parsing.
 * Eliminates duplicate logic across multiple Enum classes.
 */
public class YamlEnumHelper {

    /**
     * Private constructor to prevent instantiation of utility class.
     */
    private YamlEnumHelper() {}

    /**
     * Normalizes a raw string:
     * 1. Converts to Uppercase.
     * 2. Removes spaces.
     * 3. Removes underscores.
     *
     * @param raw The raw input string (e.g., "Blast_Freezer").
     * @return The normalized string (e.g., "BLASTFREEZER").
     */
    public static String normalize(String raw) {
        if (raw == null) return "";
        return raw.toUpperCase()
                .replace(" ", "")
                .replace("_", "");
    }

    /**
     * Generic method to find an Enum constant by matching its normalized name.
     * This works for strict 1-to-1 mapping (e.g., DeviceType).
     *
     * @param rawInput The raw string from YAML.
     * @param enumClass The class of the Enum to search in.
     * @param <E> The Enum type.
     * @return An Optional containing the matching Enum constant, or empty.
     */
    public static <E extends Enum<E>> Optional<E> resolve(String rawInput, Class<E> enumClass) {
        if (rawInput == null) return Optional.empty();

        String normalizedInput = normalize(rawInput);

        return Arrays.stream(values(enumClass))
                .filter(constant -> normalize(constant.name()).equals(normalizedInput))
                .findFirst();
    }

    // Helper to get enum constants safely
    private static <E extends Enum<E>> E[] values(Class<E> enumClass) {
        return enumClass.getEnumConstants();
    }
}