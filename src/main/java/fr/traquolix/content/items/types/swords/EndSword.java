package fr.traquolix.content.items.types.swords;

import fr.traquolix.content.generalities.identifiers.Identifier;
import fr.traquolix.content.Rarity;
import fr.traquolix.content.items.AbstractItem;
import fr.traquolix.content.items.ItemType;
import fr.traquolix.player.CPlayer;
import fr.traquolix.stats.Stat;
import net.kyori.adventure.text.Component;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.coordinate.Vec;
import net.minestom.server.entity.Player;
import net.minestom.server.item.Material;
import org.jetbrains.annotations.NotNull;

/**
 * The EndSword class represents a special sword item with a unique ability to launch the player into the air.
 */
public class EndSword extends AbstractItem {

    /**
     * The unique identifier for the EndSword item.
     */
    final public static Identifier identifier = new Identifier("item", "end_sword");

    /**
     * The mana cost required to use the special ability.
     */
    private static final int MANA_COST = 10;

    /**
     * Constructs a new EndSword item with its properties and lore.
     */
    public EndSword() {
        super(identifier,
                Component.text("End Sword"),
                ItemType.SWORD,
                Material.IRON_SWORD,
                Rarity.RARE,
                "A sword made of end stone.",
                null,
                null,
                true,
                Component.text("Right-click to use."), 0, MANA_COST);
    }

    /**
     * Initializes the requirements for using the EndSword. Currently, there are no specific requirements for this item.
     */
    @Override
    public void initRequirements() {
        // No specific requirements for the EndSword.
    }

    /**
     * Initializes the bonus stats of the EndSword. In this case, it provides additional damage bonus.
     */
    @Override
    public void initBonuses() {
        bonuses.put(Stat.DAMAGE, 100);
    }

    /**
     * Executes the special ability of the EndSword, which launches the player into the air.
     *
     * @param cplayer The player who is using the EndSword.
     * @param item    The EndSword item being used.
     */
    @Override
    public void use(CPlayer cplayer, AbstractItem item) {
        // Check if the player has enough mana to use the ability
        if (!cplayer.consumeMana(MANA_COST)) {
            return;
        }

        // Get the player and their current position
        Player player = cplayer.getPlayer();
        Pos location = player.getPosition();

        // Calculate the direction vector to launch the player (8 blocks in the direction the player is looking)
        double yaw = Math.toRadians(location.yaw());
        Vec velocity = getVelocity(location, yaw);

        // Set the player's velocity to launch them into the air
        player.setVelocity(velocity);
    }

    @NotNull
    private static Vec getVelocity(Pos location, double yaw) {
        double pitch = Math.toRadians(location.pitch());
        double x = location.x() + 8 * Math.sin(-yaw) * Math.cos(pitch);
        double y = location.y() + 8 * Math.sin(-pitch);
        double z = location.z() + 8 * Math.cos(yaw) * Math.cos(pitch);

        // Calculate the velocity vector to apply to the player for the launch effect
        Pos targetPos = new Pos(x, y, z, location.yaw(), location.pitch());
        double dx = targetPos.x() - location.x();
        double dy = targetPos.y() - location.y();
        double dz = targetPos.z() - location.z();
        double speed = 40.0; // Adjust the speed to control the launch intensity
        double distance = Math.sqrt(dx * dx + dy * dy + dz * dz);
        return new Vec(speed * dx / distance, speed * dy / distance, speed * dz / distance);
    }
}
