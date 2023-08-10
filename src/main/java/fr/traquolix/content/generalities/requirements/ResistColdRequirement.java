package fr.traquolix.content.generalities.requirements;

import fr.traquolix.content.items.AbstractItem;
import fr.traquolix.content.items.ItemRegistry;
import fr.traquolix.content.items.types.armor.helmets.FrostHelmetItem;
import fr.traquolix.player.CPlayer;
import fr.traquolix.utils.Utils;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;

public class ResistColdRequirement implements Requirement {

    Set<AbstractItem> coldResistantItems = new HashSet<>();
    public ResistColdRequirement() {
        coldResistantItems.add(ItemRegistry.getInstance().getItem(FrostHelmetItem.identifier));
    }
    /**
     * Checks if the requirement is met for the given player.
     *
     * @param player The player for whom the requirement is being checked.
     * @return {@code true} if the requirement is met, {@code false} otherwise.
     */
    @Override
    public boolean isMet(CPlayer player) {
        AtomicBoolean isMet = new AtomicBoolean(false);
        player.getEquipment().getAllArmor().forEach(item -> {
            if (coldResistantItems.contains(item)) {
                isMet.set(true);
            }
        });
        return isMet.get();
    }

    /**
     * Gets the text description of the requirement.
     *
     * @return The text description as a {@link Component}.
     */
    @Override
    public TextComponent getText() {
        return Component.text("→ Resistance to freezing temperatures")
                .decoration(TextDecoration.ITALIC, false)
                .color(NamedTextColor.GOLD);
    }
}
