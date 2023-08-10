package fr.traquolix.stats;

import fr.traquolix.content.generalities.identifiers.Identifier;
import fr.traquolix.stats.damage.Damage;
import fr.traquolix.stats.defense.Defense;
import fr.traquolix.stats.health.Health;
import fr.traquolix.stats.mana.Mana;
import lombok.Getter;
import net.kyori.adventure.text.format.NamedTextColor;

import java.util.function.Supplier;


public enum Stat {

    /**
     * Health statistic with a heart symbol ❤
     */
    HEALTH(Health::new, new Identifier(Stat.GROUP, "health"), NamedTextColor.RED, NamedTextColor.GREEN, "❤"),

    /**
     * Damage statistic with a crossed swords symbol ⚔
     */
    DAMAGE(Damage::new, new Identifier(Stat.GROUP, "damage"), NamedTextColor.YELLOW, NamedTextColor.RED, "⚔"),

    /**
     * Mana statistic with a sparkling star symbol ✨
     */
    MANA(Mana::new, new Identifier(Stat.GROUP, "mana"), NamedTextColor.BLUE, NamedTextColor.AQUA, "✨"),

    /**
     * Defense statistic with a shield symbol ⛨
     */
    DEFENSE(Defense::new, new Identifier(Stat.GROUP, "defense"), NamedTextColor.GRAY, NamedTextColor.DARK_GRAY, "⛨");

    private static final String GROUP = "stat";
    private final Supplier<AbstractStat> statSupplier;
    @Getter
    private final Identifier identifier;
    @Getter
    private final NamedTextColor color;
    @Getter
    private final NamedTextColor secondaryColor;
    @Getter
    private final String symbol;

    /**
     * Constructor for each Stat enum instance.
     *
     * @param statSupplier   A supplier function to create a new instance of the corresponding AbstractStat.
     * @param identifier     An identifier for the statistic group and the specific statistic.
     * @param color          The primary color associated with the statistic.
     * @param secondaryColor The secondary color associated with the statistic.
     * @param symbol         A symbol representing the statistic when displayed.
     */
    Stat(Supplier<AbstractStat> statSupplier, Identifier identifier, NamedTextColor color, NamedTextColor secondaryColor, String symbol) {
        this.statSupplier = statSupplier;
        this.identifier = identifier;
        this.color = color;
        this.secondaryColor = secondaryColor;
        this.symbol = symbol;
    }

    /**
     * Creates a new instance of the corresponding AbstractStat using the supplier function.
     *
     * @return A new instance of the statistic.
     */
    public AbstractStat createNewInstance() {
        return statSupplier.get();
    }
}
