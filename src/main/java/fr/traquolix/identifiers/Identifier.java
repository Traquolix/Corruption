package fr.traquolix.identifiers;

import lombok.Getter;
import lombok.Setter;
import net.minestom.server.tag.Tag;

import java.util.Objects;

/**
 * The Identifier class represents an identifier for a specific object in the game.
 * An identifier is a string that is used to uniquely identify an object.
 * It follows the format "[projectName]:[group]:[identifier]".
 * For example: "corruption:skill:mining", "corruption:item:wheat", "corruption:item:trick_or_treat".
 * Some rules for identifiers:
 * - No spaces (use "_" instead)
 * - No uppercase letters
 * - No numbers
 * - Avoid using the same name twice
 * - Avoid using plural forms
 */
@SuppressWarnings("GrazieInspection")
@Getter
@Setter
public class Identifier {

    /**
     * The name of the project.
     */
    final String projectName = "corruption";

    /**
     * The group name of the identifier.
     */
    String group;

    /**
     * The unique identifier string.
     */
    String id;

    /**
     * The global tag used to associate identifiers with game objects.
     */
    @Getter
    static Tag<String> globalTag = Tag.String("corruption_identifier");

    @Getter
    static Tag<Boolean> naturalTag = Tag.Boolean("natural");

    /**
     * Constructs an Identifier with the given group and id.
     *
     * @param group The group name of the identifier.
     * @param id    The unique identifier string.
     */
    public Identifier(String group, String id) {
        this.group = group;
        this.id = id;
    }

    /**
     * Constructs an Identifier from the given full identifier string.
     * The full identifier string should be in the format "[projectName]:[group]:[identifier]".
     *
     * @param fullIdentifier The full identifier string.
     */
    public Identifier(String fullIdentifier) {
        String[] split = fullIdentifier.split(":");
        this.group = split[1];
        this.id = split[2];
    }

    /**
     * Checks if this Identifier is equal to another object.
     * Two identifiers are considered equal if their group and id are the same.
     *
     * @param o The object to compare with.
     * @return true if the objects are equal, false otherwise.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Identifier that = (Identifier) o;
        return Objects.equals(group, that.group) &&
                Objects.equals(id, that.id);
    }

    @Override
    public String toString() {
        return projectName + ":" + group + ":" + id;
    }

    /**
     * Returns the hash code of this Identifier based on its group and id.
     *
     * @return The hash code of this Identifier.
     */
    @Override
    public int hashCode() {
        return Objects.hash(group, id);
    }
}
