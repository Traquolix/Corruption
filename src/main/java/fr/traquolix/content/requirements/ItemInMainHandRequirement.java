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
 * The {@code ItemInMainHandRequirement} class represents a requirement that checks if the player has a specific item
 * in their main hand.
 */
public class ItemInMainHandRequirement implements Requirement {

    /**
     * The item that the player must have in their main hand to meet this requirement.
     */
    private final AbstractItem item;

    /**
     * Constructs a new ItemInMainHandRequirement with the specified item.
     *
     * @param item The item that the player must have in their main hand to meet this requirement.
     */
    public ItemInMainHandRequirement(AbstractItem item) {
        this.item = item;
    }

    /**
     * Checks if the item in the player's main hand satisfies the requirement.
     *
     * @param player The player for whom the item requirement is being checked.
     * @return {@code true} if the requirement is met, {@code false} otherwise.
     */
    @Override
    public boolean isMet(CPlayer player) {
        ItemStack itemStack = player.getItemInMainHand();
        if (itemStack == null || itemStack.isAir()) return false;
        if (!itemStack.hasTag(Identifier.getGlobalTag())) return false;
        String itemId = itemStack.getTag(Identifier.getGlobalTag());
        AbstractItem item = ItemRegistry.getInstance().getItem(new Identifier(itemId));
        if (item == null) return false;
        return item.getIdentifier().equals(this.item.getIdentifier());
    }

    /**
     * Gets the formatted text representation of the item in main hand requirement.
     *
     * @return The text representation as a {@link Component}.
     */
    @Override
    public TextComponent getText() {
        return Component.text("- " + Utils.cleanName(item.getIdentifier().getId()) + " in main hand")
                .decoration(TextDecoration.ITALIC, false)
                .color(NamedTextColor.BLUE);
    }
}
