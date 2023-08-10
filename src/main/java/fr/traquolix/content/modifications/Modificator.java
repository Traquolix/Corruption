package fr.traquolix.content.modifications;

import fr.traquolix.content.generalities.identifiers.Identifier;
import fr.traquolix.content.items.AbstractItem;
import fr.traquolix.content.items.ItemRegistry;
import fr.traquolix.content.items.PureItem;
import lombok.Getter;
import net.minestom.server.item.ItemStack;
import net.minestom.server.tag.Tag;

import java.util.Objects;

/**
 * The {@code Modificator} class is used to add modifications to items.
 */
@Getter
public class Modificator {

    /**
     * The default constructor for the {@code Modificator} class.
     */

    public Modificator() {}
    @Getter
    final static Tag<String> modificationsTag = Tag.String("modifications");

    /**
     * Adds a specific modification to the given item stack.
     *
     * @param itemStack    The item stack to add the modification to.
     * @param modification The modification to add.
     * @return The modified item stack.
     */
    private static ItemStack addModification(ItemStack itemStack, Modification modification) {
        // Get the AbstractItem corresponding to the item stack's identifier
        AbstractItem abstractItem = ItemRegistry.getInstance().getItem(new Identifier(itemStack.getTag(Identifier.getGlobalTag())));

        if (!itemStack.hasTag(modificationsTag)) {
            // Add modification tag to item if it doesn't have one
            itemStack = itemStack.withTag(modificationsTag, "none");
        }

        String modifications = itemStack.getTag(modificationsTag);

        if (modifications.contains(modification.getValue())) {
            // Item already has this modification, return the original item stack
            return itemStack;
        }

        if (Objects.equals(modifications, "none")) {
            modifications = modification.getValue();
        } else {
            modifications = modifications + "," + modification.getValue();
        }

        // Check if the item is a pure item or not
        if (abstractItem == null) {
            // Pure item
            if (modification == Modification.REFORGED) {
                // Can't be reforged because pure items can't be reforged, return the original item stack
                return itemStack;
            } else if (modification == Modification.RECOMBOBULATED) {
                // Will be uncommon, return a pure item with the next rarity
                return PureItem.getPureItem(itemStack, PureItem.RARITY.getNextRarity());
            }
        } else {
            // Not a pure item
            if (!abstractItem.isReforgeable() && modification == Modification.REFORGED) {
                // Can't be reforged because the item is not reforgeable, return the original item stack
                return itemStack;
            }
            if (modification == Modification.RECOMBOBULATED) {
                // Recombobulate the item, return a new item stack with the specified rarity and modifications
                return abstractItem.buildItemStack(abstractItem.getRarity(), modifications);
            }
        }

        // Update the item stack with the modifications
        return itemStack.withTag(modificationsTag, modifications);
    }

    /**
     * Adds a recombobulation modification to the given item stack.
     *
     * @param itemStack The item stack to add the recombobulation to.
     * @return The modified item stack.
     */
    public static ItemStack addRecombobulation(ItemStack itemStack) {
        return addModification(itemStack, Modification.RECOMBOBULATED);
    }

    /**
     * Adds a reforging modification to the given item stack.
     *
     * @param itemStack The item stack to add the reforging to.
     * @return The modified item stack.
     */
    public static ItemStack addReforging(ItemStack itemStack) {
        return addModification(itemStack, Modification.REFORGED);
    }

}

