package fr.traquolix.content.generalities.requirements;

import fr.traquolix.mercenaries.AbstractMercenary;
import fr.traquolix.player.CPlayer;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;

public class SpecificMercenaryRequirement implements Requirement {

    private final AbstractMercenary mercenary;
    SpecificMercenaryRequirement(AbstractMercenary mercenary) {
        this.mercenary = mercenary;
    }

    @Override
    public boolean isMet(CPlayer player) {
        return player.getCurrentMercenary().getIdentifier().equals(mercenary.getIdentifier());
    }

    @Override
    public TextComponent getText() {
        return Component.text(
                "Only " + mercenary.getDisplayName() + " can do this."
        );
    }
}
