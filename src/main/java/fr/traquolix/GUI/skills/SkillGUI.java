package fr.traquolix.GUI.skills;

import fr.traquolix.GUI.AbstractGUI;
import fr.traquolix.GUI.MainMenuGUI;
import fr.traquolix.content.generalities.identifiers.Identifier;
import fr.traquolix.player.CPlayer;
import fr.traquolix.skills.AbstractSkill;
import fr.traquolix.utils.Utils;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.minestom.server.inventory.InventoryType;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.minestom.server.item.metadata.PlayerHeadMeta;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

// TODO Continuer le SkillGUI
public class SkillGUI extends AbstractGUI {
    public static final Identifier identifier = new Identifier("gui", "skill");
    CPlayer cPlayer;
    public SkillGUI(CPlayer cPlayer) {
        super(InventoryType.CHEST_6_ROW, "Your Skills");
        this.cPlayer = cPlayer;
    }

    @Override
    public void initIdentifier() {
        super.identifier = identifier;
    }

    @Override
    public void refresh(CPlayer cPlayer) {
        super.refresh(cPlayer);

        AtomicInteger slotCounters = new AtomicInteger(0);
        List<Integer> questSlotsAvailable = List.of(19, 20, 21, 22, 23, 24, 25,
                29, 30, 32, 33);
        questSlotsAvailable.forEach(integer -> {
            setItemStack(integer, ItemStack.of(Material.AIR));
        });

        cPlayer.getSkills().forEach(((skill, abstractSkill) -> {
            addInventoryOpener(questSlotsAvailable.get(slotCounters.get()), skill.getRepresentation()
                    .withDisplayName(
                            Component.text(Utils.capitalizeFirstLetter(skill.name()) + " " + Utils.toRomanNumeral(abstractSkill.getLevel()), NamedTextColor.YELLOW)
                                    .decoration(TextDecoration.ITALIC, false))
                    .withLore(Utils.generateLore(skill, abstractSkill)), new SpecificSkillProgressionGUI(skill, abstractSkill, 1));
            slotCounters.getAndIncrement();
        }));
        addCloseItem(49);
        addCplayerHead(4, cPlayer);
        addInventoryOpener(48, ItemStack.of(Material.ARROW).withDisplayName(Component.text("Go back").decoration(TextDecoration.ITALIC, false)), new MainMenuGUI(cPlayer));
    }

    private void addCplayerHead(int i, CPlayer cPlayer) {

        double meanSkillLevel = cPlayer.getSkills().values().stream().mapToDouble(AbstractSkill::getLevel).average().orElse(0);

        PlayerHeadMeta.Builder metaBuilder = new PlayerHeadMeta.Builder();
        metaBuilder.skullOwner(UUID.randomUUID()).playerSkin(cPlayer.getPlayer().getSkin());
        ItemStack itemStack = ItemStack.of(Material.PLAYER_HEAD).withMeta(metaBuilder.build());
        setItemStack(i, itemStack.withDisplayName(Component.text(meanSkillLevel + " - Skill Avg.", NamedTextColor.YELLOW)
                .decoration(TextDecoration.ITALIC, false)));
    }
}
