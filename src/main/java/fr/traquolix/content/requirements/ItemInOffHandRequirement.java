package fr.traquolix.content.requirements;

import fr.traquolix.identifiers.Identifier;
import fr.traquolix.content.items.AbstractItem;
import fr.traquolix.content.items.ItemRegistry;
import fr.traquolix.player.CPlayer;
import fr.traquolix.utils.Utils;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.minestom.server.item.ItemStack;

/**
 * The {@code ItemInOffHandRequirement} class represents a requirement that checks if the player has a specific item
 * in their off-hand.
 */
public class ItemInOffHandRequirement implements Requirement {

    /**
     * The item that the player must have in their off-hand to meet this requirement.
     */
    private final AbstractItem item;

    /**
     * Constructs a new ItemInOffHandRequirement with the specified item.
     *
     * @param item The item that the player must have in their off-hand to meet this requirement.
     */
    public ItemInOffHandRequirement(AbstractItem item) {
        this.item = item;
    }

    /**
     * Checks if the item in the player's off-hand satisfies the requirement.
     *
     * @param player The player for whom the item requirement is being checked.
     * @return {@code true} if the requirement is met, {@code false} otherwise.
     */
    @Override
    public boolean isMet(CPlayer player) {
        // Get the item stack in the player's off-hand
        ItemStack itemStack = player.getPlayer().getItemInOffHand();
        if (itemStack.isAir()) return false;

        // Check if the item stack has the global tag
        if (!itemStack.hasTag(Identifier.getGlobalTag())) return false;
        String itemId = itemStack.getTag(Identifier.getGlobalTag());

        // Get the AbstractItem corresponding to the item stack's identifier
        AbstractItem item = ItemRegistry.getInstance().getItem(new Identifier(itemId));
        if (item == null) return false;

        // Check if the item in off-hand is the required item
        return item.getIdentifier().equals(this.item.getIdentifier());
    }

    /**
     * Gets the formatted text representation of the item in off-hand requirement.
     *
     * @return The text representation as a {@link Component}.
     */
    @Override
    public TextComponent getText() {
        return Component.text("- ").decoration(TextDecoration.ITALIC, false).color(NamedTextColor.BLUE)
                .append(Component.text(Utils.cleanName(item.getIdentifier().getId())))
                .decoration(TextDecoration.ITALIC, false).color(NamedTextColor.WHITE)
                .append(Component.text(" in off hand"))
                .decoration(TextDecoration.ITALIC, false).color(NamedTextColor.BLUE);
    }
}
