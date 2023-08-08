package fr.traquolix.skills;

import fr.traquolix.identifiers.Identifier;
import fr.traquolix.skills.mining.Mining;
import fr.traquolix.skills.farming.Farming;
import lombok.Getter;

import java.util.function.Supplier;


@Getter
public enum Skill {

    /**
     * Skill for mining operations.
     */
    MINING(Mining::new, new Identifier(Skill.GROUP, "mining")),

    /**
     * Skill for farming activities.
     */
    FARMING(Farming::new, new Identifier(Skill.GROUP, "farming"));

    private static final String GROUP = "skill";
    private final Supplier<AbstractSkill> skillSupplier;
    @Getter
    private final Identifier identifier;

    /**
     * Constructor for each Skill enum instance.
     *
     * @param skillSupplier A supplier function to create a new instance of the corresponding AbstractSkill.
     * @param identifier    An identifier for the skill group and the specific skill.
     */
    Skill(Supplier<AbstractSkill> skillSupplier, Identifier identifier) {
        this.skillSupplier = skillSupplier;
        this.identifier = identifier;
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
