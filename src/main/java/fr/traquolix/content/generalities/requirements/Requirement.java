package fr.traquolix.content.generalities.requirements;

import fr.traquolix.content.generalities.identifiers.Identifier;
import fr.traquolix.player.CPlayer;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;

/**
 * The {@code Requirement} interface represents a condition that needs to be met in order to fulfill certain criteria.
 */
public interface Requirement {

    Identifier identifier = null;


    boolean isMet(CPlayer player);

    TextComponent getText();
}
