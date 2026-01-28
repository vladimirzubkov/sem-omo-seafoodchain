package cz.cvut.omo.sem.scm.seafood.type.role;

import cz.cvut.omo.sem.scm.seafood.util.YamlEnumHelper;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * Defines the job functions of an individual employee.
 * Used to assign tasks (Strategy Pattern) and calculate salaries.
 * <p>
 * This is a "Rich Enum" that contains logic for mapping configuration strings
 * (aliases) to specific enum constants using a unified normalization strategy.
 */
public enum LaborRoleType {
    MANUAL_LABOR("COOK", "WORKER", "FILLETER", "PACKER", "CHEF", "SOUS_CHEF"),
    MAINTENANCE("REPAIR_TECHNICIAN", "REPAIRTECHNICIAN", "MECHANIC", "TECHNICIAN"),
    MANAGEMENT("SCM_MANAGER", "SCMMANAGER", "DIRECTOR", "BOSS", "MANAGER"),
    INSPECTION("INSPECTOR", "AUDITOR", "QUALITY_CONTROL"),
    DRIVER("DRIVER", "LOGISTICS_DRIVER", "TRUCKER"),
    COURIER("COURIER", "DELIVERY_PERSON", "MESSENGER");

    private final List<String> aliases;

    LaborRoleType(String... aliases) {
        this.aliases = aliases != null
                ? Arrays.asList(aliases)
                : Collections.emptyList();
    }

    /**
     * Static lookup method.
     * Finds the correct LaborRoleType based on a string from the configuration file.
     *
     * @param yamlType The raw string from YAML (e.g., "Repair Technician").
     * @return An Optional containing the matching enum, or empty if not found.
     */
    public static Optional<LaborRoleType> fromYamlString(String yamlType) {
        if (yamlType == null) return Optional.empty();

        // Use the centralized normalization method from our Helper class
        String normalizedInput = YamlEnumHelper.normalize(yamlType);

        // Stream through all defined Enum constants (MANUAL_LABOR, MAINTENANCE, etc.).
        // NOTE: values() returns a simple array (LaborRoleType[]).
        // The Arrays.stream() utility method converts this array into a Stream,
        // enabling functional operations like .filter() and .findFirst(),
        // which are not available on standard arrays.
        return Arrays.stream(values())
                .filter(role -> role.matches(normalizedInput))
                .findFirst();
    }

    /**
     * Checks if the normalized input string matches any of the aliases for this role.
     *
     * @param normalizedInput The cleaned-up string from YAML.
     * @return true if it matches one of the aliases.
     */
    private boolean matches(String normalizedInput) {
        return aliases.stream()
                .map(YamlEnumHelper::normalize) // Apply the same helper normalization to aliases
                .anyMatch(normalizedAlias -> normalizedAlias.equals(normalizedInput));
    }
}