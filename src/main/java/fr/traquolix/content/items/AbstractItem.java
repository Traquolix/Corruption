package fr.traquolix.content.items;

import fr.traquolix.content.items.types.misc.AirItem;
import fr.traquolix.content.generalities.identifiers.Identifier;
import fr.traquolix.content.Rarity;
import fr.traquolix.content.modifications.Modificator;
import fr.traquolix.content.modifications.reforges.Reforge;
import fr.traquolix.content.generalities.requirements.Requirement;
import fr.traquolix.player.CPlayer;
import fr.traquolix.stats.Stat;
import fr.traquolix.utils.Utils;
import lombok.Getter;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import org.jetbrains.annotations.Nullable;

import java.time.Instant;
import java.util.*;

/**
 * The {@code AbstractItem} class represents a base class for different types of items in the game.
 */
@Getter
public abstract class AbstractItem {

    /**
     * The unique identifier of the item.
     */
    final Identifier identifier;

    /**
     * The display name of the item.
     */
    final Component name;

    /**
     * The type of the item.
     */
    final ItemType type;

    /**
     * The material of the item.
     */
    final Material material;

    /**
     * The rarity of the item.
     */
    Rarity rarity;

    /**
     * The description of the item when it is in its pure form.
     */
    @Nullable
    final String pureDescription;

    /**
     * The date of creation of the item.
     */
    @Nullable
    final Instant dateOfCreation;

    /**
     * The creator of the item.
     */
    @Nullable
    final Component creator;

    /**
     * Indicates whether the item can be reforged or not.
     */
    final boolean reforgeable;

    /**
     * The usage description of the item.
     */
    final Component usageDescription;

    /**
     * The cooldown duration of the item.
     */
    final int cooldown;

    /**
     * Indicates if the item is currently on cooldown.
     */
    boolean onCooldown = false;

    /**
     * The mana cost required to use the item.
     */
    final int manaCost;

    /**
     * The reforge associated with the item.
     */
    Reforge reforge;

    /**
     * The list of requirements needed to use the item.
     */
    protected Set<Requirement> requirements = new HashSet<>();

    /**
     * The map of stat bonuses provided by the item.
     */
    protected Map<Stat, Integer> bonuses = new HashMap<>();

    /**
     * Constructs an AbstractItem with the specified parameters.
     *
     * @param identifier      The unique identifier of the item.
     * @param name            The display name of the item.
     * @param type            The type of the item.
     * @param material        The material of the item.
     * @param rarity          The rarity of the item.
     * @param pureDescription The description of the item when it is in its pure form.
     * @param dateOfCreation  The date of creation of the item.
     * @param creator         The creator of the item.
     * @param reforgeable     Indicates whether the item can be reforged or not.
     * @param usageDescription The usage description of the item.
     * @param cooldown        The cooldown duration of the item.
     * @param manaCost        The mana cost required to use the item.
     */
    public AbstractItem(Identifier identifier, Component name, ItemType type, Material material, Rarity rarity,
                        @Nullable String pureDescription, @Nullable Instant dateOfCreation, @Nullable Component creator,
                        boolean reforgeable, @Nullable Component usageDescription, int cooldown, int manaCost) {
        this.identifier = identifier;
        this.name = name;
        this.type = type;
        this.material = material;
        this.rarity = rarity;
        this.pureDescription = pureDescription;
        this.dateOfCreation = dateOfCreation;
        this.creator = creator;
        this.reforgeable = reforgeable;
        this.usageDescription = usageDescription;
        this.cooldown = cooldown;
        this.manaCost = manaCost;

        initBonuses();
        initRequirements();

        ItemRegistry.getInstance().registerItem(this);
        //logger.info("[Registry] - " + identifier.toString());
    }

    /**
     * Constructs an AbstractItem with default values.
     *
     * @param identifier The unique identifier of the item.
     * @param name       The display name of the item.
     * @param type       The type of the item.
     * @param material   The material of the item.
     * @param rarity     The rarity of the item.
     * @param reforgeable Indicates whether the item can be reforged or not.
     */
    public AbstractItem(Identifier identifier, Component name, ItemType type, Material material, Rarity rarity,
                        boolean reforgeable) {
        this(identifier, name, type, material, rarity, null, null, null, reforgeable, null, 0, 0);
    }

    /**
     * Constructs an AbstractItem with the specified parameters and a pure description.
     *
     * @param identifier      The unique identifier of the item.
     * @param name            The display name of the item.
     * @param type            The type of the item.
     * @param material        The material of the item.
     * @param rarity          The rarity of the item.
     * @param pureDescription The description of the item when it is in its pure form.
     */
    public AbstractItem(Identifier identifier, Component name, ItemType type, Material material, Rarity rarity,
                        String pureDescription) {
        this(identifier, name, type, material, rarity, pureDescription, null, null, false, null, 0, 0);
    }

    /**
     * Constructs an AbstractItem with default values and no pure description.
     *
     * @param identifier The unique identifier of the item.
     * @param name       The display name of the item.
     * @param type       The type of the item.
     * @param material   The material of the item.
     * @param rarity     The rarity of the item.
     */
    public AbstractItem(Identifier identifier, Component name, ItemType type, Material material, Rarity rarity) {
        this(identifier, name, type, material, rarity, null, null, null, false, null, 0, 0);
    }

    /**
     * Initializes the requirements for the item.
     */
    public abstract void initRequirements();

    /**
     * Initializes the stat bonuses for the item.
     */
    public abstract void initBonuses();

    /**
     * Builds the item stack representation of the item with the current attributes.
     *
     * @return The built item stack.
     */
    public ItemStack buildItemStack() {
        List<Component> lore = buildLore();
        ItemStack itemStack = ItemStack.builder(material)
                .displayName(name.color(rarity.getColor()).decoration(TextDecoration.ITALIC, false))
                .lore(lore)
                .build();
        return itemStack
                .withTag(Identifier.getGlobalTag(), identifier.toString())
                .withTag(Modificator.getModificationsTag(), "none");
    }

    /**
     * Builds the item stack representation of the item with the current attributes and specified rarity and modifications.
     *
     * @param rarity        The rarity of the item stack.
     * @param modifications The modifications of the item stack.
     * @return The built item stack.
     */
    public ItemStack buildItemStack(Rarity rarity, String modifications) {
        List<Component> lore = buildLore();
        ItemStack itemStack = ItemStack.builder(material)
                .displayName(name.color(rarity.getColor()).decoration(TextDecoration.ITALIC, false))
                .lore(lore)
                .build();

        return itemStack
                .withTag(Identifier.getGlobalTag(), identifier.toString())
                .withTag(Modificator.getModificationsTag(), modifications);
    }

    /**
     * Builds the lore of the item based on its attributes.
     *
     * @return The list of lore components.
     */
    private List<Component> buildLore() {
        List<Component> loreBuilder = new ArrayList<>();

        bonuses.forEach((stat, integer) -> loreBuilder.add(Component.text(Utils.capitalizeFirstLetter(stat.getIdentifier().getId() + ":"))
                .decoration(TextDecoration.ITALIC, false)
                .color(NamedTextColor.GRAY)
                .append(Component.space())
                .append(Component.text("+" + integer)
                        .decoration(TextDecoration.ITALIC, false)
                        .color(stat.getSecondaryColor()))));

        if (usageDescription != null) {
            loreBuilder.add(Component.empty());
            if (manaCost != 0) {
                loreBuilder.add(Component.text("Cost:")
                        .decoration(TextDecoration.ITALIC, false)
                        .color(Stat.MANA.getSecondaryColor())
                        .append(Component.space())
                        .append(Component.text(Stat.MANA.getSymbol() + " " + manaCost)
                                .decoration(TextDecoration.ITALIC, false)
                                .color(Stat.MANA.getSecondaryColor())));
            }
            loreBuilder.add(usageDescription.decoration(TextDecoration.ITALIC, false)
                    .decoration(TextDecoration.BOLD, true)
                    .color(NamedTextColor.GRAY));
        }

        if (requirements != null && !requirements.isEmpty()) {
            loreBuilder.add(Component.empty());
            loreBuilder.add(Component.text("Requirements")
                    .decoration(TextDecoration.ITALIC, false)
                    .color(NamedTextColor.LIGHT_PURPLE));
            requirements.forEach(requirement -> loreBuilder.add(requirement.getText()));
        }

        if (creator != null && dateOfCreation != null) {
            loreBuilder.add(Component.empty());
            loreBuilder.add(Component.text("Created:")
                    .decoration(TextDecoration.ITALIC, false)
                    .color(NamedTextColor.DARK_GRAY)
                    .append(Component.space())
                    .append(Component.text(Utils.getDateAndHour(dateOfCreation))
                            .decoration(TextDecoration.ITALIC, false)
                            .color(NamedTextColor.DARK_GRAY)));
            loreBuilder.add(Component.text("By:")
                    .decoration(TextDecoration.ITALIC, false)
                    .color(NamedTextColor.DARK_GRAY)
                    .append(Component.space())
                    .append(Objects.requireNonNull(this.getCreator()).decoration(TextDecoration.ITALIC, false)));
        }

        if (pureDescription != null) {
            loreBuilder.add(Component.empty());
            String[] descComponents = pureDescription.split("\\.");
            for (String descComponent : descComponents) {
                loreBuilder.add(Component.text(descComponent + ".")
                        .color(NamedTextColor.DARK_GRAY)
                        .decoration(TextDecoration.ITALIC, true));
            }
        }

        if (reforgeable) {
            loreBuilder.add(Component.empty());
            loreBuilder.add(Component.text("This item can be reforged!")
                    .decoration(TextDecoration.ITALIC, false)
                    .color(NamedTextColor.DARK_GRAY));
        }
        loreBuilder.add(Component.text(rarity.getName().toUpperCase())
                .decoration(TextDecoration.ITALIC, false)
                .decoration(TextDecoration.BOLD, true)
                .color(rarity.getColor())
                .append(Component.space().decoration(TextDecoration.ITALIC, false).decoration(TextDecoration.BOLD, true))
                .append(Component.text(type.getName().toUpperCase())
                        .decoration(TextDecoration.ITALIC, false)
                        .decoration(TextDecoration.BOLD, true)
                        .color(rarity.getColor())));
        return loreBuilder;
    }

    /**
     * Upgrades the rarity of the item.
     */
    public void upgradeRarity() {
        rarity = rarity.getNextRarity();
    }

    /**
     * Uses the item by a player.
     *
     * @param cplayer The player using the item.
     * @param item    The item being used.
     */
    public abstract void use(CPlayer cplayer, AbstractItem item);

    /**
     * Resets the cooldown of the item.
     */
    public void resetCooldown() {
        onCooldown = false;
    }

    /**
     * Sets the item on cooldown.
     */
    public void setOnCooldown() {
        onCooldown = true;
    }

    /**
     * Reforges the item.
     */
    public void reforge() {
        // Implement reforge logic here
    }

    public boolean isAir() {
        return this == ItemRegistry.getInstance().getItem(AirItem.identifier);
    }
}
