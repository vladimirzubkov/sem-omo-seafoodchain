package cz.cvut.omo.sem.scm.seafood.type.role;

import cz.cvut.omo.sem.scm.seafood.util.YamlEnumHelper;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * Defines the strategic capabilities of an organization (Party).
 * Used to configure the supply chain nodes via Factory mapping.
 * <p>
 * Contains mapping logic to translate YAML strings (Archetypes) into technical Roles.
 */
public enum BusinessRoleType {

    // --- SOURCES ---
    PRODUCER("FISHER", "FARM", "CATCHER", "TRAWLER"), // Defines who creates value/items in the chain
    IMPORTER("IMPORTER", "SOURCE", "FOREIGN_SUPPLIER"), // d.w. works with money, delivers fish in big batches

    // --- PROCESSING & LOGISTICS ---
    PROCESSOR("PROCESSOR", "FACTORY", "PLANT", "MANUFACTURER"), // transform items
    STORAGE("WAREHOUSE", "COLD_STORAGE", "FREEZER_CENTER"), // store items
    TRANSPORT("DISTRIBUTOR", "LOGISTICS", "SHIPPING", "TRANSPORTER"), // moves items (Distributors imply transport + storage)

    // --- RETAIL ---
    MERCHANT("SHOP", "RETAILER", "MARKET", "TRADER"), // trades (usually a secondary role, but can be a primary config type)

    // --- CONSUMPTION ---
    RESTAURANT("RESTAURANT", "KITCHEN", "BISTRO", "CAFE", "BAR", "PUB"), // B2B: Large scale consumption + reselling
    CUSTOMER("CUSTOMER", "CLIENT", "INDIVIDUAL", "EATER", "BUYER"); // B2C: Small scale consumption + end of chain

    private final List<String> aliases;

    BusinessRoleType(String... aliases) {
        this.aliases = aliases != null
                ? Arrays.asList(aliases)
                : Collections.emptyList();
    }

    /**
     * Finds the primary BusinessRoleType based on the YAML configuration string.
     */
    public static Optional<BusinessRoleType> fromYamlString(String yamlType) {
        if (yamlType == null) return Optional.empty();

        String normalizedInput = YamlEnumHelper.normalize(yamlType);

        return Arrays.stream(values())
                .filter(role -> role.matches(normalizedInput))
                .findFirst();
    }

    private boolean matches(String normalizedInput) {
        return aliases.stream()
                // Reuse the normalization logic for aliases too
                .map(YamlEnumHelper::normalize)
                .anyMatch(normalizedAlias -> normalizedAlias.equals(normalizedInput));
    }

}