package fr.traquolix.content.items;

import fr.traquolix.content.Rarity;
import fr.traquolix.utils.Utils;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
import net.minestom.server.instance.block.Block;
import net.minestom.server.item.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * The PureItem class is used to create and build pure items with a specified rarity and lore.
 */
public class PureItem {

    /**
     * Default constructor.
     */
    private PureItem() {}

    /**
     * The default rarity for pure items.
     */
    public static final Rarity RARITY = Rarity.COMMON;

    /**
     * The default item type for pure items.
     */
    private static final ItemType TYPE = ItemType.NONE;

    /**
     * Create a pure item from a block.
     *
     * @param block The block to create the pure item from.
     * @return The ItemStack representing the pure item.
     */
    public static ItemStack getPureItem(Block block) {
        return buildItemStack(block);
    }

    /**
     * Create a pure item from an existing ItemStack.
     *
     * @param itemStack The existing ItemStack to create the pure item from.
     * @return The ItemStack representing the pure item.
     */
    public static ItemStack getPureItem(ItemStack itemStack) {
        return buildItemStack(itemStack);
    }

    /**
     * Create a pure item from an existing ItemStack with a specific rarity.
     *
     * @param itemStack The existing ItemStack to create the pure item from.
     * @param rarity The rarity of the pure item.
     * @return The ItemStack representing the pure item with the specified rarity.
     */
    public static ItemStack getPureItem(ItemStack itemStack, Rarity rarity) {
        return buildItemStack(itemStack, rarity);
    }

    /**
     * Build a pure item from an existing ItemStack with a specific rarity.
     *
     * @param itemStack The existing ItemStack to build the pure item from.
     * @param rarity The rarity of the pure item.
     * @return The ItemStack representing the pure item with the specified rarity.
     */
    private static ItemStack buildItemStack(ItemStack itemStack, Rarity rarity) {
        List<Component> lore = lore();
        assert itemStack.getDisplayName() != null;
        return ItemStack.builder(itemStack.material())
                .displayName(itemStack.getDisplayName().color(rarity.getColor()).decoration(TextDecoration.ITALIC, false))
                .lore(lore)
                .build();
    }

    /**
     * Build a pure item from an existing ItemStack with the default rarity.
     *
     * @param itemStack The existing ItemStack to build the pure item from.
     * @return The ItemStack representing the pure item with the default rarity.
     */
    private static ItemStack buildItemStack(ItemStack itemStack) {
        List<Component> lore = lore();
        if (itemStack.getDisplayName() == null) {
            return ItemStack.builder(itemStack.material())
                    .displayName(Component.text(Utils.cleanName(itemStack.material().name())).color(RARITY.getColor()).decoration(TextDecoration.ITALIC, false))
                    .lore(lore)
                    .build();
        } else {
            return ItemStack.builder(itemStack.material())
                    .displayName(itemStack.getDisplayName().color(RARITY.getColor()).decoration(TextDecoration.ITALIC, false))
                    .lore(lore)
                    .build();
        }
    }

    /**
     * Build a pure item from a block with the default rarity.
     *
     * @param block The block to build the pure item from.
     * @return The ItemStack representing the pure item with the default rarity.
     */
    private static ItemStack buildItemStack(Block block) {
        List<Component> lore = lore();
        String name = Utils.cleanName(block.name());
        return ItemStack.builder(Objects.requireNonNull(block.registry().material()))
                .displayName(Component.text(name).color(RARITY.getColor()).decoration(TextDecoration.ITALIC, false))
                .lore(lore)
                .build();
    }

    /**
     * Build the lore for the pure item.
     *
     * @return The list of lore components for the pure item.
     */
    private static List<Component> lore() {
        List<Component> loreBuilder = new ArrayList<>();

        // Add rarity and type to the lore
        loreBuilder.add(Component.text(RARITY.getName().toUpperCase())
                .decoration(TextDecoration.ITALIC, false)
                .decoration(TextDecoration.BOLD, true)
                .color(RARITY.getColor())
                .append(Component.space().decoration(TextDecoration.ITALIC, false).decoration(TextDecoration.BOLD, true).color(RARITY.getColor()))
                .append(Component.text(TYPE.getName().toUpperCase())
                        .decoration(TextDecoration.ITALIC, false)
                        .decoration(TextDecoration.BOLD, true)
                        .color(RARITY.getColor())));

        return loreBuilder;
    }

}
