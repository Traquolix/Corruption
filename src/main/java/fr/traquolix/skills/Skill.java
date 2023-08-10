package fr.traquolix.skills;

import fr.traquolix.content.generalities.identifiers.Identifier;
import fr.traquolix.skills.mining.Mining;
import fr.traquolix.skills.farming.Farming;
import lombok.Getter;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;

import java.util.function.Supplier;


@Getter
public enum Skill {

    /**
     * Skill for mining operations.
     */
    MINING(Mining::new, new Identifier(Skill.GROUP, "mining"), ItemStack.of(Material.IRON_PICKAXE)),

    /**
     * Skill for farming activities.
     */
    FARMING(Farming::new, new Identifier(Skill.GROUP, "farming"), ItemStack.of(Material.IRON_HOE));

    private static final String GROUP = "skill";
    private final Supplier<AbstractSkill> skillSupplier;
    @Getter
    private final Identifier identifier;
    private final ItemStack representation;


    /**
     * Constructor for each Skill enum instance.
     *
     * @param skillSupplier A supplier function to create a new instance of the corresponding AbstractSkill.
     * @param identifier    An identifier for the skill group and the specific skill.
     */
    Skill(Supplier<AbstractSkill> skillSupplier, Identifier identifier, ItemStack representation) {
        this.skillSupplier = skillSupplier;
        this.identifier = identifier;
        this.representation = representation;
    }

    /**
     * Creates a new instance of the corresponding AbstractSkill using the supplier function.
     *
     * @return A new instance of the skill.
     */
    public AbstractSkill createNewInstance() {
        return skillSupplier.get();
    }
}
