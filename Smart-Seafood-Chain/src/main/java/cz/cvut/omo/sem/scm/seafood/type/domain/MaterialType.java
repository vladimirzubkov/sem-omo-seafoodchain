package cz.cvut.omo.sem.scm.seafood.type.domain;

import lombok.Getter;

@Getter
public enum MaterialType {
    // Ingredients
    SUSHI_RICE("Sushi Rice"),
    RICE_VINEGAR("Rice Vinegar"),
    SALT("Sea Salt"),
    NORI_SHEET("Nori Seaweed"),
    WASABI("Wasabi Paste"),
    SOY_SAUCE("Soy Sauce"),
    OIL("Cooking Oil"),
    ICE("Dry Ice"),

    // Packaging
    PLASTIC_TRAY("Plastic Tray"),
    CARDBOARD_BOX("Cardboard Box"),
    PALLET("Wooden Pallet");

    private final String prettyName;

    MaterialType(String prettyName) {
        this.prettyName = prettyName;
    }
}