package fr.traquolix.content.items.types.pickaxes;

import fr.traquolix.identifiers.Identifier;
import fr.traquolix.content.items.AbstractItem;
import fr.traquolix.content.items.ItemType;
import fr.traquolix.content.Rarity;
import fr.traquolix.content.requirements.Requirement;
import fr.traquolix.content.requirements.SkillLevelRequirement;
import fr.traquolix.player.CPlayer;
import fr.traquolix.skills.Skill;
import fr.traquolix.stats.Stat;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.minestom.server.item.Material;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static fr.traquolix.Main.logger;

/**
 * The DwarvenPickaxe class represents a pickaxe crafted by the dwarves of the mountains.
 * It is known for its ability to break any block in one hit.
 */
public class DwarvenPickaxe extends AbstractItem {

    /**
     * The unique identifier for the DwarvenPickaxe item.
     */
    final public static Identifier identifier = new Identifier("item", "dwarven_pickaxe");

    /**
     * Constructs a new DwarvenPickaxe item with its properties and lore.
     */
    public DwarvenPickaxe() {
        super(identifier,
                Component.text("Dwarven Pickaxe"),
                ItemType.PICKAXE,
                Material.IRON_PICKAXE,
                Rarity.LEGENDARY,
                "A pickaxe made by the dwarves of the mountains." +
                        "It is said that it can break any block in one hit.",
                Instant.now(),
                Component.text("Admin").color(NamedTextColor.RED),
                true,
                Component.text("Right-click to use."),
                0, 0);
    }

    /**
     * Executes the special use of the DwarvenPickaxe. Currently, it only prints a message to the console.
     *
     * @param cplayer The player who is using the DwarvenPickaxe.
     * @param item    The DwarvenPickaxe item being used.
     */
    @Override
    public void use(CPlayer cplayer, AbstractItem item) {
        logger.info(cplayer.getUsername() + " used dwarven pickaxe");
    }

    /**
     * Initializes the requirements for using the DwarvenPickaxe. In this case, it requires a minimum mining skill level of 3.
     */
    @Override
    public void initRequirements() {
        Set<Requirement> localRequirements = getRequirements();
        localRequirements.add(new SkillLevelRequirement(Skill.MINING, 3));
        this.requirements = localRequirements;
    }

    /**
     * Initializes the bonus stats of the DwarvenPickaxe. It provides additional health and damage bonuses.
     */
    @Override
    public void initBonuses() {
        Map<Stat, Integer> localBonuses = new HashMap<>();
        localBonuses.put(Stat.HEALTH, 10);
        localBonuses.put(Stat.DAMAGE, 10);
        bonuses = localBonuses;
    }
}
