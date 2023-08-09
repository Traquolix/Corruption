package fr.traquolix.content.items;

import lombok.Getter;

/**
 * The {@code ItemType} enum represents different types of items.
 */
@Getter
public enum ItemType {

    /**
     * Represents a pickaxe type.
     */
    PICKAXE("pickaxe"),

    /**
     * Represents an axe type.
     */
    AXE("axe"),

    /**
     * Represents a sword type.
     */
    SWORD("sword"),

    /**
     * Represents a shovel type.
     */
    SHOVEL("shovel"),

    /**
     * Represents a hoe type.
     */
    HOE("hoe"),

    /**
     * Represents a helmet type.
     */
    HELMET("helmet"),

    /**
     * Represents a chestplate type.
     */
    CHESTPLATE("chestplate"),

    /**
     * Represents leggings type.
     */
    LEGGINGS("leggings"),

    /**
     * Represents boots type.
     */
    BOOTS("boots"),

    /**
     * Represents a bow type.
     */
    BOW("bow"),

    /**
     * Represents a crossbow type.
     */
    CROSSBOW("crossbow"),

    /**
     * Represents a trident type.
     */
    TRIDENT("trident"),

    /**
     * Represents a shield type.
     */
    SHIELD("shield"),

    /**
     * Represents a generic item type.
     */
    ITEM("item"),

    /**
     * Represents an undefined or none type.
     */
    NONE("");

    /**
     * The name of the item type.
     * -- GETTER --
     *  Get the name of the item type.
     */
    private final String name;

    /**
     * Constructor to set the name for each item type.
     *
     * @param name The name of the item type.
     */
    ItemType(String name) {
        this.name = name;
    }

    public boolean isArmor() {
        return this == HELMET || this == CHESTPLATE || this == LEGGINGS || this == BOOTS;
    }
}
