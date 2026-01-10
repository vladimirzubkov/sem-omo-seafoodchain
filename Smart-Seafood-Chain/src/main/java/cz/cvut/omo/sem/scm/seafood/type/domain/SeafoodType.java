package cz.cvut.omo.sem.scm.seafood.type.domain;

/**
 * Enumeration of all supported seafood types across different simulation scenarios.
 */
public enum SeafoodType {
    // Standard types
    SALMON,
    TUNA,
    COD,
    SHRIMP,
    HERRING,

    // Added for 'Russian Sea Dominator' scenario (Barents/Kara Sea)
    HADDOCK,
    KING_CRAB,
    NAVAGA,
    FLOUNDER,
    POLAR_COD,
    ARCTIC_CHAR,

    // Added for 'Nordic Fresh' and 'Baltic Frozen' scenarios
    MACKEREL,
    HALIBUT,
    SPRAT,
    PERCH,

    // Added for 'Prague Sushi Luxury' scenario (Imports)
    YELLOWTAIL,
    OCTOPUS
}