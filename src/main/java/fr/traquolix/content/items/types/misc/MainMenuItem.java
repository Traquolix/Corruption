package fr.traquolix.content.items.types.misc;

import fr.traquolix.GUI.skills.SkillGUI;
import fr.traquolix.content.Rarity;
import fr.traquolix.content.generalities.identifiers.Identifier;
import fr.traquolix.content.items.AbstractItem;
import fr.traquolix.content.items.ItemType;
import fr.traquolix.player.CPlayer;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.minestom.server.item.Material;

public class MainMenuItem extends AbstractItem {
    final public static Identifier identifier = new Identifier("item", "main_menu");
    public MainMenuItem() {
        super(identifier,
                Component.text("Main Menu", NamedTextColor.GREEN).decoration(TextDecoration.ITALIC, false),
                ItemType.NONE,
                Material.CLOCK,
                Rarity.SPECIAL);
    }

    /**
     * Initializes the requirements for the item.
     */
    @Override
    public void initRequirements() {

    }

    /**
     * Initializes the stat bonuses for the item.
     */
    @Override
    public void initBonuses() {

    }

    /**
     * Uses the item by a player.
     *
     * @param cplayer The player using the item.
     * @param item    The item being used.
     */
    @Override
    public void use(CPlayer cplayer, AbstractItem item) {
        new SkillGUI(cplayer).open(cplayer);
    }
}
