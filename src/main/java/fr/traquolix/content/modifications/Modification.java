package fr.traquolix.content.modifications;

import lombok.Getter;

/**
 * The {@code Modification} enum represents different modifications that can be applied to items.
 *
 * <p>Each modification in this enum indicates a specific modification that can be applied to an item.
 * For example, an item can be "recombobulated" or "reforged". These modifications affect the properties
 * or attributes of the item when applied.</p>
 */
@Getter
public enum Modification {

    /**
     * The "recombobulated" modification. This modification indicates that an item has been recombobulated.
     * Recombobulation is a process that enhances the item's attributes or properties.
     */
    RECOMBOBULATED("recombobulated"),

    /**
     * The "reforged" modification. This modification indicates that an item has been reforged.
     * Reforging allows the item to be re-rolled with new attributes or properties.
     */
    REFORGED("reforged");

    /**
     * The value representing the modification.
     * The value is a string representation of the modification name.
     */
    private final String value;

    /**
     * Constructor to set the value of each modification.
     *
     * @param name The name of the modification.
     */
    Modification(String name) {
        this.value = name;
    }
}
