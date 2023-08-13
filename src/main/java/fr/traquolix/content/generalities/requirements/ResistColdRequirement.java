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
    // TODO Maybe I should make it the inverse of what it is now. Rather than having a list here for that system, I should advertise the items that are cold resistant.
    //  I have to think about how to do this.
    // TODO Maybe we could disguise to get into certain camp that are agressive and get special quests if we bring either a disguise, or materials to someone who can make you that disguise ?
    //  But you can also just kill them all to get the loot. But you can't get the quests, nor the "special loot" of the quest. Being ingenious is rewarded. and taking more time is aswell. Also, we can imagine that creating objects in the mine can either add it to your collection of possible items you can bring in the cave, or be salvaged into materials later down the line.
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
        return Component.text("→ Resistance to the cold")
                .decoration(TextDecoration.ITALIC, false)
                .color(NamedTextColor.GOLD);
    }
}
