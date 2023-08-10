package fr.traquolix.gui.rumorsGUI.rumors;

import fr.traquolix.entity.EntityRegistry;
import fr.traquolix.entity.npc.npc.TutorialGuy;
import fr.traquolix.gui.rumorsGUI.RumorGui;
import fr.traquolix.identifiers.Identifier;
import fr.traquolix.quests.tutorial.TutorialCircularQuestLine;
import net.kyori.adventure.text.Component;
import net.minestom.server.entity.EntityType;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;

public class TutorialGuyRumors extends RumorGui {
    public static final Identifier identifier = new Identifier("gui", "rumors");
    public TutorialGuyRumors() {
        super("Inventory guy");
        quest = new TutorialCircularQuestLine();
    }

    @Override
    public void initEntity() {
        super.entity = EntityRegistry.getInstance().getEntity(TutorialGuy.identifier);
    }

    @Override
    public void addNpcHeadAt(int slot) {
        ItemStack itemStack = ItemStack.of(Material.PLAYER_HEAD);
        if (entity.getType() == EntityType.PLAYER) {
            itemStack = itemStack.withDisplayName(Component.text("Player"));
        }
        itemStack = itemStack.withDisplayName(entity.getName());
        setItemStack(slot, itemStack);
    }

    @Override
    public void initIdentifier() {
        super.identifier = identifier;
    }
}