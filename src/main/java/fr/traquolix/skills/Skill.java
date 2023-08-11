package fr.traquolix.skills;

import fr.traquolix.content.generalities.identifiers.Identifier;
import fr.traquolix.skills.mining.Mining;
import fr.traquolix.skills.farming.Farming;
import lombok.Getter;
import net.minestom.server.item.Enchantment;
import net.minestom.server.item.ItemHideFlag;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.minestom.server.tag.Tag;

import java.util.function.Supplier;


@Getter
public enum Skill {

    /**
     * Skill for mining operations.
     */
    MINING(Mining::new, new Identifier(Skill.GROUP, "mining"),
            ItemStack.of(Material.STONE_PICKAXE),
            ItemStack.of(Material.IRON_BLOCK),
            ItemStack.of(Material.GOLD_BLOCK),
            ItemStack.of(Material.DIAMOND_BLOCK),
            ItemStack.of(Material.EMERALD_BLOCK),
            ItemStack.of(Material.NETHERITE_BLOCK)),

    /**
     * Skill for farming activities.
     */
    FARMING(Farming::new, new Identifier(Skill.GROUP, "farming"),
            ItemStack.of(Material.CARROT),
            ItemStack.of(Material.WOODEN_HOE),
            ItemStack.of(Material.IRON_HOE),
            ItemStack.of(Material.GOLDEN_HOE),
            ItemStack.of(Material.DIAMOND_HOE),
            ItemStack.of(Material.NETHERITE_HOE));

    private static final String GROUP = "skill";
    private final Supplier<AbstractSkill> skillSupplier;
    @Getter
    private final Identifier identifier;
    private final ItemStack representation;
    private final ItemStack tier1;
    private final ItemStack tier2;
    private final ItemStack tier3;
    private final ItemStack tier4;
    private final ItemStack tier5;


    /**
     * Constructor for each Skill enum instance.
     *
     * @param skillSupplier A supplier function to create a new instance of the corresponding AbstractSkill.
     * @param identifier    An identifier for the skill group and the specific skill.
     */
    Skill(Supplier<AbstractSkill> skillSupplier,
          Identifier identifier,
          ItemStack representation,
          ItemStack tier1,
          ItemStack tier2,
          ItemStack tier3,
          ItemStack tier4,
          ItemStack tier5) {
        this.skillSupplier = skillSupplier;
        this.identifier = identifier;
        this.representation = representation;
        this.tier1 = tier1;
        this.tier2 = tier2;
        this.tier3 = tier3;
        this.tier4 = tier4;
        this.tier5 = tier5;
    }

    /**
     * Creates a new instance of the corresponding AbstractSkill using the supplier function.
     *
     * @return A new instance of the skill.
     */
    public AbstractSkill createNewInstance() {
        return skillSupplier.get();
    }

    public ItemStack getTier(int i) {
        return switch (i) {
            case 1 -> tier1;
            case 2 -> tier2;
            case 3 -> tier3;
            case 4 -> tier4;
            case 5 -> tier5;
            default -> null;
        };
    }
}
