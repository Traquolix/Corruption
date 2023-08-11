package fr.traquolix.GUI;

import fr.traquolix.GUI.skills.SkillGUI;
import fr.traquolix.content.generalities.identifiers.Identifier;
import fr.traquolix.player.CPlayer;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.minestom.server.inventory.InventoryType;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;

public class MainMenuGUI extends AbstractGUI {
    public static final Identifier identifier = new Identifier("gui", "skill");
    CPlayer cplayer;
    public MainMenuGUI(CPlayer cPlayer) {
        super(InventoryType.CHEST_6_ROW, "Main menu");
        this.cplayer = cPlayer;
    }

    @Override
    public void initIdentifier() {
        super.identifier = identifier;
    }

    public void refresh(CPlayer cPlayer) {
        super.refresh(cPlayer);
        fillInventoryWith(backGroundItem);
        addInventoryOpener(20,
                ItemStack.of(Material.IRON_SWORD)
                        .withDisplayName(Component.text("Your Skills", NamedTextColor.GREEN)
                                .decoration(TextDecoration.ITALIC, false)),
                new SkillGUI(cPlayer));





        addCloseItem(49);
    }
}
